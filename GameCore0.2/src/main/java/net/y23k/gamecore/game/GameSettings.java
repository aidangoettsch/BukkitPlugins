package net.y23k.gamecore.game;

/**
 * Class created by yayes2 on 12/8/13.
 */
public interface GameSettings {
    /**
     * Gets the name of this game.
     *
     * @return The name of this game.
     */
    public String getGameName();
    /**
     * Gets the prefix for messages sent by the plugin.
     *
     * @return The prefix for messages sent by the plugin.
     */
    public String msgPrefix();
    /**
     * Gets the short ID for this game.
     * The short ID is used as the command to open the interactive GUI.
     *
     * @return The short ID for this game.
     */
    public String shortID();

    /**
     * Gets if the game uses teams.
     *
     * @return Whether or not the game uses teams.
     */
    public boolean isTeams();
    /**
     * Gets if the game uses compass trackers.
     *
     * @return Whether or not the game uses compass trackers.
     */
    public boolean useCompassTrackers();
    /**
     * Gets if the game uses kits.
     *
     * @return Whether or not the game uses kits.
     */
    public abstract boolean useKits();
    /**
     * Gets the delay from when the game starts till players get their kits.
     *
     * @return The delay from when the game starts till players get their kits.
     */
    public long kitDelay();
    /**
     * Gets the deathmatch conditions.
     *
     * @return The deathmatch conditions.
     */
    public DeathmatchConditions getDeathmatchConditions();
    /**
     * Gets the respawn conditions.
     *
     * @return The respawn conditions.
     */
    public RespawnConditions getRespawnConditions();
    /**
     * Gets the lives that players get.
     *
     * @return The lives that players get.
     */
    public int getLives();
}
