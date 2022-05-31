package flows.algo

import flows.structures.Network

/**
 * The Edmonds-Karp Algorithm.
 */
public fun <V> Network<V>.calculateEdmondsKarpMaxflow(): UInt = calculateFordFulkersonMethodMaxflow {
    findPosPath(PathFindingMode.BFS, it)
}
