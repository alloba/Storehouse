import cli.ArgsContainer
import commands.UnrecognizedCommand
import config.RuntimeConfiguration
import database.StorehouseDatabase
import destination.local.LocalArchiveDestination
import org.slf4j.LoggerFactory
import source.local.LocalArchiveSource

fun main(args: Array<String>) {

    val logger = LoggerFactory.getLogger("MAIN")

    val argStore = ArgsContainer(args)
    if (argStore.arguments.containsKey("h") || argStore.arguments.containsKey("help")) {
        println("Help message TODO") //TODO
    }
    require(argStore.arguments.containsKey("config")) { "Missing required option [config]" }
    require(argStore.arguments.containsKey("command")) { "Missing required option [command]" }

    val runtimeConfiguration = RuntimeConfiguration.fromFilePath(argStore.arguments["config"]!!)
    val commandName = argStore.arguments["command"]!!
    val archiveManager = generateArchiveManager(runtimeConfiguration)

    val command = commands.retrieveCommand(commandName)
    if (command is UnrecognizedCommand) {
        logger.error("command $commandName not recognized as valid command. Generating UnrecognizedCommand")
    }

    val result = command.execute(archiveManager, argStore.arguments)

    println("Operation [${command.name()}]: ${result.success} - ${result.message}")
    if (!result.success) {
        throw Exception("Failed to execute command $commandName - ${result.message}")
    }
}


fun generateArchiveManager(runtimeConfiguration: RuntimeConfiguration): ArchiveManager {
    val database = StorehouseDatabase(runtimeConfiguration.databaseLocation)

    val source = when (runtimeConfiguration.sourceType) {
        "Local" -> {
            LocalArchiveSource(mapOf("path" to (runtimeConfiguration.sourceConfig["path"] ?: "")))
        }

        else -> {
            throw Exception("provided sourceType [${runtimeConfiguration.sourceType}] for storehouse is invalid")
        }
    }

    val destination = when (runtimeConfiguration.destinationType) {
        "Local" -> {
            LocalArchiveDestination(mapOf("path" to (runtimeConfiguration.destinationConfig["path"] ?: "")))
        }

        else -> {
            throw Exception("Provided destinationType [${runtimeConfiguration.destinationType}] for storehouse is invalid")
        }
    }

    return ArchiveManager(database, source, destination)
}
