package net.y23k.y23kcore.skills;

import lombok.Getter;
import lombok.Setter;
import net.y23k.y23kcore.Main;
import net.y23k.y23kcore.handlers.Handler;
import net.y23k.y23kcore.handlers.HandlerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Class created by yayes2 on 12/23/13.
 */
@Getter
@Setter
public class SkillEventHandler extends HandlerListener{
    private SkillHandler skillHandler;

    public SkillEventHandler(Main main, Handler handler) {
        super(main, handler);
        setSkillHandler((SkillHandler) getHandler());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        getSkillHandler().setupPlayer(p);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            ItemStack item = e.getItem();
            Skill skill = getSkillHandler().getSkillItems().get(item);
            if (skill != null) skill.cast(p);
        }
    }
}
