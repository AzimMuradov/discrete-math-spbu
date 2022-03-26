package compressor

public data class MessageInfo<T>(
    public val countedSymbols: Map<T, Int>,
    public val messageLength: Int,
)


public fun <T> Collection<T>.toMessageInfo(): MessageInfo<T> = MessageInfo(
    countedSymbols = groupingBy { it }.eachCount(),
    messageLength = size
)
