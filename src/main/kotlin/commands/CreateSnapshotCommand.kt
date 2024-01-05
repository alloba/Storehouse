package commands

import ArchiveManager
import java.nio.file.Path
import kotlin.io.path.isDirectory

class CreateSnapshotCommand : CommandInterface {
    override fun execute(archiveManager: ArchiveManager, commandOptions: Map<String, String>): CommandResult {
        requiredSettings().keys.forEach {
            require(commandOptions.containsKey(it)) {"CreateSnapshot -- missing required setting [$it]"}
        }

        val archiveName = commandOptions["archive"]!!.trim()
        val pathVal = commandOptions["path"]!!.trim()
        val path = Path.of(pathVal)
        require(path.isDirectory()) { "Provided path [$path] must be a valid directory." }

        return try {
            val archiveEntity = archiveManager.getArchiveByName(archiveName)
            val snapshot = archiveManager.createNewSnapshot(archiveEntity, path)
            CommandResult(true, "Created snapshot for archive [$archiveName] with id [${snapshot.id}]")
        } catch (e: Exception) {
            CommandResult(false, e.message ?: "Generic failure in CreateSnapshotCommand")
        }
    }

    override fun allowedAliases(): List<String> {
        return listOf("createSnapshot", "cs", "createsnapshot", "createsnap")
    }

    override fun name(): String {
        return "CreateSnapshot"
    }

    override fun description(): CommandDescription {
        return CommandDescription("Create a snapshot for the specified archive.")
    }

    override fun requiredSettings(): Map<String, String> {
        return mapOf(
            "archive" to "Name of archive to create the snapshot for",
            "path" to "Path of source directory for snapshot"
        )
    }

    override fun optionalSettings(): Map<String, String> {
        return emptyMap()
    }

    override fun examples(): List<CommandExample> {
        return listOf(CommandExample("--config ./path/to/config.json --command createSnapshot --archive archiveName --path ./some/directory/path"))
    }
}