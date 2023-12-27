package database.repo

import database.StorehouseDatabase
import database.entities.FileEntity
import toDb
import java.time.OffsetDateTime

class FileRepository(private val database: StorehouseDatabase) {

    fun insertFileEntity(fileEntity: FileEntity): FileEntity {
        val statement = database.connection.prepareStatement("insert into $FILE_TABLE ( $FILE_TABLE_FIELDS ) values ( $FILE_TABLE_FIELDS_INSERTS )")
        val copy = fileEntity.copy(dateCreated = OffsetDateTime.now(), dateUpdated = OffsetDateTime.now())
        statement.setString(1, copy.id)
        statement.setString(2, copy.dateCreated.toDb())
        statement.setString(3, copy.dateUpdated.toDb())
        statement.setString(4, copy.md5Hash)

        statement.execute()
        return copy
    }

    fun getFileEntityById(id: String): FileEntity?{
        val statement = database.connection.prepareStatement("select $FILE_TABLE_FIELDS from $FILE_TABLE where id = ?")
        statement.setString(1, id)
        val rs = statement.executeQuery()
        return if (!rs.next()) null
        else FileEntity.fromResultSet(rs)
    }

    fun getFileEntityByMd5Hash(hash: String): FileEntity? {
        val statement = database.connection.prepareStatement("select $FILE_TABLE_FIELDS from $FILE_TABLE where md5_hash = ?")
        statement.setString(1, hash)
        val rs = statement.executeQuery()
        return if (!rs.next()) null
        else FileEntity.fromResultSet(rs)
    }

    companion object {
        const val FILE_TABLE = "File"
        const val FILE_TABLE_FIELDS = " id, date_created, date_updated, md5_hash "
        const val FILE_TABLE_FIELDS_INSERTS = " ?, ?, ?, ? "
        const val FILE_TABLE_UPDATE_FIELDS = " date_created = ?, date_updated = ?, md5_hash = ? "
    }
}