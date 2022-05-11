package trees.utils

public class DisjointSets<T>(elements: Set<T>) {

    private val rank: MutableMap<T, Int> = elements.associateWithTo(mutableMapOf()) { 0 }

    private val parent: MutableMap<T, T> = elements.associateWithTo(mutableMapOf()) { it }

    public fun find(x: T): T {
        if (parent[x] != x) {
            parent[x] = find(parent.getValue(x))
        }
        return parent.getValue(x)
    }

    public fun union(x: T, y: T) {
        val xRoot = find(x)
        val yRoot = find(y)

        if (xRoot == yRoot) return

        when {
            rank.getValue(xRoot) < rank.getValue(yRoot) -> parent[xRoot] = yRoot
            rank.getValue(yRoot) < rank.getValue(xRoot) -> parent[yRoot] = xRoot
            else -> {
                parent[yRoot] = xRoot
                rank[xRoot] = rank.getValue(xRoot) + 1
            }
        }
    }
}
