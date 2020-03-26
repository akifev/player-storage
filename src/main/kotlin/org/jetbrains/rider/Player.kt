package org.jetbrains.rider

import kotlin.math.min

/**
 * @author akifev
 *
 * Class for storing player information. Overrides method compareTo() for ordering the rating table.
 * Also, overrides toString() and hashCode() methods for testing.
 */
internal class Player(
    private val name: String,
    private val rating: Int
) : Comparable<Player> {
    override fun hashCode(): Int {
        return toString().hashCode()
    }

    override fun toString(): String {
        return "$name#$rating"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Player) {
            return false
        }

        return compareTo(other) == 0
    }

    override fun compareTo(other: Player): Int {
        when {
            this.rating < other.rating -> return -1
            this.rating > other.rating -> return 1
            else -> {
                for (i in 0 until min(this.name.length, other.name.length)) when {
                    this.name[i] < other.name[i] -> return 1
                    this.name[i] > other.name[i] -> return -1
                }
                return when {
                    this.name.length < other.name.length -> 1
                    this.name.length > other.name.length -> -1
                    else -> 0
                }
            }
        }
    }
}