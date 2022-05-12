package compressor.utils

import kotlin.math.log2

/**
 * Calculate the [message][this] entropy.
 */
public fun <T> List<T>.entropy(): Double {
    val (countedSymbols, messageLength) = calculateMessageInfo()
    val probabilities = countedSymbols.values.map { it.toDouble() / messageLength }
    return -probabilities.sumOf { p -> p * log2(p) }
}

/**
 * Calculate the average length of [character codes][this] for the given [message].
 */
public fun <T> Map<T, Bits>.averageLength(message: List<T>): Double {
    val (countedSymbols, messageLength) = message.calculateMessageInfo()
    return countedSymbols.map { (s, cnt) ->
        cnt.toDouble() / messageLength to getValue(s).size
    }.sumOf { (p, l) -> p * l }
}

/**
 * Calculate the redundancy of [character codes][this] for the given [message].
 */
public fun <T> Map<T, Bits>.redundancy(message: List<T>): Double = 1 - message.entropy() / averageLength(message)
