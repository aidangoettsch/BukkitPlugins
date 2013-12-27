package net.y23k.y23kcore.handlers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.y23k.y23kcore.Main;
import org.bukkit.event.Listener;

/**
 * Class created by yayes2 on 12/21/13.
 */
@RequiredArgsConstructor
@Getter
public abstract class HandlerListener implements Listener{
    private final Main main;
    private final Handler handler;
}
