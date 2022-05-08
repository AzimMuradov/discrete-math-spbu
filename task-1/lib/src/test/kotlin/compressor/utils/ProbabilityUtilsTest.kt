package compressor.utils

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import compressor.utils.Bit.ONE as B1
import compressor.utils.Bit.ZERO as B0

internal class ProbabilityUtilsTest {

    @Test
    fun `calculate message entropy`() {
        assertEquals(
            expected = 1.75,
            actual = "aaaabbcd".toList().entropy()
        )
    }

    @Test
    fun `calculate average length of code for message`() {
        assertEquals(
            expected = 1.75,
            actual = mapOf(
                'a' to Bits(listOf(B0)),
                'b' to Bits(listOf(B1, B0)),
                'c' to Bits(listOf(B1, B1, B0)),
                'd' to Bits(listOf(B1, B1, B1))
            ).averageLength("aaaabbcd".toList())
        )
    }

    @Test
    fun `calculate redundancy of code for message`() {
        assertEquals(
            expected = 0.0,
            actual = mapOf(
                'a' to Bits(listOf(B0)),
                'b' to Bits(listOf(B1, B0)),
                'c' to Bits(listOf(B1, B1, B0)),
                'd' to Bits(listOf(B1, B1, B1))
            ).redundancy("aaaabbcd".toList())
        )
    }
}
