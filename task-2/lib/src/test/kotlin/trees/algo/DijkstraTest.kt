package trees.algo

import org.junit.jupiter.api.Test
import trees.structures.graph
import kotlin.test.assertEquals
import trees.structures.PosEdge as edge

internal class DijkstraTest {

    @Test
    fun test() {
        assertEquals(
            expected = graph(
                setOf('a', 'b', 'c', 'd', 'e', 'z'),
                setOf(
                    edge('a', 'b', 2u),
                    edge('a', 'd', 1u),
                    edge('b', 'c', 3u),
                    edge('c', 'd', 5u),
                    edge('c', 'e', 1u),
                    edge('c', 'z', 8u),
                    edge('d', 'e', 10u),
                    edge('e', 'z', 2u),
                )
            ).shortestPath('a', 'z'),
            actual = listOf(
                edge('a', 'b', 2u),
                edge('b', 'c', 3u),
                edge('c', 'e', 1u),
                edge('e', 'z', 2u),
            )
        )
    }

    @Test
    fun `test failed`() {
        assertEquals(
            expected = graph(
                setOf('a', 'b', 'c', 'd', 'e', 'z'),
                setOf(
                    edge('a', 'b', 2u),
                    edge('a', 'd', 1u),
                    edge('b', 'c', 3u),
                    edge('c', 'd', 5u),
                    edge('c', 'e', 1u),
                    edge('d', 'e', 10u),
                )
            ).shortestPath('a', 'z'),
            actual = null
        )
    }
}
