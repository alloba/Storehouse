package commands

import ArchiveOperator

interface CommandInterface {
    fun execute(archiveOperator: ArchiveOperator, commandOptions: String): CommandResult
    fun allowedAliases(): List<String>
    fun name(): String
    fun generateHelpInfo(): String
}