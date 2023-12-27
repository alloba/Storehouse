package database

import database.entities.SnapshotEntity
import toDb
import java.time.OffsetDateTime

class SnapshotRepository(private val database: StorehouseDatabase) {

    fun insertSnapshotEntity(snapshot: SnapshotEntity): SnapshotEntity {
        val statement = database.connection.prepareStatement(" insert into $SNAPSHOT_TABLE ($SNAPSHOT_TABLE_FIELDS) values ($SNAPSHOT_TABLE_FIELDS_INSERTS)")
        val timestamp = OffsetDateTime.now()
        statement.setString(1, snapshot.id)
        statement.setString(2, timestamp.toDb())
        statement.setString(3, timestamp.toDb())
        statement.setString(4, snapshot.description)
        statement.setString(5, snapshot.archiveId)

        statement.execute()
        return SnapshotEntity(snapshot.id, timestamp, timestamp, snapshot.description, snapshot.archiveId)
    }

    fun getSnapshotEntity(id: String): SnapshotEntity? {
        val statement = database.connection.prepareStatement("select $SNAPSHOT_TABLE_FIELDS from $SNAPSHOT_TABLE where id = ?")
        statement.setString(1, id)

        val rs = statement.executeQuery()
        return if (!rs.next()) null
        else SnapshotEntity.fromResultSet(rs)
    }

    companion object {
        const val SNAPSHOT_TABLE = "Snapshot"
        const val SNAPSHOT_TABLE_FIELDS = " id, date_created, date_updated, description, archive_id "
        const val SNAPSHOT_TABLE_FIELDS_INSERTS = " ?, ?, ?, ?, ? "
    }
}