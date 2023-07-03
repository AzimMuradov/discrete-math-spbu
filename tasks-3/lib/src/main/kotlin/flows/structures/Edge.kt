package flows.structures

public interface Edge<out V, out E : Edge<V, E, EE>, out EE : EdgeEnd<V, E, EE>> {

    public val from: V

    public val to: V


    public operator fun component1(): V

    public operator fun component2(): V


    public fun swap(): E

    public fun edgeEnd(): EE
}
