package archive

import database.DaoManager
import database.DataEntity
import input.ArgumentsContainer
import java.time.Instant
import java.util.*

fun createArchive(args: ArgumentsContainer, dao: DaoManager): Collection<DataEntity> {
    check(args.getInputs().size == 1)

    val uuid = UUID.randomUUID().toString()
    val newArchive = ArchiveEntity(
        id = uuid,
        name = args.getInputs()[0],
        createdDate = Instant.now(),
        updatedDate = Instant.now(),
        deleted = false,
        deleteDate = Instant.MAX
    )

    return emptyList()
}