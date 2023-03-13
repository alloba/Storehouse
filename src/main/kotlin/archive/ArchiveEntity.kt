package archive

import database.DataEntity
import java.time.Instant


data class ArchiveEntity(
    val id: String,
    val name: String,
    val createdDate: Instant,
    val updatedDate: Instant,
    val deleted: Boolean,
    val deleteDate: Instant
) : DataEntity()