package org.jetbrains.rider.test.correctness

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf
import org.jetbrains.kotlinx.lincheck.verifier.VerifierState
import org.jetbrains.rider.Player
import org.jetbrains.rider.Storage

/**
 * @author akifev
 *
 * Sequential specification PlayerStorage class.
 * Implements Storage interface.
 * Extends VerifierState and override extractState() method for checking state equivalents in LinChecker.
 */
internal class SequentialPlayerStorage : Storage, VerifierState() {
    private var mapVersions: MutableList<PersistentMap<String, Int>> = mutableListOf(persistentMapOf())
    private var setVersions: MutableList<PersistentSet<Player>> = mutableListOf(persistentSetOf())

    override fun registerPlayerResult(playerName: String, playerRating: Int): Boolean {
        val map = mapVersions.last()
        val set = setVersions.last()

        when (val oldPlayerRating: Int? = map[playerName]) {
            null -> {
                setVersions.plusAssign(set.add(Player(playerName, playerRating)))
                mapVersions.plusAssign(map.put(playerName, playerRating))
            }
            else -> {
                setVersions.plusAssign(
                    set.remove(Player(playerName, oldPlayerRating)).add(Player(playerName, playerRating))
                )
                mapVersions.plusAssign(map.remove(playerName).put(playerName, playerRating))
            }
        }

        return true
    }

    override fun unregisterPlayer(playerName: String): Boolean {
        val map: PersistentMap<String, Int> = mapVersions.last()
        val set: PersistentSet<Player> = setVersions.last()

        val playerRating: Int = map[playerName] ?: return false

        mapVersions.plusAssign(map.remove(playerName))
        setVersions.plusAssign(set.remove(Player(playerName, playerRating)))

        return true
    }

    override fun getPlayerRank(playerName: String): Int? {
        val map: PersistentMap<String, Int> = mapVersions.last()
        val set: PersistentSet<Player> = setVersions.last()

        val playerRating: Int = map[playerName] ?: return null


        return set.filter { it > Player(playerName, playerRating) }.size + 1
    }

    override fun rollback(step: Int): Boolean {
        if (step < 0 || mapVersions.size - step < 1) {
            return false
        }

        mapVersions = mapVersions.subList(0, mapVersions.size - step)
        setVersions = setVersions.subList(0, setVersions.size - step)

        return true
    }

    override fun extractState(): Any = Pair(mapVersions, setVersions)
}