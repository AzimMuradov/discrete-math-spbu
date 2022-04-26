package compressor

public interface Encoder<T> {

    public fun encodeSymbolsOf(message: List<T>): Map<T, Code>


    public companion object
}

public fun <T> Encoder<T>.encode(message: List<T>): EncodedMessage<T> =
    Encoder.encode(message, codes = encodeSymbolsOf(message))

public fun <T> Encoder.Companion.encode(message: List<T>, codes: Map<T, Code>): EncodedMessage<T> = EncodedMessage(
    encoded = message.flatMap { codes.getValue(it).bits }.run(::Code),
    codes = codes
)


public data class EncodedMessage<T>(
    public val encoded: Code,
    public val codes: Map<T, Code>,
)
