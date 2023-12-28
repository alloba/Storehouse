package commands

import ArchiveOperator
import org.slf4j.LoggerFactory
import reporting.models.ArchiveOverviewModel

class DisplayArchiveInfoCommand : CommandInterface {
    val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(archiveOperator: ArchiveOperator, commandOptions: String): CommandResult {
        val archiveName = commandOptions.trim()
        if (archiveName.isBlank()){
            return CommandResult(false, "No archive name provided")
        }

        return try{
            val archive = archiveOperator.getArchiveByName(archiveName)
            println(ArchiveOverviewModel(archive, archiveOperator))
            CommandResult(true)
        } catch (e: Exception){
            logger.error("unable to complete command operation", e)
            CommandResult(false, e.message?:"failed to complete command.")
        }
    }

    override fun allowedAliases(): List<String> {
        return listOf("archiveinfo", "archiveInfo", "displayArchive", "displayArchiveInfo", "da", "DA")
    }

    override fun name(): String {
        return "DisplayArchiveInformation"
    }

    override fun generateHelpInfo(): String {
        return "Displays information about the named archive in the Storehouse. Will return a failed CommandResult if the archive does not exist."
    }
}