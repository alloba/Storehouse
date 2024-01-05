import com.google.gson.Gson
import config.RuntimeConfiguration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.createTempDirectory
import kotlin.io.path.writeText
import kotlin.test.assertTrue

class MainTests {
    val harness = TestHarness()

    var runtimeFile = Path.of("")

    @BeforeEach
    fun before() {
        harness.before()
        runtimeFile = kotlin.io.path.createTempFile(harness.rootTestDirectory, "config.json")

        val runtimeConfigString = Gson().toJson(
            RuntimeConfiguration(
                databaseLocation = harness.databaseFile.toString(),
                sourceType = "Local",
                sourceConfig = mapOf("path" to harness.localArchiveSource.getOptions()["path"]!!),
                destinationType = "Local",
                destinationConfig = mapOf("path" to harness.localArchiveDestination.getOptions()["path"]!!),
            )
        )

        runtimeFile.writeText(runtimeConfigString)
    }

    @AfterEach
    fun after() {
        harness.after()
    }

    @Test
    fun `can create new archive`() {
        main(arrayOf("--config $runtimeFile --command createArchive --archive", "testArchive --description aDesc"))
        assertTrue(harness.archiveManager.getArchiveByName("testArchive").name == "testArchive")
    }

    @Test
    fun `can create new snapshot`() {
        val targetDir = createTempDirectory(harness.rootTestDirectory, "snapshotdir")
        val targetFile = kotlin.io.path.createTempFile(targetDir, "targetFile")
        targetFile.writeText("asdf")
        val archive = harness.archiveManager.createNewArchive("testArchive", "a desc")

        main(arrayOf("--config $runtimeFile --command createSnapshot --archive testArchive --path $targetDir"))
        assertTrue(harness.archiveManager.getSnapshotsByArchive(archive).size == 1)
    }

    @Test
    fun `can display archive information`() {
        val targetDir = createTempDirectory(harness.rootTestDirectory, "snapshotdir")
        val targetFile = kotlin.io.path.createTempFile(targetDir, "targetFile")
        targetFile.writeText("asdf")
        val archive = harness.archiveManager.createNewArchive("testArchive", "a desc")
        val snapshot = harness.archiveManager.createNewSnapshot(archive, targetDir)

        main(arrayOf("--config $runtimeFile --command archiveInfo --archive testArchive"))
        //if this completes assume success
    }
}