package net.y23k.SnowballCannon;

import lombok.Getter;
import lombok.Setter;
import net.y23k.y23kcore.skills.SkillHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Class created by yayes2 on 12/23/13.
 */
@Getter
@Setter
public class Main extends JavaPlugin {
    private net.y23k.y23kcore.Main Y23KCore;
    private SkillHandler skillHandler;
    private FreezeRay freezeRay;
    private SnowballCannon snowballCannon;

    @Override
    public void onEnable() {
        setY23KCore((net.y23k.y23kcore.Main) Bukkit.getServer().getPluginManager().getPlugin("Y23KCore"));
        setSkillHandler((SkillHandler) getY23KCore().getHandlers().get("SkillHandler"));
        setFreezeRay(new FreezeRay());
        setSnowballCannon(new SnowballCannon());
        getSkillHandler().registerSkill(getFreezeRay());
        getSkillHandler().registerSkill(getSnowballCannon());
    }
}
