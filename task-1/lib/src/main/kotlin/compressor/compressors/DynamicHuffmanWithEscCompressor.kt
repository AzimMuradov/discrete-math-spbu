package compressor.compressors

import compressor.CompressedMessage
import compressor.Compressor
import compressor.compressors.DynamicHuffmanWithEscCompressor.Metadata
import compressor.utils.*

/**
 * Dynamic Huffman compressor with ESC (or NYT = Not Yet Transferred).
 *
 * Compresses arbitrary message with the dynamic Huffman compressing with ESC.
 */
public class DynamicHuffmanWithEscCompressor<T : Comparable<T>> : Compressor<T, Metadata<T>> {

    private val builder = HuffmanTreeBuilder<SymbolOrEsc<T>>()

    override fun compress(message: List<T>): CompressedMessage<Metadata<T>> {
        val fullMessage: List<SymbolOrEsc<T>> = listOf(SymbolOrEsc.Esc<T>()) + message.map { SymbolOrEsc.Symbol(it) }

        val alphabet = message.toSortedSet().toList()

        val (code, _) = message.indices.fold(
            initial = Bits.EMPTY to builder.buildTree(emptyMap())
        ) { (code, tree), i ->
            val (_, symbols) = tree
            val newTree = builder.buildTree(fullMessage.subList(0, i + 1).calculateMessageInfo().countedSymbols)

            if (SymbolOrEsc.Symbol(message[i]) in symbols) {
                code + newTree.buildCodes().getValue(SymbolOrEsc.Symbol(message[i]))
            } else {
                code + newTree.buildCodes().getValue(SymbolOrEsc.Esc()) + alphabet
                    .indexOf(message[i])
                    .toString(radix = 2)
                    .padStart(
                        length = (alphabet.size - 1).toString(radix = 2).length,
                        padChar = '0'
                    )
                    .toBits()
            } to newTree
        }

        return CompressedMessage(
            compressed = code,
            metadata = Metadata(alphabet)
        )
    }


    /**
     * Metadata contains [sorted alphabet][alphabet] that was used during the compressing.
     */
    public data class Metadata<out T>(val alphabet: List<T>)
}
