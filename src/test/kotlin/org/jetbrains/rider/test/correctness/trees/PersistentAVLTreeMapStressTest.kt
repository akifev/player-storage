package org.jetbrains.rider.test.correctness.trees

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import org.jetbrains.rider.test.correctness.PlayerGenerator
import org.jetbrains.rider.trees.PersistentAVLTreeMap
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals

internal class PersistentAVLTreeMapStressTest {
    private var myMap: PersistentAVLTreeMap<String, Int> = PersistentAVLTreeMap()
    private var testMap: PersistentMap<String, Int> = persistentMapOf()

    private val random: Random = Random(0)
    private val playerGenerator = PlayerGenerator("abAB", 1, 2, -5, 5)
    private val availableNames = playerGenerator.getAvailableNames()

    @Test
    fun testStress() {
        repeat(100_000) {
            when (random.nextInt(2)) {
                0 -> { // add
                    val key = playerGenerator.generateName()
                    val value = playerGenerator.generateRating()

                    myMap = myMap.add(key, value)
                    testMap = testMap.put(key, value)
                }
                1 -> { // remove
                    val key = playerGenerator.generateName()

                    myMap = myMap.remove(key)
                    testMap = testMap.remove(key)
                }
            }
            check()
        }
    }

    private fun check() {
        availableNames.forEach { name ->
            assertEquals(testMap[name], myMap.getValue(name))
        }
    }
}