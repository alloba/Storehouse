package database

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createTempDirectory
import kotlin.io.path.deleteRecursively

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
    fun `can apply migrations`() {
        val databaseFile = kotlin.io.path.createTempFile(Path.of(tempdirPath), "database")
        val database = StorehouseDatabase(databaseFile.toString())
    }
}