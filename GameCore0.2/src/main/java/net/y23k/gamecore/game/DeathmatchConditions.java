package net.y23k.gamecore.game;

/**
 * Class created by yayes2 on 12/8/13.
 */
public interface DeathmatchConditions {
    /**
     * Gets whether or not to preform a deathmatch.
     *
     * @return Whether or not to preform a deathmatch.
     */
    public abstract boolean doDeathmatch();
    /**
     * Gets the time from the start of the game till the deathmatch.
     *
     * @return The time from the start of the game till the deathmatch.
     */
    public int timeFromStart();
    /**
     * Gets the amount of time on the final deathmatch timer.
     *
     * @return The amount of time on the final deathmatch timer.
     */
    public int finalDeathmatchTimer();
    /**
     * Gets the amount of players at which the final deathmatch counter will start.
     *
     * @return The amount of players at which the final deathmatch counter will start.
     */
    public int maxPlayersForDeathmatch();
}
