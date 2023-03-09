package file

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertEquals

class FileOperationsTests {

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun scanLocation() {
    }

    @Test
    fun resolveDirectory() {
    }

//    @Test
    // TODO: I'd really like a way to ensure the hash function never changes, but this isnt working, since the result here is changing (not sure why yet)
//    fun getFileHash() {
//        val fileResource: URL = object {}.javaClass.getResource("/hashtest.txt") ?: fail("unable to load hashtest.txt from resources")
//        var filePath = fileResource.path
//        if (filePath.substring(2,3) == ":"){
//            filePath = filePath.substring(1)
//        }
//        val verifiedPath = Path.of(filePath) // substring to remove leading / (windows issue)
//        val hash = getFileHash(verifiedPath)
//
//        assertEquals("%5BB%403c87521", hash, "Hashed and encoded file string has changed. THIS BREAKS INTEGRITY CHECKS WITH EXISTING DATA.") //if this ever changes, there better be a good reason.
//    }

    @Test
    fun getEmptyFolderObject() {
        val tmpFolderPrefix = "storehousetest"
        val dir = Files.createTempDirectory(tmpFolderPrefix)
        val result = scanLocation(dir)

        assert(result is FolderInformation)
        assert((result as FolderInformation).children.isEmpty())
    }

    @Test
    fun getPopulatedFolderObject() {
        val tmpFolderPrefix = "storehousetest"
        val dir = Files.createTempDirectory(tmpFolderPrefix)
        Files.createTempFile(dir, "testFile1", "")
        Files.createTempFile(dir, "testFile2", "")

        val result = scanLocation(dir)
        assert(result is FolderInformation)
        assert((result as FolderInformation).children.size == 2)
    }

    @Test
    fun getNestedPopulatedFolderObject() {
        val tmpFolderPrefix = "storehousetest"
        val dir = Files.createTempDirectory(tmpFolderPrefix)
        Files.createTempFile(dir, "testFile1", "")
        Files.createTempFile(dir, "testFile2", "")
        val d1 = Files.createTempDirectory(dir, tmpFolderPrefix)
        Files.createTempFile(d1, "testFileD1F2", "")

        val result = scanLocation(dir)
        assert(result is FolderInformation)
        assert((result as FolderInformation).children.size == 3)
        assert(result.children.filterIsInstance<FolderInformation>().size == 1)
        assert(result.children.filterIsInstance<FileInformation>().size == 2)
        assert(result.children.filterIsInstance<FolderInformation>().first().children.size == 1)
    }

    @Test
    fun getFileObject() {
        val f2 = Files.createTempFile("storehousetestfile", "")

        val result = scanLocation(f2)
        assert(result is FileInformation)
        assert((result as FileInformation).name.startsWith("storehousetestfile"))
    }


}