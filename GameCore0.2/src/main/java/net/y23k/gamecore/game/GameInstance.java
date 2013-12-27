package net.y23k.gamecore.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.y23k.gamecore.GamePlayer;
import net.y23k.gamecore.Main;
import net.y23k.gamecore.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class created by yayes2 on 12/8/13.
 */
@Getter
@Setter
public class GameInstance {

    private final Game game;

    private final String id;

    private Map map;

    private Main mainPlugin = (Main) Bukkit.getServer().getPluginManager().getPlugin("GameCore");

    private List<GamePlayer> playersInGame = new ArrayList<GamePlayer>();
    private List<GamePlayer> spectators = new ArrayList<GamePlayer>();

    private boolean started = false;
    private boolean inLobby = true;

    private int deathmatchTimer;

    private Location lobby;

   public GameInstance(Game game, String id) {
       this.game = game;
       this.id = id;
       int randIndex = new Random().nextInt(getGame().getMaps().size());
       Map randMap = getGame().getMaps().get(randIndex);
   }
}