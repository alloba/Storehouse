import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ArchiveManager_BasicTests {

    private val testHarness = TestHarness()

    @BeforeEach
    fun before() {
        testHarness.before()
    }

    @AfterEach
    fun after() {
        testHarness.after()
    }

    @Test
    fun `can create new archive`() {
        val archiveManager = createArchiveManager(testHarness)
        val newArchive = archiveManager.createNewArchive("testArchive", "its an archive")

        assertEquals(newArchive.name, "testArchive")
    }

    @Test
    fun `can retrieve created archive`() {
        val archiveManager = createArchiveManager(testHarness)
        val archive = archiveManager.createNewArchive("testyArchie", "es test")

        val retrievedArchive = archiveManager.getArchiveByName("testyArchie")
        assertEquals(archive.id, retrievedArchive.id)
        assertEquals(archive.dateCreated, retrievedArchive.dateCreated)
    }

    @Test
    fun `if archive name is not found throw exception`() {
        val archiveManager = createArchiveManager(testHarness)

        assertThrows<Exception> { archiveManager.getArchiveByName("doesnt exist") }
    }

    @Test
    fun `can create snapshot`() {
        val archiveManager = createArchiveManager(testHarness)
        val archive = archiveManager.createNewArchive("test archive", "archive desc")
        val snapshot = archiveManager.createNewSnapshot(archive, testHarness.rootTestDirectory)

        assertEquals(archive.id, snapshot.archiveId)
    }

    private fun createArchiveManager(testHarness: TestHarness): ArchiveManager {
        return ArchiveManager(
            testHarness.database,
            testHarness.localArchiveSource,
            testHarness.localArchiveDestination,
        )
    }
}