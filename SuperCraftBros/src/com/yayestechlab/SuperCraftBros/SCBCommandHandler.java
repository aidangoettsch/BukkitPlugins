package com.yayestechlab.SuperCraftBros;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SCBCommandHandler implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("scb")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player p = (Player) sender;
				return parseArgs(args, p);
			}
		}
		return false;
	}
	
	public boolean parseArgs(String[] args, Player p){
		if (args[0].equalsIgnoreCase("createarena")) {
			
		}
		return true;
	}
}
