package commands

import ArchiveOperator
import reporting.models.ArchiveOverviewModel

class DisplayArchiveInfoCommand: CommandInterface {
    override fun execute(archiveOperator: ArchiveOperator, commandOptions: String): Boolean {

        val archiveName = commandOptions.trim()
        val archive = archiveOperator.getArchiveByName(archiveName)
        println(ArchiveOverviewModel(archive, archiveOperator))
        return true
    }

    override fun allowedAliases(): List<String> {
        return listOf("archiveinfo", "archiveInfo", "displayArchive", "displayArchiveInfo", "da", "DA")
    }

    override fun name(): String {
        return "DisplayArchiveInformation"
    }
}