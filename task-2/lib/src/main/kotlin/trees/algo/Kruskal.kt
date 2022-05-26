package trees.algo

import trees.structures.*
import trees.utils.DisjointSet
import trees.utils.isConnected

/**
 * Kruskal's Algorithm.
 */
public fun <V> PosGraph<V>.kruskalMstForest(): PosGraph<V> {
    val sortedEdges = edges.sortedWith { e1, e2 -> e1.weight.compareTo(e2.weight) }

    val mstForestEdges = mutableSetOf<PosEdge<V>>()

    val ds = DisjointSet(vertices)

    for (edge in sortedEdges) {
        val (v, u, _) = edge

        if (!ds.isConnected(v, u)) {
            mstForestEdges += edge
            ds.union(v, u)
        }
    }

    return graph(vertices, mstForestEdges)
}
