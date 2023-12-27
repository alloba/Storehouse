package database.repo

import database.StorehouseDatabase
import database.entities.FileMetaEntity
import toDb
import java.time.OffsetDateTime

class FileMetaRepository(val database: StorehouseDatabase) {
    fun insertFileMeta(fileMetaEntity: FileMetaEntity): FileMetaEntity {
        val statement = database.connection.prepareStatement("insert into $FILEMETA_TABLE ( $FILEMETA_TABLE_FIELDS ) values ( $FILEMETA_TABLE_FIELDS_INSERTS )")
        val copy = fileMetaEntity.copy(dateCreated = OffsetDateTime.now(), dateUpdated = OffsetDateTime.now())
        statement.setString(1, copy.id)
        statement.setString(2, copy.dateCreated.toDb())
        statement.setString(3, copy.dateUpdated.toDb())
        statement.setString(4, copy.snapshotPath)
        statement.setString(5, copy.fileId)
        statement.setString(6, copy.snapshotId)

        statement.execute()
        return copy
    }

    fun getFileMetaById(id: String): FileMetaEntity? {
        val statement = database.connection.prepareStatement("select $FILEMETA_TABLE_FIELDS from $FILEMETA_TABLE where id = ?")
        statement.setString(1, id)

        val rs = statement.executeQuery()
        return if (!rs.next()) null
        else FileMetaEntity.fromResultSet(rs)
    }

    companion object {
        const val FILEMETA_TABLE = "FileMeta"
        const val FILEMETA_TABLE_FIELDS = " id, date_created, date_updated, snapshot_path, file_id, snapshot_id "
        const val FILEMETA_TABLE_FIELDS_INSERTS = " ?, ?, ?, ?, ?, ? "
        const val FILEMETA_TABLE_UPDATE_FIELDS = " date_created = ?, date_updated = ?, snapshot_path = ?, file_id = ?, snapshot_id = ? "
    }
}