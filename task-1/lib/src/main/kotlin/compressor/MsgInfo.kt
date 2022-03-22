package compressor

public data class MsgInfo<T>(
    public val countedSymbols: Map<T, Int>,
    public val messageLength: Int,
)


public fun <T> Collection<T>.toMsgInfo(): MsgInfo<T> = MsgInfo(
    countedSymbols = groupingBy { it }.eachCount(),
    messageLength = size
)
