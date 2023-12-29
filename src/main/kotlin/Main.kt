import config.RuntimeConfiguration
import database.StorehouseDatabase
import destination.local.LocalArchiveDestination
import source.local.LocalArchiveSource

fun main(args: Array<String>) {
    val argumentStore = cli.parseArguments(args)
    val runtimeConfiguration = RuntimeConfiguration.fromFilePath(argumentStore.configFilePath)
    val archiveManager = generateArchiveManager(runtimeConfiguration)

    val commandName = argumentStore.commandName
    val commandVal = argumentStore.commandString
    val command = commands.retrieveCommand(commandName)

    command.execute(archiveManager, commandVal)
}


fun generateArchiveManager(runtimeConfiguration: RuntimeConfiguration): ArchiveManager {
    val database = StorehouseDatabase(runtimeConfiguration.databaseLocation)

    val source = when (runtimeConfiguration.sourceType) {
        "Local" -> {
            LocalArchiveSource(mapOf("path" to (runtimeConfiguration.sourceConfig["path"]?:"")))
        }

        else -> {
            throw Exception("provided sourceType [${runtimeConfiguration.sourceType}] for storehouse is invalid")
        }
    }

    val destination = when (runtimeConfiguration.destinationType) {
        "Local" -> {
            LocalArchiveDestination(mapOf("path" to (runtimeConfiguration.destinationConfig["path"]?:"")))
        }

        else -> {
            throw Exception("Provided destinationType [${runtimeConfiguration.destinationType}] for storehouse is invalid")
        }
    }

    return ArchiveManager(database, source, destination)
}
