package commands

import ArchiveOperator

interface CommandInterface {
    fun execute(archiveOperator: ArchiveOperator, rawCommandString: String): Boolean
    fun allowedAliases(): List<String>
    fun name(): String
}