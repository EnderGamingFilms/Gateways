package me.endergamingfilms.gateways.gateway;

import com.Zrips.CMI.Modules.Portals.CMIPortal;
import com.Zrips.CMI.Modules.Portals.CuboidArea;
import me.endergamingfilms.gateways.Gateways;
import me.endergamingfilms.gateways.gateway.listeners.OnKeyBlockClick;
import me.endergamingfilms.gateways.gateway.listeners.OnSelectionToolUse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PortalManager {
    private final Gateways plugin;
    private Map<String,Portal> activePortals = new HashMap<>();
    public final SelectionHandler selectionHandler;

    public PortalManager(@NotNull final Gateways instance) {
        this.plugin = instance;
        this.selectionHandler = new SelectionHandler(instance);
        instance.getServer().getPluginManager().registerEvents(new OnKeyBlockClick(instance), instance);
    }

    public void addPortal(Portal portal) {
        this.activePortals.put(portal.getPortalName(), portal);
    }

    public Map<String,Portal> getActivePortals() {
        return this.activePortals;
    }

    public void beginPortalCreation(Player player, final String portalName) {
        // Check if portal with the same name already exists
        if (!activePortals.isEmpty()) {
            for (Map.Entry<String, Portal> entry : activePortals.entrySet()) {
                String name = entry.getKey();
                if (name.equalsIgnoreCase(portalName)) {
                    plugin.messageUtils.send(player, plugin.messageUtils.format("&cA portal with that name already exists."));
                    return;
                }
            }
        }

        selectionHandler.startSelection(player, portalName);
    }

    public void openPortal(Player player, String portalName) {
        Portal portal = activePortals.get(portalName);
        // Check if the portal is already open
        if (!portal.isOpened()) {
            if (plugin.cmiHook.portalModule.getByName(portalName) == null) return;

            plugin.cmiHook.portalModule.getByName(portalName).setEnabled(true);
            portal.setIsOpen(true);
            plugin.messageUtils.send(player, plugin.messageUtils.format("&7This portal is now open!"));

            // Put time-limit on open portal
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    closePortal(player, portalName);
                }
            }, 20*20L);
        } else {
            plugin.messageUtils.send(player, plugin.messageUtils.format("&7This portal is already open!"));
        }
    }

    public void closePortal(Player player, String portalName) {
        Portal portal = activePortals.get(portalName);
        // Check if the portal is already open
        if (portal.isOpened()) {
            plugin.cmiHook.portalModule.getByName(portalName).setEnabled(false);
            portal.setIsOpen(false);
            plugin.messageUtils.send(player, plugin.messageUtils.format("&7This portal is now closed!"));
        } else {
            plugin.messageUtils.send(player, plugin.messageUtils.format("&7This portal is already closed!"));
        }
    }
}
