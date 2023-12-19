package source

abstract class ArchiveSourceConfig(private val configValues: Map<String, Any>) {
    fun getConfig(): Map<String, Any> {
        return this.configValues
    }

    class ArchiveSourceConfigError(vararg errors: String) : Exception("Archive source config errors:: [${errors.joinToString(",")}]")
}
