package source.local

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import source.ArchiveSourceConfig
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createTempDirectory
import kotlin.io.path.deleteRecursively

class LocalArchiveSourceConfigTests {

    private var testDirectoryName = ""
    @BeforeEach
    fun before(){
        testDirectoryName = createTempDirectory("storehouse-testing").toString()
    }

    @OptIn(ExperimentalPathApi::class)
    @AfterEach
    fun after(){
        Path.of(testDirectoryName).deleteRecursively()
    }

    @Test
    fun `validation success`() {
        LocalArchiveSourceConfig(Path.of(testDirectoryName))
    }

    @Test
    fun `if config directory does not exist then fail`(){
        assertThrows<ArchiveSourceConfig.StorageValidationException> {
            LocalArchiveSourceConfig(Path.of(testDirectoryName+ "junkextrabit"))
        }
    }

    @Test
    fun `if config path is not a directory then fail`(){
        assertThrows<ArchiveSourceConfig.StorageValidationException> {
            LocalArchiveSourceConfig(kotlin.io.path.createTempFile(Path.of(testDirectoryName), "localstorage-test-file"))
        }
    }
}