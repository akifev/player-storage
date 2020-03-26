package org.jetbrains.rider.test.correctness

import org.jetbrains.kotlinx.lincheck.LinChecker
import org.jetbrains.kotlinx.lincheck.LoggingLevel
import org.jetbrains.kotlinx.lincheck.annotations.LogLevel
import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.annotations.Param
import org.jetbrains.kotlinx.lincheck.paramgen.IntGen
import org.jetbrains.kotlinx.lincheck.paramgen.StringGen
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressCTest
import org.jetbrains.rider.PlayerStorage
import org.jetbrains.rider.Storage
import org.junit.Test

/**
 * LinearizabilityTest class for testing PlayerStorage class operations on multi-threaded execution.
 * Checks PlayerStorage class to be thread-safe and to satisfy correctness criteria - linearizability.
 */
@LogLevel(LoggingLevel.DEBUG)
@StressCTest(sequentialSpecification = SequentialPlayerStorage::class)
//@StressCTest(actorsBefore = 0, threads = 3, actorsPerThread = 2, invocationsPerIteration = 20_000, sequentialSpecification = SequentialPlayerStorage::class)
internal class LinearizabilityTest : Storage {
    private val storage = PlayerStorage()

    @Operation
    override fun registerPlayerResult(
        @Param(gen = StringGen::class, conf = "2:abc") playerName: String,
        @Param(gen = IntGen::class, conf = "0:3") playerRating: Int
    ): Boolean {
        return storage.registerPlayerResult(playerName, playerRating)
    }

    @Operation
    override fun unregisterPlayer(@Param(gen = StringGen::class, conf = "2:abc") playerName: String): Boolean {
        return storage.unregisterPlayer(playerName)
    }

    @Operation
    override fun getPlayerRank(@Param(gen = StringGen::class, conf = "2:abc") playerName: String): Int? {
        return storage.getPlayerRank(playerName)
    }

    @Operation
    override fun rollback(@Param(gen = IntGen::class, conf = "0:3") step: Int): Boolean {
        return storage.rollback(step)
    }

    @Test
    fun runTest() = LinChecker.check(this::class.java)
}