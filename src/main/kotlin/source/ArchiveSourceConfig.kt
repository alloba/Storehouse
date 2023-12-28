package source

abstract class ArchiveSourceConfig(private val configValues: Map<String, String>) {
    fun getConfig(): Map<String, String> {
        return this.configValues
    }

    class ArchiveSourceConfigError(vararg errors: String) : Exception("Archive source config errors:: [${errors.joinToString(",")}]")
}
