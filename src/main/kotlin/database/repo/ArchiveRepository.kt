package database.repo

import database.StorehouseDatabase
import database.entities.ArchiveEntity
import toDb
import java.time.OffsetDateTime

class ArchiveRepository(private val database: StorehouseDatabase) {
    fun insertArchiveEntity(archiveEntity: ArchiveEntity): ArchiveEntity {
        val timestampEntity = archiveEntity.copy(dateCreated = OffsetDateTime.now(), dateUpdated = OffsetDateTime.now())
        val statement = database.connection.prepareStatement("insert into $ARCHIVE_TABLE ($ARCHIVE_TABLE_FIELDS) values (?, ?, ?, ?, ?)")
        statement.setString(1, timestampEntity.id)
        statement.setString(2, timestampEntity.dateCreated.toDb())
        statement.setString(3, timestampEntity.dateUpdated.toDb())
        statement.setString(4, timestampEntity.name)
        statement.setString(5, timestampEntity.description)
        statement.execute()
        return timestampEntity
    }

    fun getArchiveEntity(id: String): ArchiveEntity? {
        val query = "select $ARCHIVE_TABLE_FIELDS from $ARCHIVE_TABLE where id = ?"
        val statement = database.connection.prepareStatement(query)
        statement.setString(1, id)
        val rs = statement.executeQuery()

        return if (!rs.next()) null
        else ArchiveEntity.fromResultSet(rs)
    }

    fun getArchiveEntityByName(name: String): ArchiveEntity? {
        val query = "select $ARCHIVE_TABLE_FIELDS from $ARCHIVE_TABLE where name = ?"
        val statement = database.connection.prepareStatement(query)
        statement.setString(1, name)
        val rs = statement.executeQuery()

        return if (!rs.next()) null
        else ArchiveEntity.fromResultSet(rs)
    }

    fun getAllArchiveEntities(): List<ArchiveEntity> {
        val statement = database.connection.prepareStatement("select $ARCHIVE_TABLE_FIELDS from $ARCHIVE_TABLE")
        val rs = statement.executeQuery()

        val res = mutableListOf<ArchiveEntity>()
        while (rs.next()) {
            res.add(ArchiveEntity.fromResultSet(rs))
        }
        return res.toList()
    }

    fun updateArchiveEntity(archiveEntity: ArchiveEntity): ArchiveEntity {
        val statement = database.connection.prepareStatement("update $ARCHIVE_TABLE set $ARCHIVE_TABLE_UPDATE_FIELDS where id = ?")
        val timestampEntity = archiveEntity.copy(dateUpdated = OffsetDateTime.now())
        statement.setString(1, timestampEntity.dateCreated.toDb())
        statement.setString(2, timestampEntity.dateUpdated.toDb())
        statement.setString(3, timestampEntity.name)
        statement.setString(4, timestampEntity.description)
        statement.setString(5, timestampEntity.id)
        val res = statement.executeUpdate()

        return if (res != 0) timestampEntity else throw Exception("Failed to update ArchiveEntity: $archiveEntity")
    }

    companion object {
        const val ARCHIVE_TABLE = "Archive"
        const val ARCHIVE_TABLE_FIELDS = " id, date_created, date_updated, name, description "
        const val ARCHIVE_TABLE_UPDATE_FIELDS = " date_created = ?, date_updated = ?, name = ?, description = ? "
    }
}