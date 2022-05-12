package compressor

/**
 * Message decompressor.
 *
 * @param [T] The type of the message symbol.
 * @param [M] The type of the [compressed message][CompressedMessage] metadata.
 */
public interface Decompressor<out T, in M> {

    /**
     * Decompress the given [compressed message][compressedMessage].
     */
    public fun decompress(compressedMessage: CompressedMessage<M>): List<T>
}
