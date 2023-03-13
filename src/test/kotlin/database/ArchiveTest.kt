package database

import archive.ArchiveEntity
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.net.URL
import java.sql.Connection
import java.time.Instant
import kotlin.test.assertEquals

class ArchiveTest {

    @Test
    fun archiveInsert() {
        val connection = getSqliteConnection(":memory:")
        connection.use {
            createDatabase(connection)
            val daoManager = DaoManager(connection)
            val result = daoManager.archiveDao.insertArchive(
                ArchiveEntity(
                    id = "archiveInsert_test",
                    name = "archive insert test",
                    createdDate = Instant.now(),
                    updatedDate = Instant.now(),
                    deleted = false,
                    deleteDate = Instant.MAX,
                )
            )

            assertEquals("archiveInsert_test", result.id)
            assertEquals("archive insert test", result.name)
        }
    }

    fun createDatabase(connection: Connection) {
        val fileResource: URL = object {}.javaClass.getResource("/storehouse_schema.sql")
            ?: fail("unable to load storehouse schema from resources")

        val rawSql = fileResource.readText()
        val sqlStatements = rawSql.lines()
            .filter { !it.startsWith("-") }
            .joinToString(separator = " ")
            .split(";")
            .filter { it != "" }

        sqlStatements.forEach {
            val statement = connection.createStatement()
            statement.execute(it)
            statement.close()
        }
    }
}

