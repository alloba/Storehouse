package cli

class ArgsContainer(args: Array<String>) {
    val arguments: Map<String, String>

    init {
        val res = parseSimple(args)
        arguments = res
    }

    private fun parseSimple(args: Array<String>): Map<String, String> {
        val sections = args.joinToString(separator = " ").split("--")

        return sections
            .associate {
                if (it.contains(" ")) {
                    it.substringBefore(" ").trim() to it.substringAfter(" ").trim()
                } else {
                    it.trim() to ""
                }
            }
            .filter { it.key.isNotBlank() }
    }

    @Deprecated("This is probably more technically correct as an implementation, but it's just too complicated for what I actually need.")
    private fun parse(args: Array<String>): Pair<Map<String, String>, String> {
        if (args.isEmpty()) {
            return Pair(emptyMap(), "")
        }

        val parsedArguments = mutableMapOf<String, String>()
        var free = ""

        var i = 0
        var parsing = true
        while (parsing && i < args.size) {
            if (args[i].startsWith("--")) {
                val longFlag = args[i].substringAfter("--")
                i += 1
                var longVal = ""
                if (longFlag.isEmpty()) {
                    continue
                }

                while (i < args.size && !args[i].startsWith("-")) {
                    longVal += " " + args[i].trim()
                    i += 1
                }
                parsedArguments[longFlag] = longVal.trim()
            } else if (args[i].startsWith("-")) {
                if (args[i].length == 1) {
                    i += 1
                    continue
                }
                val bits = args[i].substringAfter("-").substringBefore(" ").toCharArray().map { it.toString() }
                bits.forEach { parsedArguments[it] = "" }
                i += 1
                var shortVal = ""
                while (i < args.size && !args[i].startsWith("-")) {
                    shortVal += " " + args[i].trim()
                    i += 1
                }
                parsedArguments[bits.last()] = shortVal.trim()
            } else if (!args.slice(i..<args.size).any { it.startsWith("-") }) {
                free = args.slice(i..<args.size).joinToString(" ") { it.trim() }
                parsing = false
            }
        }
        return Pair(parsedArguments.toMap(), free)
    }
}