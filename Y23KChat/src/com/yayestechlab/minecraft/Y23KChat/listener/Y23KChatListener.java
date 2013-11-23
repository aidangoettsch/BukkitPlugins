package com.yayestechlab.minecraft.Y23KChat.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.yayestechlab.minecraft.Y23KChat.Main;

public class Y23KChatListener implements Listener{
	
	private Main main;
	
	public Y23KChatListener(Main main) {
		this.main = main;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		String prefix = ChatColor.translateAlternateColorCodes('&', main.getChatManager().getPlayerPrefix(p));
		e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
		e.setFormat(prefix + p.getDisplayName() + ChatColor.RESET + ChatColor.GRAY + ": " + e.getMessage());
	}
}
