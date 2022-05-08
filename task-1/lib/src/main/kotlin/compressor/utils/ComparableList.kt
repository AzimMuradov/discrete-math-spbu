package compressor.utils

/**
 * Comparable list of the comparable elements.
 *
 * It's often used to create symbols that consist of a list of other symbols.
 */
public class ComparableList<T : Comparable<T>>(
    private val list: List<T>,
) : List<T> by list, Comparable<List<T>> {

    override fun compareTo(other: List<T>): Int = (this zip other)
        .map { (a, b) -> a.compareTo(b) }
        .find { it != 0 } ?: size.compareTo(other.size)


    override fun equals(other: Any?): Boolean = list == other

    override fun hashCode(): Int = list.hashCode()

    override fun toString(): String = list.toString()
}
