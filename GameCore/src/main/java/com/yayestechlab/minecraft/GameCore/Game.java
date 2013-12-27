package com.yayestechlab.minecraft.GameCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.yayestechlab.minecraft.GameCore.listener.GameListener;

public abstract class Game extends JavaPlugin{

	
	public abstract String getGameName();
	
	public abstract String msgPrefix();
	
	public abstract String shortID();
	
	public abstract int lives();
	
	public abstract boolean isTeams();
	
	public abstract boolean useCompassTracking();
	
	public abstract boolean useKits();
	
	public abstract boolean doDeathmatch();
	
	public abstract int timeTillDeathmatch();
	
	public abstract long kitDelay();
	
	public abstract String id();
	
	private final List<Kit> kits = new ArrayList<Kit>();
	
	private final ScoreboardManager manager = Bukkit.getScoreboardManager();
	
	private final Scoreboard board = manager.getNewScoreboard();
	
	private int amountOfTeams;
	
	private Team[] teams;
	
	public void onEnable() {
		mainplugin.register(this);
		getServer().getPluginManager().registerEvents(new GameListener(mainplugin), this);
	}
	
	Inventory createKitMenu(Player p) {
		Inventory kitmenu;
		kitmenu = getServer().createInventory(null, (int)(Math.ceil(kits.size() / 3) * 9), ChatColor.RED + "Select A Kit!");
		int slot = 0;
		Iterator<Kit> it = kits.iterator();
		it.next();
		for (Kit k : kits) {
			ItemStack icon = k.getIcon();
			kitmenu.setItem(slot, icon);
			ItemMeta iconmeta = icon.getItemMeta();
			List<String> iconlore = new ArrayList<String>();
			iconlore.add(ChatColor.GRAY + k.getDescription());
			if (!p.hasPermission(shortID() + "." + k.getPermlevel())) {
				iconlore.add(ChatColor.RED + "You need to be " + k.getPermlevel() + " to use this kit!");
			}
			iconmeta.setLore(iconlore);
			if (it.next().getPermlevel().equals(k.getPermlevel())) {
				slot = (int)((Math.ceil(slot) / 3) * 9) + 1;
			} else slot++;
		}
		return kitmenu;
	}
	
	public void playerDeath(PlayerDeathEvent e) {
		Entity ent = e.getEntity();
		if (ent instanceof Player) {
			Player p = (Player) ent;
			GamePlayer gp = mainplugin.getGamePlayer(p);
			DamageCause cause = p.getLastDamageCause().getCause();
			Player killer = p.getKiller();
			gp.onDeath();
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
			} else if (cause == DamageCause.BLOCK_EXPLOSION) {
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
			} else if (cause == DamageCause.FALL) {
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
			} else if (cause == DamageCause.VOID) {
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
				mainplugin.getPlayer(gp).sendMessage(msgPrefix() + "You have been teleported!");
			}
		} else {
			int teamindex = 1;
			Location[] spawns = a.getSpawns();
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
			spectators.setDisplayName("In Game ");
			spectators.setCanSeeFriendlyInvisibles(true);
			spectators.setAllowFriendlyFire(false);
			spectators.setPrefix(ChatColor.GRAY + "");
			getTeams()[1] = ingame;
			amountofteams = 1;
		}
	}
	
	public void respawn(GamePlayer gp) {
		Player p = mainplugin.getPlayer(gp);
		PlayerInventory inv = p.getInventory();
		if (useCompassTracking()) {
			ItemStack compass = new ItemStack(Material.COMPASS, 1);
			ItemMeta compassmeta = compass.getItemMeta();
			compassmeta.setDisplayName("" + ChatColor.RESET + ChatColor.RED + "Tracking Compass");
			compass.setItemMeta(compassmeta);
			inv.setItem(44, compass);
		}
		if (useKits()) {
			Kit kit = gp.getKit();
			Inventory kititems = kit.getItems();
			for (ItemStack item : kititems) {
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
		for (Kit k : kits) {
			if (k.getName() == is.getItemMeta().getDisplayName()) {
				gp.setKit(k);
				mainplugin.getPlayer(gp).sendMessage(msgPrefix() + "You have selected the "  + is.getItemMeta().getDisplayName() + " kit.");
				p.closeInventory();
			}
		}
	}

    public void addKit(Kit kit) {
		kits.add(kit);
	}
	
	public List<Kit> getKits() {
		return kits;
	}

	public Scoreboard getBoard() {
		return board;
	}

	public Team[] getTeams() {
		return teams;
	}

	public void setTeams(Team[] teams) {
		this.teams = teams;
	}
}