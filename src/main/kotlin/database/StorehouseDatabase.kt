package database

import database.entities.SchemaMigrationEntity
import org.slf4j.LoggerFactory
import org.sqlite.SQLiteException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URLDecoder
import java.nio.file.Path
import java.sql.Connection
import java.sql.DriverManager
import java.time.OffsetDateTime
import java.util.*
import java.util.jar.JarFile
import java.util.stream.Collectors
import kotlin.io.path.isDirectory
import kotlin.io.path.isReadable
import kotlin.io.path.isWritable
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.readText


class StorehouseDatabase(private val databasePath: String) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val databaseSchemaFolder = "database"
    private val migrationScriptPrefix = "-"  // splits on the last occurrence. asdf-123 -> 123; bd-test-script-3 -> 3; etc.
    private val bootstrapFile = "bootstrap.sql"

    val connection: Connection

    init {
        require(Path.of(databasePath).isReadable() && Path.of(databasePath).isWritable() && !Path.of(databasePath).isDirectory()) { "Storehouse database file $databasePath must be a writeable file" }
        connection = getSqliteConnection(databasePath)
        bootstrapDatabase()
        performMigrations()
    }

    /**
     * Provide the path to the database resource
     * The file is assumed to be a valid sqlite db. This is a hardcoded assumption enforced by the connection string in this function.
     */
    private fun getSqliteConnection(resourcePath: String): Connection {
        val connectionString = "jdbc:sqlite:$resourcePath"
        val connection = DriverManager.getConnection(connectionString)

        assert(connection.isValid(1))
        if (!connection.isValid(1)) {
            throw Exception("invalid connection - " + connection.clientInfo)
        }

        return connection
    }

    private fun performMigrations() {
        val migrationDirectory = this::class.java.classLoader.getResource("database")?: throw Exception("database schema folder failed load: $databaseSchemaFolder")

        val sqlResourcePaths = mutableListOf<String>()
        if (migrationDirectory.protocol == "jar") {
            val jarpath = migrationDirectory.path.substring(5, migrationDirectory.path.indexOf("!")) //strip protocol bits
            val jar = JarFile(URLDecoder.decode(jarpath, "UTF-8"))
            val entries = jar.entries().toList().toSet()

            val allSqlResources = entries
                .filter { it.name.startsWith(databaseSchemaFolder) }
                .map { it.name }
                .filter { it.contains(migrationScriptPrefix) && it.endsWith(".sql") }
                .sortedBy { it.substringAfter(databaseSchemaFolder).substringAfter(migrationScriptPrefix) }

            sqlResourcePaths.addAll(allSqlResources)
        } else if (migrationDirectory.protocol == "file") {
            if (!Path.of(migrationDirectory.toURI()).isDirectory()){
                throw Exception("Database schema folder must be a directory, not a file: $databaseSchemaFolder")
            }
            val migrationResources = Path.of(migrationDirectory.toURI())
                .listDirectoryEntries()
                .filter { it.name.contains(migrationScriptPrefix) && it.name.endsWith(".sql") }
                .sortedBy { it.name.substringAfterLast(migrationScriptPrefix).toInt() }
                .map { it.toString() }
            sqlResourcePaths.addAll(migrationResources)
        }

        val completedMigrations = getSchemaMigrations(this)
        val targetMigrationResources = sqlResourcePaths.filter { !completedMigrations.map { c -> c.filename }.contains(it) }
        targetMigrationResources
            .forEach {
                logger.info("Database is out of date! Attempting migration: $it")
                executeSqlScript(readText(it, migrationDirectory.protocol))
                insertSchemaMigration(this, SchemaMigrationEntity(UUID.randomUUID().toString(), it, OffsetDateTime.now()))
        }
    }

    private fun readText(path: String, protocol: String): String {
        return when (protocol){
            "file" -> Path.of(path).readText()
            "jar" -> {
                val stream = this::class.java.classLoader.getResourceAsStream(path)?: throw Exception("could not load resource stream: $path")
                BufferedReader(InputStreamReader(stream, Charsets.UTF_8)).lines().collect(Collectors.joining("\n"))
            }
            else -> throw Exception("Unsupported URI protocol $protocol. Cannot read text.")
        }
    }

    private fun bootstrapDatabase() {
        try {
            this::connection.get().prepareStatement("select 1 from bootstrap")
        } catch (e: SQLiteException) {
            if (e.message?.contains("no such table: bootstrap") == true) {
                logger.info("Database at [$databasePath] not marked as initialized, running bootstrap file - $bootstrapFile")
                val bootstrapText = this::class.java.classLoader.getResource("$databaseSchemaFolder/$bootstrapFile")?.readText()
                    ?: throw Exception("Unable to load database bootstrap file - $bootstrapFile")

                executeSqlScript(bootstrapText)
            } else throw e
        }
    }

    private fun executeSqlScript(script: String) {
        val delimiter = ";"
        val commentMarker = "--"

        script.split("\n")
            .map { it.trim() }
            .filter { !it.startsWith(commentMarker) }
            .joinToString(" ")
            .split(delimiter)
            .filter { it.isNotBlank() }
            .forEach {
                this::connection.get().prepareStatement(it).execute()
            }
    }

}

