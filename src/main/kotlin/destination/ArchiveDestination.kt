package destination

import java.nio.file.Path

abstract class ArchiveDestination(configuration: ArchiveDestinationConfig) {
    abstract fun copyFile(sourceFilePath: Path): Boolean
    abstract fun copyFiles(sourceFilePaths: List<Path>, rootParentPath: Path): Boolean
    abstract fun listFiles(): List<Path>
}