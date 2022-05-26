package trees.algo

import trees.structures.*
import java.util.*

/**
 * Prim's Algorithm.
 */
public fun <V> PosGraph<V>.primMstForest(): PosGraph<V> {
    val processedVertices = mutableSetOf<V>()

    val minEdgePQ = PriorityQueue<PosEdge<V>>(vertices.size, Comparator.comparing { it.weight })

    val mstForestEdges = mutableSetOf<PosEdge<V>>()


    fun markAsProcessed(v: V) {
        processedVertices += v
        minEdgePQ += adjList.getValue(v).map { it.edge(from = v) }.filter { it.to !in processedVertices }
    }

    for (v in vertices) {
        if (v in processedVertices) continue

        markAsProcessed(v)

        while (true) {
            val min = generateSequence(seed = minEdgePQ.poll()) {
                if (it.to in processedVertices) minEdgePQ.poll() else null
            }.lastOrNull()?.takeIf { it.to !in processedVertices } ?: break

            mstForestEdges += min

            markAsProcessed(min.to)
        }
    }

    return graph(vertices, mstForestEdges)
}
