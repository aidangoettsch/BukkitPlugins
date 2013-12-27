package com.yayestechlab.minecraft.GameCore;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Kit {
	private final String name;
	private final String description;
	private final Inventory items;
	private final ItemStack icon;
	private final String permlevel;
	
	public Kit(String name, String description, Inventory items, ItemStack icon, String permlevel){
		this.name = name;
		this.description = description;
		this.items = items;
		this.icon = icon;
		this.permlevel = permlevel;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Inventory getItems() {
		return items;
	}
	
	public ItemStack getIcon() {
		return icon;
	}
	
	public String getPermlevel() {
		return permlevel;
	}
}