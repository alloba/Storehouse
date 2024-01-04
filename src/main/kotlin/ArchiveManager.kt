import database.StorehouseDatabase
import database.entities.ArchiveEntity
import database.entities.FileEntity
import database.entities.FileMetaEntity
import database.entities.SnapshotEntity
import database.repo.ArchiveRepository
import database.repo.FileMetaRepository
import database.repo.FileRepository
import database.repo.SnapshotRepository
import destination.ArchiveDestination
import org.slf4j.LoggerFactory
import source.ArchiveSource
import java.nio.file.Path
import java.time.OffsetDateTime
import java.util.*
import kotlin.io.path.extension
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.io.path.pathString

class ArchiveManager(
    val database: StorehouseDatabase,
    val source: ArchiveSource,
    val destination: ArchiveDestination,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val archiveRepository = ArchiveRepository(database)
    private val snapshotRepository = SnapshotRepository(database)
    private val fileRepository = FileRepository(database)
    private val fileMetaRepository = FileMetaRepository(database)

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

    fun createNewSnapshot(archive: ArchiveEntity, rootPath: Path, description: String = ""): SnapshotEntity {
        require(rootPath.isDirectory() ){"Path submitted for snapshots must be a directory. Archive: ${archive.name}"}
        val snapshotEntity = snapshotRepository.insertSnapshotEntity(SnapshotEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), description, archive.id))
        val sourceFiles = rootPath.toFile()
            .walkTopDown()
            .filter { it.isFile }
            .map { it.toPath() }
            .toList()

        logger.info("Submitting ${sourceFiles.size} files for Archive: ${archive.name} - Snapshot: ${snapshotEntity.id}")
        val fileMetas = submitToSnapshot(snapshotEntity, sourceFiles)

        archiveRepository.updateArchiveEntity(archive) //set new timestamp
        return snapshotEntity
    }

    fun getArchiveByName(archiveName: String): ArchiveEntity {
        return archiveRepository.getArchiveEntityByName(archiveName) ?: throw Exception("Archive with name [$archiveName] not found.")
    }

    fun getSnapshotsByArchive(archiveEntity: ArchiveEntity): List<SnapshotEntity> {
        return snapshotRepository.getSnapshotsForArchiveId(archiveEntity.id)
    }

    fun getFilesByArchive(archiveEntity: ArchiveEntity): List<FileEntity> {
        return fileRepository.getFileEntitiesByArchiveId(archiveEntity.id)
    }

    fun getFileMetasBySnapshotId(snapshotId: String): List<FileMetaEntity> {
        return fileMetaRepository.getFileMetasBySnapshotId(snapshotId)
    }

    private fun submitToSnapshot(snapshot: SnapshotEntity, objects: List<Path>): List<FileMetaEntity> {
        val filemetas = mutableListOf<FileMetaEntity>()
        for (file in objects) {
            val hash = source.computeMd5Hash(file)
            val byteSize = source.computeFileSizeBytes(file)
            val existingFileId = fileRepository.getFileEntityByMd5Hash(hash)?.id

            if (existingFileId != null) {
                fileMetaRepository.insertFileMeta(
                    FileMetaEntity(
                        UUID.randomUUID().toString(),
                        OffsetDateTime.now(),
                        OffsetDateTime.now(),
                        file.pathString,
                        file.name,
                        file.extension,
                        existingFileId,
                        snapshot.id
                    )
                ).let { filemetas.add(it) }
            } else {
                destination.submitFile(file, hash)
                val fileEntity = fileRepository.insertFileEntity(
                    FileEntity(
                        UUID.randomUUID().toString(),
                        OffsetDateTime.now(),
                        OffsetDateTime.now(),
                        hash,
                        byteSize
                    )
                )
                fileMetaRepository.insertFileMeta(
                    FileMetaEntity(
                        UUID.randomUUID().toString(),
                        OffsetDateTime.now(),
                        OffsetDateTime.now(),
                        file.pathString,
                        file.name,
                        file.extension,
                        fileEntity.id,
                        snapshot.id
                    )
                ).let { filemetas.add(it) }
            }
        }
        return filemetas
    }
}