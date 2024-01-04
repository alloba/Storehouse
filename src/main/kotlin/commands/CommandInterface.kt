package commands

import ArchiveManager

//TODO - not sure that i'm happy with this interface. lends itself to heavy string parsing on something that should be a bit more abstract than that.
interface CommandInterface {
    fun execute(archiveManager: ArchiveManager, commandOptions: Map<String, String>): CommandResult
    fun allowedAliases(): List<String>
    fun name(): String
    fun generateHelpInfo(): String
}