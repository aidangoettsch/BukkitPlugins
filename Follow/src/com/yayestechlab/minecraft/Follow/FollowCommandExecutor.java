package com.yayestechlab.minecraft.Follow;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FollowCommandExecutor implements CommandExecutor {

	Main plugin;
	
	public FollowCommandExecutor(Main main) {
		plugin = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("follow")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(p.hasPermission("follow.follow")){
					Main.p1 = p;
					Main.p2 = plugin.getServer().getPlayer(args[0]);
					Location newloc = (Main.p2.getLocation());
					newloc.setY(Main.p2.getLocation().getY() + 3);
					Main.p1.teleport(newloc);
					Main.p1.setAllowFlight(true);
					Main.p1.setFlying(true);
					Main.p2.hidePlayer(Main.p1);
					return true;
				}
			} else {
				sender.sendMessage("You can't follow, console");
			}
		} 
		return false; 
	}



}
