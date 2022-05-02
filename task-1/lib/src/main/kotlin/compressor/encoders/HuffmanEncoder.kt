package compressor.encoders

import compressor.*
import compressor.utils.HuffmanTreeBuilder
import compressor.utils.buildCodes

public class HuffmanEncoder<T : Comparable<T>> : Encoder<T> {

    override fun encodeSymbolsOf(message: List<T>): Map<T, Code> = HuffmanTreeBuilder<T>()
        .buildTree(message.toMessageInfo().countedSymbols)
        .buildCodes()
}
