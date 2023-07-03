package compressor.utils

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class HuffmanTreeBuilderTest {

    @Test
    fun `build tree`() {
        assertEquals(
            expected = HuffmanTree(
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
            ),
            actual = HuffmanTreeBuilder<Char>().buildTree(
                countedSymbols = mapOf(
                    'a' to 4,
                    'b' to 2,
                    'c' to 1,
                    'd' to 1
                )
            )
        )
    }

    @Test
    fun `build empty tree`() {
        assertEquals(
            expected = HuffmanTree(
                root = HuffmanNode(
                    symbol = ComparableList(emptyList()),
                    children = null,
                    count = 0,
                ),
                symbols = emptyList(),
            ),
            actual = HuffmanTreeBuilder<Char>().buildTree(countedSymbols = emptyMap())
        )
    }
}
