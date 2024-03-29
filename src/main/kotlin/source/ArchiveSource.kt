package source

import java.nio.file.Path

/**
 * Generic interface for a file storage location.
 * Some of these type signatures are subject to change, especially once I start looking at S3 as a target.
 * Also, just guessing at necessary methods. So those are subject to change as well.
 */
abstract class ArchiveSource(private val options: Map<String, String>) {
    abstract fun getAllFiles(): List<Path>

    abstract fun computeMd5Hash(filePath: Path): String

    abstract fun computeFileSizeBytes(filePath: Path): Long

    fun getOptions(): Map<String, String> {
        return options
    }
}