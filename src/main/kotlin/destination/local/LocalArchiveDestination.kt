package destination.local

import destination.ArchiveDestination
import java.io.File
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.copyTo

class LocalArchiveDestination(private val config: LocalArchiveDestinationConfig) : ArchiveDestination(config) {
    /**
     * Copy the given file object to the destination path defined in the destination configuration.
     * A file with any path is considered fair game, but the destination that it will be saved to will always be the root directory.
     * Tracking the file is expected to be handled via the file meta -> file relationship within the sqlite database after this point.
     */
    @OptIn(ExperimentalPathApi::class)
    override fun copyFile(sourceFilePath: Path, md5Hash: String): Boolean {
        val newFilePath = Path.of(config.rootPath.toString() + File.separator + md5Hash + ".archive")
        sourceFilePath.copyTo(newFilePath)
        return true
    }

    override fun copyFiles(pathHashPairs: List<Pair<Path, String>>, rootParentPath: Path): Boolean {
        pathHashPairs.forEach {
            copyFile(it.first, it.second)
        }
        return true
    }

    override fun listFiles(): List<Path> {
        return config.rootPath.toFile()
            .walkTopDown()
            .filter { it.isFile }
            .map { it.toPath() }
            .toList()
    }
}