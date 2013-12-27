package com.yayestechlab.minecraft.GameCore.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.yayestechlab.minecraft.GameCore.GamePlayer;
import com.yayestechlab.minecraft.GameCore.Main;

public class GameListener implements Listener{
	private final Main main;
	
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
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand().getType() == Material.ENDER_CHEST) {
			main.getGamePlayer(p).getArena().getGame().openKitMenu(e, p);
		}
	}
	
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getInventory().getName() == ChatColor.RED + "Select a kit!") {
			main.getGamePlayer((Player)e.getWhoClicked()).getArena().getGame().clickInKitMenu(e, e.getWhoClicked());
		}
	}
}