package commands

fun retrieveCommand(commandName: String): CommandInterface {
    return commandMap[commandName] ?: UnrecognizedCommand()
}

val allEnabledCommands = listOf(CreateArchiveCommand(), CreateSnapshotCommand(), DisplayArchiveInfoCommand(), RestoreFromSnapshotCommand(), UnrecognizedCommand())

private val commandMap: Map<String, CommandInterface> =
    allEnabledCommands
        .map { command -> (command.allowedAliases() + command.name()).associateWith { command } }
        .reduce { left, right -> left + right }

