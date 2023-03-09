package database

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.net.URL
import java.sql.Connection

/**
 * Trying out this style of in-memory database testing.
 * It's a little janky, so I don't necessarily expect it to survive, but we'll see.
 */
class ConnectionEstablishmentKtTest {

    private var connection: Connection? = null

    @BeforeEach
    fun before() {
        val connectString = ":memory:"

        val schemaSource: URL = object {}.javaClass.getResource("/storehouse_schema.sql")
            ?: fail("unable to load database schema from resources")
        val txt = schemaSource.readText()

        try {
            connection = getSqliteConnection(connectString)
            val statement = connection!!.createStatement()
            val sqlCommands = txt.lines()
                .filter { !it.startsWith("--") }
                .joinToString("\n")
                .replace("\n", "")
                .split(";")
            sqlCommands.forEach { statement.executeUpdate(it) }
            statement.close()
        } catch (e: Exception) {
            connection?.close()
        }
    }

    @AfterEach
    fun after() {
        connection?.close()
    }

    @Test
    fun archiveInsert() {
        val statement = connection!!.createStatement()
        statement.executeUpdate("insert into archive values ('0', 'test', 'blah', 'bluh')")
        val rs = statement.executeQuery("select * from archive")
        while (rs.next()) {
            println(rs.getInt("id"))
            println(rs.getString("name"))
            println(rs.getString("created_date"))
            println(rs.getString("updated_date"))
        }
    }
}