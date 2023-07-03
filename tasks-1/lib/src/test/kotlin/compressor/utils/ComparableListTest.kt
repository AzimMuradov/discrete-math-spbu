package compressor.utils

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class ComparableListTest {

    @Test
    fun `compare two comparable lists`() {
        val a = ComparableList(listOf('a', 'a'))
        val aLong = ComparableList(listOf('a', 'a', 'a'))
        val b = ComparableList(listOf('b', 'b'))
        val bLong = ComparableList(listOf('b', 'b', 'b'))

        assertTrue(a <= a)
        assertTrue(aLong <= aLong)
        assertTrue(b <= b)
        assertTrue(bLong <= bLong)

        assertTrue(a <= aLong)
        assertTrue(aLong >= a)

        assertTrue(a <= b)
        assertTrue(b >= a)

        assertTrue(a <= bLong)
        assertTrue(bLong >= a)

        assertTrue(aLong <= b)
        assertTrue(b >= aLong)

        assertTrue(aLong <= bLong)
        assertTrue(bLong >= aLong)

        assertTrue(b <= bLong)
        assertTrue(bLong >= b)
    }
}
