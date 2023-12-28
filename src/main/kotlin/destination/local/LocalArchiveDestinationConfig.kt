package destination.local

import destination.ArchiveDestinationConfig
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

class LocalArchiveDestinationConfig(val rootPath: Path) : ArchiveDestinationConfig(mapOf("path" to rootPath.toString())) {
    init {
        val errors = mutableListOf<String>()
        if (!rootPath.exists()) {
            errors.add("Local archive destination root path [$rootPath] does not exist")
        }
        if (!rootPath.isDirectory()) {
            errors.add("Local archive destination root path [$rootPath] is not a directory.")
        }

        if (errors.isNotEmpty()) {
            throw ArchiveDestinationConfigError(*errors.toTypedArray())
        }
    }
}
