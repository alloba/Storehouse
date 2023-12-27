import database.repo.ArchiveRepository
import database.repo.SnapshotRepository
import database.entities.ArchiveEntity
import database.entities.FileMetaEntity
import database.entities.SnapshotEntity
import database.repo.FileRepository
import destination.local.LocalArchiveDestination
import org.slf4j.LoggerFactory
import source.ArchiveSource
import java.nio.file.Path
import java.time.OffsetDateTime
import java.util.*

class ArchiveManager(
    val source: ArchiveSource,
    val destination: LocalArchiveDestination,
    val archiveRepository: ArchiveRepository,
    val snapshotRepository: SnapshotRepository,
    val fileRepository: FileRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    fun createNewArchive(archiveName: String, archiveDescription: String): ArchiveEntity {
        logger.info("Creating new archive with name: $archiveName")
        return archiveRepository.insertArchiveEntity(
            ArchiveEntity(
                UUID.randomUUID().toString(),
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                archiveName,
                archiveDescription
            )
        )
    }

    fun getArchiveByName(archiveName: String): ArchiveEntity {
        return archiveRepository.getArchiveEntityByName(archiveName) ?: throw Exception("Archive with name [$archiveName] not found.")
    }

    fun createNewSnapshot(archive: ArchiveEntity, files: List<Path>, description: String = ""): SnapshotEntity {
        val snapshotEntity = snapshotRepository.insertSnapshotEntity(SnapshotEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), description, archive.id))

        logger.info("Submitting ${files.size} files for Archive: ${archive.name} - Snapshot: ${snapshotEntity.id}")
        val fileMetas = submitToSnapshot(snapshotEntity, files)

        return snapshotEntity
    }

    private fun submitToSnapshot(snapshot: SnapshotEntity, pathObjects: List<Path>): List<FileMetaEntity> {
        TODO()
    }

    fun tidyArchive(archive: ArchiveEntity) {
        TODO()
    }

    fun tidySnapshot(snapshot: SnapshotEntity) {
        TODO()
    }
}