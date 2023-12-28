package commands

fun interpretCommandString(command: String): Pair<CommandInterface, String> {
    val commandObject = commandMap
        .filter { command.startsWith(it.key) }
        .firstNotNullOfOrNull { it.value }
        ?: throw Exception("No valid command parsed from string [$command]")
    return Pair(commandObject, command)
}

fun retrieveCommand(commandName: String): CommandInterface {
    return commandMap[commandName]?:UnrecognizedCommand()
}

private val commandMap: Map<String, CommandInterface> =
    listOf(DisplayArchiveInfoCommand(), UnrecognizedCommand())
    .map {command -> (command.allowedAliases() + command.name()).associateWith { command } }
    .reduce { left, right -> left + right }

