package database.entities

import java.sql.ResultSet
import java.time.OffsetDateTime

data class SchemaMigrationEntity (val id: String, val filename: String, val description: String, val dateExecuted: OffsetDateTime){
    companion object{
        fun fromResultSet(rs: ResultSet): SchemaMigrationEntity {
            return SchemaMigrationEntity(
                id = rs.getString("id"),
                filename = rs.getString("filename"),
                description = rs.getString("description"),
                dateExecuted = OffsetDateTime.parse(rs.getString("dateExecuted"))
            )
        }
    }
}