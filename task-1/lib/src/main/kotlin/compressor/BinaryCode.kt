package compressor

@JvmInline
public value class BinaryCode(public val code: List<BinSym>) {

    override fun toString(): String = code.joinToString(separator = "")

    public companion object {

        public val EMPTY: BinaryCode = BinaryCode(emptyList())
    }
}

public enum class BinSym(public val int: Int) {
    ZERO(int = 0),
    ONE(int = 1);

    override fun toString(): String = "$int"
}

public fun BinSym.toBoolean(): Boolean = int % 2 != 0


public operator fun BinaryCode.plus(binSym: BinSym): BinaryCode =
    BinaryCode(code = code + binSym)

public operator fun BinSym.plus(binCode: BinaryCode): BinaryCode =
    BinaryCode(code = listOf(this) + binCode.code)

public operator fun BinaryCode.plus(binCode: BinaryCode): BinaryCode =
    BinaryCode(code = code + binCode.code)
