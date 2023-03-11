package file

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import database.DataEntity
import java.util.*

@DatabaseTable(tableName = "file_meta")
data class FilemetaEntity(
    @DatabaseField(columnName = "id", id = true) val id: String = "",
    @DatabaseField(columnName = "name") val name: String = "",
    @DatabaseField(columnName = "extension") val extension: String = "",
    @DatabaseField(columnName = "created_date") val createdDate: Date = Date(),
    @DatabaseField(columnName = "updated_date") val updatedDate: Date = Date(),
    @DatabaseField(columnName = "deleted") val deleted: Boolean = false,
    @DatabaseField(columnName = "deleted_date") val deletedDate: Date? = null
): DataEntity()