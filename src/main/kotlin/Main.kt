import config.RuntimeConfiguration
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

fun main(args: Array<String>) {
    val argumentStore = cli.parseArguments(args)
    val configModel = RuntimeConfiguration.fromFilePath(argumentStore.configFilePath)
    val archiveOperator = generateArchiveOperator(configModel)
    val commandName = argumentStore.commandName
    val commandVal = argumentStore.commandString

    val command = commands.retrieveCommand(commandName)
    command.execute(archiveOperator, commandVal)
}


fun generateArchiveOperator(runtimeConfiguration: RuntimeConfiguration): ArchiveOperator {
    val databasePath = Path.of(runtimeConfiguration.databaseLocation)
    if (databasePath.isDirectory() || !databasePath.isReadable() || !databasePath.isWritable()) {
        throw Exception("Unable to operate with provided database file $databasePath")
    }
    val database = StorehouseDatabase(databasePath.toString())

    val source = when (runtimeConfiguration.sourceType) {
        "Local" -> {
            val sourcePathString = runtimeConfiguration.sourceConfig["path"] ?: throw Exception("necessary config option not found for local source config - [path]")
            val sourcePath = Path.of(sourcePathString)
            if (!sourcePath.isDirectory()) {
                throw Exception("Provided source path [$sourcePath] is not a valid directory")
            }

            LocalArchiveSource(mapOf("path" to sourcePath.toString()))
        }

        else -> {
            throw Exception("provided sourceType [${runtimeConfiguration.sourceType}] for storehouse is invalid")
        }
    }

    val destination = when (runtimeConfiguration.destinationType) {
        "Local" -> {
            val destinationPathString = runtimeConfiguration.sourceConfig["path"] ?: throw Exception("necessary config option not found for local destination config - [path]")
            val destinationPath = Path.of(destinationPathString)
            if (!destinationPath.isDirectory()) {
                throw Exception("Provided destination path [$destinationPath] is not a valid directory")
            }
            LocalArchiveDestination(mapOf("path" to destinationPath.toString()))
        }

        else -> {
            throw Exception("Provided destinationType [${runtimeConfiguration.destinationType}] for storehouse is invalid")
        }
    }

    val archiveRepository = ArchiveRepository(database)
    val snapshotRepository = SnapshotRepository(database)
    val fileRepository = FileRepository(database)
    val fileMetaRepository = FileMetaRepository(database)

    return ArchiveOperator(source, destination, archiveRepository, snapshotRepository, fileRepository, fileMetaRepository)
}
