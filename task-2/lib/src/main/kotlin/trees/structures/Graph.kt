package trees.structures

public class Graph<V, out E : Edge<V, E, EE>, out EE : EdgeEnd<V, E, EE>> private constructor(
    public val vertices: Set<V>,
    public val edges: Set<E>,
    public val adjList: Map<V, List<EE>>,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Graph<*, *, *>) return false

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
        return "Graph(vertices=$vertices, edges=$edges, adjList=$adjList)"
    }


    public companion object {

        public fun <V, E : Edge<V, E, EE>, EE : EdgeEnd<V, E, EE>> from(
            vertices: Set<V>,
            edges: Set<E>,
        ): Graph<V, E, EE> {
            require(edges.all { (v, u) -> v in vertices && u in vertices }) { "ERROR" }
            val allEdges = edges + edges.map { it.rotate() }
            val adjList = allEdges.groupBy(
                keySelector = { it.from },
                valueTransform = { it.edgeEnd() }
            )
            return Graph(vertices, allEdges, adjList)
        }

        public fun <V, E : Edge<V, E, EE>, EE : EdgeEnd<V, E, EE>> from(
            vertices: Set<V>,
            adjList: Map<V, List<EE>>,
        ): Graph<V, E, EE> = from(
            vertices,
            edges = adjList.flatMapTo(mutableSetOf()) { (from, ends) -> ends.map { end -> end.edge(from) } }
        )
    }
}


public fun <V, E : Edge<V, E, EE>, EE : EdgeEnd<V, E, EE>> graph(
    vertices: Set<V>,
    edges: Set<E>,
): Graph<V, E, EE> = Graph.from(vertices, edges)

public fun <V, E : Edge<V, E, EE>, EE : EdgeEnd<V, E, EE>> graph(
    vertices: Set<V>,
    adjList: Map<V, List<EE>>,
): Graph<V, E, EE> = Graph.from(vertices, adjList)
