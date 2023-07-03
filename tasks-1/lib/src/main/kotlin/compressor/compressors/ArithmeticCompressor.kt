package compressor.compressors

import compressor.CompressedMessage
import compressor.Compressor
import compressor.compressors.ArithmeticCompressor.Metadata
import compressor.utils.*
import java.math.*
import kotlin.math.ceil
import kotlin.math.log2

/**
 * Arithmetic compressor.
 *
 * Compresses arbitrary message with the arithmetic compressing.
 */
public class ArithmeticCompressor<T : Comparable<T>> : Compressor<T, Metadata<T>> {

    override fun compress(message: List<T>): CompressedMessage<Metadata<T>> {
        val messageInfo = message.calculateMessageInfo()
        val codedSegment = getIntegerSegmentCode(message)
        val bits = getBinCode(messageInfo.messageLength, codedSegment).asBits()

        return CompressedMessage(
            compressed = bits,
            metadata = Metadata(messageInfo)
        )
    }


    private fun getIntegerSegmentCode(message: List<T>): Segment<BigInteger> {
        val (countedSymbols, messageLength) = message.calculateMessageInfo()

        val (symbols, counts) = countedSymbols.toSortedMap().toList().unzip()
        val bigIntegerCounts = counts.map(Int::toBigInteger)
        val normalizer = messageLength.toBigInteger()

        fun Segment<BigInteger>.segments(): List<Segment<BigInteger>> {
            val unit = r - l
            return bigIntegerCounts
                .runningFold(initial = BigInteger.ZERO) { acc, c -> acc + c }
                .zipWithNext()
                .map { (lI, rI) -> Segment(l = l * normalizer + lI * unit, r = l * normalizer + rI * unit) }
        }

        return message.fold(initial = Segment(l = BigInteger.ZERO, r = BigInteger.ONE)) { acc, s ->
            acc.segments()[symbols.indexOf(s)]
        }
    }

    private fun getBinCode(messageLength: Int, integerCodeSegment: Segment<BigInteger>): List<Bit> {
        val scale = messageLength * ceil(log2(messageLength.toDouble())).toInt()

        fun BigInteger.downscale(): BigDecimal = toBigDecimal().divide(
            messageLength.toBigDecimal().pow(messageLength),
            scale,
            RoundingMode.HALF_EVEN
        )

        val codeSegment = with(integerCodeSegment) { Segment(l = l.downscale(), r = r.downscale()) }

        fun Segment<BigDecimal>.middle() = (l + r).divide(2.toBigDecimal(), scale, RoundingMode.HALF_EVEN)

        val binaryCodeSegment = generateSequence(seed = Segment(BigDecimal.ZERO, BigDecimal.ONE)) { seg ->
            val m = seg.middle()
            if (m !in codeSegment) {
                val (leftSeg, rightSeg) = Segment(seg.l, m) to Segment(m, seg.r)

                if (codeSegment.l in rightSeg) rightSeg else leftSeg
            } else {
                null
            }
        }.last()

        return buildList {
            var middle = binaryCodeSegment.middle().stripTrailingZeros()
            while (middle != BigDecimal.ZERO) {
                val twice = (middle * 2.toBigDecimal()).stripTrailingZeros()
                add(twice.toInt().toBit())
                middle = twice % BigDecimal.ONE
            }
        }
    }


    /**
     * Metadata contains [message info][MessageInfo].
     */
    public data class Metadata<T>(val messageInfo: MessageInfo<T>)
}
