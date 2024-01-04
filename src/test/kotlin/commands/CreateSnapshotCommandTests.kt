package commands

import TestHarness
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Path
import kotlin.io.path.writeText
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateSnapshotCommandTests {
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
    fun `can create snapshot for existing archive`(){
        val tempFiles = listOf(1,2,3).map { Path.of(harness.rootTestDirectory.toString() + File.separator + it.toString()) }
        tempFiles.forEach { it.writeText(it.toString()) }

        val testArchive = harness.archiveManager.createNewArchive("testarchive", "testarchive")
        val commandResult = CreateSnapshotCommand().execute(harness.archiveManager, "testarchive ${harness.rootTestDirectory}")

        assertEquals(true, commandResult.success)
        assertTrue(harness.archiveManager.getSnapshotsByArchive(testArchive).size == 1)
        assertTrue(harness.archiveManager.getFileMetasBySnapshotId(harness.archiveManager.getSnapshotsByArchive(testArchive)[0].id).size >= 3) //snapshot contains the db itself, so +1 to expected.
    }
}