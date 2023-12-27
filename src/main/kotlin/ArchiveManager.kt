import database.repo.ArchiveRepository
import database.repo.SnapshotRepository
import database.entities.ArchiveEntity
import database.entities.FileEntity
import database.entities.FileMetaEntity
import database.entities.SnapshotEntity
import database.repo.FileMetaRepository
import database.repo.FileRepository
import destination.ArchiveDestination
import org.slf4j.LoggerFactory
import source.ArchiveSource
import java.nio.file.Path
import java.time.OffsetDateTime
import java.util.*
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.pathString

class ArchiveManager(
    val source: ArchiveSource,
    val destination: ArchiveDestination,
    val archiveRepository: ArchiveRepository,
    val snapshotRepository: SnapshotRepository,
    val fileRepository: FileRepository,
    val fileMetaRepository: FileMetaRepository
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

    private fun submitToSnapshot(snapshot: SnapshotEntity, objects: List<Path>): List<FileMetaEntity> {
        val filemetas = mutableListOf<FileMetaEntity>()
        for (file in objects){
            val hash = source.computeMd5Hash(file)
            val existingFileId = fileRepository.getFileEntityByMd5Hash(hash)?.id

            if (existingFileId != null ){
                fileMetaRepository.insertFileMeta(
                    FileMetaEntity(
                        UUID.randomUUID().toString(),
                        OffsetDateTime.now(),
                        OffsetDateTime.now(),
                        file.pathString,
                        existingFileId,
                        snapshot.id)
                ).let { filemetas.add(it) }
            } else {
                destination.copyFile(file)
                val fileEntity = fileRepository.insertFileEntity(
                    FileEntity(
                        UUID.randomUUID().toString(),
                        OffsetDateTime.now(),
                        OffsetDateTime.now(),
                        file.name,
                        file.extension,
                        hash)
                )
                fileMetaRepository.insertFileMeta(
                    FileMetaEntity(
                        UUID.randomUUID().toString(),
                        OffsetDateTime.now(),
                        OffsetDateTime.now(),
                        file.pathString,
                        fileEntity.id,
                        snapshot.id)
                ).let { filemetas.add(it) }
            }
        }
        return filemetas
    }

    fun tidyArchive(archive: ArchiveEntity) {
        TODO()
    }

    fun tidySnapshot(snapshot: SnapshotEntity) {
        TODO()
    }
}