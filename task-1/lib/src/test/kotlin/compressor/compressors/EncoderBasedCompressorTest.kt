package compressor.compressors

import compressor.CompressedMessage
import compressor.compressors.EncoderBasedCompressor.Metadata
import compressor.encoders.HuffmanEncoder
import compressor.utils.Bits
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import compressor.utils.Bit.ONE as B1
import compressor.utils.Bit.ZERO as B0

internal class EncoderBasedCompressorTest {

    @Test
    fun `compress message`() {
        assertEquals(
            expected = CompressedMessage(
                compressed = Bits(listOf(B0, B1, B1, B1, B1, B0, B0, B0, B1, B1, B0, B0, B1, B0)),
                metadata = Metadata(
                    mapOf(
                        'a' to Bits(listOf(B0)),
                        'b' to Bits(listOf(B1, B0)),
                        'c' to Bits(listOf(B1, B1, B0)),
                        'd' to Bits(listOf(B1, B1, B1)),
                    )
                )
            ),
            actual = EncoderBasedCompressor(encoder = HuffmanEncoder<Char>()).compress("adbaacab".toList())
        )
    }
}
