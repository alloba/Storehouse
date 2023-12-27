import database.StorehouseDatabase
import destination.local.LocalArchiveDestination
import destination.local.LocalArchiveDestinationConfig
import source.local.LocalArchiveSource
import source.local.LocalArchiveSourceConfig
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

    fun before() {
        rootTestDirectory = createTempDirectory(testDirNamePrefix)
        databaseFile = kotlin.io.path.createTempFile(rootTestDirectory, "database.sqlite")
        database = StorehouseDatabase(databaseFile.toString())
        localArchiveSource = LocalArchiveSource(LocalArchiveSourceConfig(createTempDirectory(rootTestDirectory, "localArchiveSource")))
        localArchiveDestination = LocalArchiveDestination(LocalArchiveDestinationConfig(createTempDirectory(rootTestDirectory, "localArchiveDestination")))
    }

    @OptIn(ExperimentalPathApi::class)
    fun after() {
        rootTestDirectory.deleteRecursively()
    }
}