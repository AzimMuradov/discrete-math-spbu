package compressor.encoders

import compressor.*
import java.util.*

public class HuffmanEncoder<T> : Encoder<Collection<T>, T> {

    override fun encode(message: Collection<T>): Map<T, BinaryCode> {
        val (countedSymbols, _) = message.toMsgInfo()
        val elements = countedSymbols.map { (symbol, count) ->
            HuffmanElement(
                symbol = listOf(symbol),
                children = null,
                count = count
            )
        }

        val queue = PriorityQueue(compareBy(HuffmanElement<T>::count)).apply {
            addAll(elements)
        }

        while (queue.size != 1) {
            val a = queue.poll()
            val b = queue.poll()
            val ab = HuffmanElement(
                symbol = a.symbol + b.symbol,
                children = a to b,
                count = a.count + b.count
            )
            queue.add(ab)
        }

        return queue.poll().buildCodes()
    }
}


private data class HuffmanElement<T>(
    val symbol: List<T>,
    val children: Pair<HuffmanElement<T>, HuffmanElement<T>>?,
    val count: Int,
)

private fun <T> HuffmanElement<T>.buildCodes(): Map<T, BinaryCode> {
    val nodesToCodes = mutableMapOf(symbol to BinaryCode.EMPTY)
    val leavesToCodes = mutableMapOf<T, BinaryCode>()

    fun HuffmanElement<T>.encodeSubTree() {
        if (children != null) {
            for ((i, child) in children.toList().withIndex()) {
                nodesToCodes[child.symbol] = nodesToCodes.getValue(symbol) + BinSym.values()[i]
                child.encodeSubTree()
            }
        } else {
            leavesToCodes[symbol.first()] = nodesToCodes.getValue(symbol)
        }
    }

    encodeSubTree()

    return leavesToCodes
}
