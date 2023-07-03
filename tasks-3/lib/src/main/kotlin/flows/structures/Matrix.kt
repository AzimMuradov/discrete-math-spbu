package flows.structures

import kotlin.math.min

public class Matrix<K, V> private constructor(
    private val data: Map<K, MutableMap<K, V>>,
    private val noValue: V,
) {

    public operator fun get(a: K, b: K): V = data.getValue(a).getValue(b)

    public operator fun get(a: K): Map<K, V> = data.getValue(a)

    public operator fun set(a: K, b: K, newValue: V): V = data.getValue(a).getValue(b).also {
        data.getValue(a)[b] = newValue
    }

    public fun remove(a: K, b: K): V = set(a, b, noValue)

    public operator fun iterator(): Iterator<Triple<K, K, V>> = iterator {
        for ((a, list) in data) {
            for ((b, value) in list) {
                yield(Triple(a, b, value))
            }
        }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Matrix<*, *>) return false

        if (data != other.data) return false
        if (noValue != other.noValue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + (noValue?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        val minValueW = Iterable(::iterator).maxOf { it.third.toString().length }
        val minKeyW = data.keys.maxOf { it.toString().length }

        val boxW = min(minKeyW, minValueW)

        val matrix = buildString {
            appendLine(
                (listOf("") + data.keys.map { it.toString() }).joinToString(separator = " ") {
                    it.padEnd(boxW, padChar = ' ')
                }
            )
            for (key in data.keys) {
                appendLine(
                    (listOf(key.toString()) + data.getValue(key).map { it.value.toString() })
                        .joinToString(separator = " ") {
                            it.padEnd(boxW, padChar = ' ')
                        }
                )
            }
        }

        return matrix
    }


    public companion object {

        public fun <K, V> empty(keys: Set<K>): Matrix<K, V?> = Matrix(
            data = keys.associateWith { keys.associateWithTo(mutableMapOf()) { null } },
            noValue = null
        )

        public fun <K, V> empty(keys: Set<K>, noValue: V): Matrix<K, V> = Matrix(
            data = keys.associateWith { keys.associateWithTo(mutableMapOf()) { noValue } },
            noValue = noValue
        )

        public fun <K, V> of(matrix: Map<K, Map<K, V>>, noValue: V): Matrix<K, V> {
            require(matrix.values.all { it.keys == matrix.keys })
            return Matrix(matrix.mapValues { it.value.toMutableMap() }, noValue)
        }
    }
}
