package compressor.encoders

import compressor.Encoder
import compressor.utils.*

/**
 * Huffman encoder.
 *
 * Encodes arbitrary message with the Huffman encoding.
 */
public class HuffmanEncoder<T : Comparable<T>> : Encoder<T> {

    override fun encode(message: List<T>): Map<T, Bits> = HuffmanTreeBuilder<T>()
        .buildTree(message.calculateMessageInfo().countedSymbols)
        .buildCodes()
}
