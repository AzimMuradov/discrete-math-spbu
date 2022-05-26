package app

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.types.choice
import flows.algo.*
import flows.structures.*

class CliApp : CliktCommand() {

    private val mode: Mode by argument(name = "mode").choice(
        choices = mapOf(
            "user" to Mode.USER,
            "auto" to Mode.AUTO,
        )
    ).default(value = Mode.USER)


    override fun run() {
//        echo("===============================PROBLEMS 19 and 20===============================")
//
//        echo()
//
//        val (graph, fromTo) = when (mode) {
//            Mode.USER -> {
//                val graph = GraphIO.readPosGraphOrNull { it.singleOrNull() } ?: error("")
//                val (from, to) = GraphIO.readLineOfWords()?.takeIf { it.size == 2 }?.map { it.first() } ?: error("")
//
//                graph to (from to to)
//            }
//            Mode.AUTO -> Pair(
//                graph(
//                    setOf('a', 'b', 'c', 'd', 'e', 'z'),
//                    setOf(
//                        PosEdge(from = 'a', to = 'b', weight = 2u),
//                        PosEdge(from = 'a', to = 'd', weight = 1u),
//                        PosEdge(from = 'b', to = 'c', weight = 3u),
//                        PosEdge(from = 'c', to = 'd', weight = 5u),
//                        PosEdge(from = 'c', to = 'e', weight = 1u),
//                        PosEdge(from = 'c', to = 'z', weight = 8u),
//                        PosEdge(from = 'd', to = 'e', weight = 10u),
//                        PosEdge(from = 'e', to = 'z', weight = 2u),
//                    )
//                ),
//                'a' to 'z'
//            )
//        }
//
//        val (from, to) = fromTo
//
//
//        runWithSeparators(
//            separator = ::echo2,
//            blocks = arrayOf(
//                { echo(mstFormatted(tree = graph.primMstForest(), algo = "Prim")) },
//                { echo(mstFormatted(tree = graph.kruskalMstForest(), algo = "Kruskal")) },
//                { echo(shortestPathFormatted(path = graph.shortestPath(from, to))) },
//            )
//        )
    }


//    private fun mstFormatted(tree: PosGraph<Char>?, algo: String) = buildString {
//        append("MST ($algo):")
//        append(
//            tree?.let {
//                val tMsg = it.edges
//                    .filter { (from, to) -> from <= to }
//                    .joinToString(separator = "\n") { (from, to, w) -> "($from)--$w--($to)" }
//                val tWeightMsg = "Weight: ${it.edges.sumOf(PosEdge<Char>::weight) / 2u}."
//                "\n\n$tMsg\n\n$tWeightMsg"
//            } ?: " no MST."
//        )
//    }
//
//    private fun shortestPathFormatted(path: List<PosEdge<Char>>?) = buildString {
//        append("Shortest path (Dijkstra):")
//        append(
//            path?.let {
//                val pMsg = "(${it.joinToString(separator = "") { (fr, _, w) -> "$fr)--$w--(" }}${it.last().to})"
//                val pLenMsg = "Length: ${it.sumOf(PosEdge<Char>::weight)}."
//                "\n\n$pMsg\n\n$pLenMsg"
//            } ?: " no path."
//        )
//    }
//
//
//    private fun runWithSeparators(separator: () -> Unit, vararg blocks: () -> Unit) {
//        if (blocks.isNotEmpty()) {
//            blocks.first()()
//            for (block in blocks.drop(n = 1)) {
//                separator()
//                block()
//            }
//        }
//    }
//
//    private fun echo2() = repeat(times = 2) { echo() }
}
