package config

import com.google.gson.Gson

data class ConfigModel(
    val databaseLocation: String = "",
    val sourceType: String,
    val sourceConfig: Map<String, String>,
    val destinationType: String,
    val destinationConfig: Map<String, String>,
) {

    companion object {
        fun fromJsonString(json: String): ConfigModel {
            return Gson().fromJson(json, ConfigModel::class.java)
        }
    }
}