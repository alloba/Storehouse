package source.local

import source.ArchiveSource
import java.nio.file.Path
import java.security.MessageDigest
import kotlin.io.path.fileSize
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.readBytes

class LocalArchiveSource(options: Map<String, String>) : ArchiveSource(options) {
    private val filePath: Path
    init {
        require(options.containsKey("path")) {"LocalArchiveSource options must contain a path value"}
        val filePathString = options["path"]?: throw IllegalArgumentException("LocalArchiveSource path option must be populated")
        filePath = Path.of(filePathString)
        require(Path.of(filePathString).isDirectory()) {"LocalArchiveSource path [$filePathString] must be a valid directory"}

    }
    override fun getAllFiles(): List<Path> {
        return filePath.toFile()
            .walkTopDown()
            .filter { it.isFile }
            .map { it.toPath() }
            .toList()
    }

    override fun computeMd5Hash(filePath: Path): String {
        if (!filePath.isRegularFile()) {
            throw Exception("Not a regular file, cannot compute hash - $filePath")
        }
        return MessageDigest.getInstance("SHA-256")
            .digest(filePath.readBytes())
            .fold(StringBuilder()) { sb, it -> sb.append("%02x".format(it)) }.toString()
    }

    override fun computeFileSizeBytes(filePath: Path): Long {
        if (!filePath.isRegularFile()){
            throw Exception("Not a regular file, cannot compute file size - $filePath")
        }

        return filePath.fileSize()
    }
}