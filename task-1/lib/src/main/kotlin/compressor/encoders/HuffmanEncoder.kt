package compressor.encoders

import compressor.*
import java.util.*

public class HuffmanEncoder<T> : Encoder<Collection<T>, T> {

    override fun encode(message: Collection<T>): Map<T, Code> {
        val (countedSymbols, _) = message.toMsgInfo()
        val nodes = countedSymbols.map { (symbol, count) ->
            Node(
                symbol = listOf(symbol),
                children = null,
                count = count
            )
        }

        val queue = PriorityQueue(compareBy(Node<T>::count)).apply {
            addAll(nodes)
        }

        while (queue.size != 1) {
            val a = queue.poll()
            val b = queue.poll()
            val ab = Node(
                symbol = a.symbol + b.symbol,
                children = a to b,
                count = a.count + b.count
            )
            queue.add(ab)
        }

        return queue.poll().buildCodes()
    }


    private data class Node<T>(
        val symbol: List<T>,
        val children: Pair<Node<T>, Node<T>>?,
        val count: Int,
    )

    private fun <T> Node<T>.buildCodes(): Map<T, Code> {
        val nodesToCodes = mutableMapOf(symbol to Code.EMPTY)
        val leavesToCodes = mutableMapOf<T, Code>()

        fun Node<T>.encodeSubTrees() {
            if (children != null) {
                for ((i, child) in children.toList().withIndex()) {
                    nodesToCodes[child.symbol] = nodesToCodes.getValue(symbol) + i.toBit()
                    child.encodeSubTrees()
                }
            } else {
                leavesToCodes[symbol.first()] = nodesToCodes.getValue(symbol)
            }
        }

        encodeSubTrees()

        return leavesToCodes
    }
}
