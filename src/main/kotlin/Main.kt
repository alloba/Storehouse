import cli.ArgsContainer
import commands.UnrecognizedCommand
import commands.allEnabledCommands
import config.RuntimeConfiguration
import database.StorehouseDatabase
import destination.local.LocalArchiveDestination
import source.local.LocalArchiveSource

fun main(args: Array<String>) {
    val argStore = ArgsContainer(args)
    if (argStore.arguments.containsKey("h") || argStore.arguments.containsKey("help")) {
        println(generateHelpMessage())
        return
    }
    require(argStore.arguments.containsKey("config")) { "Missing required option [config]" }
    require(argStore.arguments.containsKey("command")) { "Missing required option [command]" }

    val runtimeConfiguration = RuntimeConfiguration.fromFilePath(argStore.arguments["config"]!!)
    val commandName = argStore.arguments["command"]!!
    val archiveManager = generateArchiveManager(runtimeConfiguration)

    val command = commands.retrieveCommand(commandName)
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

fun generateHelpMessage(): String =
    "Storehouse is a simple archive system developed for preserving directory snapshots.\n" +
    "A command and a configuration file must always be provided. The configuration file contains source/destination config, and the path to the database. \n" +
    "Refer to RuntimeConfiguration.kt for structure.\n" +
    "Commands:\n" +
    allEnabledCommands
        .filter { it !is UnrecognizedCommand }.joinToString("\n\n") {
"""${it.name()} 
    Description: ${it.description()} 
    Aliases: ${it.allowedAliases()}
    Required Settings: ${it.requiredSettings()} 
    Optional Settings: ${it.optionalSettings()} 
    Examples: ${it.examples()}
""".trimIndent()
            }
