package compressor.decompressors

import compressor.*
import compressor.compressors.ArithmeticCompressor.Metadata
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.ceil
import kotlin.math.log2

public class ArithmeticDecompressor<T : Comparable<T>> : Decompressor<List<T>, T, List<Byte>, Metadata<T>> {

    override fun decompress(compressedMessage: CompressedMessage<List<Byte>, Metadata<T>>): List<T> {
        val (_, metadata) = compressedMessage
        val (messageInfo, _) = metadata
        val (countedSymbols, messageLength) = messageInfo

        val (symbols, counts) = countedSymbols.toSortedMap().toList().unzip()
        val bigIntegerCounts = counts.map(Int::toBigInteger)
        val normalizer = messageLength.toBigInteger()

        val code = compressedMessage.compressed.toCode(compressedMessage.metadata.compressedBitsLength)

        val initSegment = Segment(BigInteger.ZERO, messageLength.toBigInteger().pow(messageLength))

        val integerCode = "$code"
            .toBigInteger(radix = 2)
            .toBigDecimal()
            .divide(
                2.toBigDecimal().pow(code.bits.size),
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


    private data class Segment<T : Comparable<T>>(val l: T, val r: T)

    private operator fun <T : Comparable<T>> Segment<T>.contains(element: T) = element in l..r
}
