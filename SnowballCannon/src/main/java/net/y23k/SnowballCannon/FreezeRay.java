package net.y23k.SnowballCannon;

import net.y23k.y23kcore.skills.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Class created by yayes2 on 12/23/13.
 */
public class FreezeRay extends Skill {
    @Override
    public String getName() {
        return "Freeze Ray";
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void cast(Player player) {
        FallingBlock fallingBlock = player.getLocation().getWorld().spawnFallingBlock(player.getLocation(), Material.ICE, (byte) 0x0);
        fallingBlock.setVelocity(player.getLocation().getDirection());
        player.playSound(player.getLocation(), Sound.FIREWORK_BLAST, 3.0F, 1.0F);
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.DIAMOND_BARDING);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + "Freeze Ray" + ChatColor.GRAY + " (Right Click)");
        List<String> itemLore = new ArrayList<>();
        itemLore.add(ChatColor.BLUE + "Shoots ice that freezes players in place!");
        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);
        return item;
    }
}
