package net.y23k.y23kcore.skills;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.y23k.y23kcore.Main;
import net.y23k.y23kcore.handlers.Handler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class created by yayes2 on 12/23/13.
 */
@Getter
public class SkillHandler implements Handler{
    private final Main main;

    private List<Skill> skills = new ArrayList<>();
    private Map<ItemStack, Skill> skillItems = new HashMap<>();

    public SkillHandler(Main main) {
        this.main = main;
    }

    public void registerSkill(Skill skill) {
        getSkills().add(skill);
        getSkillItems().put(skill.getItem(), skill);
    }

    public void castSkill(Skill skill, Player p) {
        if (skill.getPlayerCooldown(p) == 0) {
            p.sendMessage("You used the " + skill.getName() + " skill!");
            skill.cast(p);
        } else {
            p.sendMessage("Item on cooldown.  " + skill.getPlayerCooldown(p) + " seconds remain.");
        }
    }

    public void setupPlayer(Player p) {
        for (Skill skill : skills) {
            skill.setupPlayer(p);
        }
    }

    @Override
    public void registerListener() {
        main.registerHandlerListener(new SkillEventHandler(getMain(), this));
    }
}
