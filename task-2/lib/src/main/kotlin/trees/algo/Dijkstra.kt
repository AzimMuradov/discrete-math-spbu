@file:OptIn(ExperimentalUnsignedTypes::class)

package trees.algo

import trees.structures.PosGraph
import java.util.*

/**
 * Dijkstra's Algorithm.
 */
public fun <V> PosGraph<V>.shortestPathLength(from: V, to: V): ULong? {
    val vList = vertices.toList()
    val distTo = ULongArray(vertices.size) { ULong.MAX_VALUE }

    val minPQ = PriorityQueue<Pair<V, ULong>>(vertices.size) { p1, p2 ->
        when {
            p1.second < p2.second -> -1
            p1.second > p2.second -> 1
            else -> 0
        }
    }
    for (v in vertices) {
        minPQ += v to ULong.MAX_VALUE
    }

    minPQ += from to 0UL

    while (true) {
        val (v, distToV) = minPQ.poll()

        if (distToV == ULong.MAX_VALUE) break
        if (distTo[vList.indexOf(v)] != ULong.MAX_VALUE) continue
        if (v == to) return distToV

        distTo[vList.indexOf(v)] = distToV

        if (v in adjList.keys) {
            for ((u, weight) in adjList.getValue(v)) {
                minPQ += u to distToV + weight
            }
        }
    }

    return null
}
