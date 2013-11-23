package com.yayestechlab.minecraft.serverpads.listeners.commands;

//import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.yayestechlab.minecraft.serverpads.ServerPads;
import com.yayestechlab.minecraft.serverpads.config.DataManager;

public class ServerPadsCommandExecutor implements CommandExecutor {
	private ServerPads plugin;

	public ServerPadsCommandExecutor(ServerPads plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if(label.equalsIgnoreCase("serverpads") || label.equalsIgnoreCase("sp")){
				if (args.length == 5){
					if(args[0].equalsIgnoreCase("add")){
						DataManager dm = new DataManager();
						Player p = (Player) sender;
						Vector loc = plugin.convertLocationToVector(p.getLocation().getBlock().getLocation());
						dm.set("padloc." + args[1], loc, plugin.cfg);
						dm.set("padvelocity." + "padloc." + args[1], new Vector(Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4])), plugin.cfg);
						DataManager.savePadConfig(plugin, plugin.cfg);
						sender.sendMessage("Added pad");
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			sender.sendMessage("You must be a player!");
			return false;
		}
	}
}
