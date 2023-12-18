package source

abstract class ArchiveSourceConfig(private val configOptions: Map<String, Any>) {
    fun getConfig(): Map<String, Any> {
        return this.configOptions
    }

    class StorageValidationException(vararg errors: String): Exception("Storage validation errors:: [${errors.joinToString(",")}]")
}
