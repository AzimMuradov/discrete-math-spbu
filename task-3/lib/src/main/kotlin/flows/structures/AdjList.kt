package flows.structures

public class AdjList<V, out E : Edge<V, E, EE>, out EE : EdgeEnd<V, E, EE>> private constructor(
    private val data: Map<V, List<EE>>,
) : Map<V, List<EE>> by data {

    public companion object {

        public fun <V, E : Edge<V, E, EE>, EE : EdgeEnd<V, E, EE>> of(
            list: Map<V, List<EE>>,
        ): AdjList<V, E, EE> = AdjList(list)
    }
}
