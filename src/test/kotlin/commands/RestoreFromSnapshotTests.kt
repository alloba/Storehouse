package commands

import TestHarness
import cli.ArgsContainer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.io.path.createTempDirectory
import kotlin.io.path.writeText
import kotlin.test.assertEquals

class RestoreFromSnapshotTests {
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
    fun `can restore from snapshot`(){
        val sourceDir = createTempDirectory(harness.rootTestDirectory, "restoretest")
        val sourceFiles = listOf(1,2,3).map { kotlin.io.path.createTempFile(sourceDir, "testFile_$it") }
        sourceFiles.forEach { it.writeText(it.toString()) }

        val archive = harness.archiveManager.createNewArchive("testarchive", "test")
        val snapshot = harness.archiveManager.createNewSnapshot(archive, sourceDir, "test snapshot")
        val targetDir = createTempDirectory(harness.rootTestDirectory, "restoredestination")

        RestoreFromSnapshotCommand().execute(
            harness.archiveManager,
            ArgsContainer(arrayOf("--command restore --snapshot ${snapshot.id} --destination $targetDir")).arguments
        )

        assertEquals(3, targetDir.toFile().walkTopDown().filter { it.isFile }.toList().size)
    }
}