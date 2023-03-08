package file

import java.lang.Exception
import java.net.URI
import java.net.URL
import java.net.URLEncoder
import java.nio.file.Files
import java.nio.file.Path
import java.security.DigestInputStream
import java.security.MessageDigest
import java.util.Base64
import kotlin.io.path.extension
import kotlin.io.path.fileSize
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.toPath
import kotlin.streams.toList

class VerifiedPath {
    lateinit var path: Path

    constructor (potentialPath: Path) {
        if (Files.exists(potentialPath)) {
            this.path = potentialPath
        } else {
            throw Exception("could not verify path: $path")
        }
    }

    constructor(potentialPath: String) : this(potentialPath = Path.of(potentialPath))

    override fun toString(): String {
        return this.path.toString()
    }
}

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

fun scanLocation(path: VerifiedPath): List<ByteObject> {
    when {
        Files.isDirectory(path.path) -> {
            return resolveDirectory(path)
        }

        Files.isRegularFile(path.path) -> {
            return listOf(
                FileInformation(
                    name = path.path.nameWithoutExtension,
                    byteSize = path.path.fileSize(),
                    extension = path.path.extension,
                    hash = getFileHash(path)
                )
            )
        }

        Files.notExists(path.path) -> {
            throw Exception("provided path does not resolve: $path")
        }

        else -> {
            throw Exception("Cannot access given path $path")
        }
    }
}

fun resolveDirectory(path: VerifiedPath): List<ByteObject> {
    when {
        Files.isRegularFile(path.path) -> {
            return listOf(
                FileInformation(
                    name = path.path.nameWithoutExtension,
                    byteSize = path.path.fileSize(),
                    extension = path.path.extension,
                    hash = getFileHash(path)
                )
            )
        }

        Files.isDirectory(path.path) -> {
            return Files.list(path.path).map { resolveDirectory(VerifiedPath(it)) }.toList().flatMap { it.toList() }
        }

        Files.notExists(path.path) -> {
            throw Exception("Unable to resolve subpath: $path")
        }

        else -> {
            throw Exception("Unable to process path: $path")
        }
    }
}

fun getFileHash(path: VerifiedPath): String {
    val md = MessageDigest.getInstance("SHA-256")
    DigestInputStream(Files.newInputStream(path.path), md).use {
        it.readAllBytes()
    }

    val hash = Base64.getEncoder().encode(md.digest())
    return URLEncoder.encode(hash.toString(), "UTF-8")
}

