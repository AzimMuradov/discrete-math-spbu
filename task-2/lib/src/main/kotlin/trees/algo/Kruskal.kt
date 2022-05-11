package trees.algo

import trees.structures.*
import trees.utils.DisjointSets

/**
 * Kruskal's Algorithm.
 */
public fun <V> PosGraph<V>.kruskalMstForest(): PosGraph<V> {
    val sortedEdges = edges.sortedWith { e1, e2 -> e1.weight.compareTo(e2.weight) }

    val mstForestEdges = mutableSetOf<PosEdge<V>>()
    var edgeCnt = 0

    val ds = DisjointSets(vertices)

    for (edge in sortedEdges) {
        val (v, u, _) = edge

        if (ds.find(v) != ds.find(u)) {
            mstForestEdges += edge
            edgeCnt += 1
            ds.union(v, u)
        }
    }

    return graph(vertices, mstForestEdges)
}
