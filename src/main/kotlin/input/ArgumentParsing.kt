package input

import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option

class ArgumentsContainer {

    @Option(
        name = "-v",
        aliases = ["--vault"],
        usage = "The path to the file vault. Where all submitted files are stored.",
        required = true
    )
    var archiveLocation = ""

    @Option(
        name = "-d",
        aliases = ["--database", "--database-location"],
        usage = "The path to the database file. Is assumed to be the same as archiveLocation if not provided",
        required = false
    )
    var databaseLocation = ""
        get() = if (field == "") archiveLocation else field

    @Argument
    var unnamedArguments = mutableListOf<String>()

    fun getCommand() = unnamedArguments[0]

    fun getInputs() = unnamedArguments.subList(1, unnamedArguments.size)

    override fun toString(): String {
        return "{\n" +
                "\tArchive Location: $archiveLocation, \n" +
                "\tDatabase Location: $databaseLocation, \n" +
                "\tUnnamed Arguments: $unnamedArguments \n" +
                "\tCommand: ${getCommand()} \n" +
                "}"
    }
}


fun parseArgs(args: Array<String>): ArgumentsContainer {
    val argumentsContainer = ArgumentsContainer()
    CmdLineParser(argumentsContainer).parseArgument(args.toList())
    if (argumentsContainer.unnamedArguments.size == 0) {
        throw Exception("Missing primary command in provided arguments: $args")
    }

    return argumentsContainer
}

