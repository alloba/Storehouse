package destination.local

import destination.ArchiveDestinationConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Path
import kotlin.io.path.createTempDirectory
import kotlin.io.path.deleteIfExists

class LocalArchiveDestinationConfigTests {
    @Test
    fun `when the root path is invalid then throw exception`() {
        assertThrows<ArchiveDestinationConfig.ArchiveDestinationConfigError> { LocalArchiveDestinationConfig(Path.of("asdf/asdf/asdf/asdf/asd/fa/sdf/asdf/a/dfs/das")) }
    }

    @Test
    fun `when the root path is not a directory then throw exception`() {
        val testFile = kotlin.io.path.createTempFile("storehouse-configfile")
        assertThrows<ArchiveDestinationConfig.ArchiveDestinationConfigError> { LocalArchiveDestinationConfig(testFile) }
        testFile.deleteIfExists()
    }

    @Test
    fun `success when providing an existing directory`() {
        val testDir = createTempDirectory("storehouse-configdir")
        LocalArchiveDestinationConfig(testDir)
        testDir.deleteIfExists()
    }
}