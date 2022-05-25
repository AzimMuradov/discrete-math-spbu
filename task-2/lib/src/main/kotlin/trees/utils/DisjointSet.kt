package trees.utils

/**
 * Disjoint Set
 *
 * In computer science, a disjoint-set data structure, also called a [union]–[find] data structure or merge–find set, is a data structure that stores a collection of disjoint (non-overlapping) sets.
 * Equivalently, it stores a partition of a set into disjoint subsets.
 */
internal class DisjointSet<T>(elements: Set<T>) {

    private val parents = elements.associateWithTo(mutableMapOf()) { it }

    private val ranks = elements.associateWithTo(mutableMapOf()) { 0 }


    /**
     * Find a representative member of a set with the [element].
     */
    fun find(element: T): T {
        if (parents[element] != element) {
            parents[element] = find(parents.getValue(element))
        }
        return parents.getValue(element)
    }

    /**
     * Merge two sets respectively with elements [a] and [b] (replacing them by their union).
     */
    fun union(a: T, b: T) {
        val aRoot = find(a)
        val bRoot = find(b)

        if (aRoot == bRoot) return

        when {
            ranks.getValue(aRoot) < ranks.getValue(bRoot) -> parents[aRoot] = bRoot
            ranks.getValue(bRoot) < ranks.getValue(aRoot) -> parents[bRoot] = aRoot
            else -> {
                parents[bRoot] = aRoot
                ranks[aRoot] = ranks.getValue(aRoot) + 1
            }
        }
    }
}

/**
 * Check if elements [a] and [b] are in the same set.
 */
internal fun <T> DisjointSet<T>.isConnected(a: T, b: T): Boolean = find(a) == find(b)
