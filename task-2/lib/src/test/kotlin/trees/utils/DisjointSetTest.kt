package trees.utils

import org.junit.jupiter.api.Test
import kotlin.test.*

internal class DisjointSetTest {

    @Test
    fun find() {
        assertEquals(
            expected = 'a',
            actual = DisjointSet(setOf('a', 'b')).find('a'),
        )
    }

    @Test
    fun `fail to find`() {
        assertFails { DisjointSet(setOf('a', 'b')).find('c') }
    }

    @Test
    fun union() {
        val ds = DisjointSet(setOf('a', 'b'))
        ds.union('a', 'b')
        assertTrue { (ds.find('a') == ds.find('b')) }
    }

    @Test
    fun `fail to union`() {
        assertFails { DisjointSet(setOf('a', 'b')).union('a', 'c') }
    }

    @Test
    fun `doubled union`() {
        val ds = DisjointSet(setOf('a', 'b'))
        ds.union('a', 'b')
        ds.union('a', 'b')
        assertTrue { (ds.find('a') == ds.find('b')) }
    }

    @Test
    fun `complex union`() {
        val ds = DisjointSet(setOf(1, 2, 3, 4, 5, 6, 7, 8))
        ds.union(1, 2)
        ds.union(3, 4)
        ds.union(1, 8)
        ds.union(7, 1)
        assertTrue { (ds.find(1) == ds.find(8)) }
    }

    @Test
    fun `is connected`() {
        assertFalse(DisjointSet(setOf('a', 'b')).isConnected('a', 'b'))
    }
}
