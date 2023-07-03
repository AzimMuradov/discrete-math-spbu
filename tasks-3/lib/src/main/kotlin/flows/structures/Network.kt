package flows.structures

public class Network<V> private constructor(
    public val graph: PosDirectedGraph<V>,
    public val source: V,
    public val sink: V,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Network<*>) return false

        if (graph != other.graph) return false
        if (source != other.source) return false
        if (sink != other.sink) return false

        return true
    }

    override fun hashCode(): Int {
        var result = graph.hashCode()
        result = 31 * result + (source?.hashCode() ?: 0)
        result = 31 * result + (sink?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String = "Network(graph=$graph, source=$source, sink=$sink)"


    public companion object {

        public fun <V> from(graph: PosDirectedGraph<V>, source: V, sink: V): Network<V> {
            require(graph.edges.all { it.value != 0u })
            require(source in graph.vertices && sink in graph.vertices)
            return Network(graph, source, sink)
        }
    }
}


public fun <V> network(graph: PosDirectedGraph<V>, source: V, sink: V): Network<V> =
    Network.from(graph, source, sink)
