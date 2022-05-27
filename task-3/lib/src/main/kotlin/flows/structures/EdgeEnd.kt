package flows.structures

public interface EdgeEnd<out V, out E : Edge<V, E, EE>, out EE : EdgeEnd<V, E, EE>> {

    public val to: V


    public fun edge(from: @UnsafeVariance V): E
}
