package config

import destination.ArchiveDestination
import destination.ArchiveDestinationConfig
import source.ArchiveSource
import source.ArchiveSourceConfig
import java.nio.file.Path

data class AppConfiguration (
    val sourceLocation: ArchiveSource,
    val destinationLocation: ArchiveDestination,
    val sourceConfig: ArchiveSourceConfig,
    val destinationConfig: ArchiveDestinationConfig,
    val databaseSource: Path
)