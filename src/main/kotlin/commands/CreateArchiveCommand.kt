package commands

import ArchiveManager

class CreateArchiveCommand: CommandInterface {
    override fun execute(archiveManager: ArchiveManager, commandOptions: Map<String, String>): CommandResult {
        require(commandOptions.containsKey("archive")) {"CreateArchive -- missing required option [archive]"}
        require(commandOptions.containsKey("description")) {"CreateArchive -- missing required option [description]"}

        return try {
            archiveManager.createNewArchive(commandOptions["archive"]!!, commandOptions["description"]!!)
            CommandResult(true, "Successfully created archive [${commandOptions["archive"]}]")
        } catch (e: Exception){
            CommandResult(false, e.message?:"Unspecified failure")
        }
    }

    override fun allowedAliases(): List<String> {
        return listOf("createArchive", "createarchive", "newArchive", "newarchive")
    }

    override fun name(): String {
        return "CreateArchive"
    }

    override fun generateHelpInfo(): String {
        return "Creates a new archive with the given name. If an archive with the same name exists, will fail. \n" +
                "--command createArchive --archive \"some ArchiveName\" --description \"archive description\""
    }
}