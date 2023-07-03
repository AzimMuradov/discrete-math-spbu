package compressor.decompressors

import compressor.CompressedMessage
import compressor.Decompressor
import compressor.compressors.DynamicHuffmanCompressor.Metadata
import compressor.encoders.HuffmanEncoder

/**
 * Dynamic Huffman decompressor.
 *
 * Decompresses message that was compressed with [dynamic Huffman compressor][compressor.compressors.DynamicHuffmanCompressor].
 */
public class DynamicHuffmanDecompressor<T : Comparable<T>> : Decompressor<T, Metadata<T>> {

    private val huffmanEncoder = HuffmanEncoder<T>()

    override fun decompress(compressedMessage: CompressedMessage<Metadata<T>>): List<T> {
        val (compressed, metadata) = compressedMessage

        var i = 0
        val message = metadata.alphabet.toMutableList()
        var codes = huffmanEncoder.encode(message)
        while (i != compressed.size) {
            val (symbol, symbolCode) = codes.entries.first { (_, c) ->
                i + c.size <= compressed.size && compressed.subList(i, i + c.size) == c
            }
            i += symbolCode.size
            message += symbol
            codes = huffmanEncoder.encode(message)
        }

        return message.drop(metadata.alphabet.size)
    }
}
