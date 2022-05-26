@file:OptIn(ExperimentalUnsignedTypes::class)

package trees.algo

import trees.structures.PosEdge
import trees.structures.PosGraph
import java.util.*

/**
 * Dijkstra's Algorithm.
 */
public fun <V> PosGraph<V>.shortestPath(from: V, to: V): List<PosEdge<V>>? {
    val vList = vertices.toList()
    val distTo = ULongArray(vertices.size) { ULong.MAX_VALUE }

    val minPQ = PriorityQueue<Triple<V, List<PosEdge<V>>, ULong>>(
        vertices.size,
        Comparator.comparing { it.third }
    )

    for (v in vertices) {
        minPQ += Triple(v, emptyList(), ULong.MAX_VALUE)
    }
    minPQ += Triple(from, emptyList(), 0UL)

    while (true) {
        val (v, path, distToV) = minPQ.poll()

        if (distToV == ULong.MAX_VALUE) break
        if (distTo[vList.indexOf(v)] != ULong.MAX_VALUE) continue
        if (v == to) return path

        distTo[vList.indexOf(v)] = distToV

        for ((u, weight) in adjList.getValue(v)) {
            minPQ += Triple(u, path + PosEdge(v, u, weight), distToV + weight)
        }
    }

    return null
}
