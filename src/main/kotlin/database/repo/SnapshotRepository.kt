package database.repo

import database.StorehouseDatabase
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

    fun getSnapshotsForArchiveId(archiveId: String): List<SnapshotEntity> {
        val statement = database.connection.prepareStatement("select $SNAPSHOT_TABLE_FIELDS from $SNAPSHOT_TABLE where archive_id = ?")
        statement.setString(1, archiveId)

        val results = mutableListOf<SnapshotEntity>()
        val rs = statement.executeQuery()
        while (rs.next()) {
            results.add(SnapshotEntity.fromResultSet(rs))
        }
        return results.toList()
    }

    fun updateSnapshotEntity(snapshot: SnapshotEntity): SnapshotEntity {
        val statement = database.connection.prepareStatement("update $SNAPSHOT_TABLE set $SNAPSHOT_TABLE_UPDATE_FIELDS where id = ?")
        val updateEntity = snapshot.copy(dateUpdated = OffsetDateTime.now())
        statement.setString(1, updateEntity.dateCreated.toDb())
        statement.setString(2, updateEntity.dateUpdated.toDb())
        statement.setString(3, updateEntity.description)
        statement.setString(4, updateEntity.archiveId)

        statement.setString(5, snapshot.id)

        val res = statement.executeUpdate()

        return if (res != 0) updateEntity
        else throw Exception("Failed to update snapshot entity: [${snapshot.id} ${snapshot.description}]")
    }

    companion object {
        const val SNAPSHOT_TABLE = "Snapshot"
        const val SNAPSHOT_TABLE_FIELDS = " id, date_created, date_updated, description, archive_id "
        const val SNAPSHOT_TABLE_FIELDS_INSERTS = " ?, ?, ?, ?, ? "
        const val SNAPSHOT_TABLE_UPDATE_FIELDS = " date_created = ?, date_updated = ?, description = ?, archive_id = ? "
    }
}