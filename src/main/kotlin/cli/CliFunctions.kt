package cli

fun parseArguments(args: Array<String>): CliArguments {
    val organizedArgs = ArgsContainer(args)
    return CliArguments(
        configFilePath = organizedArgs.arguments["config"]?:"",
        commandBlock = organizedArgs.arguments["command"]?:"",
    )
}