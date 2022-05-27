package flows.algo

import flows.structures.Matrix
import flows.structures.Network

/**
 * The Dinitz's Algorithm.
 */
public fun <V> Network<V>.calculateDinitzMaxflow(): UInt {
    var maxflow = 0u

    val verticesList = graph.vertices.toList()
    val verticesIndexed = run {
        var j = 0
        graph.vertices.associateWith { j++ }
    }

    val residualNetworkCapacities = Matrix.empty(keys = graph.vertices, noValue = 0u).apply {
        for ((v, u, cap) in graph.edges) {
            set(v, u, cap)
        }
    }

    val depths = graph.vertices.associateWithTo(mutableMapOf()) { UInt.MAX_VALUE }


    while (bfs(residualNetworkCapacities, depths)) {
        val pointers: MutableMap<V, V?> = graph.vertices.associateWithTo(mutableMapOf()) { graph.vertices.first() }

        maxflow += generateSequence(
            seed = dfs(
                residualNetworkCapacities,
                depths,
                verticesList,
                verticesIndexed,
                pointers,
                source,
                UInt.MAX_VALUE
            )
        ) {
            dfs(
                residualNetworkCapacities,
                depths,
                verticesList,
                verticesIndexed,
                pointers,
                source,
                UInt.MAX_VALUE
            ).takeUnless { it == 0u }
        }.sum()
    }

    return maxflow
}


private fun <V> Network<V>.bfs(
    residualNetworkCapacities: Matrix<V, UInt>,
    depths: MutableMap<V, UInt>,
): Boolean {
    depths.replaceAll { _, _ -> UInt.MAX_VALUE }
    depths[source] = 0u

    val deque = ArrayDeque<V>().apply {
        add(source)
    }

    while (deque.isNotEmpty()) {
        val v = deque.removeFirst()
        for ((u, cap) in residualNetworkCapacities[v]) {
            if (depths[u] == UInt.MAX_VALUE && cap != 0u) {
                depths[u] = depths.getValue(v) + 1u
                deque.add(u)
            }
        }
    }

    return depths.getValue(sink) != UInt.MAX_VALUE
}

private fun <V> Network<V>.dfs(
    residualNetworkCapacities: Matrix<V, UInt>,
    depth: MutableMap<V, UInt>,
    verticesList: List<V>,
    verticesIndexed: Map<V, Int>,
    pointers: MutableMap<V, V?>,
    u: V,
    minC: UInt,
): UInt {
    if (u == sink || minC == 0u) return minC

    if (pointers[u] != null) {
        for (v in verticesList.subList(verticesIndexed.getValue(pointers[u]!!), verticesList.size)) {
            if (depth[v] == depth[u]!! + 1u) {
                val delta = dfs(
                    residualNetworkCapacities,
                    depth,
                    verticesList,
                    verticesIndexed,
                    pointers,
                    v,
                    minOf(minC, residualNetworkCapacities[u, v])
                )
                if (delta != 0u) {
                    residualNetworkCapacities[u, v] -= delta
                    residualNetworkCapacities[v, u] += delta // насыщаем рёбра по пути dfs
                    return delta
                }
            }
            pointers[u] = verticesList.getOrNull(verticesIndexed[pointers[u]!!]!! + 1)
        }
    }
    return 0u
}
