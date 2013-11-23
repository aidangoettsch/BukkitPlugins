package com.yayestechlab.minecraft.randomtppads;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.yayestechlab.minecraft.randomtppads.config.DataManager;
import com.yayestechlab.minecraft.randomtppads.listeners.RandomTPPadsListener;
import com.yayestechlab.minecraft.randomtppads.listeners.commands.RandomTPPadsCommandExecutor;

public class RandomTPPads extends JavaPlugin{
	
	public FileConfiguration cfg;
	
	public RandomTPPads getPluginMain(){
		return this;
	}
	
	public Location convertVectorToLocation(Vector v){
		World world = getServer().getWorld("world");
		Location loc = new Location(world, v.getX(), v.getY(), v.getZ());
		return loc;
	}
	
	public Vector convertLocationToVector(Location loc){
		Vector v = new Vector(loc.getX(), loc.getY(), loc.getZ());
		return v;
	}
	
	public void onEnable(){
		DataManager.saveDefaultPadConfig(this);
		cfg = DataManager.getPadConfig(this);
		getCommand("randomtppads").setExecutor(new RandomTPPadsCommandExecutor(this));
		getCommand("rtpp").setExecutor(new RandomTPPadsCommandExecutor(this));
		getServer().getPluginManager().registerEvents(new RandomTPPadsListener(this), this);
	}
 
	public void onDisable(){
		DataManager.savePadConfig(this, cfg);
	}

}
