package compressor.utils

import compressor.*
import java.util.*

internal class HuffmanTreeBuilder<T : Comparable<T>> {

    fun buildTree(countedSymbols: Map<T, Int>): HuffmanTree<T> {
        if (countedSymbols.isEmpty()) return HuffmanTree(
            root = HuffmanNode(
                symbol = ComparableList(emptyList()),
                children = null,
                count = 0
            ),
            symbols = emptyList()
        )

        val nodes = countedSymbols.map { (symbol, count) ->
            HuffmanNode(
                symbol = ComparableList(listOf(symbol)),
                children = null,
                count = count
            )
        }

        val queue = PriorityQueue(compareBy(HuffmanNode<T>::count).thenComparing(HuffmanNode<T>::symbol)).apply {
            addAll(nodes)
        }

        while (queue.size != 1) {
            val a = queue.poll()
            val b = queue.poll()
            val ab = HuffmanNode(
                symbol = ComparableList(list = a.symbol + b.symbol),
                children = a to b,
                count = a.count + b.count
            )
            queue.add(ab)
        }

        return HuffmanTree(
            root = queue.poll(),
            symbols = countedSymbols.keys.sorted()
        )
    }
}
