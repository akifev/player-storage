package org.jetbrains.rider.trees

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

    fun add(key: K, value: V): PersistentAVLTreeMap<K, V> {
        return PersistentAVLTreeMap(set.add(MapPair(key, value)))
    }

    fun getValue(key: K): V? {
        return set.get(MapPair(key))?.value
    }

    fun remove(key: K): PersistentAVLTreeMap<K, V> {
        return PersistentAVLTreeMap(set.remove(MapPair(key)))
    }
}