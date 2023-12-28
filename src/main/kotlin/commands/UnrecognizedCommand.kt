package commands

import ArchiveOperator

class UnrecognizedCommand : CommandInterface {
    override fun execute(archiveOperator: ArchiveOperator, commandOptions: String): CommandResult {
        return CommandResult(false, "No recognized command found - no action taken")
    }

    override fun allowedAliases(): List<String> {
        return emptyList()
    }

    override fun name(): String {
        return "Unrecognized Command"
    }

    override fun generateHelpInfo(): String {
        return "This command should never be used. This is the fallback command that is returned when no recognized command is passed in from user input."
    }
}