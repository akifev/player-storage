package org.jetbrains.rider.trees

/**
 * @author akifev
 *
 * Persistent map based on AVL tree
 */
internal class PersistentAVLTreeMap<K : Comparable<K>, V>() {
    private class MapPair<K : Comparable<K>, V>(var key: K, var value: V? = null) : Comparable<MapPair<K, V?>> {
        override fun compareTo(other: MapPair<K, V?>): Int {
            return this.key.compareTo(other.key)
        }
    }

    private var set: PersistentAVLTreeSet<MapPair<K, V?>> = PersistentAVLTreeSet()

    private constructor(set: PersistentAVLTreeSet<MapPair<K, V?>>) : this() {
        this.set = set
    }

    /**
     * Adds a new pair to the tree.
     * Creates new tree version. Current tree version remains the same.
     *
     * @param key is a key in the tree to be added
     * @param value is a value associated with the key
     *
     * @return a new instance of class with added pair
     */
    fun add(key: K, value: V): PersistentAVLTreeMap<K, V> {
        return PersistentAVLTreeMap(set.add(MapPair(key, value)))
    }

    /**
     * Returns the value associated with the key or null, if there is no such a key.
     *
     * @param key is a key in the tree for searching
     *
     * @return the value associated with the key or null, if there is no such a key
     */
    fun getValue(key: K): V? {
        return set.get(MapPair(key))?.value
    }

    /**
     * Removes the key, and the value associated with it from the tree.
     * Creates new tree version. Current tree version remains the same.
     *
     * @param key is a key in the tree to be removed
     *
     * @return a new instance of class without removed pair
     */
    fun remove(key: K): PersistentAVLTreeMap<K, V> {
        return PersistentAVLTreeMap(set.remove(MapPair(key)))
    }
}