package compressor.decompressors

import compressor.CompressedMessage
import compressor.compressors.ArithmeticCompressor
import compressor.utils.Bits
import compressor.utils.MessageInfo
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import compressor.utils.Bit.ONE as B1
import compressor.utils.Bit.ZERO as B0

internal class ArithmeticDecompressorTest {

    @Test
    fun `decompress message`() {
        assertEquals(
            expected = "adbaacab".toList(),
            actual = ArithmeticDecompressor<Char>().decompress(
                CompressedMessage(
                    compressed = Bits(listOf(B0, B1, B1, B1, B1, B0, B0, B0, B1, B1, B0, B0, B1)),
                    metadata = ArithmeticCompressor.Metadata(
                        messageInfo = MessageInfo(
                            countedSymbols = mapOf(
                                'a' to 4,
                                'b' to 2,
                                'c' to 1,
                                'd' to 1
                            ),
                            messageLength = 8,
                        )
                    )
                ),
            ),
        )
    }
}
