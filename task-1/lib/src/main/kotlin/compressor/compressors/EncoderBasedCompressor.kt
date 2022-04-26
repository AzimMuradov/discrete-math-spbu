package compressor.compressors

import compressor.*
import compressor.compressors.EncoderBasedCompressor.Metadata

public class EncoderBasedCompressor<T>(
    private val encoder: Encoder<T>,
) : Compressor<List<T>, T, List<Byte>, Metadata<T>> {

    override fun compress(message: List<T>): CompressedMessage<List<Byte>, Metadata<T>> {
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
