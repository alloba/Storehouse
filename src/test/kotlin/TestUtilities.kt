import database.StorehouseDatabase
import database.repo.ArchiveRepository
import database.repo.FileMetaRepository
import database.repo.FileRepository
import database.repo.SnapshotRepository
import destination.local.LocalArchiveDestination
import source.local.LocalArchiveSource
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createTempDirectory
import kotlin.io.path.deleteRecursively

class TestHarness {
    private val testDirNamePrefix = "storehouse-testing"
    lateinit var rootTestDirectory: Path
    lateinit var databaseFile: Path
    lateinit var database: StorehouseDatabase
    lateinit var localArchiveSource: LocalArchiveSource
    lateinit var localArchiveDestination: LocalArchiveDestination
    lateinit var archiveManager: ArchiveManager

    fun before() {
        rootTestDirectory = createTempDirectory(testDirNamePrefix)
        databaseFile = kotlin.io.path.createTempFile(rootTestDirectory, "database.sqlite")
        database = StorehouseDatabase(databaseFile.toString())
        localArchiveSource = LocalArchiveSource(mapOf("path" to createTempDirectory(rootTestDirectory, "localArchiveSource").toString()))
        localArchiveDestination = LocalArchiveDestination(mapOf("path" to createTempDirectory(rootTestDirectory, "localArchiveDestination").toString()))
        archiveManager = ArchiveManager(
            source = localArchiveSource,
            destination = localArchiveDestination,
            archiveRepository = ArchiveRepository(database),
            snapshotRepository = SnapshotRepository(database),
            fileRepository = FileRepository(database),
            fileMetaRepository = FileMetaRepository(database)
        )
    }

    @OptIn(ExperimentalPathApi::class)
    fun after() {
        rootTestDirectory.deleteRecursively()
    }
}