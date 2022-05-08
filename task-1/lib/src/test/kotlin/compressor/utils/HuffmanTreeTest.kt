package compressor.utils

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import compressor.utils.Bit.ONE as B1
import compressor.utils.Bit.ZERO as B0

internal class HuffmanTreeTest {

    @Test
    fun `build codes`() {
        assertEquals(
            expected = mapOf(
                'a' to Bits(listOf(B0)),
                'b' to Bits(listOf(B1, B0)),
                'c' to Bits(listOf(B1, B1, B0)),
                'd' to Bits(listOf(B1, B1, B1)),
            ),
            actual = HuffmanTree(
                root = HuffmanNode(
                    symbol = ComparableList(listOf('a', 'b', 'c', 'd')),
                    children = Pair(
                        HuffmanNode(
                            symbol = ComparableList(listOf('a')),
                            children = null,
                            count = 4,
                        ),
                        HuffmanNode(
                            symbol = ComparableList(listOf('b', 'c', 'd')),
                            children = Pair(
                                HuffmanNode(
                                    symbol = ComparableList(listOf('b')),
                                    children = null,
                                    count = 2,
                                ),
                                HuffmanNode(
                                    symbol = ComparableList(listOf('c', 'd')),
                                    children = Pair(
                                        HuffmanNode(
                                            symbol = ComparableList(listOf('c')),
                                            children = null,
                                            count = 1,
                                        ),
                                        HuffmanNode(
                                            symbol = ComparableList(listOf('d')),
                                            children = null,
                                            count = 1,
                                        ),
                                    ),
                                    count = 2,
                                ),
                            ),
                            count = 4,
                        ),
                    ),
                    count = 8,
                ),
                symbols = listOf('a', 'b', 'c', 'd'),
            ).buildCodes()
        )
    }
}
