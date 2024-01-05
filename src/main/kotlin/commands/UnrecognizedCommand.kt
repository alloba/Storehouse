package commands

import ArchiveManager

class UnrecognizedCommand : CommandInterface {
    override fun execute(archiveManager: ArchiveManager, commandOptions: Map<String, String>): CommandResult {
        return CommandResult(false, "No recognized command found - no action taken")
    }

    override fun allowedAliases(): List<String> {
        return emptyList()
    }

    override fun name(): String {
        return "Unrecognized Command"
    }

    override fun description(): CommandDescription {
        return CommandDescription("This command should never be used. This is the fallback command that is returned when no recognized command is passed in from user input.")
    }

    override fun requiredSettings(): Map<String, String> {
        return emptyMap()
    }

    override fun optionalSettings(): Map<String, String> {
        return emptyMap()
    }

    override fun examples(): List<CommandExample> {
        return emptyList()
    }
}