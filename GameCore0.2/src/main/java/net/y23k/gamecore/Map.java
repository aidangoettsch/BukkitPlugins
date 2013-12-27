package net.y23k.gamecore;

import lombok.Getter;
import lombok.Setter;
import net.y23k.gamecore.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class created by yayes2 on 12/8/13.
 */
@Getter
@Setter
public class Map{
    private Game game;

    private String name;

    private Location lobby;
    private Location deathmatchArena;
    private Location[] spawns;

    private int playerLimit;
    private int playersNeeded;

    public Map(Game game, String name) {
        setGame(game);
        setName(name);
    }

    public void addSpawn(Location spawn) {
        spawns[spawns.length] = spawn;
    }

    public Map(java.util.Map<String, Object> map) {
        setName((String) map.get("name"));
        setGame((Game) Bukkit.getServer().getPluginManager().getPlugin((String) map.get("game")));
        setLobby(new Location(Bukkit.getServer().getWorld((String) map.get(map.get("lobbyWorld"))),(double) map.get("lobbyX"), (double) map.get("lobbyY"), (double) map.get("lobbyZ")));
        setDeathmatchArena(new Location(Bukkit.getServer().getWorld((String) map.get(map.get("deathmatchArenaWorld"))),(double) map.get("deathmatchArenaX"), (double) map.get("deathmatchArenaY"), (double) map.get("deathmatchArenaZ")));
        int i = 1;
        List<String> spawnsWorld = (ArrayList) map.get("spawnsWorld");
        List<Double> spawnsX = (ArrayList) map.get("spawnsX");
        List<Double> spawnsY = (ArrayList) map.get("spawnsY");
        List<Double> spawnsZ = (ArrayList) map.get("spawnsZ");
        while (i >= spawnsWorld.size()) {
            addSpawn(new Location(Bukkit.getServer().getWorld(spawnsWorld.get(i)), spawnsX.get(i), spawnsY.get(i), spawnsZ.get(i)));
            i++;
        }
    }

    public boolean isReady() {
        return getLobby() == null || getDeathmatchArena() == null || getSpawns()[getPlayerLimit()] == null;
    }
}
