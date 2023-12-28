package commands

import ArchiveOperator

interface CommandInterface {
    fun execute(archiveOperator: ArchiveOperator, commandOptions: String): Boolean
    fun allowedAliases(): List<String>
    fun name(): String
}