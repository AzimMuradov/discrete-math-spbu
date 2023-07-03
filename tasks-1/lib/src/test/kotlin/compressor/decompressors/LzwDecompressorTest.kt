package compressor.decompressors

import compressor.CompressedMessage
import compressor.compressors.LzwCompressor
import compressor.utils.Bits
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import compressor.utils.Bit.ONE as B1
import compressor.utils.Bit.ZERO as B0

internal class LzwDecompressorTest {

    @Test
    fun `decompress message`() {
        assertEquals(
            expected = "abacabadabacabae".toList(),
            actual = LzwDecompressor<Char>().decompress(
                CompressedMessage(
                    compressed = Bits(
                        listOf(
                            B0, B0, B0, B0, B0, B1, B0, B0, B0, B0, B1, B0, B0, B1, B0, B1, B0,
                            B0, B0, B0, B0, B0, B1, B1, B1, B0, B0, B1, B1, B0, B0, B0,
                            B0, B1, B1, B0, B0, B1, B0, B0
                        )
                    ),
                    metadata = LzwCompressor.Metadata(alphabet = listOf('a', 'b', 'c', 'd', 'e'))
                ),
            ),
        )
    }
}
