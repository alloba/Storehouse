package file


import database.DataEntity
import java.util.*

data class FileRawEntity(
    val id: String = "",
    val hash: String = "",
    val createdDate: Date = Date(),
    val updatedDate: Date = Date(),
    val deleted: Boolean = false,
    val deletedDate: Date? = null
) : DataEntity()