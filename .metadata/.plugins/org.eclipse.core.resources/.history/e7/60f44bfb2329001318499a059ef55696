package com.yayestechlab.minecraft.Follow;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class FollowListener implements Listener{
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if (p == Main.p2){
			Location location = p.getLocation();
			if (location.getWorld() == Main.p1.getLocation().getWorld()){
				int speed = 1;
				Vector dir = location.toVector().subtract(Main.p1.getLocation().toVector()).normalize();
				dir.setY(0);
				Main.p1.setVelocity(dir.multiply(speed));
			} else {
				Location newloc = p.getLocation();
				newloc.setY(p.getLocation().getY() + 3);
				Main.p1.teleport(newloc);
			}
		}
	}
}
