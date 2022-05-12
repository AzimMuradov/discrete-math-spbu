package compressor.decompressors

import compressor.CompressedMessage
import compressor.compressors.DynamicHuffmanCompressor
import compressor.utils.Bits
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import compressor.utils.Bit.ONE as B1
import compressor.utils.Bit.ZERO as B0

internal class DynamicHuffmanDecompressorTest {

    @Test
    fun `decompress message`() {
        assertEquals(
            expected = "adbaacab".toList(),
            actual = DynamicHuffmanDecompressor<Char>().decompress(
                CompressedMessage(
                    compressed = Bits(
                        listOf(B0, B0, B1, B0, B1, B1, B0, B0, B1, B1, B1, B1, B1, B0, B1, B1, B1, B1, B0)
                    ),
                    metadata = DynamicHuffmanCompressor.Metadata(
                        alphabet = listOf('a', 'b', 'c', 'd')
                    )
                )
            )
        )
    }
}
