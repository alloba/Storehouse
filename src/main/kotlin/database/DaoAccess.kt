package database

import archive.ArchiveDao
import java.sql.Connection

data class DaoManager(
    private val connection: Connection,
    val archiveDao: ArchiveDao = ArchiveDao(connection)
)