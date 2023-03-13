package archive

import java.sql.Connection
import java.time.Instant

class ArchiveDao(private val connection: Connection) {
    private val insertArchiveSql = "insert into archive(id, name, created_date, updated_date, deleted, delete_date) values (?,?,?,?,?,?);"
    private val getArchiveByIdSql = "select id, name, created_date, updated_date, deleted, delete_date from archive where id like ?;"

    fun insertArchive(archiveEntity: ArchiveEntity): ArchiveEntity {
        require(archiveEntity.id != "")

        val preparedStatement = connection.prepareStatement(insertArchiveSql)
        preparedStatement.setString(1, archiveEntity.id)
        preparedStatement.setString(2, archiveEntity.name)
        preparedStatement.setString(3, archiveEntity.createdDate.toString())
        preparedStatement.setString(4, archiveEntity.updatedDate.toString())
        preparedStatement.setInt(5, if (archiveEntity.deleted) 1 else 0)
        preparedStatement.setString(6, archiveEntity.deleteDate.toString())

        val row = preparedStatement.executeUpdate()
        if (row != 1) throw Exception("failed to insert archive into database: $archiveEntity")

        return getArchiveById(archiveEntity.id)
    }

    fun getArchiveById(id: String): ArchiveEntity {
        val preppedStatement = connection.prepareStatement(getArchiveByIdSql)
        preppedStatement.setString(1, id)

        val resultSet = preppedStatement.executeQuery() ?: throw Exception("No archive fetched with id: $id")
        while (resultSet.next()) {
            return ArchiveEntity(
                id = resultSet.getString(1),
                name = resultSet.getString(2),
                createdDate = Instant.parse(resultSet.getString(3)),
                updatedDate = Instant.parse(resultSet.getString(4)),
                deleted = resultSet.getInt(5) != 0,
                deleteDate = Instant.parse(resultSet.getString(6))
            )
        }

        throw Exception("no results found for archive id: $id")
    }
}