package compressor.decompressors

import compressor.CompressedMessage
import compressor.compressors.EncoderBasedCompressor.Metadata
import compressor.utils.Bits
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import compressor.utils.Bit.ONE as B1
import compressor.utils.Bit.ZERO as B0

internal class PrefixTreeDecompressorTest {

    @Test
    fun `decompress message`() {
        assertEquals(
            expected = "adbaacab".toList(),
            actual = PrefixTreeDecompressor<Char>().decompress(
                CompressedMessage(
                    compressed = Bits(listOf(B0, B1, B1, B1, B1, B0, B0, B0, B1, B1, B0, B0, B1, B0)),
                    metadata = Metadata(
                        mapOf(
                            'a' to Bits(listOf(B0)),
                            'b' to Bits(listOf(B1, B0)),
                            'c' to Bits(listOf(B1, B1, B0)),
                            'd' to Bits(listOf(B1, B1, B1)),
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `fail to decompress message 1`() {
        assertFailsWith<IllegalStateException>(message = "Fail to decompress message, not a prefix tree.") {
            PrefixTreeDecompressor<Char>().decompress(
                CompressedMessage(
                    compressed = Bits(listOf(B0, B1, B1, B1, B1, B0, B0, B0, B1, B1, B0, B0, B1, B0)),
                    metadata = Metadata(
                        mapOf(
                            'a' to Bits(listOf(B1)),
                            'b' to Bits(listOf(B1, B0)),
                            'c' to Bits(listOf(B1, B1, B0)),
                            'd' to Bits(listOf(B1, B1, B1)),
                        )
                    )
                )
            )
        }
    }

    @Test
    fun `fail to decompress message 2`() {
        assertFailsWith<IllegalStateException>(message = "Fail to decompress message, not a prefix tree.") {
            PrefixTreeDecompressor<Char>().decompress(
                CompressedMessage(
                    compressed = Bits(listOf(B0, B1, B1, B1, B1, B0, B0, B0, B1, B1, B0, B0, B1, B0)),
                    metadata = Metadata(
                        mapOf(
                            'a' to Bits(listOf(B0)),
                            'b' to Bits(listOf(B1, B1)),
                            'c' to Bits(listOf(B1, B1, B0)),
                            'd' to Bits(listOf(B1, B1, B1)),
                        )
                    )
                )
            )
        }
    }
}
