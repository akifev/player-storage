package org.jetbrains.rider.test.correctness

import org.jetbrains.rider.PlayerStorage
import org.jetbrains.rider.Storage
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals

/**
 * Stress-test for PlayerStorage class.
 * One thread. Many operations.
 */
internal class StressTest {
    private val myPlayerStorage: Storage = PlayerStorage()
    private val testPlayerStorage: Storage = SequentialPlayerStorage()

    private val random: Random = Random(0)
    private val playerGenerator = PlayerGenerator("ab", 1, 3, -5, 5) // YOU MAY CHANGE ME
    private val availableNames = playerGenerator.getAvailableNames()

    @Test
    fun testStress() {
        repeat(1_000_000) { // CHANGE ME, IF YOU KNOW THE CONSEQUENCES
            when (random.nextInt(3)) {
                0 -> { // registerPlayerResult
                    val name = playerGenerator.generateName()
                    val rating = playerGenerator.generateRating()

                    assertEquals(
                        testPlayerStorage.registerPlayerResult(name, rating),
                        myPlayerStorage.registerPlayerResult(name, rating)
                    )
                }
                1 -> { // unregisterPlayer
                    val name = playerGenerator.generateName()

                    assertEquals(testPlayerStorage.unregisterPlayer(name), myPlayerStorage.unregisterPlayer(name))
                }
                3 -> { // rollback
                    val step = random.nextInt(-100, 10)

                    assertEquals(testPlayerStorage.rollback(step), myPlayerStorage.rollback(step))
                }
            }
            check()
        }
    }

    private fun check() {
        availableNames.forEach { name ->
            assertEquals(testPlayerStorage.getPlayerRank(name), myPlayerStorage.getPlayerRank(name))
        }
    }
}