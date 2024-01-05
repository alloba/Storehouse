package cli

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CliParsingTests {

    @Test
    fun `can parse empty arg string`(){
        val input = arrayOf("")
        val result = ArgsContainer(input)

        assertTrue(result.arguments.isEmpty())
    }

    @Test
    fun `can parse long flag with no value`(){
        val input = arrayOf("--long")
        val result = ArgsContainer(input)

        assertEquals("", result.arguments["long"])
    }
    @Test
    fun `will store associated value with long flag`(){
        val input = arrayOf("--long", "value")
        val result = ArgsContainer(input)

        assertEquals("value", result.arguments["long"])
    }

    @Test
    fun `will store multiple long flags with values`(){
        val input = arrayOf("--a value", "--bee", "value2")
        val result = ArgsContainer(input)

        assertEquals("value", result.arguments["a"])
        assertEquals("value2", result.arguments["bee"])
    }

    @Test
    fun `will parse long flags with and without values`(){
        val input = arrayOf("--a", "--bee", "value2 --zzz")
        val result = ArgsContainer(input)

        assertEquals("", result.arguments["a"])
        assertEquals("", result.arguments["zzz"])
        assertEquals("value2", result.arguments["bee"])
    }
}