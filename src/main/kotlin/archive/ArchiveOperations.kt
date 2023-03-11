package archive

import database.DaoAccess
import database.DataEntity
import input.ArgumentsContainer
import java.util.*

fun createArchive(args: ArgumentsContainer, dao: DaoAccess): Collection<DataEntity> {
    check(args.getInputs().size == 1)
    val uuid = UUID.randomUUID().toString()
    val newArchive = ArchiveEntity(
        id = uuid,
        name = args.getInputs()[0],
        createdDate = Date(),
        updatedDate = Date(),
        deleted = false,
        deletedDate = null
    )
    dao.archiveDao.create(newArchive)
    return listOf(dao.archiveDao.queryForId(uuid))
}