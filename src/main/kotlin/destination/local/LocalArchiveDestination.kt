package destination.local

import destination.ArchiveDestination
import java.io.File
import java.nio.file.Path
import kotlin.io.path.copyTo
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile

class LocalArchiveDestination(options: Map<String, String>) : ArchiveDestination(options) {
    private val filePath: Path

    init {
        require(options.containsKey("path")) { "LocalArchiveDestination options must contain a path" }
        val filePathString = options["path"] ?: throw IllegalArgumentException("LocalArchiveDestination path option must contain a value")
        filePath = Path.of(filePathString)
        require(filePath.isDirectory()) { "LocalArchiveDestination provided path [$filePathString] must be a valid directory" }
    }

    /**
     * Copy the given file object to the destination path defined in the destination configuration.
     * A file with any path is considered fair game, but the destination that it will be saved to will always be the root directory.
     * Tracking the file is expected to be handled via the file meta -> file relationship within the sqlite database after this point.
     */
    override fun submitFile(sourceFilePath: Path, md5Hash: String): Boolean {
        require(!sourceFilePath.isDirectory()) { "File [$sourceFilePath] cannot be a directory" }

        val newFilePath = Path.of(filePath.toString() + File.separator + md5Hash)
        sourceFilePath.copyTo(newFilePath)
        return true
    }

    override fun submitFiles(pathHashPairs: List<Pair<Path, String>>, rootParentPath: Path): Boolean {
        pathHashPairs.forEach {
            submitFile(it.first, it.second)
        }
        return true
    }

    override fun listFiles(): List<Path> {
        return filePath.toFile()
            .walkTopDown()
            .filter { it.isFile }
            .map { it.toPath() }
            .toList()
    }

    override fun retrieveFileByHash(hash: String): Path? {
        val target = Path.of(filePath.toString() + File.separator + hash)

        return if (target.isRegularFile()) target
        else null
    }
}