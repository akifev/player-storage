package org.jetbrains.rider.test.correctness

import org.jetbrains.rider.Player
import kotlin.math.pow
import kotlin.random.Random

internal class PlayerGenerator(
    private val alphabet: String = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_",
    private val minNameLength: Int = 1,
    private val maxNameLength: Int = 15,
    private val minRating: Int = 1,
    private val maxRating: Int = 100
) {
    private val random: Random = Random(0)

    fun generateName(): String {
        val cs = CharArray(random.nextInt(minNameLength, maxNameLength + 1))
        for (i in cs.indices) cs[i] = alphabet[random.nextInt(alphabet.length)]
        return String(cs)
    }

    fun generateRating(): Int {
        return random.nextInt(minRating, maxRating + 1)
    }

    fun generatePlayer(): Player {
        return Player(generateName(), generateRating())
    }

    fun getAvailableNames(): ArrayList<String> {
        val names: MutableList<String> = mutableListOf()
        val aSize = alphabet.length

        for (length in minNameLength..maxNameLength) {
            val name = CharArray(length)
            for (mask in 0 until aSize.toDouble().pow(length).toInt()) {
                for (i in name.indices) {
                    name[i] = alphabet[(mask / (aSize.toDouble().pow(i).toInt())) % aSize]
                }
                names += String(name)
            }
        }

        return names as ArrayList<String>
    }

    fun getAvailablePlayers(): ArrayList<Player> {
        val players: MutableList<Player> = mutableListOf()

        getAvailableNames().forEach { name ->
            for (rating in minRating..maxRating) {
                players += Player(name, rating)
            }
        }

        return players as ArrayList<Player>
    }
}