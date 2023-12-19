package database

import database.entities.ArchiveEntity
import toDb

class ArchiveRepository(private val database: StorehouseDatabase) {
    fun insertArchiveEntity(archiveEntity: ArchiveEntity): ArchiveEntity{
        val statement = database.connection.prepareStatement("insert into $ARCHIVE_TABLE ($ARCHIVE_TABLE_FIELDS) values (?, ?, ?, ?, ?)")
        statement.setString(1, archiveEntity.id)
        statement.setString(2, archiveEntity.dateCreated.toDb())
        statement.setString(3, archiveEntity.dateUpdated.toDb())
        statement.setString(4, archiveEntity.name)
        statement.setString(5, archiveEntity.description)
        statement.execute()
        return archiveEntity
    }

    fun getArchiveEntity(id: String): ArchiveEntity? {
        val query = "select $ARCHIVE_TABLE_FIELDS from $ARCHIVE_TABLE where id = ?"
        val statement = database.connection.prepareStatement(query)
        statement.setString(1, id)
        val rs = statement.executeQuery()

        return if (! rs.next()) null
        else ArchiveEntity.fromResultSet(rs)
    }

    fun getAllArchiveEntities(): List<ArchiveEntity> {
        val statement = database.connection.prepareStatement("select $ARCHIVE_TABLE_FIELDS from $ARCHIVE_TABLE")
        val rs = statement.executeQuery()

        val res = mutableListOf<ArchiveEntity>()
        while (rs.next()){
            res.add(ArchiveEntity.fromResultSet(rs))
        }
        return res.toList()
    }

    fun updateArchiveEntity(archiveEntity: ArchiveEntity): ArchiveEntity {
        val statement = database.connection.prepareStatement("update $ARCHIVE_TABLE set $ARCHIVE_TABLE_UPDATE_FIELDS where id = ?")
        statement.setString(1, archiveEntity.dateCreated.toDb())
        statement.setString(2, archiveEntity.dateUpdated.toDb())
        statement.setString(3, archiveEntity.name)
        statement.setString(4, archiveEntity.description)
        statement.setString(5, archiveEntity.id)
        val res = statement.executeUpdate()

        return if (res != 0) archiveEntity else throw Exception("Failed to update ArchiveEntity: $archiveEntity")
    }

    companion object {
        const val ARCHIVE_TABLE = "Archive"
        const val ARCHIVE_TABLE_FIELDS = " id, date_created, date_updated, name, description "
        const val ARCHIVE_TABLE_UPDATE_FIELDS = " date_created = ?, date_updated = ?, name = ?, description = ? "
    }
}