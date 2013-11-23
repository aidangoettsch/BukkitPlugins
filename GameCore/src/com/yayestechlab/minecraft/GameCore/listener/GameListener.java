package com.yayestechlab.minecraft.GameCore.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.yayestechlab.minecraft.GameCore.GamePlayer;
import com.yayestechlab.minecraft.GameCore.Main;

public class GameListener implements Listener{
	Main main;
	
	public GameListener(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		GamePlayer gp = main.getGamePlayer(p);
		if (!gp.getArena().inLobby() || !gp.getArena().started()){
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		Entity ent = e.getEntity();
		if (ent instanceof Player) {
			Player p = (Player) ent;
			main.getGamePlayer(p).getArena().getGame().playerDeath(e);
		}
	}
}
