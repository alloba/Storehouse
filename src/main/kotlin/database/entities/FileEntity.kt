package database.entities

import java.sql.ResultSet
import java.time.OffsetDateTime

data class FileEntity(
    val id: String,
    val dateCreated: OffsetDateTime,
    val dateUpdated: OffsetDateTime,
    val md5Hash: String,
    val sizeBytes: Long
) {
    companion object {
        fun fromResultSet(resultSet: ResultSet): FileEntity {
            return FileEntity(
                id = resultSet.getString("id"),
                dateCreated = OffsetDateTime.parse(resultSet.getString("date_created")),
                dateUpdated = OffsetDateTime.parse(resultSet.getString("date_updated")),
                md5Hash = resultSet.getString("md5_hash"),
                sizeBytes = resultSet.getLong("size_bytes")
            )
        }
    }
}