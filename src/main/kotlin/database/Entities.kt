package database

import java.util.Date

data class Archive(
    val id: String,
    val name: String,
    val createdDate: Date,
    val updatedDate: Date,
    val deleted: Boolean,
    val deletedDate: Date?
)
data class Collection(
    val id: String,
    val name: String,
    val createdDate: Date,
    val updatedDate: Date,
    val deleted: Boolean,
    val deletedDate: Date?
)
data class Snapshot(
    val id: String,
    val name: String,
    val createdDate: Date,
    val updatedDate: Date,
    val deleted: Boolean,
    val deletedDate: Date?
)
data class FileMeta(
    val id: String,
    val name: String,
    val extension: String,
    val createdDate: Date,
    val updatedDate: Date,
    val deleted: Boolean,
    val deletedDate: Date?
)
data class FileRaw(
    val id: String,
    val hash: String,
    val createdDate: Date,
    val updatedDate: Date,
    val deleted: Boolean,
    val deletedDate: Date?
)
