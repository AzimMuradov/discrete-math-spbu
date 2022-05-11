package trees.algo

import trees.structures.*
import java.util.*

/**
 * Prim's Algorithm.
 */
public fun <V> PosGraph<V>.primMstForest(): PosGraph<V> {
    val processedVertices = mutableSetOf<V>()

    val minPQ = PriorityQueue<PosEdge<V>>(vertices.size) { e1, e2 ->
        when {
            e1.weight < e2.weight -> -1
            e1.weight > e2.weight -> 1
            else -> 0
        }
    }

    val mstForestEdges = mutableSetOf<PosEdge<V>>()

    for (v in vertices) {
        if (v in processedVertices) continue

        processedVertices += v
        minPQ += adjList.getValue(v).map { it.edge(from = v) }

        while (true) {
            val min = generateSequence(seed = minPQ.poll()) {
                if (it.from in processedVertices && it.to in processedVertices) {
                    minPQ.poll()
                } else {
                    null
                }
            }.lastOrNull()?.takeIf { it.from !in processedVertices || it.to !in processedVertices } ?: break

            mstForestEdges += min

            if (min.to !in processedVertices) {
                processedVertices += min.to
                minPQ += adjList.getValue(min.to).map { it.edge(from = min.to) }
            }
            if (min.from !in processedVertices) {
                processedVertices += min.from
                minPQ += adjList.getValue(min.from).map { it.edge(from = min.from) }
            }
        }
    }

    return graph(vertices, mstForestEdges)
}
