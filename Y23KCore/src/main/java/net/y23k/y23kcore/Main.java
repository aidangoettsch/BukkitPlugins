package net.y23k.y23kcore;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import net.y23k.y23kcore.handlers.Handler;
import net.y23k.y23kcore.handlers.HandlerListener;
import net.y23k.y23kcore.infobar.InfoBarHandler;
import net.y23k.y23kcore.skills.SkillHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Class created by yayes2 on 12/10/13.
 */
@Getter
public class Main extends JavaPlugin {
    private ProtocolManager protocolManager;
    private Map<String, Handler> handlers = new HashMap<>();

    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public void onEnable() {
        registerHandlers();
    }

    public void registerHandlers() {
        getHandlers().put("InfoBarHandler", new InfoBarHandler(this));
        getHandlers().put("ScoreboardHandler", new InfoBarHandler(this));
        getHandlers().put("SkillHandler", new SkillHandler(this));
    }

    public void registerHandlerListener(HandlerListener handlerListener) {
        Bukkit.getServer().getPluginManager().registerEvents(handlerListener, this);
    }
}
