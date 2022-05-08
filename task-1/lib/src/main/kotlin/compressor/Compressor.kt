package compressor

import compressor.utils.Bits

/**
 * Message compressor.
 *
 * It compresses any message to the [sequence of bits][Bits].
 *
 * @param [T] The type of the message symbol.
 * @param [M] The type of the [compressed message][CompressedMessage] metadata. It usually contains information necessary to [decompress][Decompressor] the output message.
 */
public interface Compressor<in T, out M> {

    /**
     * Compress the given [message].
     */
    public fun compress(message: List<T>): CompressedMessage<M>
}


/**
 * Compressed message.
 *
 * It contains the resulting [output message][compressed] and it's [metadata].
 */
public data class CompressedMessage<out M>(
    val compressed: Bits,
    val metadata: M,
)
