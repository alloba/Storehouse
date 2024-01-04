import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.io.path.createTempDirectory
import kotlin.io.path.writeText
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ArchiveManager_FileOperationsTests {
    val harness = TestHarness()

    @BeforeEach
    fun before() {
        harness.before()
    }

    @AfterEach
    fun after() {
        harness.after()
    }

    @Test
    fun `multiple files can be submitted to a snapshot and then will persist in the database and in storage, and can be retrieved`() {
        val tempdir = createTempDirectory(harness.rootTestDirectory, "subtestdir")
        val file1 = kotlin.io.path.createTempFile(tempdir, "storehouse-testing-file1")
        val file2 = kotlin.io.path.createTempFile(tempdir, "storehouse-testing-file2")
        val file3 = kotlin.io.path.createTempFile(tempdir, "storehouse-testing-file1")
        file1.writeText("this is file1")
        file2.writeText("this is file2")
        file3.writeText("this is file3")

        val archive = harness.archiveManager.createNewArchive("archive name here", "for testing")
        val snapshotEntity = harness.archiveManager.createNewSnapshot(archive, tempdir)

        val persistedFileMetaEntities = harness.fileMetaRepository.getFileMetasBySnapshotId(snapshotEntity.id)
        assertEquals(3, persistedFileMetaEntities.size)

        val persistedFileEntities = persistedFileMetaEntities
            .map { it.fileId }
            .map { harness.fileRepository.getFileEntityById(it) }
            .mapNotNull { it?.md5Hash }

        val archivedTextFiles = persistedFileEntities.map {
            val archivedFile = harness.localArchiveDestination.retrieveFileByHash(it)
            archivedFile!!.toFile().readText()
        }

        assertTrue(archivedTextFiles.contains("this is file1"))
        assertTrue(archivedTextFiles.contains("this is file2"))
        assertTrue(archivedTextFiles.contains("this is file3"))
    }
}