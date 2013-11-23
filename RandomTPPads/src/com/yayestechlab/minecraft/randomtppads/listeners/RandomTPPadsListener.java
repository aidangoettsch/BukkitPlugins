package com.yayestechlab.minecraft.randomtppads.listeners;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.yayestechlab.minecraft.randomtppads.RandomTPPads;
import com.yayestechlab.minecraft.randomtppads.config.DataManager;

public class RandomTPPadsListener implements Listener{
	RandomTPPads plugin;
	
	public RandomTPPadsListener(RandomTPPads p){
		plugin = p;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		final Player p = e.getPlayer();
		Vector ploc = plugin.convertLocationToVector(p.getLocation().getBlock().getLocation());
		FileConfiguration cfg = plugin.cfg;
		DataManager dm = new DataManager();
		String key = dm.getKeyByValueVector(ploc, cfg, plugin);
		if(key != null){
			Vector velocity = cfg.getVector("padvelocity." + key);
			p.setVelocity(velocity);
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run(){
					int randx = (0 + (int)(Math.random() * ((1024 - 0) + 1))) - 512;
					int randz = (0 + (int)(Math.random() * ((1024 - 0) + 1))) - 512;
					int y = plugin.getServer().getWorld("survival").getHighestBlockYAt(randx, randz);
					p.teleport(new Location(plugin.getServer().getWorld("survival"), randx, y, randz));
				}
			}, 10L);
		}
	}
}
