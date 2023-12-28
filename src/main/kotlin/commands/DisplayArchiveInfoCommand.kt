package commands

import ArchiveOperator
import reporting.models.ArchiveOverviewModel

class DisplayArchiveInfoCommand : CommandInterface {
    override fun execute(archiveOperator: ArchiveOperator, commandOptions: String): Boolean {
        val archiveName = commandOptions.trim()
        require(archiveName.isNotBlank()) { "DisplayArchiveInfoCommand - archive name must not be blank" }
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