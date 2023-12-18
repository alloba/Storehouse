package database.entities

import java.sql.ResultSet
import java.time.OffsetDateTime

data class SchemaMigrationEntity (val id: String, val filename: String, val dateExecuted: OffsetDateTime){
    companion object{
        fun fromResultSet(rs: ResultSet): SchemaMigrationEntity {
            return SchemaMigrationEntity(
                id = rs.getString("id"),
                filename = rs.getString("filename"),
                dateExecuted = OffsetDateTime.parse(rs.getString("date_executed"))
            )
        }
    }
}