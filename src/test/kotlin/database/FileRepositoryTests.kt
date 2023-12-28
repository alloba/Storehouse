package database

import TestHarness
import database.entities.FileEntity
import database.repo.FileRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class FileRepositoryTests {
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
    fun `can create file in db`() {
        val input = FileEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "hash", 123)
        val fileRepository = FileRepository(testHarness.database)
        val output = fileRepository.insertFileEntity(input)

        assertEquals(input.id, output.id)
        assertEquals(input.md5Hash, output.md5Hash)
        assertNotEquals(input.dateCreated, output.dateCreated)
        assertEquals(input.sizeBytes, output.sizeBytes)
    }

    @Test
    fun `can retrieve file by id`() {
        val input = FileEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "hash", 0)
        val fileRepository = FileRepository(testHarness.database)
        val saved = fileRepository.insertFileEntity(input)
        val result = fileRepository.getFileEntityById(input.id)

        assertEquals(saved, result)
    }

    @Test
    fun `can retrieve file by hash`() {
        val input = FileEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "hash", 0)
        val fileRepository = FileRepository(testHarness.database)
        val saved = fileRepository.insertFileEntity(input)
        val result = fileRepository.getFileEntityByMd5Hash(saved.md5Hash)!!

        assertEquals(saved.id, result.id)
    }

    @Test
    fun `return null when no file found in db by id`() {
        val fileRepository = FileRepository(testHarness.database)
        assertTrue(fileRepository.getFileEntityById("blah") == null)
    }

    @Test
    fun `return null when no file found in db by hash`() {
        val fileRepository = FileRepository(testHarness.database)
        assertTrue(fileRepository.getFileEntityByMd5Hash("blah") == null)
    }
}