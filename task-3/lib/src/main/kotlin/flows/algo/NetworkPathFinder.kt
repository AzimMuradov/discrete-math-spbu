package flows.algo

import flows.algo.PathFindingMode.BFS
import flows.algo.PathFindingMode.DFS
import flows.structures.Matrix
import flows.structures.Network

/**
 * Find path in the [network][this] that can be valid flow in the [residual network][residualNetworkCapacities] using given [mode].
 */
internal fun <V> Network<V>.findPosPath(
    mode: PathFindingMode,
    residualNetworkCapacities: Matrix<V, UInt>,
) = when (mode) {
    DFS -> dfsOrBfs(residualNetworkCapacities, popOp = ArrayDeque<V>::removeLast)
    BFS -> dfsOrBfs(residualNetworkCapacities, popOp = ArrayDeque<V>::removeFirst)
}

/**
 * Path finding mode. Either [Depth-First Search][DFS] or [Breadth-First Search][BFS].
 */
internal enum class PathFindingMode { DFS, BFS }


private inline fun <V> Network<V>.dfsOrBfs(
    residualNetworkCapacities: Matrix<V, UInt>,
    popOp: (ArrayDeque<V>) -> V,
): Sequence<Pair<V, V>> {
    val parents: MutableMap<V, V?> = graph.vertices.associateWithTo(mutableMapOf()) { null }

    val isVisited = graph.vertices.associateWithTo(mutableMapOf()) { false }
    val deque = ArrayDeque<V>()

    isVisited[source] = true
    deque.add(source)

    while (deque.isNotEmpty()) {
        val v = popOp(deque)
        for ((u, cap) in residualNetworkCapacities[v]) {
            if (!isVisited.getValue(u) && cap != 0u) {
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
