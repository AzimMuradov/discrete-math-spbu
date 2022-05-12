package compressor.compressors

import compressor.CompressedMessage
import compressor.utils.Bits
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import compressor.utils.Bit.ONE as B1
import compressor.utils.Bit.ZERO as B0

internal class DynamicHuffmanWithEscCompressorTest {

    @Test
    fun `compress message`() {
        assertEquals(
            expected = CompressedMessage(
                compressed = Bits(
                    listOf(B0, B0, B0, B1, B1, B1, B0, B0, B1, B0, B1, B0, B1, B1, B0, B1, B0, B0, B0, B0, B1)
                ),
                metadata = DynamicHuffmanWithEscCompressor.Metadata(
                    alphabet = listOf('a', 'b', 'c', 'd')
                )
            ),
            actual = DynamicHuffmanWithEscCompressor<Char>().compress("adbaacab".toList())
        )
    }
}
