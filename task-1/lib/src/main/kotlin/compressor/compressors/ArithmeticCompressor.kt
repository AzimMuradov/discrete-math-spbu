package compressor.compressors

import compressor.*
import compressor.compressors.ArithmeticCompressor.Metadata
import java.math.*
import kotlin.math.ceil
import kotlin.math.log2

public class ArithmeticCompressor<T : Comparable<T>> : Compressor<List<T>, T, List<Byte>, Metadata<T>> {

    override fun compress(message: List<T>): CompressedMessage<List<Byte>, Metadata<T>> {
        val messageInfo = message.toMessageInfo()
        val codedSegment = getIntegerSegmentCode(message)
        val bits = getBinCode(messageInfo.messageLength, codedSegment)

        return CompressedMessage(
            compressed = Code(bits).toByteList(),
            metadata = Metadata(messageInfo = messageInfo, compressedBitsLength = bits.size)
        )
    }


    private fun getIntegerSegmentCode(message: List<T>): Segment<BigInteger> {
        val (countedSymbols, messageLength) = message.toMessageInfo()

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
            if (seg.middle() !in codeSegment) {
                val m = seg.middle()
                val (leftSeg, rightSeg) = Segment(seg.l, m) to Segment(m, seg.r)

                if (codeSegment.r in leftSeg) leftSeg else rightSeg
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


    public data class Metadata<T>(
        val messageInfo: MessageInfo<T>,
        val compressedBitsLength: Int,
    )


    private data class Segment<T : Comparable<T>>(val l: T, val r: T)

    private operator fun <T : Comparable<T>> Segment<T>.contains(element: T) = element in l..r
}
