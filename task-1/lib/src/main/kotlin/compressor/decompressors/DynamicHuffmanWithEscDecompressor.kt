@file:Suppress("UPPER_BOUND_VIOLATED_WARNING", "TYPE_MISMATCH_WARNING")

package compressor.decompressors

import compressor.*
import compressor.compressors.DynamicHuffmanWithEscCompressor.Metadata
import compressor.encoders.HuffmanEncoder
import compressor.utils.SymbolOrEsc

public class DynamicHuffmanWithEscDecompressor<T : Comparable<T>> : Decompressor<List<T>, T, List<Byte>, Metadata<T>> {

    private val huffmanEncoder = HuffmanEncoder<SymbolOrEsc<out T>>()

    override fun decompress(compressedMessage: CompressedMessage<List<Byte>, Metadata<T>>): List<T> {
        val (compressed, metadata) = compressedMessage

        val alphabet = metadata.alphabet

        val symLen = (alphabet.size - 1).toString(radix = 2).length

        val code = compressed.toCode(metadata.bitsLength)

        var i = 0
        val message = mutableListOf<SymbolOrEsc<out T>>(SymbolOrEsc.Esc)
        var encodedMessage = huffmanEncoder.encode(message)
        while (i != code.bits.size) {
            val (symbol, symbolCode) = encodedMessage.codes.toList().first { (_, c) ->
                i + c.bits.size <= code.bits.size && code.bits.subList(i, i + c.bits.size) == c.bits
            }
            when (symbol) {
                is SymbolOrEsc.Symbol -> {
                    i += symbolCode.bits.size
                    message += symbol
                    encodedMessage = huffmanEncoder.encode(message)
                }
                SymbolOrEsc.Esc -> {
                    i += symbolCode.bits.size + symLen
                    message += SymbolOrEsc.Symbol(
                        alphabet[(code.bits.subList(i - symLen, i)).run(::Code).toString().toInt(radix = 2)]
                    )
                    encodedMessage = huffmanEncoder.encode(message)
                }
            }
        }

        return message.drop(1).filterIsInstance<SymbolOrEsc.Symbol<T>>().map(SymbolOrEsc.Symbol<T>::value)
    }
}
