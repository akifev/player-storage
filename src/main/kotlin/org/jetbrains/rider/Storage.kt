package org.jetbrains.rider

/**
 * @author akifev
 *
 * Interface for PlayerStorage class.
 */
interface Storage {
    /**
     * Adds a player with a rating or updates a player rating, if one has been already added.
     *
     * @param playerName is a name of a player to be added or updated.
     * @param playerRating is a rating of a player.
     *
     * @return true, if player has been added. Otherwise, throws an exception.
     */
    fun registerPlayerResult(playerName: String, playerRating: Int): Boolean

    /**
     * Deletes the player from the storage.
     *
     * @param playerName is a name of a player to be removed.
     *
     * @return true, if player has been removed. Returns false, if there is no such a player.
     */
    fun unregisterPlayer(playerName: String): Boolean

    /**
     * Returns player rank. Rank is a position in the rating table.
     *
     * @param playerName is a name of a player to be found.
     *
     * @return position in the rating table or null, if there is no such a player.
     */
    fun getPlayerRank(playerName: String): Int?

    /**
     * Rolls the last [step] registerPlayerResult or unregisterPlayer invocations back.
     *
     * @param step is a number of steps to roll back.
     *
     * @return true, if rollback has been performed. Returns false, if [step] less than 0 or [step] greater than number
     * of performed registerPlayerResult or unregisterPlayer invocations.
     */
    fun rollback(step: Int): Boolean
}