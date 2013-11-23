package com.yayestechlab.minecraft.GameCore;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class Arena {
	private Main mainplugin = (Main) Bukkit.getServer().getPluginManager().getPlugin("GameCore");
	
	private List<GamePlayer> playersingame = new ArrayList<GamePlayer>();
	
	private IGame game;
	
	private int lives = getGame().lives();
	
	private String name;
	
	private Location lobby;
	
	private Location[] spawns;
	
	private boolean started;
	
	private boolean inLobby;
	
	private int playerlimit;
	
	private int playersneeded;
	
	public Arena(Selection sel, String name, IGame game){
		this.game = game;
		this.name = name;
	}
	
	public void addPlayer(GamePlayer p){
		if (lobby != null || spawns[playerlimit] == null) {
			if (playersingame.size() != playerlimit){
				playersingame.add(p);
				p.setLives(lives);
				mainplugin.getPlayer(p).teleport(lobby);
			} else {
				mainplugin.getPlayer(p).kickPlayer("Arena full!");
			}
		} else {
			mainplugin.getPlayer(p).kickPlayer("Arena not ready!");
		}
	}
	
	public void playerLeave(GamePlayer p){
		playersingame.remove(p);
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);

		try {
			out.writeUTF("Connect");
			out.writeUTF("MinigamesLobby");
		} catch (IOException e) {
			e.printStackTrace();
		}

		mainplugin.getPlayer(p).sendPluginMessage(mainplugin, "BungeeCord", b.toByteArray());
	}
	
	public List<GamePlayer> getPlayers(){
		return playersingame;
	}

	public String getName() {
		return name;
	}

	public void setNextSpawn(Location l){
		int nextspawn = getSpawns().length;
		getSpawns()[nextspawn] = l;
	}
	
	public boolean inLobby(){
		return inLobby;
	}
	
	public boolean started(){
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public Location[] getSpawns() {
		return spawns;
	}

	public void setSpawns(Location[] spawns) {
		this.spawns = spawns;
	}

	public void setInLobby(boolean inLobby) {
		this.inLobby = inLobby;
	}

	public IGame getGame() {
		return game;
	}

	public int getPlayerLimit() {
		return playerlimit;
	}

	public void setPlayerLimit(int playerlimit) {
		this.playerlimit = playerlimit;
	}

	public int getPlayersNeeded() {
		return playersneeded;
	}

	public void setPlayersNeeded(int playersneeded) {
		this.playersneeded = playersneeded;
	}
}
