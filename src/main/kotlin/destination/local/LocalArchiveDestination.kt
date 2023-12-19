package destination.local

import destination.ArchiveDestination
import java.nio.file.Path
import kotlin.io.path.copyTo

class LocalArchiveDestination(private val config: LocalArchiveDestinationConfig) : ArchiveDestination(config) {
    override fun copyFile(sourceFilePath: Path, rootParentPath: Path): Boolean {
        if (!sourceFilePath.startsWith(rootParentPath)) {
            throw Exception("Local archive - cannot copy file - target source [$sourceFilePath] is not a child of [$rootParentPath]")
        }

        val relativePath = rootParentPath.relativize(sourceFilePath)
        val destinationPath = Path.of(config.rootPath.toString(), relativePath.toString())

        sourceFilePath.copyTo(destinationPath)
        return true
    }

    override fun copyFiles(sourceFilePaths: List<Path>, rootParentPath: Path): Boolean {
        sourceFilePaths.forEach {
            copyFile(it, rootParentPath)
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