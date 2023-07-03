package flows.structures

public typealias PosDirectedGraph<V> = DirectedGraph<V, PosEdge<V>, PosEdgeEnd<V>>


public data class PosEdge<out V>(
    override val from: V, override val to: V,
    val value: UInt,
) : Edge<V, PosEdge<V>, PosEdgeEnd<V>> {

    override fun swap(): PosEdge<V> = copy(from = to, to = from)

    override fun edgeEnd(): PosEdgeEnd<V> = PosEdgeEnd(to, value)
}

public data class PosEdgeEnd<out V>(
    override val to: V,
    val weight: UInt,
) : EdgeEnd<V, PosEdge<V>, PosEdgeEnd<V>> {

    override fun edge(from: @UnsafeVariance V): PosEdge<V> = PosEdge(from, to, weight)
}
