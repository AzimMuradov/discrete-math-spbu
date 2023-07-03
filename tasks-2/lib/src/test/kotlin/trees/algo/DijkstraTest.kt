package trees.algo

import org.junit.jupiter.api.Test
import trees.structures.graph
import kotlin.test.assertEquals
import trees.structures.PosEdge as edge

internal class DijkstraTest {

    @Test
    fun test() {
        assertEquals(
            expected = listOf(
                edge(from = 'a', to = 'b', weight = 2u),
                edge(from = 'b', to = 'c', weight = 3u),
                edge(from = 'c', to = 'e', weight = 1u),
                edge(from = 'e', to = 'z', weight = 2u),
            ),
            actual = graph(
                setOf('a', 'b', 'c', 'd', 'e', 'z'),
                setOf(
                    edge(from = 'a', to = 'b', weight = 2u),
                    edge(from = 'a', to = 'd', weight = 1u),
                    edge(from = 'b', to = 'c', weight = 3u),
                    edge(from = 'c', to = 'd', weight = 5u),
                    edge(from = 'c', to = 'e', weight = 1u),
                    edge(from = 'c', to = 'z', weight = 8u),
                    edge(from = 'd', to = 'e', weight = 10u),
                    edge(from = 'e', to = 'z', weight = 2u),
                )
            ).shortestPath('a', 'z')
        )
    }

    @Test
    fun `test failed`() {
        assertEquals(
            expected = null,
            actual = graph(
                setOf('a', 'b', 'c', 'd', 'e', 'z'),
                setOf(
                    edge(from = 'a', to = 'b', weight = 2u),
                    edge(from = 'a', to = 'd', weight = 1u),
                    edge(from = 'b', to = 'c', weight = 3u),
                    edge(from = 'c', to = 'd', weight = 5u),
                    edge(from = 'c', to = 'e', weight = 1u),
                    edge(from = 'd', to = 'e', weight = 10u),
                )
            ).shortestPath('a', 'z')
        )
    }
}
