package flows.algo

import flows.structures.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import flows.structures.PosEdge as edge

internal class ScalingTest {

    @ParameterizedTest
    @MethodSource("testCases")
    fun maxflow(testCase: Pair<Network<Int>, UInt>) {
        val (network: Network<Int>, maxflow: UInt) = testCase
        assertEquals(expected = maxflow, actual = network.calculateMaxflowByScaling())
    }


    private companion object {

        @JvmStatic
        fun testCases() = listOf(
            network(
                graph = directedGraph(
                    setOf(1, 2, 3, 4, 5, 6),
                    setOf(
                        edge(from = 1, to = 2, value = 3u),
                        edge(from = 1, to = 3, value = 5u),
                        edge(from = 2, to = 4, value = 5u),
                        edge(from = 3, to = 2, value = 4u),
                        edge(from = 3, to = 5, value = 2u),
                        edge(from = 4, to = 5, value = 6u),
                        edge(from = 4, to = 6, value = 5u),
                        edge(from = 5, to = 6, value = 7u),
                    )
                ),
                source = 1, sink = 6
            ) to 7u,
            network(
                graph = directedGraph(
                    setOf(1, 2, 3, 4, 5, 6),
                    setOf(
                        edge(from = 1, to = 2, value = 7u),
                        edge(from = 1, to = 3, value = 4u),
                        edge(from = 2, to = 3, value = 4u),
                        edge(from = 2, to = 4, value = 2u),
                        edge(from = 3, to = 4, value = 8u),
                        edge(from = 3, to = 5, value = 4u),
                        edge(from = 4, to = 5, value = 4u),
                        edge(from = 4, to = 6, value = 5u),
                        edge(from = 5, to = 6, value = 12u),
                    )
                ),
                source = 1, sink = 6
            ) to 10u,
            network(
                graph = directedGraph(
                    (-1..16).toSet(),
                    setOf(
                        // synthetic source
                        edge(from = -1, to = 0, value = 1000u),
                        edge(from = -1, to = 1, value = 1000u),

                        // network edges
                        edge(from = 0, to = 4, value = 8u),
                        edge(from = 0, to = 5, value = 1u),
                        edge(from = 0, to = 9, value = 6u),
                        edge(from = 1, to = 2, value = 6u),
                        edge(from = 1, to = 3, value = 6u),
                        edge(from = 2, to = 3, value = 3u),
                        edge(from = 2, to = 6, value = 4u),
                        edge(from = 3, to = 4, value = 4u),
                        edge(from = 3, to = 7, value = 4u),
                        edge(from = 4, to = 5, value = 3u),
                        edge(from = 4, to = 8, value = 5u),
                        edge(from = 4, to = 9, value = 10u),
                        edge(from = 5, to = 9, value = 6u),
                        edge(from = 6, to = 3, value = 9u),
                        edge(from = 6, to = 10, value = 5u),
                        edge(from = 7, to = 4, value = 10u),
                        edge(from = 7, to = 6, value = 8u),
                        edge(from = 7, to = 11, value = 5u),
                        edge(from = 8, to = 7, value = 8u),
                        edge(from = 8, to = 12, value = 5u),
                        edge(from = 8, to = 13, value = 12u),
                        edge(from = 9, to = 8, value = 7u),
                        edge(from = 9, to = 13, value = 7u),
                        edge(from = 10, to = 7, value = 10u),
                        edge(from = 10, to = 14, value = 6u),
                        edge(from = 11, to = 8, value = 12u),
                        edge(from = 11, to = 10, value = 8u),
                        edge(from = 11, to = 14, value = 9u),
                        edge(from = 12, to = 11, value = 8u),
                        edge(from = 12, to = 15, value = 7u),
                        edge(from = 13, to = 12, value = 7u),
                        edge(from = 13, to = 15, value = 6u),

                        // synthetic sink
                        edge(from = 14, to = 16, value = 1000u),
                        edge(from = 15, to = 16, value = 1000u),
                    )
                ),
                source = -1, sink = 16
            ) to 27u
        )
    }
}
