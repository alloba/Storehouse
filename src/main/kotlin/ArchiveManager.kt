import database.ArchiveRepository
import database.StorehouseDatabase
import database.entities.ArchiveEntity
import database.entities.SnapshotEntity
import destination.local.LocalArchiveDestination
import source.ArchiveSource
import java.nio.file.Path
import java.time.OffsetDateTime
import java.util.*

class ArchiveManager(
    val source: ArchiveSource,
    val destination: LocalArchiveDestination,
    val archiveRepository: ArchiveRepository,
) {
    fun createNewArchive(archiveName: String, archiveDescription: String): ArchiveEntity {
        return archiveRepository.insertArchiveEntity(
            ArchiveEntity(
                UUID.randomUUID().toString(),
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                archiveName,
                archiveDescription)
        )
    }

    fun getArchiveByName(archiveName: String): ArchiveEntity {
        return archiveRepository.getArchiveEntityByName(archiveName) ?: throw Exception("Archive with name [$archiveName] not found.")
    }

    fun createNewSnapshot(archive: ArchiveEntity): SnapshotEntity {
        TODO()
    }

    fun submitToSnapshot(snapshot: SnapshotEntity, pathObjects: List<Path>) {
        TODO()
    }

    fun tidyArchive(archive: ArchiveEntity) {
        TODO()
    }

    fun tidySnapshot(snapshot: SnapshotEntity) {
        TODO()
    }
}