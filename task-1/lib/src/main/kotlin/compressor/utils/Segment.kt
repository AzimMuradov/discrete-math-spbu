package compressor.utils

/**
 * Segment [[l]..[r]) of type [T].
 */
public data class Segment<T : Comparable<T>>(val l: T, val r: T)

/**
 * Check if [element] is contained in [this] segment.
 */
public operator fun <T : Comparable<T>> Segment<T>.contains(element: T): Boolean = element != r && element in l..r
