package org.jetbrains.rider.test.correctness.trees

import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import org.jetbrains.rider.Player
import org.jetbrains.rider.test.correctness.PlayerGenerator
import org.jetbrains.rider.trees.PersistentAVLTreeSet
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals

internal class PersistentAVLTreeSetStressTest {
    private var mySet: PersistentAVLTreeSet<Player> = PersistentAVLTreeSet()
    private var testSet: PersistentSet<Player> = persistentSetOf()

    private val random: Random = Random(0)
    private val playerGenerator = PlayerGenerator("abAB", 1, 2, -5, 5) // YOU MAY CHANGE ME
    private val availablePlayers = playerGenerator.getAvailablePlayers()

    @Test
    fun testStress() {
        repeat(100_000) { // CHANGE ME, IF YOU KNOW THE CONSEQUENCES
            when (random.nextInt(2)) {
                0 -> { // add
                    val key = playerGenerator.generatePlayer()

                    mySet = mySet.add(key)
                    testSet = testSet.add(key)
                }
                1 -> { // remove
                    val key = playerGenerator.generatePlayer()

                    mySet = mySet.remove(key)
                    testSet = testSet.remove(key)
                }
            }
            check()
        }
    }

    private fun check() {
        availablePlayers.forEach { player ->
            assertEquals(testSet.contains(player), mySet.contains(player))
            if (testSet.contains(player)) {
                assertEquals(testSet.filter { it > player }.size + 1, mySet.getPosition(player))
            } else {
                assertEquals(null, mySet.getPosition(player))
            }
        }
    }
}