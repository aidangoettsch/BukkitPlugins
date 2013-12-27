package net.y23k.SnowballCannon;

import net.y23k.y23kcore.skills.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Class created by yayes2 on 12/23/13.
 */
public class SnowballCannon extends Skill {
    @Override
    public String getName() {
        return "Snowball Cannon";
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void cast(Player player) {
        player.launchProjectile(Snowball.class);
        player.playSound(player.getLocation(), Sound.FUSE, 3.0F, 1.0F);
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.IRON_BARDING);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + "Snowball Cannon" + ChatColor.GRAY + " (Right Click)");
        List<String> itemLore = new ArrayList<>();
        itemLore.add(ChatColor.GRAY + "Shoots snowballs at other players!");
        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);
        return item;
    }
}
