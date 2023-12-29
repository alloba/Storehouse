import com.google.gson.Gson
import config.RuntimeConfiguration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.writeText
import kotlin.test.assertEquals

class MainKtTest {

    val harness = TestHarness()
    lateinit var configJson: String
    lateinit var configJsonFilePath: Path

    @BeforeEach
    fun before() {
        harness.before()
        configJson = Gson().toJson(
            RuntimeConfiguration(
                databaseLocation = harness.databaseFile.toString(),
                sourceType = "Local",
                sourceConfig = mapOf("path" to harness.localArchiveSource.getOptions()["path"]!!), //TODO - really gotta clean up this config stuff.
                destinationType = "Local",
                destinationConfig = mapOf("path" to harness.localArchiveDestination.getOptions()["path"]!!),
            )
        )
        configJsonFilePath = kotlin.io.path.createTempFile(harness.rootTestDirectory, "configurationfile.json")
        configJsonFilePath.writeText(configJson)
    }

    @AfterEach
    fun after() {
        harness.after()
    }


    @Test
    fun placeholderTest() {
        assertEquals(1, 1)
    }

    @Test
    fun `can execute info display command`() {
        val args = arrayOf("--config", configJsonFilePath.toString(), "--command", "DA", "test")
        harness.archiveManager.createNewArchive("test", "test archive")
        main(args)
    }

}