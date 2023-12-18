package source.local

import source.ArchiveSourceConfig
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

class LocalArchiveSourceConfig(val filepath: Path): ArchiveSourceConfig(mapOf("filepath" to filepath)) {
    init {
        val errors = mutableListOf<String>()
        if (! filepath.exists()){
            errors.add("Does not exist in filesystem - $filepath.")
        }
        if (! filepath.isDirectory()){
            errors.add("Not a valid directory - $filepath.")
        }

        if (errors.isNotEmpty()){
            throw StorageValidationException(*errors.toTypedArray())
        }
    }
}