package commands

import ArchiveManager
import org.slf4j.LoggerFactory
import reporting.models.ArchiveOverviewModel

class DisplayArchiveInfoCommand : CommandInterface {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(archiveManager: ArchiveManager, commandOptions: Map<String, String>): CommandResult {
        requiredSettings().keys.forEach {
            require(commandOptions.containsKey(it)) {"DisplayArchiveInfo -- missing required setting [$it]"}
        }

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

    override fun description(): CommandDescription {
        return CommandDescription("Displays information about the named archive in the Storehouse. Will return a failed CommandResult if the archive does not exist.")
    }

    override fun requiredSettings(): Map<String, String> {
        return mapOf("archive" to "The name of the archive to get information about.")
    }

    override fun optionalSettings(): Map<String, String> {
        return emptyMap()
    }

    override fun examples(): List<CommandExample> {
        return listOf( CommandExample("--config ./path/to/config.json --command archiveInfo --archive archiveName") )
    }
}