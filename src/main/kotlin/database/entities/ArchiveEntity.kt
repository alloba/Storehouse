package database.entities

import java.sql.ResultSet
import java.time.OffsetDateTime

data class ArchiveEntity(
    val id: String,
    val dateCreated: OffsetDateTime,
    val dateUpdated: OffsetDateTime,
    val name: String,
    val description: String
) {
    companion object {
        fun fromResultSet(rs: ResultSet): ArchiveEntity{
            return ArchiveEntity(
                id = rs.getString("id"),
                dateCreated = OffsetDateTime.parse(rs.getString("date_created")),
                dateUpdated = OffsetDateTime.parse(rs.getString("date_updated")),
                name = rs.getString("name"),
                description = rs.getString("description"),
            )
        }
    }
}