package input

import database.Archive
import database.DaoAccess
import database.DataEntity
import java.util.UUID
import java.util.Date

enum class CommandType(
    val command: String,
    val executor: (ArgumentsContainer, DaoAccess) -> Collection<DataEntity>
) {
    CREATE_ARCHIVE("create-archive", ::createArchive),
    CREATE_SNAPSHOT("create-snapshot", ::placeholderOp),
    CREATE_COLLECTION("create-collection", ::placeholderOp),
    CREATE_FILE_INFO("create-file-info", ::placeholderOp);

    companion object {
        private val mapped = CommandType.values().associateBy(CommandType::command)
        fun of(name: String): CommandType =
            mapped[name] ?: throw Exception("No supported command found with name $name")
    }
}

fun createArchive(args: ArgumentsContainer, dao: DaoAccess): Collection<DataEntity> {
    check(args.getInputs().size == 1)
    val uuid = UUID.randomUUID().toString()
    val newArchive = Archive(
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

fun placeholderOp(args: ArgumentsContainer, dao: DaoAccess): Collection<DataEntity> {
    return emptyList()
}