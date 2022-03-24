package compressor.encoders

import compressor.*
import kotlin.math.ceil
import kotlin.math.log2

public class ShannonEncoder<T> : Encoder<Collection<T>, T> {

    override fun encode(message: Collection<T>): Map<T, Code> {
        val (countedSymbols, len) = message.toMsgInfo()
        val (sortedSymbols, sortedCounters) = countedSymbols.toList().sortedByDescending { it.second }.unzip()

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
            }.map(Int::toBit).run(::Code)
        }

        return (sortedSymbols zip codes).toMap()
    }
}
