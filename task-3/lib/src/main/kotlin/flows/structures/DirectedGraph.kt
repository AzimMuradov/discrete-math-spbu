package flows.structures

public class DirectedGraph<V, out E : Edge<V, E, EE>, out EE : EdgeEnd<V, E, EE>> private constructor(
    public val vertices: Set<V>,
    public val edges: Set<E>,
    public val adjList: AdjList<V, E, EE>,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DirectedGraph<*, *, *>) return false

        if (vertices != other.vertices) return false
        if (edges != other.edges) return false

        return true
    }

    override fun hashCode(): Int {
        var result = vertices.hashCode()
        result = 31 * result + edges.hashCode()
        return result
    }

    override fun toString(): String {
        return "DirectedGraph(vertices=$vertices, edges=$edges, adjList=$adjList)"
    }


    public companion object {

        public fun <V, E : Edge<V, E, EE>, EE : EdgeEnd<V, E, EE>> from(
            vertices: Set<V>,
            edges: Set<E>,
        ): DirectedGraph<V, E, EE> {
            require(edges.all { (v, u) -> v in vertices && u in vertices })
            val adjList = AdjList.of(edges.groupBy(keySelector = { it.from }, valueTransform = { it.edgeEnd() }))
            return DirectedGraph(vertices, edges, adjList)
        }
    }
}


public fun <V, E : Edge<V, E, EE>, EE : EdgeEnd<V, E, EE>> directedGraph(
    vertices: Set<V>,
    edges: Set<E>,
): DirectedGraph<V, E, EE> = DirectedGraph.from(vertices, edges)
