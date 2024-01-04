package commands

import ArchiveManager
import org.slf4j.LoggerFactory
import reporting.models.ArchiveOverviewModel

class DisplayArchiveInfoCommand : CommandInterface {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(archiveManager: ArchiveManager, commandOptions: Map<String, String>): CommandResult {
        require(commandOptions.containsKey("archive")) {"DisplayArchiveInfoCommand missing required argument: [archive]"}
        val archiveName = commandOptions["archive"]!!.trim()

        if (archiveName.isBlank()) {
            return CommandResult(false, "No archive name provided")
        }

        return try {
            logger.info("Executing command: ${name()}")

            val archive = archiveManager.getArchiveByName(archiveName)
            println(ArchiveOverviewModel(archive, archiveManager))

            logger.info("Command ${name()} success")
            CommandResult(true)
        } catch (e: Exception) {
            logger.error("Failure to complete command: ${name()}", e)
            CommandResult(false, e.message ?: "failed to complete command.")
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