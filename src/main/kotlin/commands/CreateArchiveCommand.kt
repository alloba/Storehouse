package commands

import ArchiveManager

class CreateArchiveCommand : CommandInterface {
    override fun execute(archiveManager: ArchiveManager, commandOptions: Map<String, String>): CommandResult {
        requiredSettings().keys.forEach {
            require(commandOptions.containsKey(it)) {"CreateArchive -- missing required setting [$it]"}
        }

        return try {
            archiveManager.createNewArchive(commandOptions["archive"]!!, commandOptions["description"]!!)
            CommandResult(true, "Successfully created archive [${commandOptions["archive"]}]")
        } catch (e: Exception) {
            CommandResult(false, e.message ?: "Unspecified failure")
        }
    }

    override fun allowedAliases(): List<String> {
        return listOf("createArchive", "createarchive", "newArchive", "newarchive")
    }

    override fun name(): String {
        return "CreateArchive"
    }

    override fun description(): CommandDescription {
        return CommandDescription("Creates a new archive with the given name. If an archive with the same name exists, will fail.")
    }

    override fun requiredSettings(): Map<String, String> {
        return mapOf(
            "archive" to "The name of the target archive",
            "description" to "The description of the archive that is to be created"
        )
    }

    override fun optionalSettings(): Map<String, String> {
        return emptyMap()
    }

    override fun examples(): List<CommandExample> {
        return listOf(
            CommandExample("--command createArchive --archive \"some ArchiveName\" --description \"archive description\"")
        )
    }
}
