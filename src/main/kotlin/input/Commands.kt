package input

import archive.createArchive
import database.DaoManager
import database.DataEntity

enum class CommandType(val command: String, val executor: (ArgumentsContainer, DaoManager) -> Collection<DataEntity>) {
    CREATE_ARCHIVE("create-archive", ::createArchive),
    CREATE_SNAPSHOT("create-snapshot", ::placeholderOp),
    CREATE_COLLECTION("create-collection", ::placeholderOp);
//    CREATE_FILE_INFO("create-file-info", ::placeholderOp); //todo: document the fact that file info should not be managed by the user ever.

    companion object {
        private val mapped = CommandType.values().associateBy(CommandType::command)
        fun of(name: String): CommandType =
            mapped[name] ?: throw Exception("No supported command found with name $name")
    }
}

fun placeholderOp(args: ArgumentsContainer, dao: DaoManager): Collection<DataEntity> {
    return emptyList()
}