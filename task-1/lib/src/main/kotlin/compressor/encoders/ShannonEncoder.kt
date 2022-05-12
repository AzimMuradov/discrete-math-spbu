package compressor.encoders

import compressor.Encoder
import compressor.utils.*
import kotlin.math.ceil
import kotlin.math.log2

/**
 * Shannon encoder.
 *
 * Encodes arbitrary message with the Shannon encoding.
 */
public class ShannonEncoder<T : Comparable<T>> : Encoder<T> {

    override fun encode(message: List<T>): Map<T, Bits> {
        val (countedSymbols, len) = message.calculateMessageInfo()
        val (sortedSymbols, sortedCounters) = countedSymbols
            .toList()
            .sortedWith(compareByDescending(Pair<T, Int>::second).thenComparing(Pair<T, Int>::first))
            .unzip()

        val codesLengths = sortedCounters.map {
            ceil(-log2(it.toDouble() / len)).toInt()
        }

        val sumsOfPrevPrs = sortedCounters.runningFold(initial = 0) { rSum, cnt -> cnt + rSum }.dropLast(n = 1).map {
            it.toDouble() / len
        }

        val codes = (codesLengths zip sumsOfPrevPrs).map { (codeLen, sumOfPrevPrs) ->
            buildList {
                var x = sumOfPrevPrs
                repeat(codeLen) {
                    x *= 2
                    add(x.toInt())
                    x -= x.toInt()
                }
            }.map(Int::toBit).asBits()
        }

        return (sortedSymbols zip codes).toMap()
    }
}
