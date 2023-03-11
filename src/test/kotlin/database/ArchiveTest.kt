package database

import com.j256.ormlite.support.ConnectionSource
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.util.*
import kotlin.test.assertEquals

class ArchiveTest {

    var connection: ConnectionSource? = null
    var dao: DaoAccess? = null

    @BeforeEach
    fun before() {
        connection = getSqliteConnection(":memory:")
        dao = DaoAccess(connection!!)
        dao!!.createTables()
    }

    @AfterEach
    fun after() {
        connection?.close()
        connection = null
        dao = null
    }

    @Test
    fun canInsertArchive() {
        dao!!.archiveDao.create(
            Archive(
                id = "a",
                name = "a",
                createdDate = Date(),
                updatedDate = Date(),
                deleted = false,
                deletedDate = null
            )
        )
    }

    @Test
    fun canRetrieveArchive() {
        dao!!.archiveDao.create(
            Archive(
                id = "canRetrieveArchive",
                name = "canRetrieveArchive_name",
                createdDate = Date(),
                updatedDate = Date(),
                deleted = false,
                deletedDate = null
            )
        )

        val archive = dao!!.archiveDao.queryForId("canRetrieveArchive")?: fail("Unable to fetch archive")
        assertEquals(archive.name, "canRetrieveArchive_name")
    }
}

