package compressor.compressors

import compressor.CompressedMessage
import compressor.Compressor
import compressor.compressors.DynamicHuffmanCompressor.Metadata
import compressor.encoders.HuffmanEncoder
import compressor.utils.Bits
import compressor.utils.plus

/**
 * Dynamic Huffman compressor.
 *
 * Compresses arbitrary message with the dynamic Huffman compressing.
 */
public class DynamicHuffmanCompressor<T : Comparable<T>> : Compressor<T, Metadata<T>> {

    private val huffmanEncoder = HuffmanEncoder<T>()

    override fun compress(message: List<T>): CompressedMessage<Metadata<T>> {
        val alphabet = message.toSortedSet().toList()

        val fullMessage = alphabet + message

        val bits = message.indices.fold(initial = Bits.EMPTY) { acc, i ->
            acc + huffmanEncoder.encode(
                message = fullMessage.subList(0, alphabet.size + i)
            ).getValue(message[i])
        }

        return CompressedMessage(
            compressed = bits,
            metadata = Metadata(alphabet)
        )
    }


    /**
     * Metadata contains [sorted alphabet][alphabet] that was used during the compressing.
     */
    public data class Metadata<out T>(val alphabet: List<T>)
}
