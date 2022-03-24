package compressor

@JvmInline
public value class Code(public val bits: List<Bit>) {

    override fun toString(): String = bits.joinToString(separator = "")

    public companion object {

        public val EMPTY: Code = Code(emptyList())
    }
}

public fun Code.toByteList(): List<Byte> = bits.chunked(size = 8).map {
    it.joinToString(separator = "").padEnd(length = 8, padChar = '0').toUByte(radix = 2).toByte()
}


public enum class Bit(public val int: Int) {
    ZERO(int = 0),
    ONE(int = 1);

    override fun toString(): String = "$int"
}

public fun Bit.toBoolean(): Boolean = int % 2 != 0

public fun Int.toBit(): Bit = when (this) {
    0 -> Bit.ZERO
    1 -> Bit.ONE
    else -> throw IllegalArgumentException()
}


public operator fun Code.plus(bit: Bit): Code =
    Code(bits = bits + bit)

public operator fun Bit.plus(code: Code): Code =
    Code(bits = listOf(this) + code.bits)

public operator fun Code.plus(code: Code): Code =
    Code(bits = bits + code.bits)
