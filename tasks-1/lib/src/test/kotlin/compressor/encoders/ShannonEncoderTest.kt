package compressor.encoders

import compressor.utils.Bits
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import compressor.utils.Bit.ONE as B1
import compressor.utils.Bit.ZERO as B0

internal class ShannonEncoderTest {

    @Test
    fun `encode message`() {
        assertEquals(
            expected = mapOf(
                'a' to Bits(listOf(B0)),
                'b' to Bits(listOf(B1, B0)),
                'c' to Bits(listOf(B1, B1, B0)),
                'd' to Bits(listOf(B1, B1, B1)),
            ),
            actual = ShannonEncoder<Char>().encode("adbaacab".toList())
        )
    }
}
