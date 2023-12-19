package database

import database.entities.ArchiveEntity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Path
import java.time.OffsetDateTime
import java.util.*
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createTempDirectory
import kotlin.io.path.deleteRecursively
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ArchiveRepositoryTests {
    private var database: StorehouseDatabase? = null
    private var dbFilePath = ""
    private var testDir = ""

    @BeforeEach
    fun before() {
        val dirpath = createTempDirectory("storehouse-archive-test")
        testDir = dirpath.toString()
        dbFilePath = kotlin.io.path.createTempFile(dirpath, "database.sqlite").toString()

        database = StorehouseDatabase(dbFilePath)
    }

    @OptIn(ExperimentalPathApi::class)
    @AfterEach
    fun after() {
        Path.of(testDir).deleteRecursively()
    }

    @Test
    fun `can insert archive entity`() {
        val archiverepo = ArchiveRepository(database!!)
        archiverepo.insertArchiveEntity(ArchiveEntity("uuid", OffsetDateTime.now(), OffsetDateTime.now(), "name", "description"))
    }

    @Test
    fun `can retrieve archive entity`() {
        val archiverepo = ArchiveRepository(database!!)
        val archive = ArchiveEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "name", "description")

        archiverepo.insertArchiveEntity(archive)
        val result = archiverepo.getArchiveEntity(archive.id)

        assertEquals(archive, result)
    }

    @Test
    fun `can retrieve multiple archive entities`() {
        val archiveRepo = ArchiveRepository(database!!)
        assertTrue(archiveRepo.getAllArchiveEntities().isEmpty())
        archiveRepo.insertArchiveEntity(ArchiveEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "name1", "description"))
        archiveRepo.insertArchiveEntity(ArchiveEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "name2", "description"))

        val res = archiveRepo.getAllArchiveEntities()
        assertTrue(res.size == 2)
    }

    @Test
    fun `if no archive found return null`(){
        val archiveRepository = ArchiveRepository(database!!)
        val res = archiveRepository.getArchiveEntity("doesnt exist")
        assertTrue(res == null)
    }
    @Test
    fun `if no archives fetch all returns empty list`(){
        val archiveRepository = ArchiveRepository(database!!)
        val res = archiveRepository.getAllArchiveEntities()
        assertTrue(res.isEmpty())
    }

    @Test
    fun `can update archive`(){
        val archiveRepository = ArchiveRepository(database!!)
        val archive1 = ArchiveEntity(UUID.randomUUID().toString(), OffsetDateTime.now(), OffsetDateTime.now(), "name", "desc")
        val archive2 = ArchiveEntity(archive1.id, OffsetDateTime.MAX, OffsetDateTime.MIN, "name2", "desc2")

        archiveRepository.insertArchiveEntity(archive1)
        val res1 = archiveRepository.getArchiveEntity(archive1.id)
        assertEquals(archive1, res1)

        archiveRepository.updateArchiveEntity(archive2)
        val res2 = archiveRepository.getArchiveEntity(archive1.id)
        assertEquals(archive2, res2)
        assertNotEquals(res1, res2)
    }

    @Test
    fun `cannot update archive if not exists`() {
        val archiveRepository = ArchiveRepository(database!!)
        assertThrows<Exception> { archiveRepository.updateArchiveEntity(ArchiveEntity("asdfasdf", OffsetDateTime.now(), OffsetDateTime.now(), "name", "desc")) }
    }
}