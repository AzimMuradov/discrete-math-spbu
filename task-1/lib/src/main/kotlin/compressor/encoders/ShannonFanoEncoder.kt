package compressor.encoders

import compressor.Encoder
import compressor.utils.*
import kotlin.math.abs

/**
 * Shannon-Fano encoder.
 *
 * Encodes arbitrary message with the Shannon-Fano encoding.
 */
public class ShannonFanoEncoder<T : Comparable<T>> : Encoder<T> {

    override fun encode(message: List<T>): Map<T, Bits> {
        val (countedSymbols, _) = message.calculateMessageInfo()
        val (sortedSymbols, sortedCounters) = countedSymbols
            .toList()
            .sortedWith(compareByDescending(Pair<T, Int>::second).thenComparing(Pair<T, Int>::first))
            .unzip()

        val bits = MutableList(size = countedSymbols.size) { Bits.EMPTY }

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
                    bits[j] = bits[j] + i.toBit()
                }
                half.encodeSubTrees()
            }
        }

        sortedCounters.indices.encodeSubTrees()

        return (sortedSymbols zip bits).toMap()
    }
}
