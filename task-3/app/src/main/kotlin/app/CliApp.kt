package app

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.types.choice
import flows.algo.calculateEdmondsKarpMaxflow
import flows.algo.calculateFordFulkersonMaxflow
import flows.structures.*
import flows.utils.GraphIO

class CliApp : CliktCommand() {

    private val mode: Mode by argument(name = "mode").choice(
        choices = mapOf(
            "user" to Mode.USER,
            "auto" to Mode.AUTO,
        )
    ).default(value = Mode.USER)


    override fun run() {
        echo("===================================PROBLEM 11===================================")

        echo()

        val networks = listOf(
            network(
                graph = directedGraph(
                    setOf(1, 2, 3, 4, 5, 6),
                    setOf(
                        PosEdge(from = 1, to = 2, value = 3u),
                        PosEdge(from = 1, to = 3, value = 5u),
                        PosEdge(from = 2, to = 4, value = 5u),
                        PosEdge(from = 3, to = 2, value = 4u),
                        PosEdge(from = 3, to = 5, value = 2u),
                        PosEdge(from = 4, to = 5, value = 6u),
                        PosEdge(from = 4, to = 6, value = 5u),
                        PosEdge(from = 5, to = 6, value = 7u),
                    )
                ),
                source = 1, sink = 6
            ),
            network(
                graph = directedGraph(
                    setOf(1, 2, 3, 4, 5, 6),
                    setOf(
                        PosEdge(from = 1, to = 2, value = 7u),
                        PosEdge(from = 1, to = 3, value = 4u),
                        PosEdge(from = 2, to = 3, value = 4u),
                        PosEdge(from = 2, to = 4, value = 2u),
                        PosEdge(from = 3, to = 4, value = 8u),
                        PosEdge(from = 3, to = 5, value = 4u),
                        PosEdge(from = 4, to = 5, value = 4u),
                        PosEdge(from = 4, to = 6, value = 5u),
                        PosEdge(from = 5, to = 6, value = 12u),
                    )
                ),
                source = 1, sink = 6
            ),
            network(
                graph = directedGraph(
                    (-1..16).toSet(),
                    setOf(
                        // synthetic source
                        PosEdge(from = -1, to = 0, value = 1000u),
                        PosEdge(from = -1, to = 1, value = 1000u),

                        // network edges
                        PosEdge(from = 0, to = 4, value = 8u),
                        PosEdge(from = 0, to = 5, value = 1u),
                        PosEdge(from = 0, to = 9, value = 6u),
                        PosEdge(from = 1, to = 2, value = 6u),
                        PosEdge(from = 1, to = 3, value = 6u),
                        PosEdge(from = 2, to = 3, value = 3u),
                        PosEdge(from = 2, to = 6, value = 4u),
                        PosEdge(from = 3, to = 4, value = 4u),
                        PosEdge(from = 3, to = 7, value = 4u),
                        PosEdge(from = 4, to = 5, value = 3u),
                        PosEdge(from = 4, to = 8, value = 5u),
                        PosEdge(from = 4, to = 9, value = 10u),
                        PosEdge(from = 5, to = 9, value = 6u),
                        PosEdge(from = 6, to = 3, value = 9u),
                        PosEdge(from = 6, to = 10, value = 5u),
                        PosEdge(from = 7, to = 4, value = 10u),
                        PosEdge(from = 7, to = 6, value = 8u),
                        PosEdge(from = 7, to = 11, value = 5u),
                        PosEdge(from = 8, to = 7, value = 8u),
                        PosEdge(from = 8, to = 12, value = 5u),
                        PosEdge(from = 8, to = 13, value = 12u),
                        PosEdge(from = 9, to = 8, value = 7u),
                        PosEdge(from = 9, to = 13, value = 7u),
                        PosEdge(from = 10, to = 7, value = 10u),
                        PosEdge(from = 10, to = 14, value = 6u),
                        PosEdge(from = 11, to = 8, value = 12u),
                        PosEdge(from = 11, to = 10, value = 8u),
                        PosEdge(from = 11, to = 14, value = 9u),
                        PosEdge(from = 12, to = 11, value = 8u),
                        PosEdge(from = 12, to = 15, value = 7u),
                        PosEdge(from = 13, to = 12, value = 7u),
                        PosEdge(from = 13, to = 15, value = 6u),

                        // synthetic sink
                        PosEdge(from = 14, to = 16, value = 1000u),
                        PosEdge(from = 15, to = 16, value = 1000u),
                    )
                ),
                source = -1, sink = 16
            ),
        )

        runWithSeparators(
            separator = ::echo2,
            blocks = networks.map { network -> { runAlgorithms(network, algorithms) } }.toTypedArray()
        )


        echo4()


        echo("===================================PROBLEM 12===================================")

        echo()

        when (mode) {
            Mode.USER -> {
                val graph = GraphIO.readPosDirectedGraphOrNull(parseT = String::toIntOrNull) ?: error("")
                val (source, sink) = GraphIO.readLineOfWords()?.takeIf { it.size == 2 }?.map(String::toInt) ?: error("")

                runAlgorithms(network = network(graph, source, sink), algorithms)
            }
            Mode.AUTO -> echo("Auto mode has been chosen, skipping...")
        }
    }


    private fun <V> runAlgorithms(
        network: Network<V>,
        algorithms: List<Pair<String, (Network<V>) -> UInt>>,
    ) {
        echo(network)
        echo()
        runWithSeparators(
            separator = {},
            blocks = algorithms.map { (name, algo) ->
                { echo("Maxflow ($name) = ${algo(network)}.") }
            }.toTypedArray()
        )
    }

    private val algorithms = listOf(
        "Ford-Fulkerson" to Network<Int>::calculateFordFulkersonMaxflow,
        "Edmonds-Karp" to Network<Int>::calculateEdmondsKarpMaxflow,
    )


    private fun runWithSeparators(separator: () -> Unit, vararg blocks: () -> Unit) {
        if (blocks.isNotEmpty()) {
            blocks.first()()
            for (block in blocks.drop(n = 1)) {
                separator()
                block()
            }
        }
    }

    private fun echo2() = repeat(times = 2) { echo() }

    private fun echo4() = repeat(times = 4) { echo() }
}
