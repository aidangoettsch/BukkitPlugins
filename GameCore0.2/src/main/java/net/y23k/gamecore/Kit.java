package net.y23k.gamecore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Class created by yayes2 on 12/8/13.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Kit {
    private final String name;
    private final String description;
    private final Inventory[] items;
    private final ItemStack icon;
    private final String permLevel;
}
