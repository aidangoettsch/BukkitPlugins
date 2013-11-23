package com.yayestechlab.bungeecord.BungeeChat.listeners.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import com.yayestechlab.bungeecord.BungeeChat.BungeeChat;

public class MSGCommand extends Command {
	
	private ProxyServer proxy;
	private Logger logger;
	
	public MSGCommand(BungeeChat This) {
		super("msg", "bungeechat.msg");
		proxy = ProxyServer.getInstance();
		logger = proxy.getLogger();
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer){
			if(args.length > 2){
				if(proxy.getPlayer(args[0]) != null){
					String argsstring = ""; 
					boolean donefirst = false;
					for (String arg : args){
						if(donefirst){
							argsstring = argsstring + arg + " ";
						} else {
							donefirst = true;
						}
					}
					proxy.getPlayer(args[0]).sendMessage("[" + sender.getName() + "--> you]: " + argsstring );
					logger.log(Level.INFO, "[" + sender.getName() + "--> " + proxy.getPlayer(args[0]) + "]:" + argsstring);
				} else {
					sender.sendMessage(ChatColor.RED + "No such player!");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Too many/too little arguments. Proper format: /msg <player> <message>");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "You must be a player!");
		}
	}

}
