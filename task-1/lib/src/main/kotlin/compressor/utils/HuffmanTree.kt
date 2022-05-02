package compressor.utils

import compressor.*

public data class HuffmanTree<T : Comparable<T>>(
    val root: HuffmanNode<T>,
    val symbols: List<T>,
)

public data class HuffmanNode<T : Comparable<T>>(
    val symbol: ComparableList<T>,
    val children: Pair<HuffmanNode<T>, HuffmanNode<T>>?,
    val count: Int,
)

public fun <T : Comparable<T>> HuffmanTree<T>.buildCodes(): Map<T, Code> {
    val nodesToCodes = mutableMapOf(root.symbol to Code.EMPTY)
    val leavesToCodes = mutableMapOf<T, Code>()

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
