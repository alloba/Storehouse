package database.entities

import java.sql.ResultSet
import java.time.OffsetDateTime

data class SnapshotEntity(
    val id: String,
    val dateCreated: OffsetDateTime,
    val dateUpdated: OffsetDateTime,
    val description: String,
    val archiveId: String,
    ) {
    companion object {
        fun fromResultSet(rs: ResultSet): SnapshotEntity {
            return SnapshotEntity(
                id = rs.getString("id"),
                dateCreated = OffsetDateTime.parse(rs.getString("date_created")),
                dateUpdated = OffsetDateTime.parse(rs.getString("date_updated")),
                description = rs.getString("description"),
                archiveId = rs.getString("archive_id"),
            )
        }
    }
}