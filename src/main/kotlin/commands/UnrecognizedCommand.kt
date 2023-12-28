package commands

import ArchiveOperator

class UnrecognizedCommand: CommandInterface {
    override fun execute(archiveOperator: ArchiveOperator, commandOptions: String): Boolean {
        throw Exception("Unrecognized command provided - no action will be taken")
    }

    override fun allowedAliases(): List<String> {
        return emptyList()
    }

    override fun name(): String {
        return "Unrecognized Command"
    }
}