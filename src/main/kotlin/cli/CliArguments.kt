package cli

import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.isReadable

data class CliArguments(
    val configFilePath: String,  // eventually this would be an optional field, if a different source were to be provided.
    val commandName: String,
    val commandString: String
) {
    init {
        require(!Path.of(configFilePath).isDirectory() && Path.of(configFilePath).isReadable()) { "CliArgumentsModel - provided path $configFilePath must be a valid file" }
        require(commandName.isNotBlank()) { "CliArgumentsModel - provided command must not be blank" }
    }
}