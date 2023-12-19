package destination

abstract class ArchiveDestinationConfig(private val configValues: Map<String, Any>) {
    fun getConfig(): Map<String, Any> {
        return this.configValues
    }

    class ArchiveDestinationConfigError(vararg errors: String) : Exception("Archive destination config errors:: [${errors.joinToString(",")}]")
}