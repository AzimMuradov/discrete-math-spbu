package compressor.decompressors

import compressor.*
import compressor.compressors.DynamicHuffmanWithEscCompressor.Metadata
import compressor.encoders.HuffmanEncoder
import compressor.utils.SymbolOrEsc
import compressor.utils.asBits

/**
 * Dynamic Huffman decompressor with ESC (or NYT = Not Yet Transferred).
 *
 * Decompresses message that was compressed with [dynamic Huffman compressor with ESC][compressor.compressors.DynamicHuffmanWithEscCompressor].
 */
public class DynamicHuffmanWithEscDecompressor<T : Comparable<T>> : Decompressor<T, Metadata<T>> {

    private val huffmanEncoder = HuffmanEncoder<SymbolOrEsc<T>>()

    override fun decompress(compressedMessage: CompressedMessage<Metadata<T>>): List<T> {
        val (compressed, metadata) = compressedMessage

        val alphabet = metadata.alphabet

        val symLen = (alphabet.size - 1).toString(radix = 2).length

        var i = 0
        val message = mutableListOf<SymbolOrEsc<T>>(SymbolOrEsc.Esc())
        var codes = huffmanEncoder.encode(message)
        while (i != compressed.size) {
            val (symbol, symbolCode) = codes.entries.first { (_, c) ->
                i + c.size <= compressed.size && compressed.subList(i, i + c.size) == c
            }
            when (symbol) {
                is SymbolOrEsc.Symbol -> {
                    i += symbolCode.size
                    message += symbol
                    codes = huffmanEncoder.encode(message)
                }
                is SymbolOrEsc.Esc -> {
                    i += symbolCode.size + symLen
                    message += SymbolOrEsc.Symbol(
                        alphabet[(compressed.subList(i - symLen, i)).asBits().toString().toInt(radix = 2)]
                    )
                    codes = huffmanEncoder.encode(message)
                }
            }
        }

        return message.drop(n = 1).filterIsInstance<SymbolOrEsc.Symbol<T>>().map(SymbolOrEsc.Symbol<T>::value)
    }
}
