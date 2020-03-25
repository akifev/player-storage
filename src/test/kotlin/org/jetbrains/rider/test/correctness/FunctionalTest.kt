package org.jetbrains.rider.test.correctness

import org.jetbrains.rider.PlayerStorage
import org.jetbrains.rider.Storage
import org.junit.Test
import kotlin.test.assertEquals

internal class FunctionalTest {
    private val storage: Storage = PlayerStorage()

    @Test
    fun testRegisterPlayerResult() {
        // registration
        for (i in -100..100) {
            storage.registerPlayerResult(i.toString(), i)
        }
        // checking
        var currentRank = 1
        for (i in 100 downTo -100) {
            assertEquals(currentRank, storage.getPlayerRank(i.toString()))
            currentRank++
        }
        // updating
        for (i in -100..100) {
            storage.registerPlayerResult(i.toString(), -i)
        }

        // checking
        currentRank = 1
        for (i in -100..100) {
            assertEquals(currentRank, storage.getPlayerRank(i.toString()))
            currentRank++
        }
    }

    @Test
    fun testUnregisterPlayer() {
        storage.unregisterPlayer("unknown")
        // registration
        for (i in -100..100) {
            storage.registerPlayerResult(i.toString(), i)
        }
        // removing half of players
        for (i in -100..0) {
            storage.unregisterPlayer(i.toString())
        }
        // checking
        var currentRank = 1
        for (i in 100 downTo 1) {
            assertEquals(currentRank, storage.getPlayerRank(i.toString()))
            currentRank++
        }
        for (i in 0 downTo -100) {
            assertEquals(null, storage.getPlayerRank(i.toString()))
        }

    }

    @Test
    fun testGetPlayerRank() {
        // testing correctness.storage with players with different ratings
        storage.registerPlayerResult("a", 1)
        assertEquals(1, storage.getPlayerRank("a"))
        storage.registerPlayerResult("b", 2)
        assertEquals(1, storage.getPlayerRank("b"))
        assertEquals(2, storage.getPlayerRank("a"))
        storage.registerPlayerResult("c", 3)
        assertEquals(1, storage.getPlayerRank("c"))
        assertEquals(2, storage.getPlayerRank("b"))
        assertEquals(3, storage.getPlayerRank("a"))
        storage.unregisterPlayer("a")
        assertEquals(null, storage.getPlayerRank("a"))
        assertEquals(1, storage.getPlayerRank("c"))
        assertEquals(2, storage.getPlayerRank("b"))
        storage.unregisterPlayer("b")
        assertEquals(null, storage.getPlayerRank("b"))
        assertEquals(1, storage.getPlayerRank("c"))
        storage.unregisterPlayer("c")
        assertEquals(null, storage.getPlayerRank("c"))

        // testing correctness.storage with players with equal ratings
        storage.registerPlayerResult("aaa", 1)
        storage.registerPlayerResult("aba", 1)
        storage.registerPlayerResult("aca", 1)
        assertEquals(1, storage.getPlayerRank("aaa"))
        assertEquals(2, storage.getPlayerRank("aba"))
        assertEquals(3, storage.getPlayerRank("aca"))
        storage.registerPlayerResult("aa", 1)
        storage.registerPlayerResult("ab", 1)
        assertEquals(1, storage.getPlayerRank("aa"))
        assertEquals(2, storage.getPlayerRank("aaa"))
        assertEquals(3, storage.getPlayerRank("ab"))
        assertEquals(4, storage.getPlayerRank("aba"))
        assertEquals(5, storage.getPlayerRank("aca"))
        storage.rollback(5)

        // testing correctness.storage with players with different and equal ratings
        storage.registerPlayerResult("a", 1)
        storage.registerPlayerResult("ba", 2)
        storage.registerPlayerResult("aa", 1)
        storage.registerPlayerResult("b", 1)
        storage.registerPlayerResult("aaa", 2)
        assertEquals(1, storage.getPlayerRank("aaa"))
        assertEquals(2, storage.getPlayerRank("ba"))
        assertEquals(3, storage.getPlayerRank("a"))
        assertEquals(4, storage.getPlayerRank("aa"))
        assertEquals(5, storage.getPlayerRank("b"))
        storage.rollback(5)
    }

    @Test
    fun testRollback() {
        storage.rollback(0) // doing nothing

        storage.registerPlayerResult("a", 1)
        assertEquals(1, storage.getPlayerRank("a"))
        storage.registerPlayerResult("b", 2)
        assertEquals(1, storage.getPlayerRank("b"))
        assertEquals(2, storage.getPlayerRank("a"))

        storage.rollback(2)
        assertEquals(null, storage.getPlayerRank("a"))
        assertEquals(null, storage.getPlayerRank("b"))

        storage.registerPlayerResult("a", 1)
        storage.rollback(1)
        assertEquals(null, storage.getPlayerRank("a"))
    }
}