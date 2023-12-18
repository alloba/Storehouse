package config

import storage.ArchiveSource
import storage.ArchiveSourceConfig
import java.nio.file.Path

data class AppConfiguration (
    val sourceLocation: ArchiveSource,
    val destinationLocation: ArchiveSource,
    val sourceConfig: ArchiveSourceConfig,
    val destinationConfig: ArchiveSourceConfig,
    val databaseSource: Path
)