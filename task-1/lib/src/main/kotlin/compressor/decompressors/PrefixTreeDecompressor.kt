package compressor.decompressors

import compressor.CompressedMessage
import compressor.Decompressor
import compressor.compressors.EncoderBasedCompressor
import compressor.utils.Bit
import compressor.utils.Bits

/**
 * Decompressor that relies on the prefix property of the [given codes][EncoderBasedCompressor.Metadata.codes].
 */
public class PrefixTreeDecompressor<T> : Decompressor<T, EncoderBasedCompressor.Metadata<T>> {

    override fun decompress(compressedMessage: CompressedMessage<EncoderBasedCompressor.Metadata<T>>): List<T> {
        val (compressed, metadata) = compressedMessage
        val codes = metadata.codes

        val prefixTree = checkNotNull(codes.prefixTreeOrNull())

        return buildList {
            var i = 0
            while (i < compressed.size) {
                var node = prefixTree
                while (true) {
                    when (node) {
                        is Node.InternalNode -> {
                            check(i < compressed.size)
                            node = when (compressed[i]) {
                                Bit.ZERO -> node.children.first
                                    ?: error("Fail to decompress message, not a prefix tree.")
                                Bit.ONE -> node.children.second
                                    ?: error("Fail to decompress message, not a prefix tree.")
                            }
                            i++
                        }
                        is Node.Leave -> {
                            add(node.symbol)
                            break
                        }
                    }
                }
            }
        }
    }


    private fun Map<T, Bits>.prefixTreeOrNull(): Node<T>? {
        fun List<Pair<Bits, T>>.prefixTreeOrNull(i: Int): Node<T>? {
            return when {
                size == 1 && first().first.size == i -> {
                    Node.Leave(first().second)
                }
                isNotEmpty() && all { first().first.size > i } -> {
                    val (leftCodes, rightCodes) = partition { (c) -> c[i] == Bit.ZERO }
                    val leftChild = leftCodes.takeIf { it.isNotEmpty() }?.prefixTreeOrNull(i = i + 1)
                    val rightChild = rightCodes.takeIf { it.isNotEmpty() }?.prefixTreeOrNull(i = i + 1)
                    Node.InternalNode(children = leftChild to rightChild)
                }
                else -> {
                    null
                }
            }
        }

        return entries.takeIf { values.toSet().size == values.size }?.map { (k, v) -> v to k }?.prefixTreeOrNull(i = 0)
    }

    private sealed interface Node<T> {

        data class InternalNode<T>(val children: Pair<Node<T>?, Node<T>?>) : Node<T>

        data class Leave<T>(val symbol: T) : Node<T>
    }
}
