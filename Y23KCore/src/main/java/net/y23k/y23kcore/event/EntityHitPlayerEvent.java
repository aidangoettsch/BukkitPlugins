package net.y23k.y23kcore.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Class created by yayes2 on 12/23/13.
 */

@Getter
public class EntityHitPlayerEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Entity entity;

    public EntityHitPlayerEvent(Entity entity) {
        this.entity = entity;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
