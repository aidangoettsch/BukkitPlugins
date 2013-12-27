package net.y23k.gamecore.game;

import lombok.Getter;
import lombok.Setter;
import net.y23k.gamecore.GamePlayer;
import net.y23k.gamecore.Map;
import net.y23k.gamecore.Kit;
import net.y23k.gamecore.Main;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Class created by yayes2 on 12/8/13.
 */
@Getter
@Setter
public abstract class Game extends JavaPlugin{
    private final HashMap<String, Kit> kits = new HashMap<>();
    private final List<Map> maps = new ArrayList<>();

    private final ScoreboardManager manager = Bukkit.getScoreboardManager();
    private final Scoreboard board = manager.getNewScoreboard();

    private Main mainPlugin;

    private int amountOfTeams;

    private Team[] teams;

    public abstract GameSettings getSettings();

    public void onEnable() {
        mainPlugin = (Main) getServer().getPluginManager().getPlugin("GameCore");
    }

    public void addKit(Kit kit) {
        getKits().put(kit.getName(), kit);
    }

    public void addPlayer(GamePlayer gp, GameInstance gi){
        if (gi.getMap().isReady()) {
            if (gi.getPlayersInGame().size() != gi.getMap().getPlayerLimit()){
                gi.getPlayersInGame().add(gp);
                switch (getSettings().getRespawnConditions()) {
                    case INFINITERESPAWN:
                        break;
                    case ELIMINATION:
                        gp.setLives(1);
                        break;
                    case SETAMOUNTOFLIVES:
                        gp.setLives(getSettings().getLives());
                        break;
                }
            } if (gi.getPlayersInGame().size() < gi.getMap().getPlayerLimit()) {
                getMainPlugin().getPlayer(gp).teleport(gi.getMap().getLobby());
            } else {
                getMainPlugin().getPlayer(gp).kickPlayer("Arena full!");
            }
        } else {
            getMainPlugin().getPlayer(gp).kickPlayer("Arena not ready!");
        }
    }

    public void playerLeave(GamePlayer p, GameInstance gi){
        gi.getPlayersInGame().remove(p);
        if (gi.getSpectators().contains(p)) {
            gi.getSpectators().remove(p);
        }
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF("MinigamesLobby");
        } catch (IOException e) {
            e.printStackTrace();
        }

        getMainPlugin().getPlayer(p).sendPluginMessage(getMainPlugin(), "BungeeCord", b.toByteArray());
    }

    public void setSpectator(GamePlayer gp, GameInstance gi) {
        Player p = getMainPlugin().getPlayer(gp);
        Team tp = getBoard().getPlayerTeam(p);
        tp.removePlayer(p);
        Team tSpec = getTeams()[0];
        tSpec.addPlayer(p);
        gi.getSpectators().add(gp);
        gp.setSpectating(true);
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1, true));
        for (GamePlayer playerGp : gi.getPlayersInGame()) {
            Player playerP = getMainPlugin().getPlayer(playerGp);
            playerP.hidePlayer(p);
        }
    }

    Inventory createKitMenu(Player p) {
        Inventory kitMenu = getServer().createInventory(null, (int)(Math.ceil(kits.size() / 3) * 9), ChatColor.RED + "Select A Kit!");
        int slot = 0;
        Iterator<Kit> it = kits.values().iterator();
        it.next();
        for (Kit k : kits.values()) {
            ItemStack icon = k.getIcon();
            kitMenu.setItem(slot, icon);
            ItemMeta iconMeta = icon.getItemMeta();
            List<String> iconLore = new ArrayList<String>();
            iconLore.add(ChatColor.GRAY + k.getDescription());
            if (!p.hasPermission(getSettings().shortID() + "." + k.getPermLevel())) {
                iconLore.add(ChatColor.RED + "You need to be " + k.getPermLevel() + " to use this kit!");
            }
            iconMeta.setLore(iconLore);
            if (!it.next().getPermLevel().equals(k.getPermLevel())) {
                slot = (int)((Math.ceil(slot) / 3) * 9) + 1;
            } else slot++;
        }
        return kitMenu;
    }

    public void playerDeath(PlayerDeathEvent e) {
        Entity ent = e.getEntity();
        if (ent instanceof Player) {
            Player p = (Player) ent;
            GamePlayer gp = getMainPlugin().getGamePlayer(p);
            EntityDamageEvent.DamageCause cause = p.getLastDamageCause().getCause();
            Player killer = p.getKiller();
            switch (getSettings().getRespawnConditions()) {
                case INFINITERESPAWN:
                    break;
                case ELIMINATION:
                    gp.setLives(0);
                    break;
                case SETAMOUNTOFLIVES:
                    gp.setLives(getSettings().getLives());
                    break;
            }
            e.setDeathMessage("");
            PlayerInventory inv = p.getInventory();
            inv.clear();
            inv.setArmorContents(new ItemStack[4]);
            if (killer != null) {
                p.sendMessage(msgPrefix() + "You killer had " + killer.getHealth() + " health");
                for (GamePlayer othergp : gp.getArena().getPlayers()){
                    Player otherp = mainplugin.getPlayer(othergp);
                    if (gp.getLives() > 0 || gp.getLives() == -2) {
                        otherp.sendMessage(msgPrefix() + p.getDisplayName() + " was killed by " + killer.getDisplayName());
                        if (gp.getLives() != -2) {
                            otherp.sendMessage(msgPrefix() + p.getDisplayName() + " has" + gp.getLives() + " remaining");
                        }
                    } else {
                        mainplugin.getPlayer(othergp).sendMessage(msgPrefix() + p.getDisplayName() + " was killed by " + killer.getDisplayName());
                        if (gp.getLives() != -1 || gp.getLives() != -2) {
                            otherp.sendMessage(p.getDisplayName() + " has been eliminated!");
                        }
                        if (gp.getLives() != -2){
                            gp.getArena().setSpectator(gp);
                        }
                    }
                }
            } else if (cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                for (GamePlayer othergp : gp.getArena().getPlayers()){
                    Player otherp = mainplugin.getPlayer(othergp);
                    if (gp.getLives() > 0 || gp.getLives() == -2) {
                        otherp.sendMessage(msgPrefix() + p.getDisplayName() + " blew up");
                        if (gp.getLives() != -2) {
                            otherp.sendMessage(msgPrefix() + p.getDisplayName() + " has" + gp.getLives() + " remaining");
                        }
                    } else {
                        mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " blew up");
                        if (gp.getLives() != -1 || gp.getLives() != -2) {
                            otherp.sendMessage(msgPrefix() + p.getDisplayName() + " has been eliminated!");
                        }
                        if (gp.getLives() != -2){
                            gp.getArena().setSpectator(gp);
                        }
                    }
                }
            } else if (cause == EntityDamageEvent.DamageCause.FALL) {
                for (GamePlayer othergp : gp.getArena().getPlayers()){
                    Player otherp = mainplugin.getPlayer(othergp);
                    if (gp.getLives() > 0 || gp.getLives() == -2) {
                        otherp.sendMessage(msgPrefix() + p.getDisplayName() + " hit the ground too hard");
                        if (gp.getLives() != -2) {
                            otherp.sendMessage(msgPrefix() + p.getDisplayName() + " has" + gp.getLives() + " remaining");
                        }
                    } else {
                        mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " hit the ground too hard");
                        if (gp.getLives() != -1 || gp.getLives() != -2) {
                            otherp.sendMessage(msgPrefix() + p.getDisplayName() + " has been eliminated!");
                        }
                        if (gp.getLives() != -2){
                            gp.getArena().setSpectator(gp);
                        }
                    }
                }
            } else if (cause == EntityDamageEvent.DamageCause.VOID) {
                for (GamePlayer othergp : gp.getArena().getPlayers()){
                    Player otherp = mainplugin.getPlayer(othergp);
                    if (gp.getLives() > 0 || gp.getLives() == -2) {
                        otherp.sendMessage(msgPrefix() + p.getDisplayName() + " fell out of the world");
                        if (gp.getLives() != -2) {
                            otherp.sendMessage(msgPrefix() + p.getDisplayName() + " has" + gp.getLives() + " remaining");
                        }
                    } else {
                        mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " fell out of the world");
                        if (gp.getLives() != -1 || gp.getLives() != -2) {
                            otherp.sendMessage(msgPrefix() + p.getDisplayName() + " has been eliminated!");
                        }
                        if (gp.getLives() != -2){
                            gp.getArena().setSpectator(gp);
                        }
                    }
                }
            } else {
                for (GamePlayer othergp : gp.getArena().getPlayers()){
                    Player otherp = mainplugin.getPlayer(othergp);
                    if (gp.getLives() > 0 || gp.getLives() == -2) {
                        otherp.sendMessage(msgPrefix() + p.getDisplayName() + " died");
                        if (gp.getLives() != -2) {
                            otherp.sendMessage(msgPrefix() + p.getDisplayName() + " has" + gp.getLives() + " remaining");
                        }
                    } else {
                        mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " died");
                        if (gp.getLives() != -1 || gp.getLives() != -2) {
                            otherp.sendMessage(msgPrefix() + p.getDisplayName() + " has been eliminated!");
                        }
                        if (gp.getLives() != -2){
                            gp.getArena().setSpectator(gp);
                        }
                    }
                }
            }
        }
    }

    void gameStart(Arena a) {
        a.setStarted(true);
        for (final GamePlayer gp : a.getPlayers()){
            Player p = mainplugin.getPlayer(gp);
            p.sendMessage(msgPrefix() + "Game started!");
            final PlayerInventory inv = p.getInventory();
            inv.clear();
            if (useCompassTracking()) {
                ItemStack compass = new ItemStack(Material.COMPASS, 1);
                ItemMeta compassmeta = compass.getItemMeta();
                compassmeta.setDisplayName("" + ChatColor.RESET + ChatColor.RED + "Tracking Compass");
                compass.setItemMeta(compassmeta);
                inv.setItem(8, compass);
            }
            if (useKits()) {
                getServer().getScheduler().runTaskLater(this, new Runnable(){
                    public void run() {
                        Kit kit = gp.getKit();
                        Inventory kititems = kit.getItems();
                        for (ItemStack item : kititems) {
                            if (item != null){
                                inv.addItem(item);
                            }
                        }
                    }
                }, kitDelay());
            }
            if (doDeathmatch()) {
                getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
                    public void run() {
                        for (Arena a : arenas.values()) {
                            int dmatchtimer = a.getDeathmatchtimer();
                            dmatchtimer--;
                            a.setDeathmatchtimer(dmatchtimer);
                            if (a.getDeathmatchtimer() == 0) {
                                a.startDeathmatch();
                            }
                        }
                    }
                }, 1L, 1L);
            }
        }
    }

    void preGameEnd(Arena a) {
        a.setInLobby(false);
        int i = 0;
        if (!isTeams()) {
            Location[] spawns = a.getSpawns();
            for (GamePlayer gp : a.getPlayers()){
                Player p = mainplugin.getPlayer(gp);
                p.teleport(spawns[i]);
                i++;
                gp.sendMessage(getSettings().msgPrefix() + "You have been teleported!");
            }
        } else {
            int teamIndex = 1;
            Location[] spawns = gi.getMap().getSpawns();
            for (Team t : teams) {
                if (t.getName() != "Spectators") {
                    for (OfflinePlayer op : t.getPlayers()) {
                        if (op.isOnline()) {
                            Player p = (Player) op;
                            p.teleport(spawns[teamindex]);
                        }
                    }
                }
                if (teamindex == amountofteams){
                    teamindex = 1;
                } else {
                    teamindex++;
                }
            }
        }
        startGameStartTimer(a);
        if(isTeams()){
            int teamindex = 1;
            for(GamePlayer  gp : a.getPlayers()){
                Player p = mainplugin.getPlayer(gp);
                Team t = getTeams()[teamindex];
                t.addPlayer(p);
                if (teamindex == amountofteams){
                    teamindex = 1;
                } else {
                    teamindex++;
                }
            }
        } else {
            for(GamePlayer  gp : a.getPlayers()){
                Player p = mainplugin.getPlayer(gp);
                Team t = getTeams()[1];
                t.addPlayer(p);
            }
        }
        if (useKits()) {
            for(GamePlayer  gp : a.getPlayers()){
                Player p = mainplugin.getPlayer(gp);
                PlayerInventory inv = p.getInventory();
                ItemStack ec = new ItemStack(Material.ENDER_CHEST, 1);
                ItemMeta ecmeta = ec.getItemMeta();
                ecmeta.setDisplayName("" + ChatColor.RESET + ChatColor.RED + "Kit Selector");
                ec.setItemMeta(ecmeta);
                inv.setItem(8, ec);
            }
        }
    }

    public void playerJoin(Arena a, Player p) {
        if (a.getPlayers().size() != a.getPlayerLimit()){
            a.setInLobby(true);
            a.setStarted(false);
            p.sendMessage("You have joined " + a.getName() + "!");
            GamePlayer gp = mainplugin.getGamePlayer(p);
            a.addPlayer(gp);
            if (a.getPlayers().size() == a.getPlayersNeeded()) {
                preGameStartTimer(a);
            }
        } else if (a.started() || !a.inLobby()) {
            p.kickPlayer("Game in progress");
        }  else {
            p.kickPlayer("Arena full!");
        }
    }

    public void gameEnd(Arena a){
        for (GamePlayer p : a.getPlayers()){
            a.playerLeave(p);
        }
        a.setInLobby(true);
    }

    public HashMap<String, Arena> getArenas(){
        return arenas;
    }

    void startGameStartTimer(final Arena a){
        for (GamePlayer gp : a.getPlayers()){
            Player p = mainplugin.getPlayer(gp);
            p.setLevel(30);
        }
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
            public void run(){
                for (GamePlayer gp : a.getPlayers()){
                    final Player p = mainplugin.getPlayer(gp);
                    p.setExp(p.getExp() - .05F);
                    if (p.getExp() == p.getLevel() && p.getLevel() != 0){
                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1F, 1F);
                        if (p.getLevel() <= 10 && p.getLevel() != 1 && p.getLevel() != 0){
                            p.sendMessage(msgPrefix() + "Game starting in " + p.getLevel() + " seconds!");
                        } else if (p.getLevel() == 1){
                            p.sendMessage(msgPrefix() + "Game starting in " + p.getLevel() + " second!");
                        }
                    } else if (p.getExp() == 0 ) {
                        gameStart(a);
                    }
                }
            }
        }, 1L, 0L);
    }

    void preGameStartTimer(final Arena a){
        for (GamePlayer gp : a.getPlayers()){
            Player p = mainplugin.getPlayer(gp);
            p.setLevel(60);
        }
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
            public void run(){
                for (GamePlayer gp : a.getPlayers()){
                    final Player p = mainplugin.getPlayer(gp);
                    p.setExp(p.getExp() - .05F);
                    if (p.getExp() == p.getLevel() && p.getLevel() != 0){
                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1F, 1F);
                        if (p.getLevel() <= 10 && p.getLevel() != 1 && p.getLevel() != 0){
                            p.sendMessage(msgPrefix() + "You will be teleported in" + p.getLevel() + " seconds!");
                        } else if (p.getLevel() == 1){
                            p.sendMessage(msgPrefix() + "You will be teleported in " + p.getLevel() + " second!");
                        }
                    } else if (p.getExp() == 0 ) {
                        preGameEnd(a);
                    }
                }
            }
        }, 1L, 0L);
    }

    public void registerTeams(){
        Team spectators = getBoard().registerNewTeam("Spectators");
        spectators.setDisplayName("Spectators");
        spectators.setCanSeeFriendlyInvisibles(true);
        spectators.setAllowFriendlyFire(false);
        spectators.setPrefix(ChatColor.AQUA + "");
        getTeams()[0] = spectators;
        if (!isTeams()){
            Team ingame = getBoard().registerNewTeam("In Game");
            spectators.setDisplayName("In Game");
            spectators.setCanSeeFriendlyInvisibles(true);
            spectators.setAllowFriendlyFire(false);
            spectators.setPrefix(ChatColor.GRAY + "");
            getTeams()[1] = ingame;
            amountofteams = 1;
        }
    }

    public void respawn(GamePlayer gp) {
        Player p = mainPlugin.getPlayer(gp);
        PlayerInventory inv = p.getInventory();
        if (getSettings().useCompassTrackers()) {
            ItemStack compass = new ItemStack(Material.COMPASS, 1);
            ItemMeta compassMeta = compass.getItemMeta();
            compassMeta.setDisplayName("" + ChatColor.RESET + ChatColor.RED + "Tracking Compass");
            compass.setItemMeta(compassMeta);
            inv.setItem(44, compass);
        }
        if (getSettings().useKits()) {
            Kit kit = gp.getKit();
            Inventory kitItems = kit.getItems()[gp.getKitLevels().get(kit)];
            for (ItemStack item : kitItems) {
                if (item != null){
                    inv.addItem(item);
                }
            }
        }
    }

    public void openKitMenu(PlayerInteractEvent e, Player p) {
        p.openInventory(createKitMenu(p));
    }

    public void clickInKitMenu(InventoryClickEvent e, HumanEntity he) {
        Player p = (Player)he;
        GamePlayer gp = mainplugin.getGamePlayer(p);
        ItemStack is = e.getCurrentItem();
        Kit k = getKits().get(is.getItemMeta().getDisplayName());
        gp.setKit(k);
        mainplugin.getPlayer(gp).sendMessage(msgPrefix() + "You have selected the "  + is.getItemMeta().getDisplayName() + " kit.");
        p.closeInventory();
    }
    }
}
