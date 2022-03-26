package compressor.encoders

import compressor.*
import kotlin.math.abs

public class ShannonFanoEncoder<T> : Encoder<Collection<T>, T> {

    override fun encodeSymbolsOf(message: Collection<T>): Map<T, Code> {
        val (countedSymbols, _) = message.toMessageInfo()
        val (sortedSymbols, sortedCounters) = countedSymbols.toList().sortedByDescending { it.second }.unzip()

        val codes = MutableList(size = countedSymbols.size) { Code.EMPTY }

        fun IntRange.encodeSubTrees() {
            val size = last - first + 1

            if (size == 1) return

            val subList = sortedCounters.slice(indices = this)

            val sum = subList.sum()

            val dividerIndex = first + subList.runningFold(initial = 0) { rSum, cnt -> cnt + rSum }.map {
                abs(sum - it * 2)
            }.withIndex().minByOrNull { it.value }!!.index

            val halves = listOf(first until dividerIndex, dividerIndex..last)

            for ((i, half) in halves.withIndex()) {
                for (j in half) {
                    codes[j] = codes[j] + i.toBit()
                }
                half.encodeSubTrees()
            }
        }

        sortedCounters.indices.encodeSubTrees()

        return (sortedSymbols zip codes).toMap()
    }
}
