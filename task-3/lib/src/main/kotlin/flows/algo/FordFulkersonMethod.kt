package flows.algo

import flows.structures.Matrix
import flows.structures.Network

/**
 * The Ford-Fulkerson Method.
 */
internal inline fun <V> Network<V>.calculateFordFulkersonMethodMaxflow(
    findPositivePath: (Matrix<V, UInt>) -> Sequence<Pair<V, V>>,
): UInt {
    var maxflow = 0u

    val residualNetworkCapacities = Matrix.empty(keys = graph.vertices, noValue = 0u).apply {
        for ((v, u, cap) in graph.edges) {
            set(v, u, cap)
        }
    }

    while (true) {
        val path = findPositivePath(residualNetworkCapacities)
        val augFlow = path.minOfOrNull { (v, p) -> residualNetworkCapacities[p, v] } ?: break

        maxflow += augFlow

        for ((v, p) in path) {
            residualNetworkCapacities[p, v] -= augFlow
            residualNetworkCapacities[v, p] += augFlow
        }
    }

    return maxflow
}
