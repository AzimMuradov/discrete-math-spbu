package compressor.compressors

import compressor.CompressedMessage
import compressor.Compressor
import compressor.utils.*

/**
 * Lempel-Ziv-Welch (LZW) compressor.
 *
 * Compresses arbitrary message with the LZW compressing.
 */
public class LzwCompressor<T : Comparable<T>> : Compressor<T, LzwCompressor.Metadata<T>> {

    override fun compress(message: List<T>): CompressedMessage<Metadata<T>> {
        val alphabet = message.toSortedSet().toList()

        val phrases = alphabet.mapTo(mutableListOf()) { listOf(it) }
        var currentPhrase = emptyList<T>()


        var code = Bits.EMPTY

        fun addPhraseToCode(phrase: List<T>) {
            val index = phrases.indexOf(phrase)
            val codeLen = phrases.lastIndex.toString(radix = 2).length
            code += index.toString(radix = 2).padStart(length = codeLen, padChar = '0').toBits()
        }

        for (symbol in message) {
            val newPhrase: List<T> = currentPhrase + symbol
            if (newPhrase in phrases) {
                currentPhrase = newPhrase
            } else {
                addPhraseToCode(currentPhrase)
                currentPhrase = listOf(symbol)
                phrases += newPhrase
            }
        }
        if (currentPhrase.isNotEmpty()) addPhraseToCode(currentPhrase)


        return CompressedMessage(compressed = code, metadata = Metadata(alphabet))
    }


    /**
     * Metadata contains [sorted alphabet][alphabet] that was used during the compressing.
     */
    public data class Metadata<out T>(val alphabet: List<T>)
}
