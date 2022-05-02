package compressor.decompressors

import compressor.*
import compressor.compressors.DynamicHuffmanCompressor.Metadata
import compressor.encoders.HuffmanEncoder

public class DynamicHuffmanDecompressor<T : Comparable<T>> : Decompressor<List<T>, T, List<Byte>, Metadata<T>> {

    private val huffmanEncoder = HuffmanEncoder<T>()

    override fun decompress(compressedMessage: CompressedMessage<List<Byte>, Metadata<T>>): List<T> {
        val (compressed, metadata) = compressedMessage

        val code = compressed.toCode(metadata.bitsLength)

        var i = 0
        val message = metadata.alphabet.toMutableList()
        var encodedMessage = huffmanEncoder.encode(message)
        while (i != code.bits.size) {
            val (symbol, symbolCode) = encodedMessage.codes.toList().first { (_, c) ->
                i + c.bits.size <= code.bits.size && code.bits.subList(i, i + c.bits.size) == c.bits
            }
            i += symbolCode.bits.size
            message += symbol
            encodedMessage = huffmanEncoder.encode(message)
        }

        return message.drop(metadata.alphabet.size)
    }
}
