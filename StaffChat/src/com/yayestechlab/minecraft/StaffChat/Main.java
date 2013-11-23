package com.yayestechlab.minecraft.StaffChat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("sc")){
			if(sender.hasPermission("staffchat.staff")){
				if (args.length > 0) {
					for (Player p : this.getServer().getOnlinePlayers()){
						if (p.hasPermission("staffchat.staff") || p.isOp()){
							Player senderp = (Player) sender;
							String argsstring = ""; 
							for (String arg : args){
								argsstring = argsstring + arg + " ";
							}
							p.sendMessage(ChatColor.RED + "[SC] " + senderp.getDisplayName() + ": " + argsstring);
							return true;
						}
					}
				}
			}
		}
		return false; 
	}
}
