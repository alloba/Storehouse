package source.local

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createFile
import kotlin.io.path.createParentDirectories
import kotlin.io.path.createTempDirectory
import kotlin.io.path.deleteRecursively
import kotlin.io.path.writeText
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class LocalArchiveSourceTests {
    private var tempDirectoryPath = ""
    @BeforeEach
    fun before() {
        tempDirectoryPath = createTempDirectory("storehouse-testing").toString()
    }

    @OptIn(ExperimentalPathApi::class)
    @AfterEach
    fun after(){
        Path.of(tempDirectoryPath).deleteRecursively()
    }

    @Test
    fun `get all nested files in archive source`() {
        val sourcePath = Path.of(tempDirectoryPath)

        val rootFile = Path.of(sourcePath.toString() + File.separator + "rootFile.tmp")
        rootFile.writeText("placeholder")

        val childFile = Path.of(sourcePath.toString() + File.separator + "folder" + File.separator + "childFile.tmp").createParentDirectories()
        childFile.writeText("placeholder")
        val localArchiveSource = LocalArchiveSource(LocalArchiveSourceConfig(Path.of(tempDirectoryPath)))
        val res = localArchiveSource.getAllFiles()

        assertEquals(2, res.size)
        assertTrue(res.any { it.toString().contains("childFile.tmp") })
        assertTrue(res.any { it.toString().contains("rootFile.tmp") })
    }

    @Test
    fun `file hash computation returns consistent values`() {
        val file1 = Path.of(tempDirectoryPath + File.separator + "file1.tmp").createFile()
        val file2 = Path.of(tempDirectoryPath + File.separator + "file2.tmp").createFile()
        val file3 = Path.of(tempDirectoryPath + File.separator + "file3.tmp").createFile()
        val file4 = Path.of(tempDirectoryPath + File.separator + "file4.tmp").createFile()

        file3.writeText("textå¬ºª")
        file4.writeText("textå¬ºª")

        val localArchiveSource = LocalArchiveSource(LocalArchiveSourceConfig(Path.of(tempDirectoryPath)))

        assertEquals(localArchiveSource.computeMd5Hash(file1), localArchiveSource.computeMd5Hash(file2))
        assertEquals(localArchiveSource.computeMd5Hash(file3), localArchiveSource.computeMd5Hash(file4))
        assertNotEquals(localArchiveSource.computeMd5Hash(file1), localArchiveSource.computeMd5Hash(file4))
    }

    @Test
    fun `file hash will not be computed if a path does not point to a regular file`(){
        val localArchiveSource = LocalArchiveSource(LocalArchiveSourceConfig(Path.of(tempDirectoryPath)))
        Path.of(tempDirectoryPath + File.separator + "tempfile.tmp").createFile()
        assertThrows<Exception> { localArchiveSource.computeMd5Hash(Path.of(tempDirectoryPath)) }
    }
}