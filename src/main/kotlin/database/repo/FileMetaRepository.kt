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
        statement.setString(5, copy.name)
        statement.setString(6, copy.fileExtension)
        statement.setString(7, copy.fileId)
        statement.setString(8, copy.snapshotId)

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

    fun getFileMetasBySnapshotId(snapshotId: String): List<FileMetaEntity>{
        val statement = database.connection.prepareStatement(" select $FILEMETA_TABLE_FIELDS from $FILEMETA_TABLE where snapshot_id = ?")
        statement.setString(1, snapshotId)

        val rs = statement.executeQuery()
        val results = mutableListOf<FileMetaEntity>()
        while (rs.next()){
            results.add(FileMetaEntity.fromResultSet(rs))
        }
        return results.toList()
    }

    companion object {
        const val FILEMETA_TABLE = "FileMeta"
        const val FILEMETA_TABLE_FIELDS = " id, date_created, date_updated, original_path, name, file_extension, file_id, snapshot_id "
        const val FILEMETA_TABLE_FIELDS_INSERTS = " ?, ?, ?, ?, ?, ?, ?, ? "
        const val FILEMETA_TABLE_UPDATE_FIELDS = " date_created = ?, date_updated = ?, original_path = ?, name = ?, file_extension = ?, file_id = ?, snapshot_id = ? "
    }
}