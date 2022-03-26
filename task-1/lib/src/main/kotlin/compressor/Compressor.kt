package compressor

public interface Compressor<in In : Iterable<T>, T, out Out : Iterable<Byte>, M> {

    public fun compress(message: In): CompressedMessage<Out, M>
}


public data class CompressedMessage<out I : Iterable<Byte>, M>(
    val compressed: I,
    val metadata: M,
)
