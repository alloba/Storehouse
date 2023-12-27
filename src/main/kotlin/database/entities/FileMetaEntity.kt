package database.entities

import java.sql.ResultSet
import java.time.OffsetDateTime

data class FileMetaEntity (
    val id: String,
    val dateCreated: OffsetDateTime,
    val dateUpdated: OffsetDateTime,
    val snapshotPath: String,
    val fileId: String,
    val snapshotId: String
) {
    companion object {
        fun fromResultSet(rs: ResultSet): FileMetaEntity{
            return FileMetaEntity(
                id = rs.getString("id"),
                dateCreated = OffsetDateTime.parse(rs.getString("date_created")),
                dateUpdated = OffsetDateTime.parse(rs.getString("date_updated")),
                snapshotPath = rs.getString("snapshot_path"),
                fileId = rs.getString("file_id"),
                snapshotId = rs.getString("snapshot_id")
            )
        }
    }
}