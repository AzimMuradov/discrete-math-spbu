package flows.algo

import flows.structures.Network

/**
 * The Ford-Fulkerson Algorithm.
 */
public fun <V> Network<V>.calculateFordFulkersonMaxflow(): UInt = calculateFordFulkersonMethodMaxflow {
    findPosPath(PathFindingMode.DFS, it)
}
