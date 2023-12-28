package cli

fun parseArguments(args: Array<String>): CliArgumentsModel {
    val organizedArgs = createArgMap(args)
    return createStoreFromMap(organizedArgs)
}

private fun createArgMap(args: Array<String>): Map<String, String> {
    if (args.isEmpty()) {
        throw Exception("No arguments provided to storehouse. No action taken.")
    }

    val parsedArguments = mutableMapOf<String, String>()

    var i = 0
    var parsing = true
    while (parsing && i < args.size) {
        if (args[i].startsWith("--")) {
            val longArg = args[i].substringAfter("--")
            if (longArg.isEmpty()) {
                i += 1
                continue
            }

            if (i == args.size - 1 || args[i + 1].startsWith("-")) {
                i += 1
                continue
            }
            val longArgVal = args[i + 1]
            parsedArguments[longArg] = longArgVal
            i += 2
        } else if (args[i].startsWith("-")) {
            val shortArg = args[i].substringAfter("-")
            if (shortArg.isEmpty()) {
                i += 1
                continue
            }
            parsedArguments[shortArg] = "."
            i += 1
        } else if (!args.slice(i..<args.size).any { it.startsWith("-") }) {
            parsedArguments["remainder"] = args.slice(i..<args.size).joinToString(" ")
            parsing = false
            i += 1
        }
    }
    return parsedArguments.toMap()
}

private fun createStoreFromMap(argMap: Map<String, String>): CliArgumentsModel {
    require(argMap["config"] != null) { "Missing required CLI argument [config]" }
    require(argMap["command"] != null) { "Missing required CLI argument [command]" }
    return CliArgumentsModel(
        configFilePath = argMap["config"] ?: "",
        commandName = argMap["command"] ?: "",
        commandString = argMap["remainder"] ?: "",
    )
}