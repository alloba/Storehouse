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
        assertEquals("", result.free)
    }
    @Test
    fun `can parse short flag with no value`(){
        val input = arrayOf("-a")
        val result = ArgsContainer(input)

        assertTrue(result.arguments["a"] == "")
        assertEquals("", result.free)
    }

    @Test
    fun `can parse short flag with single value`(){
        val input = arrayOf("-a", "val")
        val result = ArgsContainer(input)

        assertTrue(result.arguments["a"] == "val")
        assertEquals("", result.free)
    }

    @Test
    fun `can parse short flag with multiple values`(){
        val input = arrayOf("-a", "  val1  ", "  val2  ")
        val result = ArgsContainer(input)

        assertEquals("val1 val2", result.arguments["a"])
        assertEquals("", result.free)
    }

    @Test
    fun `can parse multiple short flags joined together`(){
        val input = arrayOf("-abc")
        val result = ArgsContainer(input)

        assertEquals("", result.arguments["a"])
        assertEquals("", result.arguments["b"])
        assertEquals("", result.arguments["c"])
        assertEquals("", result.free)
    }

    @Test
    fun `can parse multiple short flags and attach a value to the last one`(){
        val input = arrayOf("-abc", "value")
        val result = ArgsContainer(input)

        assertEquals("", result.arguments["a"])
        assertEquals("", result.arguments["b"])
        assertEquals("value", result.arguments["c"])
        assertEquals("", result.free)
    }

    @Test
    fun `can parse long flag with no value`(){
        val input = arrayOf("--long")
        val result = ArgsContainer(input)

        assertEquals("", result.arguments["long"])
        assertEquals("", result.free)
    }
    @Test
    fun `will store associated value with long flag`(){
        val input = arrayOf("--long", "value")
        val result = ArgsContainer(input)

        assertEquals("value", result.arguments["long"])
        assertEquals("", result.free)
    }
    @Test
    fun `will store multiple remaining args as single string attached to last long arg`(){
        val input = arrayOf("--long", "  val1  ", "   val2  ")
        val result = ArgsContainer(input)

        assertEquals("val1 val2", result.arguments["long"])
        assertEquals("", result.free)
    }

    @Test
    fun `will store everything as a remainder if no flags provided`(){
        val input = arrayOf("  val1  ", "   val2  ")
        val result = ArgsContainer(input)

        assertEquals("val1 val2", result.free)
    }
    @Test
    fun `can parse long flag followed by short flag`(){
        val input = arrayOf("--long", "  val1  ", "-a", "   val2  ")
        val result = ArgsContainer(input)

        assertEquals("val1", result.arguments["long"])
        assertEquals("val2", result.arguments["a"])
        assertEquals("", result.free)
    }
    @Test
    fun `can parse short flag followed by long flag`(){
        val input = arrayOf("--a", "  val1  ", "--long", "   val2  ")
        val result = ArgsContainer(input)

        assertEquals("val1", result.arguments["a"])
        assertEquals("val2", result.arguments["long"])
        assertEquals("", result.free)
    }
    @Test
    fun `can parse large mix`(){
        val input = arrayOf("--long1", "--long2", "long2val1" , "long2val2", "-a", "-b", "bval1", "bval2", "-cd", "dval1", "dval2", "--long3")
        val result = ArgsContainer(input)

        assertEquals("", result.arguments["long1"])
        assertEquals("long2val1 long2val2", result.arguments["long2"])
        assertEquals("", result.arguments["a"])
        assertEquals("bval1 bval2", result.arguments["b"])
        assertEquals("", result.arguments["c"])
        assertEquals("dval1 dval2", result.arguments["d"])
        assertEquals("", result.arguments["long3"])
        assertEquals("", result.free)
    }
    @Test
    fun `if short flag and remainder, properly group the remainder`(){}
}