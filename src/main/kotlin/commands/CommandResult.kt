package commands

data class CommandResult(
    val success: Boolean,
    val message: String = ""
) {
    init {
        if (!success)
            require(message.isNotBlank()){"Failed command results must contain a non-blank status message"}
    }
}