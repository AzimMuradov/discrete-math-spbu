package trees.structures

public data class PosEdge<out V>(
    override val from: V, override val to: V,
    val weight: UInt,
) : Edge<V, PosEdge<V>, PosEdgeEnd<V>> {

    override fun swap(): PosEdge<V> = copy(from = to, to = from)

    override fun edgeEnd(): PosEdgeEnd<V> = PosEdgeEnd(to, weight)
}
