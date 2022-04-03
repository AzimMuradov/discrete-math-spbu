package compressor

public data class CompressedMessage<out I : Iterable<Byte>, M>(
    val compressed: I,
    val metadata: M,
)
