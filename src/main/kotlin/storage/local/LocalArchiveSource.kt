package storage.local

import storage.ArchiveSource
import java.nio.file.Path
import java.security.MessageDigest
import kotlin.io.path.readBytes

class LocalArchiveSource(private val config: LocalArchiveSourceConfig): ArchiveSource(config) {
    override fun getAllFiles(): List<Path> {
        return config.filepath.toFile()
            .walkTopDown()
            .filter { it.isFile }
            .map { it.toPath() }
            .toList()
    }

    override fun computeMd5Hash(filepath: Path): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(filepath.readBytes())
            .fold(StringBuilder()) { sb, it -> sb.append( "%02x".format(it)) }.toString()
    }
}