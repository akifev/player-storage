package org.jetbrains.rider

interface Storage {
    fun registerPlayerResult(playerName: String, playerRating: Int): Boolean
    fun unregisterPlayer(playerName: String): Boolean
    fun getPlayerRank(playerName: String): Int?
    fun rollback(step: Int): Boolean
}