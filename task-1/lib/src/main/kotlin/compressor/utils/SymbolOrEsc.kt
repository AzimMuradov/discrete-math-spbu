package compressor.utils

/**
 * Symbol or ESC.
 *
 * It gives ability to create expanded alphabets with synthetic 'ESC' symbol.
 * The main property of the newly created alphabet is that ESC < Symbol<T> for any given Symbol<T>.
 */
public sealed class SymbolOrEsc<T : Comparable<T>> : Comparable<SymbolOrEsc<T>> {

    public data class Symbol<T : Comparable<T>>(public val value: T) : SymbolOrEsc<T>()

    public class Esc<T : Comparable<T>> : SymbolOrEsc<T>() {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Esc<*>) return false
            return true
        }

        override fun hashCode(): Int = javaClass.hashCode()

        override fun toString(): String = "ESC"
    }


    final override fun compareTo(other: SymbolOrEsc<T>): Int = when (this) {
        is Symbol -> when (other) {
            is Symbol -> value.compareTo(other.value)
            is Esc -> 1
        }
        is Esc -> when (other) {
            is Symbol -> -1
            is Esc -> 0
        }
    }
}
