package commands

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class CommandFunctionsTests {

    @Test
    fun `if valid command name return instance`(){
        val result = retrieveCommand("DisplayArchiveInformation")
        assertTrue(result is DisplayArchiveInfoCommand)
    }
    @Test
    fun `if valid alias return instance`(){
        val result = retrieveCommand("da")
        assertTrue(result is DisplayArchiveInfoCommand)
    }

    @Test
    fun `if invalid command name return unrecognized command instance`(){
        val result = retrieveCommand("!!))((!!")
        assertTrue(result is UnrecognizedCommand)
    }

}