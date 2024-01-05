package commands

import TestHarness
import cli.ArgsContainer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CreateArchiveCommandTests {
    val harness = TestHarness()

    @BeforeEach
    fun before() {
        harness.before()
    }

    @AfterEach
    fun after() {
        harness.after()
    }

    @Test
    fun `can create archive`() {
        val res = CreateArchiveCommand().execute(harness.archiveManager, ArgsContainer(arrayOf("--command createArchive --archive testyyy --description something")).arguments)
        assertTrue(res.success)
        assertTrue(harness.archiveManager.getArchiveByName("testyyy").name == "testyyy")
    }

    @Test
    fun `cannot create archive with same name as existing one`() {
        harness.archiveManager.createNewArchive("z", "zzz")
        val res = CreateArchiveCommand().execute(harness.archiveManager, ArgsContainer(arrayOf("--command createArchive --archive z --description something")).arguments)
        assertFalse(res.success)
    }

    @Test
    fun `cannot create archive with missing name`() {
        harness.archiveManager.createNewArchive("z", "zzz")
        assertThrows<IllegalArgumentException> { CreateArchiveCommand().execute(harness.archiveManager, ArgsContainer(arrayOf("--command createArchive  --description something")).arguments) }
    }

    @Test
    fun `cannot create archive with missing description`() {
        harness.archiveManager.createNewArchive("z", "zzz")
        assertThrows<IllegalArgumentException> { CreateArchiveCommand().execute(harness.archiveManager, ArgsContainer(arrayOf("--command createArchive --archive z ")).arguments) }
    }
}