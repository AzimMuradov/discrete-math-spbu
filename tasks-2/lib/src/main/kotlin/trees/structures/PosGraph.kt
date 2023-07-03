package trees.structures

public typealias PosGraph<V> = Graph<V, PosEdge<V>, PosEdgeEnd<V>>


public data class PosEdge<out V>(
    override val from: V, override val to: V,
    val weight: UInt,
) : Edge<V, PosEdge<V>, PosEdgeEnd<V>> {

    override fun swap(): PosEdge<V> = copy(from = to, to = from)

    override fun edgeEnd(): PosEdgeEnd<V> = PosEdgeEnd(to, weight)
}

public data class PosEdgeEnd<out V>(
    override val to: V,
    val weight: UInt,
) : EdgeEnd<V, PosEdge<V>, PosEdgeEnd<V>> {

    override fun edge(from: @UnsafeVariance V): PosEdge<V> = PosEdge(from, to, weight)
}
