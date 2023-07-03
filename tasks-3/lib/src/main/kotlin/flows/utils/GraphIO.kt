package flows.utils

import flows.structures.*

public object GraphIO {

    public fun <V> readPosDirectedGraphOrNull(parseT: (String) -> V?): PosDirectedGraph<V>? {
        val (n, m) = readLineOfInts()?.takeIf { it.size >= 2 } ?: return null

        val vertices: Set<V> = readLineOfWords()?.mapTo(mutableSetOf()) {
            parseT(it) ?: return null
        }?.takeIf { it.size == n } ?: return null

        val edges = mutableSetOf<PosEdge<V>>()

        repeat(times = m) {
            val (v, u, weight) = readLineOfWords()
                ?.takeIf { it.size == 3 }
                ?.let { (vStr, uStr, w) ->
                    Triple(
                        first = parseT(vStr)?.takeIf { it in vertices } ?: return null,
                        second = parseT(uStr)?.takeIf { it in vertices } ?: return null,
                        third = w.toUIntOrNull() ?: return null,
                    )
                } ?: return null

            edges += PosEdge(v, u, weight)
        }

        return directedGraph(vertices, edges)
    }


    public fun readLineOfWords(): List<String>? = readlnOrNull()?.split(' ')

    private fun readLineOfInts(): List<Int>? = readLineOfWords()
        ?.map(String::toIntOrNull)
        ?.takeUnless { null in it }
        ?.filterNotNull()
}
