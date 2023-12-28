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
        statement.setLong(5, copy.sizeBytes)

        statement.execute()
        return copy
    }

    fun getFileEntityById(id: String): FileEntity? {
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

    fun getFileEntitiesByArchiveId(archiveId: String): List<FileEntity> {
        val statement = database.connection.prepareStatement(
            """
            select F.id, F.date_created, f.date_updated, F.md5_hash, f.size_bytes
            from File F
                join FileMeta FM on F.id = FM.file_id
                join Snapshot SN on SN.id = FM.snapshot_id
                join Archive AR on SN.archive_id = AR.id
            where AR.id = ?
            group by F.id
        """.trimIndent()
        )
        statement.setString(1, archiveId)

        val results = mutableListOf<FileEntity>()
        val rs = statement.executeQuery()
        while (rs.next()) {
            results.add(FileEntity.fromResultSet(rs))
        }
        return results.toList()
    }

    companion object {
        const val FILE_TABLE = "File"
        const val FILE_TABLE_FIELDS = " id, date_created, date_updated, md5_hash, size_bytes "
        const val FILE_TABLE_FIELDS_INSERTS = " ?, ?, ?, ?, ? "
        const val FILE_TABLE_UPDATE_FIELDS = " date_created = ?, date_updated = ?, md5_hash = ?, size_bytes = ? "
    }
}