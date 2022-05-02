package compressor.utils

public sealed class SymbolOrEsc<T : Comparable<T>> : Comparable<SymbolOrEsc<T>> {

    public data class Symbol<T : Comparable<T>>(public val value: T) : SymbolOrEsc<T>()

    public object Esc : SymbolOrEsc<Nothing>()


    final override fun compareTo(other: SymbolOrEsc<T>): Int = when (this) {
        is Symbol -> when (other) {
            is Symbol -> value.compareTo(other.value)
            Esc -> 1
        }
        Esc -> when (other) {
            is Symbol -> -1
            Esc -> 0
        }
    }
}
