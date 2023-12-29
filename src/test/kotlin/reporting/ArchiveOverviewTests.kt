package reporting

import TestHarness
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reporting.models.ArchiveOverviewModel
import kotlin.io.path.writeText

class ArchiveOverviewTests {
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
    fun `can generate a string with correct reporting info`() {
        val snapshotFiles = listOf(1, 2, 3).map { kotlin.io.path.createTempFile(harness.rootTestDirectory, "testfile_$it.txt") }
        val additionalSnapshotFiles = listOf("z").map { kotlin.io.path.createTempFile(harness.rootTestDirectory, "testfile_$it.txt") }

        snapshotFiles.forEach { it.writeText(it.toString()) }
        additionalSnapshotFiles.forEach { it.writeText(it.toString()) }


        val archive = harness.archiveManager.createNewArchive("testarchive", "testing")
        val snapshot1 = harness.archiveManager.createNewSnapshot(archive, snapshotFiles, "first snap")
        val snapshot2 = harness.archiveManager.createNewSnapshot(archive, additionalSnapshotFiles, "second snap")
        val snapshot3 = harness.archiveManager.createNewSnapshot(archive, snapshotFiles + additionalSnapshotFiles, "third snap")

        val report = ArchiveOverviewModel(archive, harness.archiveManager).toString()

        assertTrue(report.lowercase().contains("Archive ${archive.name}".lowercase()))
        assertTrue(report.lowercase().contains("Last Snapshot Created: ${snapshot3.dateCreated}".lowercase()))
        assertTrue(report.lowercase().contains("Raw Files: ${4}".lowercase()))
        assertFalse(report.lowercase().contains("Estimated Total Size: ${0}".lowercase()))
        println(report)
    }
}