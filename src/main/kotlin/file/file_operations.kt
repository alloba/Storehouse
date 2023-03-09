package file

import java.net.URLEncoder
import java.nio.file.Files
import java.nio.file.Path
import java.security.DigestInputStream
import java.security.MessageDigest
import java.util.*
import kotlin.io.path.extension
import kotlin.io.path.fileSize
import kotlin.io.path.nameWithoutExtension

interface ByteObject {
    val name: String
    val byteSize: Long
}

data class FileInformation(
    override val name: String,
    override val byteSize: Long,
    val extension: String,
    val hash: String
) : ByteObject

data class FolderInformation(
    override val name: String,
    override val byteSize: Long,
    val children: List<ByteObject>
) : ByteObject

fun scanLocation(path: Path): List<ByteObject> {
    when {
        Files.isDirectory(path) -> {
            return resolveDirectory(path)
        }

        Files.isRegularFile(path) -> {
            return listOf(
                FileInformation(
                    name = path.nameWithoutExtension,
                    byteSize = path.fileSize(),
                    extension = path.extension,
                    hash = getFileHash(path)
                )
            )
        }

        Files.notExists(path) -> {
            throw Exception("provided path does not resolve: $path")
        }

        else -> {
            throw Exception("Cannot access given path $path")
        }
    }
}

fun resolveDirectory(path: Path): List<ByteObject> {
    when {
        Files.isRegularFile(path) -> {
            return listOf(
                FileInformation(
                    name = path.nameWithoutExtension,
                    byteSize = path.fileSize(),
                    extension = path.extension,
                    hash = getFileHash(path)
                )
            )
        }

        Files.isDirectory(path) -> {
            return Files.list(path).map { resolveDirectory(it) }.toList().flatMap { it.toList() }
        }

        Files.notExists(path) -> {
            throw Exception("Unable to resolve subpath: $path")
        }

        else -> {
            throw Exception("Unable to process path: $path")
        }
    }
}

fun getFileHash(path: Path): String {
    val md = MessageDigest.getInstance("SHA-256")
    DigestInputStream(Files.newInputStream(path), md).use {
        it.readAllBytes()
    }

    val hash = Base64.getEncoder().encode(md.digest())
    return URLEncoder.encode(hash.toString(), "UTF-8")
}

