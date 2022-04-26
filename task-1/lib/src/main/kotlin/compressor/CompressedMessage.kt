package compressor

public data class CompressedMessage<out I : Iterable<Byte>, out M>(
    val compressed: I,
    val metadata: M,
)
