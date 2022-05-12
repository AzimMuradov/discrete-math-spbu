package compressor.utils

/**
 * Message info.
 *
 * It's commonly used for the encoding/compressing algorithms as it gives the necessary information about the message.
 *
 * @property [countedSymbols] All message symbols with their count.
 * @property [messageLength] The length of the message.
 */
public data class MessageInfo<T>(
    public val countedSymbols: Map<T, Int>,
    public val messageLength: Int,
)


/**
 * Calculates the [MessageInfo] from the given [message][this].
 */
public fun <T> List<T>.calculateMessageInfo(): MessageInfo<T> = MessageInfo(
    countedSymbols = groupingBy { it }.eachCount(),
    messageLength = size
)
