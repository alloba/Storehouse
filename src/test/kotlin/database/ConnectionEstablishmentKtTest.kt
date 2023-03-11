package database

import org.junit.jupiter.api.Test

class ConnectionEstablishmentKtTest {
    @Test
    fun testGetMemoryConnectionAndCreateTables() {
        val conn = getSqliteConnection(":memory:")
        conn.use {
            val dao = DaoAccess(it)
            dao.createTables()
        }
    }
}