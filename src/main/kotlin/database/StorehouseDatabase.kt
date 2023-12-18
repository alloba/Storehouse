package database

import database.entities.SchemaMigrationEntity
import org.slf4j.LoggerFactory
import org.sqlite.SQLiteException
import java.nio.file.Path
import java.sql.Connection
import java.sql.DriverManager
import java.time.OffsetDateTime
import java.util.*
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.readText


class StorehouseDatabase(private val databasePath: String) {
    private val LOGGER = LoggerFactory.getLogger(this::class.java)
    private val databaseSchemaFolder = "database"
    private val databaseMigrationPrefix = "db-process-"
    private val bootstrapFile = "bootstrap.sql"

    val connection: Connection
    init {
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
        if (!connection.isValid(1)){
            throw Exception("invalid connection - " + connection.clientInfo)
        }

        return connection
    }

    private fun performMigrations(){
        val migrationDirectory = this::class.java.classLoader.getResource("database")?.toURI()
            ?: throw Exception("Unable to load database schema resources - $databaseSchemaFolder not found in runtime resources.")

        val allMigrationResources = Path.of(migrationDirectory)
            .listDirectoryEntries()
            .filter { it.name.startsWith(databaseMigrationPrefix) }
            .sortedBy { it.name.substringAfterLast(databaseMigrationPrefix).toInt() }

        val completedMigrations = getSchemaMigrations(this)
        val targetMigrationResources = allMigrationResources.filter { ! completedMigrations.map { c -> c.filename }.contains(it.name) }

        if (targetMigrationResources.isNotEmpty()) {
            LOGGER.info("Database is out of date! Attempting migrations: ${targetMigrationResources.map { it.name }}")
            targetMigrationResources.forEach {
                executeSqlScript(it.readText())
                insertSchemaMigration(this, SchemaMigrationEntity(UUID.randomUUID().toString(), it.fileName.toString(), OffsetDateTime.now()))
            }
        }

        println()
    }

    private fun bootstrapDatabase() {
        try{
            this::connection.get().prepareStatement("select 1 from ${DatabaseConstants.BOOTSTRAP_TABLE}")
        } catch (e: SQLiteException){
            if (e.message?.contains("no such table: bootstrap") == true){
                LOGGER.info("Database at [$databasePath] not marked as initialized, running bootstrap file - $bootstrapFile")
                val bootstrapText = this::class.java.classLoader.getResource("$databaseSchemaFolder/$bootstrapFile")?.readText()
                    ?: throw Exception("Unable to load database bootstrap file - $bootstrapFile")

                executeSqlScript(bootstrapText)
            }
            else throw e
        }
    }

    private fun executeSqlScript(script: String){
        val delimiter = ";"
        val commentMarker = "--"

        script.split("\n")
            .map { it.trim() }
            .filter { ! it.startsWith(commentMarker) }
            .joinToString(" ")
            .split(delimiter)
            .filter { it.isNotEmpty() }
            .forEach {
                LOGGER.info("Executing statement: \n\t$it")
                this::connection.get().prepareStatement(it).execute()
            }
    }

}

