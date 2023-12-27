package database.entities

import java.sql.ResultSet
import java.time.OffsetDateTime

data class FileEntity(
    val id: String,
    val dateCreated: OffsetDateTime,
    val dateUpdated: OffsetDateTime,
    val name: String,
    val fileExtension: String,
    val md5Hash: String
) {
    companion object {
        fun fromResultSet(resultSet: ResultSet): FileEntity {
            return FileEntity(
                id = resultSet.getString("id"),
                dateCreated = OffsetDateTime.parse(resultSet.getString("date_created")),
                dateUpdated = OffsetDateTime.parse(resultSet.getString("date_updated")),
                name = resultSet.getString("name"),
                fileExtension = resultSet.getString("file_extension"),
                md5Hash = resultSet.getString("md5_hash")
            )
        }
    }
}