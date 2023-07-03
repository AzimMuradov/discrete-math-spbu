package compressor.decompressors

import compressor.CompressedMessage
import compressor.Decompressor
import compressor.compressors.ArithmeticCompressor.Metadata
import compressor.utils.Segment
import compressor.utils.contains
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.ceil
import kotlin.math.log2

/**
 * Arithmetic decompressor.
 *
 * Decompresses message that was compressed with [arithmetic compressor][compressor.compressors.ArithmeticCompressor].
 */
public class ArithmeticDecompressor<T : Comparable<T>> : Decompressor<T, Metadata<T>> {

    override fun decompress(compressedMessage: CompressedMessage<Metadata<T>>): List<T> {
        val (_, metadata) = compressedMessage
        val (countedSymbols, messageLength) = metadata.messageInfo

        val (symbols, counts) = countedSymbols.toSortedMap().toList().unzip()
        val bigIntegerCounts = counts.map(Int::toBigInteger)
        val normalizer = messageLength.toBigInteger()

        val code = compressedMessage.compressed

        val initSegment = Segment(BigInteger.ZERO, messageLength.toBigInteger().pow(messageLength))

        val integerCode = "$code"
            .toBigInteger(radix = 2)
            .toBigDecimal()
            .divide(
                2.toBigDecimal().pow(code.size),
                messageLength * ceil(log2(messageLength.toDouble())).toInt(),
                RoundingMode.HALF_EVEN
            )
            .multiply(initSegment.r.toBigDecimal())
            .stripTrailingZeros()
            .toBigInteger()

        fun Segment<BigInteger>.segments(): List<Segment<BigInteger>> {
            val unit = (r - l) / normalizer
            return bigIntegerCounts
                .runningFold(initial = BigInteger.ZERO) { acc, c -> acc + c * unit }
                .zipWithNext()
                .map { (lI, rI) -> Segment(l = l + lI, r = l + rI) }
        }

        val (out, _) = (0 until messageLength).fold(initial = emptyList<T>() to initSegment) { (out, seg), _ ->
            val segments = seg.segments()
            val nextSegIndex = segments.indexOfFirst { integerCode in it }
            (out + symbols[nextSegIndex]) to segments[nextSegIndex]
        }

        return out
    }
}
