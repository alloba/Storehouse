package commands

import ArchiveOperator
import reporting.models.ArchiveOverviewModel

class DisplayArchiveInfoCommand: CommandInterface {
    override fun execute(archiveOperator: ArchiveOperator, rawCommandString: String): Boolean {

        val matchedCommand = (allowedAliases() + name())
            .firstOrNull { rawCommandString.startsWith(it) }
            ?: throw Exception("Could not interpret command string for DisplayArchiveInfoCommand")
        val archiveName = rawCommandString.substringAfter(matchedCommand).trim()

        val archive = archiveOperator.getArchiveByName(archiveName)
        println(ArchiveOverviewModel(archive, archiveOperator))
        return true
    }

    override fun allowedAliases(): List<String> {
        return listOf("archive info", "archiveInfo", "displayArchive", "displayArchiveInfo", "da", "DA")
    }

    override fun name(): String {
        return "DisplayArchiveInformation"
    }
}