package snapshot


import database.DataEntity
import java.util.*

data class SnapshotEntity(
    val id: String,
    val name: String,
    val createdDate: Date = Date(),
    val updatedDate: Date = Date(),
    val deleted: Boolean = false,
    val deletedDate: Date? = null
) : DataEntity()