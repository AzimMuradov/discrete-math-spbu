package compressor

import compressor.utils.Bits

/**
 * Message encoder.
 *
 * It encodes every message symbol with the [bit sequence][Bits].
 *
 * @param [T] The type of the message symbol.
 */
public interface Encoder<T> {

    /**
     * Encode the given [message].
     */
    public fun encode(message: List<T>): Map<T, Bits>
}
