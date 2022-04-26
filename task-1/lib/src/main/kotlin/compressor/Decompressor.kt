package compressor

public interface Decompressor<out In : Iterable<T>, out T, in Out : Iterable<Byte>, in M> {

    public fun decompress(compressedMessage: CompressedMessage<Out, M>): In
}
