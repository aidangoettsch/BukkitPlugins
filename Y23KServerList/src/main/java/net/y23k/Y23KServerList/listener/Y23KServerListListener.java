package net.y23k.Y23KServerList.listener;

import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.y23k.Y23KServerList.Main;

/**
 * Class created by yayes2 on 12/12/13.
 */
@RequiredArgsConstructor
public class Y23KServerListListener implements Listener {
    private final Main main;

    @EventHandler
    public void onProxyPing(ProxyPingEvent e) {
        ProxyServer proxy = main.getProxy();
        // Use the hostname used for pinging
        String forcedHost = "";
        ServerInfo forcedHostInfo = AbstractReconnectHandler.getForcedHost(e.getConnection());
        if (forcedHostInfo != null) {
            forcedHost = forcedHostInfo.getName();
        } else if (e.getConnection().getVirtualHost() != null) {
            // Use the hostname used for pinging
            forcedHost = e.getConnection().getVirtualHost().getHostString();
        }
        String motd;
        if (forcedHost != "y23k.net") {
            motd = "§3=====§5[§4Y23K §1Network§5]§3===== §r                              §cNew IP! y23k.net! If you were banned, you are no longer banned!";
        } else {
            motd = "§3=====§5[§4Y23K §1Network&5]§3===== §r                              §cIf you were banned, you are no longer banned!";
        }
        ServerPing ping = e.getResponse();
        ping.setDescription(motd);
        ServerPing.PlayerInfo[] fakePlayers = new ServerPing.PlayerInfo[0];
        fakePlayers[0] = new ServerPing.PlayerInfo(ChatColor.RED + "" + ChatColor.UNDERLINE + "Welcome to the Y23K " + ChatColor.BLUE + "Network" + ChatColor.RED + "!", "1");
        ping.getPlayers().setSample(fakePlayers);
        e.setResponse(ping);
    }
}
