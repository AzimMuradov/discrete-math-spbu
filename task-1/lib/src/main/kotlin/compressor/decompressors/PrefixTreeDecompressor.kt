package compressor.decompressors

import compressor.*
import compressor.compressors.EncoderBasedCompressor.Metadata

public class PrefixTreeDecompressor<T> : Decompressor<List<T>, T, List<Byte>, Metadata<T>> {

    override fun decompress(compressedMessage: CompressedMessage<List<Byte>, Metadata<T>>): List<T> {
        val (compressed, metadata) = compressedMessage
        val compressedBits = compressed.toCode(metadata.length)
        val codes = metadata.codes

        val prefixTree = checkNotNull(codes.prefixTreeOrNull())

        return buildList {
            var i = 0
            while (i < compressedBits.bits.size) {
                var node = prefixTree
                while (true) {
                    when (node) {
                        is Node.InternalNode -> {
                            check(i < compressedBits.bits.size)
                            node = when (compressedBits.bits[i]) {
                                Bit.ZERO -> node.children.first ?: error("")
                                Bit.ONE -> node.children.second ?: error("")
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


    private fun Map<T, Code>.prefixTreeOrNull(): Node<T>? {
        fun List<Pair<Code, T>>.prefixTreeOrNull(i: Int): Node<T>? {
            return when {
                size == 1 && first().first.bits.size == i -> {
                    Node.Leave(first().second)
                }
                isNotEmpty() && all { first().first.bits.size > i } -> {
                    val (leftCodes, rightCodes) = partition { (c) -> c.bits[i] == Bit.ZERO }
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
