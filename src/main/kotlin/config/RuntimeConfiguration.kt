package config

import com.google.gson.Gson
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.isReadable
import kotlin.io.path.readText

data class RuntimeConfiguration(
    val databaseLocation: String = "",
    val sourceType: String,
    val sourceConfig: Map<String, String>,
    val destinationType: String,
    val destinationConfig: Map<String, String>,
) {

    companion object {
        fun fromJsonString(json: String): RuntimeConfiguration {
            return Gson().fromJson(json, RuntimeConfiguration::class.java)
        }

        fun fromFilePath(configFilePath: String): RuntimeConfiguration {
            val configPath = Path.of(configFilePath)
            require(!configPath.isDirectory() && configPath.isReadable()) { "Config file [${configPath}] path must be readable file." }

            return fromJsonString(configPath.readText())
        }
    }
}