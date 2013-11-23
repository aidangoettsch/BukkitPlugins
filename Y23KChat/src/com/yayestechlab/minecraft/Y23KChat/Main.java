package com.yayestechlab.minecraft.Y23KChat;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.yayestechlab.minecraft.Y23KChat.listener.Y23KChatListener;

public class Main extends JavaPlugin{
	private Chat chat = null;

	private boolean setupChat() {
		RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (chatProvider != null) {
			chat = chatProvider.getProvider();
		}
		
		return (chat != null);
	}
	
	public void onEnable() {
		setupChat();
		getServer().getPluginManager().registerEvents(new Y23KChatListener(this), this);
	}

	public Chat getChatManager() {
		return chat;
	}
}
