package net.y23k.y23kcore.infobar;

import com.comphenix.packetwrapper.Packet0FSpawnMob;
import com.comphenix.packetwrapper.Packet18EntityTeleport;
import com.comphenix.packetwrapper.Packet1CEntityMetadata;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.y23k.y23kcore.Main;
import net.y23k.y23kcore.handlers.Handler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Class created by yayes2 on 12/16/13.
 */
@Getter
public class InfoBarHandler implements Handler{
    private final Main main;

    private Map<Player, FakeWither> withers = new HashMap<>();

    public InfoBarHandler(Main main) {
        this.main = main;
        registerListener();
    }

    @Override
    public void registerListener() {
        main.registerHandlerListener(new InfoBarEventHandler(main, this));
    }

    @Getter
    public class FakeWither {
        @Setter
        private int id;
        private String name;
        private float health;
        @Setter
        private Player player;

        public FakeWither(Location loc, Player p, String name) {
            Packet0FSpawnMob witherPacket = new Packet0FSpawnMob();

            WrappedDataWatcher watcher = getDefaultWatcher(Bukkit.getServer().getWorlds().get(0), EntityType.WITHER);
            watcher.setObject(10, name);
            this.name = name;
            watcher.setObject(6, 100F);
            health = 100F;

            setId(p.getEntityId() + 127645);
            setPlayer(p);

            witherPacket.setEntityID(getId());

            witherPacket.setType(EntityType.WITHER);

            witherPacket.setX(loc.getX());
            witherPacket.setY(loc.getY());
            witherPacket.setZ(loc.getZ());

            witherPacket.setMetadata(watcher);

            try {
                main.getProtocolManager().sendServerPacket(p, witherPacket.getHandle());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public WrappedDataWatcher getDefaultWatcher(World world, EntityType type) {
            Entity entity = world.spawnEntity(new Location(world, 0, 256, 0), type);
            WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(entity).deepClone();
            entity.remove();
            return watcher;
        }

        public void setName(String name) {
            this.name = name;

            Packet1CEntityMetadata witherMetaPacket = new Packet1CEntityMetadata();

            WrappedDataWatcher watcher = getDefaultWatcher(Bukkit.getServer().getWorlds().get(0), EntityType.WITHER);
            watcher.setObject(10, name);
            watcher.setObject(6, getHealth());

            witherMetaPacket.setEntityId(getId());
            witherMetaPacket.setEntityMetadata(watcher.getWatchableObjects());

            try {
                main.getProtocolManager().sendServerPacket(getPlayer(), witherMetaPacket.getHandle());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public void setHealth(float health) {
            this.health = health;

            Packet1CEntityMetadata witherMetaPacket = new Packet1CEntityMetadata();

            WrappedDataWatcher watcher = getDefaultWatcher(Bukkit.getServer().getWorlds().get(0), EntityType.WITHER);
            watcher.setObject(10, getName());
            watcher.setObject(6, health);

            witherMetaPacket.setEntityId(getId());
            witherMetaPacket.setEntityMetadata(watcher.getWatchableObjects());

            try {
                main.getProtocolManager().sendServerPacket(getPlayer(), witherMetaPacket.getHandle());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public void move(Location location) {
            Packet18EntityTeleport witherMovePacket = new Packet18EntityTeleport();

            witherMovePacket.setX(location.getX());
            witherMovePacket.setY(location.getY());
            witherMovePacket.setZ(location.getZ());

            witherMovePacket.setEntityID(getId());

            try {
                main.getProtocolManager().sendServerPacket(getPlayer(), witherMovePacket.getHandle());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void setupPlayer(Player p) {
        Location loc = p.getLocation();
        loc.setY(loc.getBlockY() - 100);
        FakeWither wither = new FakeWither(loc, p, ChatColor.DARK_GREEN + "Welcome to the " + ChatColor.RED + "Y23K " + ChatColor.BLUE + "Network");
        getWithers().put(p, wither);
    }

    public void setInfoBarText(String text, Player p) {
        getWithers().get(p).setName(text);
    }

    public void setInfoBarHealth(float health, Player p) {
        getWithers().get(p).setHealth(health);
    }

    public void movePlayerWither(Player p) {
        Location location = p.getLocation();
        location.setY(location.getY() - 100);
        getWithers().get(p).move(location);
    }
}
