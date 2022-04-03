package compressor

public interface Decompressor<out In : Iterable<T>, T, in Out : Iterable<Byte>, M> {

    public fun decompress(compressedMessage: CompressedMessage<Out, M>): In
}
