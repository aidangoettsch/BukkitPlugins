package com.yayestechlab.minecraft.GameCore;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import com.sk89q.worldedit.bukkit.selections.Selection;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class Arena {
	private final Main mainplugin = (Main) Bukkit.getServer().getPluginManager().getPlugin("GameCore");
	
	private final List<GamePlayer> playersingame = new ArrayList<GamePlayer>();
	
	private final Game game;
	
	private final int lives = getGame().lives();
	
	private final String name;
	
	private Location lobby;
	
	private Location[] spawns;
	
	private Location deathmatcharena;
	
	private boolean started;
	
	private boolean inLobby;
	
	private int playerlimit;
	
	private int playersneeded;
	
	private int deathmatchtimer;
	
	private final List<GamePlayer> spectators = new ArrayList<GamePlayer>();
	
	public Arena(Selection sel, String name, Game game){
		this.game = game;
		this.name = name;
	}
	
	public void addPlayer(GamePlayer p){
		if (lobby != null || spawns[playerlimit] == null || deathmatcharena == null) {
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
		if (spectators.contains(p)) {
			spectators.remove(p);
		}
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
	
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
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

    public void setLobby(Location lobby) {
        this.lobby = lobby;
    }

	public void setInLobby(boolean inLobby) {
		this.inLobby = inLobby;
	}

	public Game getGame() {
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
	
	public void setSpectator(GamePlayer gp) {
		Player p = mainplugin.getPlayer(gp);
		Team tp = game.getBoard().getPlayerTeam(p);
		tp.removePlayer(p);
		Team tspec = game.getTeams()[0];
		tspec.addPlayer(p);
		spectators.add(gp);
		gp.setSpectating(true);
		p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1, true));
		for (GamePlayer playergp : gp.getArena().getPlayers()) {
			Player playerp = mainplugin.getPlayer(playergp);
			playerp.hidePlayer(p);
		}
	}
	
	
	public List<GamePlayer> getSpectators() {
		return spectators;
	}

	public int getDeathmatchtimer() {
		return deathmatchtimer;
	}

	public void setDeathmatchtimer(int deathmatchtimer) {
		this.deathmatchtimer = deathmatchtimer;
	}
	
	public void startDeathmatch() {
		for (GamePlayer gp : playersingame) {
			mainplugin.getPlayer(gp).teleport(deathmatcharena);
		}
	}
	
	public void setDeathmatchArena(Location deathmatcharena) {
		this.deathmatcharena = deathmatcharena;
	}
}