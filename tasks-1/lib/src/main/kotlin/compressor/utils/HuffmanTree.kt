package compressor.utils

/**
 * Tree created using Huffman encoding.
 *
 * Like any tree it contains the [root] node. It also has the list of all symbols that this tree encodes.
 */
public data class HuffmanTree<T : Comparable<T>>(
    val root: HuffmanNode<T>,
    val symbols: List<T>,
)

/**
 * Node of the [HuffmanTree].
 *
 * It contains the [symbol] of this node, symbol's [count] and the node's [children].
 */
public data class HuffmanNode<T : Comparable<T>>(
    val symbol: ComparableList<T>,
    val children: Pair<HuffmanNode<T>, HuffmanNode<T>>?,
    val count: Int,
)

/**
 * Build codes from [this] Huffman tree.
 *
 * Every time it goes left it adds '0', and every time it goes right it adds '1' to the currently calculated code.
 */
public fun <T : Comparable<T>> HuffmanTree<T>.buildCodes(): Map<T, Bits> {
    val nodesToCodes = mutableMapOf(root.symbol to Bits.EMPTY)
    val leavesToCodes = mutableMapOf<T, Bits>()

    fun HuffmanNode<T>.encodeSubTrees() {
        if (children != null) {
            for ((i, child) in children.toList().withIndex()) {
                nodesToCodes[child.symbol] = nodesToCodes.getValue(symbol) + i.toBit()
                child.encodeSubTrees()
            }
        } else {
            leavesToCodes[symbol.first()] = nodesToCodes.getValue(symbol)
        }
    }

    root.encodeSubTrees()

    return leavesToCodes
}
