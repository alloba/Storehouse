package database

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createTempDirectory
import kotlin.io.path.deleteRecursively
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SchemaMigrationTests {
    private val tempDirPrefix = "storehouse-test"
    private var tempdirPath = ""

    @BeforeEach
    fun before() {
        val tempdir = createTempDirectory(tempDirPrefix)
        tempdirPath = tempdir.toString()
        kotlin.io.path.createTempFile(tempdir, "database")
    }

    @OptIn(ExperimentalPathApi::class)
    @AfterEach
    fun after() {
        Path.of(tempdirPath).deleteRecursively()
    }

    @Test
    fun `ping bootstrap table`() {
        val databaseFile = kotlin.io.path.createTempFile(Path.of(tempdirPath), "database")
        pingBootstrap(StorehouseDatabase(databaseFile.toString()))
    }

    @Test
    fun `can apply migrations`() {
        val databaseFile = kotlin.io.path.createTempFile(Path.of(tempdirPath), "database")
        val database = StorehouseDatabase(databaseFile.toString())
        val migrations = getSchemaMigrations(database)
        assertTrue(migrations.isNotEmpty())
    }

    @Test
    fun `migrations are not applied multiple times`() {
        val databaseFile = kotlin.io.path.createTempFile(Path.of(tempdirPath), "database")
        val database1 = StorehouseDatabase(databaseFile.toString())
        val migrations1 = getSchemaMigrations(database1)

        assertTrue(migrations1.isNotEmpty())

        database1.connection.close()
        val database2 = StorehouseDatabase(databaseFile.toString())
        val migrations2 = getSchemaMigrations(database2)

        assertEquals(migrations1.size, migrations2.size)
    }
}