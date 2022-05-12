package compressor.compressors

import compressor.*
import compressor.compressors.EncoderBasedCompressor.Metadata
import compressor.utils.Bits
import compressor.utils.asBits

/**
 * Compressor that uses [encoder] as an underlying compressor engine.
 */
public class EncoderBasedCompressor<T>(
    private val encoder: Encoder<T>,
) : Compressor<T, Metadata<T>> {

    override fun compress(message: List<T>): CompressedMessage<Metadata<T>> {
        val codes = encoder.encode(message)
        return CompressedMessage(
            compressed = message.flatMap(codes::getValue).asBits(),
            metadata = Metadata(codes)
        )
    }


    /**
     * [CompressedMessage] metadata, it is equal to the output of the [Encoder.encode] method.
     */
    public data class Metadata<T>(val codes: Map<T, Bits>)
}
