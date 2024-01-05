package commands

import ArchiveManager
import java.nio.file.Path
import kotlin.io.path.isDirectory

class RestoreFromSnapshotCommand : CommandInterface {
    override fun execute(archiveManager: ArchiveManager, commandOptions: Map<String, String>): CommandResult {
        requiredSettings().keys.forEach {
            require(commandOptions.containsKey(it)) {"RestoreFromSnapshot -- missing required setting [$it]"}
        }
        require(commandOptions["destination"]!!.isNotBlank()) { "RestoreFromSnapshot - provided destination cannot be blank" }
        require(Path.of(commandOptions["destination"]!!).isDirectory()) { "RestoreFromSnapshot - provided destination [${commandOptions["destination"]}] must be an existing directory" }

        return try {
            archiveManager.restoreFromSnapshot(snapshotId = commandOptions["snapshot"]!!, destinationPath = Path.of(commandOptions["destination"]!!))
            CommandResult(true, "successfully restore snapshot [${commandOptions["snapshot"]}] to destination [${commandOptions["destination"]}]")
        } catch (e: Exception) {
            CommandResult(false, e.message ?: "Unspecified error while executing ${name()}")
        }
    }

    override fun allowedAliases(): List<String> {
        return listOf("restore", "restoresnap", "restoreSnap", "restoreSnapshot")
    }

    override fun name(): String {
        return "RestoreFromSnapshot"
    }

    override fun description(): CommandDescription {
        return CommandDescription("Provided a valid Snapshot ID and destination folder, will restore all files listed in the snapshot to the folder. \n")

    }

    override fun requiredSettings(): Map<String, String> {
        return mapOf(
            "snapshot" to "The ID of the snapshot to restore files from",
            "destination" to "The path to restore the snapshot to. Must be an existing directory.",
        )
    }

    override fun optionalSettings(): Map<String, String> {
        return emptyMap()
    }

    override fun examples(): List<CommandExample> {
        return listOf(CommandExample("--config ./path/to/config.json --command restore --snapshot 123-abc-def-456 --destination \"./a/valid/path/\""))
    }

}