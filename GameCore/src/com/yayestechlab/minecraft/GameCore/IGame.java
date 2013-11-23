package com.yayestechlab.minecraft.GameCore;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

public abstract class IGame {
	private Main mainplugin = (Main) Bukkit.getServer().getPluginManager().getPlugin("GameCore");
	
	private HashMap<String, Arena> arenas = new HashMap<String, Arena>(); 
	
	public abstract String getName();
	
	public abstract int lives();
	
	public void playerDeath(PlayerDeathEvent e) {
		Entity ent = e.getEntity();
		if (ent instanceof Player) {
			Player p = (Player) ent;
			GamePlayer gp = mainplugin.getGamePlayer(p);
			DamageCause cause = p.getLastDamageCause().getCause();
			Player killer = p.getKiller();
			gp.onDeath();
			e.setDeathMessage("");
			if (killer != null) {
				p.sendMessage("You killer had " + killer.getHealth() + " hearts");
				for (GamePlayer othergp : gp.getArena().getPlayers()){
					if (gp.getLives() < 0) {
						mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " was killed by " + killer.getDisplayName());
						mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " has" + gp.getLives() + " remaining");
					} else {
						mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " was killed by " + killer.getDisplayName());
						if (gp.getLives() != -1) {
							mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " has been eliminated!");
						}
						gp.getArena().playerLeave(gp);
					}
				}
			} else if (cause == DamageCause.BLOCK_EXPLOSION) {
				for (GamePlayer othergp : gp.getArena().getPlayers()){
					if (gp.getLives() > 0) {
						mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " blew up");
						mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " has" + gp.getLives() + " remaining");
					} else {
						mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " blew up");
						if (gp.getLives() != -1) {
							mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " has been eliminated!");
						}
						gp.getArena().playerLeave(gp);
					}
				}
			} else if (cause == DamageCause.FALL) {
				for (GamePlayer othergp : gp.getArena().getPlayers()){
					if (gp.getLives() > 0) {
						mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " fell to their doom");
						mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " has" + gp.getLives() + " remaining");
					} else {
						mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " fell to their doom");
						if (gp.getLives() != -1) {
							mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " has been eliminated!");
						}
						gp.getArena().playerLeave(gp);
					}
				}
			} else if (cause == DamageCause.VOID) {
				for (GamePlayer othergp : gp.getArena().getPlayers()){
					if (gp.getLives() > 0) {
						mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " fell out of the world");
						mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " has" + gp.getLives() + " remaining");
					} else {
						mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " fell out of the world");
						if (gp.getLives() != -1) {
							mainplugin.getPlayer(othergp).sendMessage(p.getDisplayName() + " has been eliminated!");
						}
						gp.getArena().playerLeave(gp);
					}
				}
			}
		}
	}
	
	public void gameStart(Arena a) {
		a.setStarted(true);
		for (GamePlayer gp : a.getPlayers()){
			 mainplugin.getPlayer(gp).sendMessage("Game started!");
		}
	}
	
	public void preGameEnd(Arena a) {
		a.setInLobby(false);
		int i = 0;
		for (GamePlayer gp : a.getPlayers()){
			mainplugin.getPlayer(gp).teleport(a.getSpawns()[i]);
			mainplugin.getPlayer(gp).sendMessage("You have been teleported!");
			i++;
		}
		startGameStartTimer(a);
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
		} else if (a.started() || (a.inLobby() == false)) {
			p.kickPlayer("Game in progress");
		}  else {
			p.kickPlayer("Arena full!");
		}
	}
	
	public void gameEnd(Arena a){
		for (GamePlayer p : a.getPlayers()){
			a.playerLeave(p);
		}
	}
	
	public HashMap<String, Arena> getArenas(){
		return arenas;
	}
	
	public void startGameStartTimer(final Arena a){
		mainplugin.getServer().getScheduler().scheduleSyncRepeatingTask(mainplugin, new Runnable(){
			public void run(){
				for (GamePlayer gp : a.getPlayers()){
					final Player p = mainplugin.getPlayer(gp);
					p.setExp(p.getExp() - .05F);
					if (p.getExp() == p.getLevel() && p.getLevel() != 0){
						p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1F, 1F);
						if (p.getLevel() <= 10 && p.getLevel() != 1 && p.getLevel() != 0){
							p.sendMessage("Game starting in " + p.getLevel() + " seconds!");
						} else if (p.getLevel() == 1){
							p.sendMessage("Game starting in " + p.getLevel() + " second!");
						}
					} else if (p.getLevel() == 0 ) {
						gameStart(a);
					}
				}
			}
		}, 1L, 0L);
	}
	
	public void preGameStartTimer(final Arena a){
		mainplugin.getServer().getScheduler().scheduleSyncRepeatingTask(mainplugin, new Runnable(){
			public void run(){
				for (GamePlayer gp : a.getPlayers()){
					final Player p = mainplugin.getPlayer(gp);
					p.setExp(p.getExp() - .05F);
					if (p.getExp() == p.getLevel() && p.getLevel() != 0){
						p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1F, 1F);
						if (p.getLevel() <= 10 && p.getLevel() != 1 && p.getLevel() != 0){
							p.sendMessage("You will be teleported in" + p.getLevel() + " seconds!");
						} else if (p.getLevel() == 1){
							p.sendMessage("You will be teleported in " + p.getLevel() + " second!");
						}
					} else if (p.getLevel() == 0 ) {
						preGameEnd(a);
					}
				}
			}
		}, 1L, 0L);
	}
	
}
