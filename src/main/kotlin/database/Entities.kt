package database

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.util.Date

abstract class DataEntity

@DatabaseTable(tableName = "archive")
data class Archive(
    @DatabaseField(columnName = "id", id = true) val id: String = "",
    @DatabaseField(columnName = "name") val name: String = "",
    @DatabaseField(columnName = "created_date") val createdDate: Date = Date(),
    @DatabaseField(columnName = "updated_date") val updatedDate: Date = Date(),
    @DatabaseField(columnName = "deleted") val deleted: Boolean = false,
    @DatabaseField(columnName = "deleted_date") val deletedDate: Date? = null
): DataEntity()

@DatabaseTable(tableName = "collection")
data class Collection(
    @DatabaseField(columnName = "id") val id: String = "",
    @DatabaseField(columnName = "name") val name: String = "",
    @DatabaseField(columnName = "created_date") val createdDate: Date = Date(),
    @DatabaseField(columnName = "updated_date") val updatedDate: Date = Date(),
    @DatabaseField(columnName = "deleted") val deleted: Boolean = false,
    @DatabaseField(columnName = "deleted_date") val deletedDate: Date? = null
): DataEntity()

@DatabaseTable(tableName = "snapshot")
data class Snapshot(
    @DatabaseField(columnName = "id", id = true) val id: String,
    @DatabaseField(columnName = "name") val name: String,
    @DatabaseField(columnName = "created_date") val createdDate: Date = Date(),
    @DatabaseField(columnName = "updated_date") val updatedDate: Date = Date(),
    @DatabaseField(columnName = "deleted") val deleted: Boolean = false,
    @DatabaseField(columnName = "deleted_date") val deletedDate: Date? = null
): DataEntity()

@DatabaseTable(tableName = "file_meta")
data class FileMeta(
    @DatabaseField(columnName = "id", id = true) val id: String = "",
    @DatabaseField(columnName = "name") val name: String = "",
    @DatabaseField(columnName = "extension") val extension: String = "",
    @DatabaseField(columnName = "created_date") val createdDate: Date = Date(),
    @DatabaseField(columnName = "updated_date") val updatedDate: Date = Date(),
    @DatabaseField(columnName = "deleted") val deleted: Boolean = false,
    @DatabaseField(columnName = "deleted_date") val deletedDate: Date? = null
): DataEntity()

@DatabaseTable(tableName = "file_raw")
data class FileRaw(
    @DatabaseField(columnName = "id", id = true) val id: String = "",
    @DatabaseField(columnName = "hash") val hash: String = "",
    @DatabaseField(columnName = "created_date") val createdDate: Date = Date(),
    @DatabaseField(columnName = "updated_date") val updatedDate: Date = Date(),
    @DatabaseField(columnName = "deleted") val deleted: Boolean = false,
    @DatabaseField(columnName = "deleted_date") val deletedDate: Date? = null
): DataEntity()
