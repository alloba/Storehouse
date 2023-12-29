package commands

import ArchiveManager

interface CommandInterface {
    fun execute(archiveManager: ArchiveManager, commandOptions: String): CommandResult
    fun allowedAliases(): List<String>
    fun name(): String
    fun generateHelpInfo(): String
}