package compressor.decompressors

import compressor.CompressedMessage
import compressor.Decompressor
import compressor.compressors.LzwCompressor
import compressor.utils.asBits

/**
 * Lempel-Ziv-Welch (LZW) decompressor.
 *
 * Decompresses message that was compressed with [LZW compressor][compressor.compressors.LzwCompressor].
 */
public class LzwDecompressor<T> : Decompressor<T, LzwCompressor.Metadata<T>> {

    override fun decompress(compressedMessage: CompressedMessage<LzwCompressor.Metadata<T>>): List<T> {
        val (compressed, metadata) = compressedMessage

        val alphabet = metadata.alphabet

        val phrases = alphabet.mapTo(mutableListOf()) { listOf(it) }

        var current = phrases[
                compressed.subList(
                    fromIndex = 0,
                    toIndex = alphabet.lastIndex.toString(radix = 2).length
                ).asBits().toString().toInt(radix = 2)
        ]

        val message = current.toMutableList()


        var i = alphabet.lastIndex.toString(radix = 2).length

        while (i != compressed.size) {
            val codeLen = phrases.size.toString(radix = 2).length
            val index = compressed.subList(fromIndex = i, toIndex = i + codeLen).asBits().toString().toInt(radix = 2)

            val phrase = if (index < phrases.size) {
                phrases[index]
            } else {
                current + current.first()
            }

            phrases += current + phrase.first()
            current = phrase

            message += phrase

            i += codeLen
        }

        return message
    }
}
