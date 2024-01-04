package commands

import ArchiveManager
import java.nio.file.Path
import kotlin.io.path.isDirectory

class CreateSnapshotCommand: CommandInterface {
    override fun execute(archiveManager: ArchiveManager, commandOptions: String): CommandResult {
        require(commandOptions.contains(" ")) {"Command [${name()}] must have two arguments: 1- Archive Name, 2- Path of Snapshot source."}
        val archiveName = commandOptions.substringBefore(" ").trim()
        val path = Path.of(commandOptions.substringAfter(" ").trim())
        require(path.isDirectory()) {"Provided path [$path] must be a valid directory."}

        return try {
            val archiveEntity = archiveManager.getArchiveByName(archiveName)
            val snapshot = archiveManager.createNewSnapshot(archiveEntity, path)
            CommandResult(true, "Created snapshot for archive [$archiveName] with id [${snapshot.id}]")
        } catch (e: Exception){
            CommandResult(false, e.message?:"Generic failure in CreateSnapshotCommand")
        }
    }

    override fun allowedAliases(): List<String> {
        return listOf("createSnapshot", "cs", "createsnapshot", "createsnap")
    }

    override fun name(): String {
        return "CreateSnapshot"
    }

    override fun generateHelpInfo(): String {
        return "Create a snapshot for the specified archive. It is expected that the first word in the command option is the archive name, followed by the path to the directory that will be submitted."
    }
}