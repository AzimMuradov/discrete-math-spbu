package trees.algo

import org.junit.jupiter.api.Test
import trees.structures.PosEdge
import trees.structures.graph
import kotlin.test.assertEquals

internal class KruskalTest {

    @Test
    fun test() {
        assertEquals(
            expected = graph(
                setOf('a', 'b', 'c', 'd', 'e', 'z'),
                setOf(
                    PosEdge(from = 'a', to = 'b', weight = 2u),
                    PosEdge(from = 'a', to = 'd', weight = 1u),
                    PosEdge(from = 'b', to = 'c', weight = 3u),
                    PosEdge(from = 'c', to = 'e', weight = 1u),
                    PosEdge(from = 'e', to = 'z', weight = 2u),
                )
            ),
            actual = graph(
                setOf('a', 'b', 'c', 'd', 'e', 'z'),
                setOf(
                    PosEdge(from = 'a', to = 'b', weight = 2u),
                    PosEdge(from = 'a', to = 'd', weight = 1u),
                    PosEdge(from = 'b', to = 'c', weight = 3u),
                    PosEdge(from = 'c', to = 'd', weight = 5u),
                    PosEdge(from = 'c', to = 'e', weight = 1u),
                    PosEdge(from = 'c', to = 'z', weight = 8u),
                    PosEdge(from = 'd', to = 'e', weight = 10u),
                    PosEdge(from = 'e', to = 'z', weight = 2u),
                )
            ).kruskalMstForest()
        )
    }
}
