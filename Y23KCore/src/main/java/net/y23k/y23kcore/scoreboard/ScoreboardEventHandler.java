package net.y23k.y23kcore.scoreboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.y23k.y23kcore.Main;
import net.y23k.y23kcore.handlers.Handler;
import net.y23k.y23kcore.handlers.HandlerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Class created by yayes2 on 12/15/13.
 */
@Getter
public class ScoreboardEventHandler extends HandlerListener {
    private ScoreboardHandler scoreboardHandler;

    public ScoreboardEventHandler(Main main, Handler handler) {
        super(main, handler);
        scoreboardHandler = (ScoreboardHandler) getHandler();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        getScoreboardHandler().setupPlayer(e.getPlayer());
    }
}
