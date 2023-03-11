package database

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

class DaoAccess(private val connection: ConnectionSource) {
    val archiveDao: Dao<Archive, String> = DaoManager.createDao(connection, Archive::class.java)
    fun createTables() {
        TableUtils.createTableIfNotExists(connection, Archive::class.java)
    }
}