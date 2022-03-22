package compressor

public interface Encoder<in I : Iterable<T>, T> {

    public fun encode(message: I): Map<T, BinaryCode>
}
