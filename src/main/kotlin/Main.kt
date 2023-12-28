import config.ConfigModel
import database.StorehouseDatabase
import database.repo.ArchiveRepository
import database.repo.FileMetaRepository
import database.repo.FileRepository
import database.repo.SnapshotRepository
import destination.local.LocalArchiveDestination
import source.local.LocalArchiveSource
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.isReadable
import kotlin.io.path.isWritable
import kotlin.io.path.readText

fun main(args: Array<String>) {
    if (args.size.mod( 2) != 0){
        throw Exception("Invalid command line args - all options must have an associated value supplied")
    }
    val argsMap = args.toList().chunked(2).associate { it[0] to it[1] }
    val configModel = generateConfigModel(argsMap)
    val archiveOperator = generateArchiveOperator(configModel)
    val (command, commandString) = commands.interpretCommandString(argsMap["command"]?:"")

    command.execute(archiveOperator, commandString)
}

fun generateConfigModel(argsMap: Map<String,String>): ConfigModel{
    val configPath = argsMap["config"] ?: throw Exception("Invalid command line args - must provide a config file path (must be json matching spec)")
    val configFilePath = Path.of(configPath)

    if (configFilePath.isDirectory() || ! configFilePath.isReadable()){
        throw Exception("Invalid command line args - interpreted config file path $configFilePath not valid.")
    }

    return ConfigModel.fromJsonString(configFilePath.readText())
}

fun generateArchiveOperator(configModel: ConfigModel): ArchiveOperator{
    val databasePath = Path.of(configModel.databaseLocation)
    if (databasePath.isDirectory() || ! databasePath.isReadable() || ! databasePath.isWritable()){
        throw Exception("Unable to operate with provided database file $databasePath")
    }
    val database = StorehouseDatabase(databasePath.toString())

    val source = when(configModel.sourceType){
        "Local" ->  {
            val sourcePathString = configModel.sourceConfig["path"]?:throw Exception("necessary config option not found for local source config - [path]")
            val sourcePath = Path.of(sourcePathString)
            if (! sourcePath.isDirectory()){
                throw Exception("Provided source path [$sourcePath] is not a valid directory")
            }

            LocalArchiveSource(mapOf("path" to sourcePath.toString()))
        }
        else -> {
            throw Exception("provided sourceType [${configModel.sourceType}] for storehouse is invalid")
        }
    }

    val destination = when(configModel.destinationType){
        "Local" -> {
            val destinationPathString = configModel.sourceConfig["path"]?: throw Exception("necessary config option not found for local destination config - [path]")
            val destinationPath = Path.of(destinationPathString)
            if (! destinationPath.isDirectory()){
                throw Exception("Provided destination path [$destinationPath] is not a valid directory")
            }
            LocalArchiveDestination(mapOf("path" to destinationPath.toString()))
        }
        else -> {
            throw Exception("Provided destinationType [${configModel.destinationType}] for storehouse is invalid")
        }
    }

    val archiveRepository = ArchiveRepository(database)
    val snapshotRepository = SnapshotRepository(database)
    val fileRepository = FileRepository(database)
    val fileMetaRepository = FileMetaRepository(database)

    return ArchiveOperator(source, destination, archiveRepository, snapshotRepository, fileRepository, fileMetaRepository)
}
