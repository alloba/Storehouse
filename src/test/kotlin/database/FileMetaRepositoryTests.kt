package database

import TestHarness
import database.entities.ArchiveEntity
import database.entities.FileEntity
import database.entities.FileMetaEntity
import database.entities.SnapshotEntity
import database.repo.FileMetaRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.sqlite.SQLiteException
import java.time.OffsetDateTime
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FileMetaRepositoryTests {
    val testHarness = TestHarness()

    @BeforeEach
    fun before() {
        testHarness.before()
    }

    @AfterEach
    fun after() {
        testHarness.after()
    }

    @Test
    fun `cannot insert filemeta into db without associated file and snapshot`() {
        val input = FileMetaEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "snappath", "fileId", "", "", "snapshotId")
        assertThrows<SQLiteException> { FileMetaRepository(testHarness.database).insertFileMeta(input) }
    }

    @Test
    fun `can insert filemeta into db with dependencies`() {
        val inputArchive = testHarness.archiveOperator.archiveRepository.insertArchiveEntity(ArchiveEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "name", "desc"))
        val inputSnapshot =
            testHarness.archiveOperator.snapshotRepository.insertSnapshotEntity(SnapshotEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "desc", inputArchive.id))
        val inputFile = testHarness.archiveOperator.fileRepository.insertFileEntity(FileEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "hash", 0))

        val input = FileMetaEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "snappath", "", "", inputFile.id, inputSnapshot.id)
        val output = FileMetaRepository(testHarness.database).insertFileMeta(input)

        assertEquals(input.id, output.id)
        assertEquals(input.fileId, output.fileId)
        assertEquals(input.snapshotId, output.snapshotId)
        assertEquals(input.snapshotPath, output.snapshotPath)
        assertNotEquals(input.dateCreated, output.dateCreated)
        assertNotEquals(input.dateUpdated, output.dateUpdated)
    }

    @Test
    fun `can get filemeta from db`() {
        val inputArchive = testHarness.archiveOperator.archiveRepository.insertArchiveEntity(ArchiveEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "name", "desc"))
        val inputSnapshot =
            testHarness.archiveOperator.snapshotRepository.insertSnapshotEntity(SnapshotEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "desc", inputArchive.id))
        val inputFile = testHarness.archiveOperator.fileRepository.insertFileEntity(FileEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "hash", 0))

        val input1 = FileMetaEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "snappath", "", "", inputFile.id, inputSnapshot.id)
        val output1 = testHarness.archiveOperator.fileMetaRepository.insertFileMeta(input1)
        val output2 = testHarness.archiveOperator.fileMetaRepository.getFileMetaById(output1.id)!!

        assertEquals(output1, output2)
    }

    @Test
    fun `return null if no matching id found in db`() {
        assertNull(testHarness.archiveOperator.fileMetaRepository.getFileMetaById("blah"))
    }
}