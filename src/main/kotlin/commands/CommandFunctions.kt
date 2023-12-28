package commands

fun retrieveCommand(commandName: String): CommandInterface {
    return commandMap[commandName] ?: UnrecognizedCommand()
}

private val commandMap: Map<String, CommandInterface> =
    listOf(DisplayArchiveInfoCommand(), UnrecognizedCommand())
        .map { command -> (command.allowedAliases() + command.name()).associateWith { command } }
        .reduce { left, right -> left + right }

