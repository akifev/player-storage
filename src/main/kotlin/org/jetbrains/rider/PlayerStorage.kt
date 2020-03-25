package org.jetbrains.rider

import org.jetbrains.rider.trees.PersistentAVLTreeMap
import org.jetbrains.rider.trees.PersistentAVLTreeSet

class PlayerStorage : Storage {
    private var mapVersions: MutableList<PersistentAVLTreeMap<String, Int>> = mutableListOf(PersistentAVLTreeMap())
    private var setVersions: MutableList<PersistentAVLTreeSet<Player>> = mutableListOf(PersistentAVLTreeSet())

    @Volatile
    private var latestVersion = Pair(mapVersions.last(), setVersions.last())

    private fun updateLatestVersion() {
        latestVersion = Pair(mapVersions.last(), setVersions.last())
    }

    @Synchronized
    override fun registerPlayerResult(playerName: String, playerRating: Int): Boolean {
        val (map, set) = latestVersion

        when (val oldPlayerRating: Int? = map.getValue(playerName)) {
            null -> {
                mapVersions.add(map.add(playerName, playerRating))
                setVersions.add(set.add(Player(playerName, playerRating)))

            }
            else -> {
                mapVersions.add(map.add(playerName, playerRating))
                setVersions.add(
                    set.remove(Player(playerName, oldPlayerRating)).add(Player(playerName, playerRating))
                )
            }
        }
        updateLatestVersion()

        return true
    }

    @Synchronized
    override fun unregisterPlayer(playerName: String): Boolean {
        val (map, set) = latestVersion

        val playerRating: Int = map.getValue(playerName) ?: return false

        mapVersions.add(map.remove(playerName))
        setVersions.add(set.remove(Player(playerName, playerRating)))

        updateLatestVersion()

        return true


    }

    override fun getPlayerRank(playerName: String): Int? {
        val (map, set) = latestVersion

        val playerRating: Int = map.getValue(playerName) ?: return null

        return set.getPosition(Player(playerName, playerRating))
    }

    @Synchronized
    override fun rollback(step: Int): Boolean {
        if (step < 0 || mapVersions.size - step < 1) {
            return false
        }

        mapVersions = mapVersions.subList(0, mapVersions.size - step)
        setVersions = setVersions.subList(0, setVersions.size - step)

        updateLatestVersion()

        return true
    }
}