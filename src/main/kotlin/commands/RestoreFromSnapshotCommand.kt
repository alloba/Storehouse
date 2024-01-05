package commands

import ArchiveManager
import java.nio.file.Path
import kotlin.io.path.isDirectory

class RestoreFromSnapshotCommand: CommandInterface {
    override fun execute(archiveManager: ArchiveManager, commandOptions: Map<String, String>): CommandResult {
        require(commandOptions.containsKey("snapshot")) {"RestoreFromSnapshotCommand - missing required argument [snapshot]"}
        require(commandOptions.containsKey("destination")) {"RestoreFromSnapshotCommand - missing required argument [destination]"}
        require(commandOptions["destination"]!!.isNotBlank()) {"RestoreFromSnapshotCommand - provided destination cannot be blank"}
        require(Path.of(commandOptions["destination"]!!).isDirectory()) {"RestoreFromSnapshotCommand - provided destination [${commandOptions["destination"]}] must be an existing directory" }

        return try{
            archiveManager.restoreFromSnapshot(snapshotId = commandOptions["snapshot"]!!, destinationPath = Path.of(commandOptions["destination"]!!))
            CommandResult(true, "successfully restore snapshot [${commandOptions["snapshot"]}] to destination [${commandOptions["destination"]}]")
        } catch (e: Exception){
            CommandResult(false, e.message?: "Unspecified error while executing ${name()}")
        }
    }

    override fun allowedAliases(): List<String> {
        return listOf("restore", "restoresnap", "restoreSnap", "restoreSnapshot")
    }

    override fun name(): String {
        return "RestoreFromSnapshot"
    }

    override fun generateHelpInfo(): String {
        return "Provided a valid Snapshot ID and destination folder, will restore all files listed in the snapshot to the folder. \n" +
                "--command restore --snapshot 123-abc --destination \"./a/valid/path/\""
    }

}