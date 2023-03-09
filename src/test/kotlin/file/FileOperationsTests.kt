package file

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.lang.Exception
import java.net.URL
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

    @Test
    fun getFileHash() {
        val fileResource: URL = object {}.javaClass.getResource("/hashtest.txt") ?: fail("unable to load hashtest.txt from resources")
        var filePath = fileResource.path
        if (filePath.substring(2,3) == ":"){
            filePath = filePath.substring(1)
        }
        val verifiedPath = Path.of(filePath) // substring to remove leading / (windows issue)
        val hash = getFileHash(verifiedPath)

        assertEquals("%5BB%403c87521", hash, "Hashed and encoded file string has changed. THIS BREAKS INTEGRITY CHECKS WITH EXISTING DATA.") //if this ever changes, there better be a good reason.
    }
}