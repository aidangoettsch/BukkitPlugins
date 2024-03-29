package com.yayestechlab.minecraft.randomtppads.config;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.yayestechlab.minecraft.randomtppads.RandomTPPads;

public class DataManager {

	public static File pads;
	private static FileConfiguration padsConfig;
	
	public static void reloadPadConfig(Plugin p) {
	    if (pads == null) {
	    	pads = new File(p.getDataFolder(), "pads.yml");
	    }
	    
	    padsConfig = YamlConfiguration.loadConfiguration(pads);
	}
	
	public static FileConfiguration getPadConfig(Plugin p) {
		if (padsConfig == null) {
			reloadPadConfig(p);
		}
		return padsConfig;
	}
	
	public static void savePadConfig(Plugin p, FileConfiguration cfg) {
	    if (padsConfig == null || pads == null) {
	    	return;
	    }
	    try {
	        cfg.save(pads);
	    } catch (IOException ex) {
	        p.getLogger().log(Level.SEVERE, "Could not save config to " + pads, ex);
	    }
	}
	
	public static void saveDefaultPadConfig(Plugin p) {
	    if (pads == null) {
	        pads = new File(p.getDataFolder(), "pads.yml");
	    }
	    if (
	    		!pads.exists()) {
	    	
	         p.saveResource("pads.yml", false);
	     }
	}
	
	public String getKeyByValueVector(Vector v, FileConfiguration cfg, RandomTPPads plugin) {
		List<String> keys = cfg.getStringList("keylist");
		for (String key : keys) {
			if(!key.contains("padname")){
				if (cfg.getVector(key).equals(v)){
					return key;
				}
			}
		}
		return null;
	}

	public void set(String arg0, Object arg1, FileConfiguration cfg){
		cfg.set(arg0, arg1);
		List<String> keys = cfg.getStringList("keylist");
		if (arg1 != null){
			if (!keys.contains(arg0)){
				keys.add(arg0);
				cfg.set("keylist", keys);
			}
		
		}
	}
}
