package app

import com.github.ajalt.clikt.core.CliktCommand
import compressor.*
import compressor.compressors.*
import compressor.decompressors.*
import compressor.encoders.*
import compressor.utils.*

class CliApp : CliktCommand() {

    override fun run() {
        val messageString = listOf(
            "abcbbbbbacabbacddacdbbaccbbadadaddd",
            "abcccccbacabbacbbaddbdaccbbddadadcc",
            "bcabbcdabacbbacbbddcbbaccbbdbdadaacf",
        ).joinToString(separator = " ")

        echo("Message string: \"$messageString\"")

        val message = messageString.toList()

        runAllTests(
            message = message,
            symToString = Char::toString,
            sym2ToString = { it.joinToString(separator = "", transform = Char::toString) }
        )
    }


    private fun <T : Comparable<T>> runAllTests(
        message: List<T>,
        symToString: (T) -> String,
        sym2ToString: (List<T>) -> String,
    ) {
        echo("Message: $message")
        echo("Message length: ${message.count()}")
        echo("Message entropy: ${message.entropy()}")

        echo()
        echo()

        runEncoderTests(message, symToString, sym2ToString)
        runCompressorsTests(message)
    }


    private fun <T : Comparable<T>> runEncoderTests(
        message: List<T>,
        symToString: (T) -> String,
        sym2ToString: (List<T>) -> String,
    ) {
        runEncoderTest(message, HuffmanEncoder(), symToString)
        runEncoderTest(message, ShannonEncoder(), symToString)
        runEncoderTest(message, ShannonFanoEncoder(), symToString)
        runEncoderTest(message.chunked(size = 2).map(::ComparableList), HuffmanEncoder(), sym2ToString)
        runEncoderTest(message.chunked(size = 2).map(::ComparableList), ShannonEncoder(), sym2ToString)
        runEncoderTest(message.chunked(size = 2).map(::ComparableList), ShannonFanoEncoder(), sym2ToString)
    }

    private fun <T : Comparable<T>> runCompressorsTests(message: List<T>) {
        runCompressorTest(message, ArithmeticCompressor(), ArithmeticDecompressor())

        echo()
        echo()
        echo()

        runCompressorTest(
            message = message.chunked(size = 2).map(::ComparableList),
            compressor = ArithmeticCompressor(),
            decompressor = ArithmeticDecompressor()
        )

        echo()
        echo()
        echo()

        runCompressorTest(
            message = message,
            compressor = DynamicHuffmanCompressor(),
            decompressor = DynamicHuffmanDecompressor()
        )

        echo()
        echo()
        echo()

        runCompressorTest(
            message = message,
            compressor = DynamicHuffmanWithEscCompressor(),
            decompressor = DynamicHuffmanWithEscDecompressor()
        )
    }


    private fun <T> runEncoderTest(
        message: List<T>,
        encoder: Encoder<T>,
        symToString: (T) -> String,
    ) {
        val codes = encoder.encode(message)

        echo("Encoder: ${encoder::class.simpleName}")
        echo("Codes:")
        echo(codes.toList().joinToString(separator = "\n") { (s, c) -> "'${symToString(s)}' - $c" })

        echo("Code average length: ${codes.averageLength(message)}")
        echo("Code redundancy: ${codes.redundancy(message)}")

        echo()

        runCompressorTest(
            message = message,
            compressor = EncoderBasedCompressor(encoder),
            decompressor = PrefixTreeDecompressor()
        )

        echo()
        echo()
        echo()
    }

    private fun <T, M> runCompressorTest(
        message: List<T>,
        compressor: Compressor<T, M>,
        decompressor: Decompressor<T, M>? = null,
    ) {
        val (compressed, metadata) = compressor.compress(message)

        echo("Compressor: ${compressor::class.simpleName}")
        echo("Compressed: $compressed")
        echo("Compressed in UTF-8: ${compressed.toByteArray().decodeToString()}")
        echo("Compressed metadata: $metadata")

        if (decompressor != null) {
            echo()

            echo("Decompressor: ${decompressor::class.simpleName}")
            echo("Decompressed: ${decompressor.decompress(CompressedMessage(compressed, metadata))}")
        }
    }
}
