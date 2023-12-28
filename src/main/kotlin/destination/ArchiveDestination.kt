package destination

import java.nio.file.Path

abstract class ArchiveDestination(configuration: ArchiveDestinationConfig) {
    abstract fun copyFile(sourceFilePath: Path, md5Hash: String): Boolean
    abstract fun copyFiles(pathHashPairs: List<Pair<Path, String>>, rootParentPath: Path): Boolean
    abstract fun listFiles(): List<Path>
    abstract fun retrieveFileByHash(hash: String): Path?
}