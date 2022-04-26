package compressor

public interface Compressor<in In : Iterable<T>, out T, out Out : Iterable<Byte>, out M> {

    public fun compress(message: In): CompressedMessage<Out, M>
}
