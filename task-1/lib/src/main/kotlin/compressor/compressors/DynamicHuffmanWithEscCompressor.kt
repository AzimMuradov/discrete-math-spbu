@file:Suppress("UPPER_BOUND_VIOLATED_WARNING", "TYPE_MISMATCH_WARNING")

package compressor.compressors

import compressor.*
import compressor.compressors.DynamicHuffmanWithEscCompressor.Metadata
import compressor.utils.*

public class DynamicHuffmanWithEscCompressor<T : Comparable<T>> : Compressor<List<T>, T, List<Byte>, Metadata<T>> {

    private val builder = HuffmanTreeBuilder<SymbolOrEsc<out T>>()

    override fun compress(message: List<T>): CompressedMessage<List<Byte>, Metadata<T>> {
        val fullMessage: List<SymbolOrEsc<out T>> = listOf(SymbolOrEsc.Esc) + message.map { SymbolOrEsc.Symbol(it) }

        val alphabet = message.toSortedSet().toList()

        val (code, _) = message.indices.fold(
            initial = Code(emptyList()) to builder.buildTree(emptyMap())
        ) { (code, tree), i ->
            val (_, symbols) = tree
            val newTree = builder.buildTree(fullMessage.subList(0, i + 1).toMessageInfo().countedSymbols)

            if (SymbolOrEsc.Symbol(message[i]) in symbols) {
                code + newTree.buildCodes().getValue(SymbolOrEsc.Symbol(message[i]))
            } else {
                code + newTree.buildCodes().getValue(SymbolOrEsc.Esc) + alphabet
                    .indexOf(message[i])
                    .toString(radix = 2)
                    .padStart(
                        length = (alphabet.size - 1).toString(radix = 2).length,
                        padChar = '0'
                    )
                    .toCode()
            } to newTree
        }

        return CompressedMessage(
            compressed = code.toByteList(),
            metadata = Metadata(
                alphabet = alphabet,
                bitsLength = code.bits.size
            )
        )
    }


    public data class Metadata<out T>(
        val alphabet: List<T>,
        val bitsLength: Int,
    )
}
