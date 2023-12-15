package database

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createTempDirectory
import kotlin.io.path.deleteRecursively
import kotlin.io.path.pathString
import kotlin.test.assertTrue

class DatabaseConnectivityTests {

    private var tempSubdir: Path? = null

    @BeforeEach
    fun before() {
        tempSubdir = createTempDirectory("storehouse-testing")
    }

    @OptIn(ExperimentalPathApi::class)
    @AfterEach
    fun after(){
        tempSubdir?.deleteRecursively()
    }

    @Test
    fun `able to create test database`(){
        val db = generateTestDatabaseFile()
        val connection = getSqliteConnection(db.pathString)

        assertTrue(connection.isValid(1))
    }

    @Test
    fun `able to close database connection`() {
        val db = generateTestDatabaseFile()
        val connection = getSqliteConnection(db.pathString)
        connection.close()
        assertTrue (connection.isClosed)
    }

    private fun generateTestDatabaseFile(): Path {
        val dbFile = kotlin.io.path.createTempFile(tempSubdir, "storehouse.sqlite")
        dbFile.toFile().deleteOnExit()
        return dbFile
    }
}