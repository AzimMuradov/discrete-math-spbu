package compressor.compressors

import compressor.*
import compressor.compressors.EncoderBasedCompressor.Metadata

public class EncoderBasedCompressor<I : Iterable<T>, T>(
    private val encoder: Encoder<I, T>,
) : Compressor<I, T, Collection<Byte>, Metadata<T>> {

    override fun compress(message: I): CompressedMessage<Collection<Byte>, Metadata<T>> {
        val (encoded, codes) = encoder.encode(message)
        return CompressedMessage(
            compressed = encoded.toByteList(),
            metadata = Metadata(
                codes = codes,
                length = encoded.bits.size
            )
        )
    }


    public data class Metadata<T>(
        val codes: Map<T, Code>,
        val length: Int,
    )
}
