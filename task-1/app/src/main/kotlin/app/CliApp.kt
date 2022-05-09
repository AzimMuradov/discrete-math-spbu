package app

import com.github.ajalt.clikt.core.CliktCommand
import compressor.*
import compressor.compressors.*
import compressor.decompressors.*
import compressor.encoders.*
import compressor.utils.*
import java.io.File
import java.util.*

class CliApp : CliktCommand() {

    override fun run() = runWithSeparators(
        separator = ::echo5,
        blocks = arrayOf(::runProblem1, ::runProblem2, ::runProblem3, ::runProblem4, ::runProblem5)
    )


    private fun runProblem1() {
        echo("===================================PROBLEM-01===================================")

        echo()

        echo("{ 1,  10,   0,  01} - ❌")
        echo("{00, 010, 011,  01} - ❌")
        echo("{10, 010, 011,  11} - ✅")
        echo("{ 1,  00, 010, 011} - ✅")
    }

    private fun runProblem2() {
        echo("===================================PROBLEM-02===================================")

        echo()

        echo("""{00, 010, 011, 01} - ✅ -            postfix code""")
        echo("""{ 1,  10,   0, 01} - ❌ - "10" = '1' + '0' = '10'""")
        echo("""{10, 010, 011, 11} - ✅ -             prefix code""")
        echo("""{01, 010, 110, 11} - ✅ -            postfix code""")
    }

    private fun runProblem3() {
        echo("===================================PROBLEM-03===================================")

        echo()

        val message = "aaaaabbbcd".toList()

        val averageLengthFormatted = String.format(
            locale = Locale.ENGLISH,
            format = "%.1f",
            args = arrayOf(ShannonFanoEncoder<Char>().encode(message).averageLength(message))
        )

        echo(averageLengthFormatted)
    }

    private fun runProblem4() {
        echo("===================================PROBLEM-04===================================")

        echo()

        val messageString = listOf(
            "abcbbbbbacabbacddacdbbaccbbadadaddd",
            "abcccccbacabbacbbaddbdaccbbddadadcc",
            "bcabbcdabacbbacbbddcbbaccbbdbdadaacf",
        ).joinToString(separator = " ")

        echo("Message string: \"$messageString\"")

        echo()

        runAlgorithms(
            message = messageString.toList(),
            symToString = Char::toString,
            message2 = messageString.toList().chunked(size = 2).map(::ComparableList),
            sym2ToString = { it.joinToString(separator = "", transform = Char::toString) }
        )
    }

    private fun runProblem5() {
        echo("===================================PROBLEM-05===================================")

        echo()

        echo("File: \"src/main/resources/teapot.bmp\"")

        echo()

        val messageString = File("src/main/resources/teapot.bmp").readText()

        runAlgorithmsMin(
            message = messageString.toList(),
            message2 = messageString.toList().chunked(size = 2).map(::ComparableList),
        )
    }


    private fun <T : Comparable<T>> runAlgorithms(
        message: List<T>,
        symToString: (T) -> String,
        message2: List<ComparableList<T>>,
        sym2ToString: (List<T>) -> String,
    ) = runWithSeparators(
        separator = ::echo3,
        blocks = arrayOf(
            { runEncoder(message, HuffmanEncoder(), symToString) },
            { runEncoder(message, ShannonEncoder(), symToString) },
            { runEncoder(message, ShannonFanoEncoder(), symToString) },

            { runEncoder(message2, HuffmanEncoder(), sym2ToString) },
            { runEncoder(message2, ShannonEncoder(), sym2ToString) },
            { runEncoder(message2, ShannonFanoEncoder(), sym2ToString) },

            { runCompressor(message, ArithmeticCompressor(), ArithmeticDecompressor()) },
            { runCompressor(message, DynamicHuffmanCompressor(), DynamicHuffmanDecompressor()) },
            { runCompressor(message, DynamicHuffmanWithEscCompressor(), DynamicHuffmanWithEscDecompressor()) },
            // { runCompressor(message, LzwCompressor(), LzwDecompressor()) },
        )
    )


    private fun <T : Comparable<T>> runAlgorithmsMin(
        message: List<T>,
        message2: List<ComparableList<T>>,
    ) = runWithSeparators(
        separator = ::echo3,
        blocks = arrayOf(
            { runEncoderMinInfo(message, HuffmanEncoder()) },
            { runEncoderMinInfo(message, ShannonEncoder()) },
            { runEncoderMinInfo(message, ShannonFanoEncoder()) },

            { runEncoderMinInfo(message2, HuffmanEncoder()) },
            { runEncoderMinInfo(message2, ShannonEncoder()) },
            { runEncoderMinInfo(message2, ShannonFanoEncoder()) },

            { runCompressorMinInfo(message, ArithmeticCompressor(), ArithmeticDecompressor()) },
            { runCompressorMinInfo(message, DynamicHuffmanCompressor(), DynamicHuffmanDecompressor()) },
            { runCompressorMinInfo(message, DynamicHuffmanWithEscCompressor(), DynamicHuffmanWithEscDecompressor()) },
            // { runCompressor(message, LzwCompressor(), LzwDecompressor()) },
        )
    )

    private fun <T> runEncoder(
        message: List<T>,
        encoder: Encoder<T>,
        symToString: (T) -> String,
    ) {
        val codes = encoder.encode(message)

        echo("Encoder: ${encoder::class.simpleName}")
        echo("Codes:")
        echo(codes.toList().joinToString(separator = "\n") { (s, c) -> "'${symToString(s)}' - $c" })

        echo("Message: $message")
        echo("Message length: ${message.count()}")
        echo("Message entropy: ${message.entropy()}")
        echo("Code average length: ${codes.averageLength(message)}")
        echo("Code redundancy: ${codes.redundancy(message)}")

        echo()

        runCompressor(message, EncoderBasedCompressor(encoder), PrefixTreeDecompressor())
    }

    private fun <T, M> runCompressor(
        message: List<T>,
        compressor: Compressor<T, M>,
        decompressor: Decompressor<T, M>,
    ) {
        val (compressed, metadata) = compressor.compress(message)

        echo("Compressor: ${compressor::class.simpleName}")
        echo("Compressed in bits: $compressed")
        echo("Compressed metadata: $metadata")
        echo("Compressed in bits length: ${compressed.size}")
        echo("Compressed in bytes length: ${compressed.toByteArray().size}")

        echo()

        echo("Decompressor: ${decompressor::class.simpleName}")
        echo("Decompressor status: ${decompressor.decompress(CompressedMessage(compressed, metadata)) == message}")
    }

    private fun <T> runEncoderMinInfo(
        message: List<T>,
        encoder: Encoder<T>,
    ) {
        val codes = encoder.encode(message)

        echo("Encoder: ${encoder::class.simpleName}")
        echo("Message length: ${message.count()}")
        echo("Message entropy: ${message.entropy()}")
        echo("Code average length: ${codes.averageLength(message)}")
        echo("Code redundancy: ${codes.redundancy(message)}")

        echo()

        runCompressorMinInfo(message, EncoderBasedCompressor(encoder), PrefixTreeDecompressor())
    }

    private fun <T, M> runCompressorMinInfo(
        message: List<T>,
        compressor: Compressor<T, M>,
        decompressor: Decompressor<T, M>,
    ) {
        val (compressed, metadata) = compressor.compress(message)

        echo("Compressor: ${compressor::class.simpleName}")
        echo("Compressed in bits length: ${compressed.size}")
        echo("Compressed in bytes length: ${compressed.toByteArray().size}")

        echo()

        echo("Decompressor: ${decompressor::class.simpleName}")
        echo("Decompressor status: ${decompressor.decompress(CompressedMessage(compressed, metadata)) == message}")
    }


    private fun runWithSeparators(separator: () -> Unit, vararg blocks: () -> Unit) {
        if (blocks.isNotEmpty()) {
            blocks.first()()
            for (block in blocks.drop(n = 1)) {
                separator()
                block()
            }
        }
    }

    private fun echo3() = repeat(times = 3) { echo() }

    private fun echo5() = repeat(times = 5) { echo() }
}
