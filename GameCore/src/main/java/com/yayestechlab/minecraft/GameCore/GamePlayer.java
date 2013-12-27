package com.yayestechlab.minecraft.GameCore;

public class GamePlayer{
	private Arena arena;
	private int lives;
	private boolean spectating;
	private Kit kit;
	
	public void joinArena(Arena arenatojoin) {
		arena = arenatojoin;
        setSpectating(false);
	}
	
	public Arena getArena() {
		return arena;
	}
	
	public void onDeath() {
		if (lives != -1 || getLives() != -2){
			lives--;
		}
	}

	public void setLives(int lives) {
		this.lives = lives;
	}
	
	public int getLives() {
		return lives;
	}

    public boolean isSpectating() {
        return spectating;
    }

    public void setSpectating(boolean spectating) {
        this.spectating = spectating;
    }

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }
}