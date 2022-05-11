package trees.structures

public data class PosEdgeEnd<out V>(
    override val to: V,
    val weight: UInt,
) : EdgeEnd<V, PosEdge<V>, PosEdgeEnd<V>> {

    override fun edge(from: @UnsafeVariance V): PosEdge<V> = PosEdge(from, to, weight)
}
