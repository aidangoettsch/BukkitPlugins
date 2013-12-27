package net.y23k.y23kcore.scoreboard;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Class created by yayes2 on 12/14/13.
 */
@Getter
@Setter
public class ScoreboardHandler {
    public ScoreboardManager scoreboardManager = Bukkit.getServer().getScoreboardManager();

    public Map<OfflinePlayer, Scoreboard> boards = new HashMap<>();
    public Map<OfflinePlayer, Objective> sidebarObjectives = new HashMap<>();
    public Map<OfflinePlayer, String> prefixes = new HashMap<>();

    public void setupPlayer(OfflinePlayer op) {
        if (!getBoards().containsKey(op)) {
            Scoreboard scoreboard = getScoreboardManager().getNewScoreboard();
            getBoards().put(op, scoreboard);
            getSidebarObjectives().put(op, getBoards().get(op).registerNewObjective("sidebar", "sidebar"));
        }
        for (Scoreboard board : getBoards().values()) {
            if (board.getTeam(op.getName()) == null) {
                Team pTeam = board.registerNewTeam(op.getName());
                pTeam.addPlayer(op);
            }
        }
    }

    public void setSidebarTitle(String title, OfflinePlayer p) {
        setupPlayer(p);
        if (!title.equals("")) {
            getSidebarObjectives().get(p).setDisplayName(title);
        } else {
            getBoards().get(p).clearSlot(DisplaySlot.SIDEBAR);
        }
    }

    public void setSidebarScore(String name, int score) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(name);
        setupPlayer(op);
        getBoards().get(op).getObjective(DisplaySlot.SIDEBAR).getScore(op).setScore(score);
    }

    public void setPrefix(String prefix, OfflinePlayer p) {
        setupPlayer(p);
        getPrefixes().put(p, prefix);
        for (Scoreboard board : getBoards().values()) {
            Team pTeam = board.getPlayerTeam(p);
            pTeam.setPrefix(prefix);
        }
    }

    public void setSuffix(String suffix, OfflinePlayer p) {
        setupPlayer(p);
        getPrefixes().put(p, suffix);
        for (Scoreboard board : getBoards().values()) {
            Team pTeam = board.getPlayerTeam(p);
            pTeam.setSuffix(suffix);
        }
    }

    public void setColor(ChatColor color, OfflinePlayer p) {
        String prefix = getPrefixes().get(p) + color;
        for (Scoreboard board : getBoards().values()) {
            Team pTeam = board.getPlayerTeam(p);
            pTeam.setPrefix(prefix);
        }
    }
}
