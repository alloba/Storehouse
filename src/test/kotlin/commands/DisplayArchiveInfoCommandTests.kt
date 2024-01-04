package commands

import TestHarness
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DisplayArchiveInfoCommandTests {
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
    fun `when command executed for archive that does not exist then return failed status`(){
        val result = DisplayArchiveInfoCommand().execute(harness.archiveManager, mapOf("archive" to "asdf"))
        assertFalse(result.success)
    }

    @Test
    fun `when command executed for archive then return success`(){
        harness.archiveManager.createNewArchive("testarchive", "test")
        val result = DisplayArchiveInfoCommand().execute(harness.archiveManager, mapOf("archive" to "testarchive"))
        assertTrue(result.success)
    }
}