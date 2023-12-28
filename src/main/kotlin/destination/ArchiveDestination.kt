package destination

import java.nio.file.Path

abstract class ArchiveDestination(private val options: Map<String, String>) {
    abstract fun submitFile(sourceFilePath: Path, md5Hash: String): Boolean
    abstract fun submitFiles(pathHashPairs: List<Pair<Path, String>>, rootParentPath: Path): Boolean
    abstract fun listFiles(): List<Path>
    abstract fun retrieveFileByHash(hash: String): Path?
    fun getOptions() = options
}