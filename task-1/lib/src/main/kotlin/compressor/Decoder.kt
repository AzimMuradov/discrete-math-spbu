package compressor

public interface Decoder<out I : Iterable<T>, T> {

    public fun decode(encodedMessage: Code, codes: Map<T, Code>): I
}
