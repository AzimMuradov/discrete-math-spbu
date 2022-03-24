package compressor

public interface Encoder<in I : Iterable<T>, T> {

    public fun encodeSymbolsOf(message: I): Map<T, Code>


    public companion object
}

public fun <I : Iterable<T>, T> Encoder<I, T>.encode(message: I): EncodedMessage<T> =
    Encoder.encode(message, codes = encodeSymbolsOf(message))

public fun <I : Iterable<T>, T> Encoder.Companion.encode(
    message: I,
    codes: Map<T, Code>,
): EncodedMessage<T> = EncodedMessage(
    encoded = message.flatMap { codes.getValue(it).bits }.run(::Code),
    codes = codes
)


public data class EncodedMessage<T>(
    public val encoded: Code,
    public val codes: Map<T, Code>,
)
