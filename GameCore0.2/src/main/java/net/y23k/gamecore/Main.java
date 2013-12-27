package net.y23k.gamecore;

import lombok.Getter;
import net.y23k.gamecore.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class created by yayes2 on 12/8/13.
 */
public class Main extends JavaPlugin implements PluginMessageListener {
    private final HashMap<Player, GamePlayer> gamePlayers = new HashMap<>();
    private final HashMap<GamePlayer, Player> players = new HashMatp<GamePlayer, Player>();
    @Getter
    private final HashMap<String, Game> games = new HashMap<>();

    public GamePlayer getGamePlayer(Player p) {
        if (gamePlayers.containsKey(p)) {
            return gamePlayers.get(p);
        } else {
            GamePlayer gp = new GamePlayer();
            gamePlayers.put(p, gp);
            players.put(gp, p);
            return gp;
        }
    }

    public Player getPlayer(GamePlayer gp){
        return players.get(gp);
    }

    public void register(Game game) {
        games.put(game.getName(), game);
    }

    @Override
    public void onEnable() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        String inData = "";
        try{
            short len = in.readShort();
            byte[] msgBytes = new byte[len];
            in.readFully(msgBytes);

            DataInputStream msgIn = new DataInputStream(new ByteArrayInputStream(msgBytes));
            inData = msgIn.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] data = inData.split(" ");
        String msgChannel = data[0];
        if (msgChannel.equals("PlayerJoin")) {
            Game game =  games.get(data[1]);
            Map map = game.getArenas().get(data[3]);
            game.playerJoin(map, Bukkit.getPlayer(data[1]));
        } else if (msgChannel.equals("SpawnSession")) {
            Game game =  games.get(data[2]);
        }
    }
}
