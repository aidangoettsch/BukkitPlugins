package net.y23k.y23kcore.skills;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Class created by yayes2 on 12/23/13.
 */
@Getter
public abstract class Skill {
    private Map<Player, Integer> cooldowns = new HashMap<>();

    public abstract String getName();

    public abstract int getCooldown();

    public void setupPlayer(Player p) {
        resetCooldown(p);
    }

    public void setCooldown(Player p, int cooldown) {
        getCooldowns().put(p, cooldown);
    }

    public int getPlayerCooldown(Player p) {
        return getCooldowns().get(p);
    }

    public abstract void cast(Player p);

    public void resetCooldown(Player p) {
        setCooldown(p, getCooldown());
    }

    public abstract ItemStack getItem();
}
