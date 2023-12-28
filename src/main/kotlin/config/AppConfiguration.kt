package config

import destination.ArchiveDestination
import source.ArchiveSource
import java.nio.file.Path

data class AppConfiguration (
    val sourceLocation: ArchiveSource,
    val destinationLocation: ArchiveDestination,
    val databaseSource: Path
)