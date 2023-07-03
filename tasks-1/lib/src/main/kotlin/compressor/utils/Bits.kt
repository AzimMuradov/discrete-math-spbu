package compressor.utils

import compressor.utils.Bit.ONE
import compressor.utils.Bit.ZERO

/**
 * List of bits.
 */
public class Bits internal constructor(private val bits: List<Bit>) : List<Bit> by bits {

    override fun equals(other: Any?): Boolean = bits == other

    override fun hashCode(): Int = bits.hashCode()

    /**
     * Join bits to string without separator.
     *
     * For example:
     * Bits([0, 1, 0, 1, 1, 1]).toString() == "010111".
     */
    override fun toString(): String = bits.joinToString(separator = "")


    public companion object {

        /**
         * Empty list of bits.
         */
        public val EMPTY: Bits = Bits(bits = emptyList())
    }
}

/**
 * Convert [this] to [Bits].
 */
public fun List<Bit>.asBits(): Bits = Bits(bits = this)


// Bits <-> Byte List <-> String

/**
 * Convert [Bits] to the list of bytes, if bits.size % 8 != 0, then it will be padded at the end with zeros.
 *
 * For example:
 * Bits([0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1]).toByteList() == [0b01011101, 0b01110000].
 */
public fun Bits.toByteArray(): ByteArray {
    val chunkedBits = chunked(size = 8)

    return ByteArray(size = chunkedBits.size) {
        chunkedBits[it].joinToString(separator = "").padEnd(length = 8, padChar = '0').toUByte(radix = 2).toByte()
    }
}

/**
 * Convert list of bytes to [Bits] and take first [length] elements.
 *
 * For example:
 * [0b01011101, 0b01110000].toBits(length = 12) == Bits([0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1]).
 */
public fun ByteArray.toBits(length: Int): Bits = joinToString(separator = "") {
    it.toUByte().toString(radix = 2).padStart(length = 8, padChar = '0')
}.take(length).toBits()

/**
 * Convert string of '0' and '1' characters to [Bits].
 *
 * @throws IllegalArgumentException if it contains characters other than '0' or '1'.
 */
public fun String.toBits(): Bits = map { it.digitToInt().toBit() }.asBits()


/**
 * Binary digit. It can be either [0][ZERO] or [1][ONE].
 */
public enum class Bit(public val int: Int) {
    ZERO(int = 0),
    ONE(int = 1);

    /**
     * Convert this bit to the string.
     */
    override fun toString(): String = "$int"
}


/**
 * Convert int to bit, or throw [IllegalArgumentException] if [this] int isn't 0 or 1.
 */
public fun Int.toBit(): Bit = when (this) {
    0 -> ZERO
    1 -> ONE
    else -> throw IllegalArgumentException("Bit can be either 0 or 1")
}


// Concatenation operators

/**
 * Concatenate [bits][this] with [bit].
 */
public operator fun Bits.plus(bit: Bit): Bits = (this + listOf(bit)).asBits()

/**
 * Concatenate [bit][this] with [bits].
 */
public operator fun Bit.plus(bits: Bits): Bits = (listOf(this) + bits).asBits()

/**
 * Concatenate [bits][this] with [bits].
 */
public operator fun Bits.plus(bits: Bits): Bits = (this + bits as List<Bit>).asBits()
