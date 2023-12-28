package destination

abstract class ArchiveDestinationConfig(private val configValues: Map<String, String>) {
    fun getConfig(): Map<String, String> {
        return this.configValues
    }

    class ArchiveDestinationConfigError(vararg errors: String) : Exception("Archive destination config errors:: [${errors.joinToString(",")}]")
}