package compressor

import kotlin.math.log2

public fun <T> Collection<T>.entropy(): Double {
    val (countedSymbols, messageLength) = toMessageInfo()
    val probabilities = countedSymbols.values.map { it.toDouble() / messageLength }
    return -probabilities.sumOf { p -> p * log2(p) }
}

public fun <T> Map<T, Code>.averageLength(message: Collection<T>): Double {
    val (countedSymbols, messageLength) = message.toMessageInfo()
    return countedSymbols.map { (s, cnt) ->
        cnt.toDouble() / messageLength to getValue(s).bits.size
    }.sumOf { (p, l) -> p * l }
}

public fun <T> Map<T, Code>.redundancy(message: Collection<T>): Double =
    redundancy(entropy = message.entropy(), averageLength = averageLength(message))

public fun redundancy(entropy: Double, averageLength: Double): Double = 1 - entropy / averageLength
