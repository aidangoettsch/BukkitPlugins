package com.yayestechlab.minecraft.GameCore;

public class GamePlayer{
	private Arena arena;
	private int lives;
	
	public void joinArena(Arena arenatojoin) {
		arena = arenatojoin;
	}
	
	public Arena getArena() {
		return arena;
	}
	
	public void onDeath() {
		if (lives != -1){
			lives--;
		}
	}

	public void setLives(int lives) {
		this.lives = lives;
	}
	
	public int getLives() {
		return lives;
	}
}
