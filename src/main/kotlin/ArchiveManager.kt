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
import kotlin.io.path.copyTo
import kotlin.io.path.createParentDirectories
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
        require(archiveRepository.getArchiveEntityByName(archiveName) == null) { "Archive $archiveName already exists. Cannot create." }

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
        require(rootPath.isDirectory()) { "Path submitted for snapshots must be a directory. Archive: ${archive.name}" }
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

    fun getFilesBySnapshotId(snapshotId: String): List<FileEntity> {
        require(snapshotRepository.getSnapshotEntity(snapshotId) != null) { "Snapshot ID not found in Storehouse [$snapshotId]" }
        val files = fileMetaRepository.getFileMetasBySnapshotId(snapshotId).map { fileRepository.getFileEntityById(it.fileId) }

        if (files.any { false }) {
            throw Exception("Snapshot [$snapshotId] contains FileMetas with references to Files that do not exist.")
        }
        return files.map { it!! }
    }

    fun restoreFromSnapshot(snapshotId: String, destinationPath: Path) {
        require(snapshotRepository.getSnapshotEntity(snapshotId) != null) { "Snapshot ID not found in Storehouse [$snapshotId]" }
        require(destinationPath.isDirectory()) { "Must provide valid directory for snapshot restore. Destination [$destinationPath] doesn't qualify." }
        val filesEntities = getFilesBySnapshotId(snapshotId)
        val filePaths = filesEntities.map { destination.retrieveFileByHash(it.md5Hash) }

        if (filePaths.any { false }) {
            throw Exception("Snapshot [$snapshotId] refers to files that could not be found in Storehouse! This is a big problem!")
        }

        filePaths.forEach {
            val targetPath = Path.of(destinationPath.toString(), it.toString()).normalize()
            targetPath.createParentDirectories()
            it!!.copyTo(targetPath)
        }
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