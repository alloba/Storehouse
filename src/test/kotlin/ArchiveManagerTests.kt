import database.ArchiveRepository
import database.StorehouseDatabase
import database.entities.ArchiveEntity
import destination.local.LocalArchiveDestination
import destination.local.LocalArchiveDestinationConfig
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import source.local.LocalArchiveSource
import source.local.LocalArchiveSourceConfig
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createTempDirectory
import kotlin.io.path.deleteRecursively
import kotlin.test.assertEquals

class ArchiveManagerTests {

   val testHarness = TestHarness()
    @BeforeEach
    fun before(){

    }

    @AfterEach
    fun after(){
        testHarness.after()
    }

    @Test
    fun `can create new archive`(){
        val archiveManager = ArchiveManager(
            testHarness.localArchiveSource,
            testHarness.localArchiveDestination,
            ArchiveRepository(testHarness.database)
        )

        val newArchive = archiveManager.createNewArchive("testArchive", "its an archive")

        assertEquals(newArchive.name, "testArchive")
    }

    @Test
    fun `can retrieve created archive`() {
        val archiveManager = ArchiveManager(
            testHarness.localArchiveSource,
            testHarness.localArchiveDestination,
            ArchiveRepository(testHarness.database)
        )
        val archive = archiveManager.createNewArchive("testyArchie", "es test")

        val retrievedArchive = archiveManager.getArchiveByName("testyArchie")
        assertEquals(archive.id, retrievedArchive.id)
        assertEquals(archive.dateCreated, retrievedArchive.dateCreated)
    }

    @Test
    fun `if archive name is not found throw exception`(){
        val archiveManager = ArchiveManager(
            testHarness.localArchiveSource,
            testHarness.localArchiveDestination,
            ArchiveRepository(testHarness.database)
        )

        assertThrows<Exception> { archiveManager.getArchiveByName("doesnt exist") }
    }
}