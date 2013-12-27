package net.y23k.gamecore;

/**
 * Class created by yayes2 on 12/8/13.
 */

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
@RequiredArgsConstructor
public class GamePlayer {
    private final Player player;
    private Map map;
    private int lives;
    private boolean spectating;
    private Kit kit;
    private java.util.Map<Kit, Integer> kitLevels = new HashMap<>();

    public void joinArena(Map mapToJoin) {
        setMap(mapToJoin);
        setSpectating(false);
    }

    public void sendMessage(String msg) {
        player.sendMessage(msg);
    }
}
