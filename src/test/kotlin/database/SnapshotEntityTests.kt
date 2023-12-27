package database

import TestHarness
import database.entities.ArchiveEntity
import database.entities.SnapshotEntity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.sqlite.SQLiteException
import java.time.OffsetDateTime
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class SnapshotEntityTests {
    val harness = TestHarness()

    @BeforeEach
    fun before(){
        harness.before()
    }

    @AfterEach
    fun after(){
        harness.after()
    }

    @Test
    fun `snapshot must be associated with existing archive`(){
        val snapshotRepository = SnapshotRepository(harness.database)
        val input = SnapshotEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "desc", "123")

        assertThrows<SQLiteException> { snapshotRepository.insertSnapshotEntity(input) }
    }

    @Test
    fun `can insert snapshot into db`(){
        val snapshotRepository = SnapshotRepository(harness.database)
        val archiveRepository = ArchiveRepository(harness.database)

        val archiveInput = ArchiveEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "", "")
        val archiveOutput = archiveRepository.insertArchiveEntity(archiveInput)
        val snapshotInput = SnapshotEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "snapdesc", archiveInput.id)
        val snapshotOutput = snapshotRepository.insertSnapshotEntity(snapshotInput)

        assertEquals(snapshotInput.id, snapshotOutput.id)
        assertEquals(snapshotInput.description, snapshotOutput.description)
        assertEquals(snapshotInput.archiveId, snapshotOutput.archiveId)

        assertNotEquals(snapshotInput.dateCreated, snapshotOutput.dateCreated)
        assertNotEquals(snapshotInput.dateUpdated, snapshotOutput.dateUpdated)
    }

    @Test
    fun `multiple snapshots can refer to the same archive`(){
        val snapshotRepository = SnapshotRepository(harness.database)
        val archiveRepository = ArchiveRepository(harness.database)
        val archiveInput = ArchiveEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "", "")
        val archiveOutput = archiveRepository.insertArchiveEntity(archiveInput)

        val snapshotInput1 = SnapshotEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "snapdesc", archiveInput.id)
        val snapshotInput2 = SnapshotEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "snapdesc", archiveInput.id)
        snapshotRepository.insertSnapshotEntity(snapshotInput1)
        snapshotRepository.insertSnapshotEntity(snapshotInput2)
    }
}