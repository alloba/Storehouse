package destination.local

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createTempDirectory
import kotlin.io.path.deleteRecursively
import kotlin.test.Test
import kotlin.test.assertTrue

class LocalArchiveDestinationTests {
    var tempDirPath = ""

    @BeforeEach
    fun before() {
        tempDirPath = createTempDirectory("storehouse-localarchivedestination").toString()
    }

    @OptIn(ExperimentalPathApi::class)
    @AfterEach
    fun after() {
        Path.of(tempDirPath).deleteRecursively()
    }

    @Test
    fun `can create destination object`() {
        LocalArchiveDestination(mapOf("path" to tempDirPath))
    }

    @Test
    fun `can copy a file from other local path`() {
        val destination = LocalArchiveDestination(mapOf("path" to tempDirPath))
        val otherDir = createTempDirectory("storehouse-localarchive-testing")
        val otherFile = kotlin.io.path.createTempFile(otherDir, "testfile-zzz")

        assertTrue(destination.listFiles().isEmpty())
        destination.submitFile(otherFile, "asdfffff")

        assertTrue(destination.listFiles().size == 1)
    }
}