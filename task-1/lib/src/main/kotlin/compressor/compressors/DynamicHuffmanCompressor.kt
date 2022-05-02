package compressor.compressors

import compressor.*
import compressor.compressors.DynamicHuffmanCompressor.Metadata
import compressor.encoders.HuffmanEncoder

public class DynamicHuffmanCompressor<T : Comparable<T>> : Compressor<List<T>, T, List<Byte>, Metadata<T>> {

    private val huffmanEncoder = HuffmanEncoder<T>()

    override fun compress(message: List<T>): CompressedMessage<List<Byte>, Metadata<T>> {
        val alphabet = message.toSortedSet().toList()

        val fullMessage = alphabet + message

        val code = message.indices.fold(initial = Code(emptyList())) { acc, i ->
            acc + huffmanEncoder.encodeSymbolsOf(
                message = fullMessage.subList(0, alphabet.size + i)
            ).getValue(message[i])
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
