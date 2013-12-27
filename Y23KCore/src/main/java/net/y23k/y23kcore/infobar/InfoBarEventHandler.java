package net.y23k.y23kcore.infobar;

import lombok.Getter;
import net.y23k.y23kcore.Main;
import net.y23k.y23kcore.handlers.Handler;
import net.y23k.y23kcore.handlers.HandlerListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Class created by yayes2 on 12/21/13.
 */
@Getter
public class InfoBarEventHandler extends HandlerListener{
    private InfoBarHandler infoBarHandler;
    
    public InfoBarEventHandler(Main main, Handler handler) {
        super(main, handler);
        infoBarHandler = (InfoBarHandler) getHandler();
    }

    //@EventHandler
    //public void onPlayerMove(PlayerMoveEvent event) {
    //    getInfoBarHandler().movePlayerWither(event.getPlayer());
    //}

    //@EventHandler
    //public void onPlayerJoin(PlayerJoinEvent event) {
        //getInfoBarHandler().setupPlayer(event.getPlayer());
    //}
}
