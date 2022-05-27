package flows.algo

import flows.structures.Matrix
import flows.structures.Network
import kotlin.math.*

/**
 * TODO
 */
public fun <V> Network<V>.calculateMaxflowByScaling(): UInt {
    var maxflow = 0u

    val residualNetworkCapacities = Matrix.empty(keys = graph.vertices, noValue = 0u).apply {
        for ((v, u, cap) in graph.edges) {
            set(v, u, cap)
        }
    }

    var scale = 2.0.pow(floor(log2(graph.edges.maxOf { it.value }.toDouble()))).toUInt()

    while (scale >= 1u) {
        while (true) {
            val path = bfs(residualNetworkCapacities, scale)
            val augFlow = path.minOfOrNull { (v, p) -> residualNetworkCapacities[p, v] } ?: break

            maxflow += augFlow

            for ((v, p) in path) {
                residualNetworkCapacities[p, v] -= augFlow
                residualNetworkCapacities[v, p] += augFlow
            }
        }

        scale /= 2u
    }
    return maxflow
}


private fun <V> Network<V>.bfs(
    residualNetworkCapacities: Matrix<V, UInt>,
    minCap: UInt,
): Sequence<Pair<V, V>> {
    val parents: MutableMap<V, V?> = graph.vertices.associateWithTo(mutableMapOf()) { null }

    val isVisited = graph.vertices.associateWithTo(mutableMapOf()) { false }.apply {
        set(source, true)
    }

    val deque = ArrayDeque<V>().apply {
        add(source)
    }

    while (deque.isNotEmpty()) {
        val v = deque.removeLast()
        for ((u, cap) in residualNetworkCapacities[v]) {
            if (!isVisited.getValue(u) && cap >= minCap) {
                parents[u] = v
                if (u != sink) {
                    isVisited[u] = true
                    deque.add(u)
                } else {
                    break
                }
            }
        }
    }

    return generateSequence(sink) { v -> parents.getValue(v) }.zipWithNext()
}
