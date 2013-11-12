package com.yayestechlab.minecraft.Follow;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class FollowListener implements Listener{
	
	private Main plugin;
	
	public FollowListener(Main main) {
		plugin = main;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if (p == Main.p2){
			Location location = p.getLocation();
			location.setY(location.getY() + 3);
			Main.p1.teleport(location);
			Main.p1.setAllowFlight(true);
			Main.p1.setFlying(true);
		}
	}
}
