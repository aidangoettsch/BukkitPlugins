package net.y23k.Y23KServerList;

import net.md_5.bungee.api.plugin.Plugin;
import net.y23k.Y23KServerList.listener.Y23KServerListListener;

/**
 * Class created by yayes2 on 12/12/13.
 */
public class Main extends Plugin {
    @Override
    public void onEnable() {
        this.getProxy().getPluginManager().registerListener(this, new Y23KServerListListener(this));
    }
}
