package me.endergamingfilms.gateways.gateway;

import me.endergamingfilms.gateways.Gateways;
import me.endergamingfilms.gateways.gateway.listeners.OnKeyBlockClick;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PortalManager {
    private final Gateways plugin;
    private final Map<String, Portal> activePortals = new HashMap<>();
    private final Map<String, PortalKey> portalKeys = new HashMap<>();
    public final SelectionHandler selectionHandler;

    public PortalManager(@NotNull final Gateways instance) {
        this.plugin = instance;
        this.selectionHandler = new SelectionHandler(instance);
        instance.getServer().getPluginManager().registerEvents(new OnKeyBlockClick(instance), instance);
    }

    public void addPortal(Portal portal) {
        this.activePortals.put(portal.getPortalName(), portal);
    }

    public Map<String, Portal> getActivePortals() {
        return this.activePortals;
    }

    public void addPortalKey(String portalName, PortalKey key) {
        this.portalKeys.put(portalName, key);
    }

    public Map<String, PortalKey> getPortalKeys() {
        return this.portalKeys;
    }

    public void beginPortalCreation(Player player, final String[] args) {
        // Check if player is already creating a portal
        if (selectionHandler.getCreationTasks().containsKey(player.getUniqueId())) {
            plugin.messageUtils.send(player, plugin.messageUtils.format("&cYou are already creating a gateway!"));
            return;
        }
        String passedName = "gateway_" + args[1];
        // Check if portal with the same name already exists
        if (!activePortals.isEmpty()) {
            for (Map.Entry<String, Portal> entry : activePortals.entrySet()) {
                String name = entry.getKey();
                if (name.equalsIgnoreCase(passedName) || plugin.cmiHook.portalModule.getByName(passedName) != null) {
                    plugin.messageUtils.send(player, plugin.respond.gatewayExists());
                    return;
                }
            }
        }
        // Start portal creation process
        selectionHandler.startSelection(player, args);
    }

    public void openPortal(Player player, String portalName) {
        Portal portal = activePortals.get(portalName);
        int onTime = Math.max(plugin.fileManager.defaultPortalOnTime, 1);
        // Set portal on time
        // TODO

        // Check if the portal is already open
        if (!portal.isOpened()) {
            if (plugin.cmiHook.portalModule.getByName(portalName) == null) return;
            // Announce portal is opening
            plugin.messageUtils.send(plugin.getServer(), plugin.respond.gatewayOpeningMessage(portal.getCustomName()));
            // Put time-limit on open portal
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.cmiHook.portalModule.getByName(portalName).setEnabled(true);
                    portal.setIsOpen(true);
                    plugin.messageUtils.send(plugin.getServer(), plugin.respond.gatewayOpened(portal.getCustomName()));
                }
            }, 20L * 30);

            // Put time-limit on open portal
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    closePortal(portalName);
                }
            }, 20L * 60 * onTime);
        } else
            plugin.messageUtils.send(player, plugin.respond.gatewayAlreadyOpen());
    }

    public void closePortal(String portalName) {
        Portal portal = activePortals.get(portalName);
        // Check if the portal is already open
        if (portal.isOpened()) {
            plugin.cmiHook.portalModule.getByName(portalName).setEnabled(false);
            portal.setIsOpen(false);
            plugin.messageUtils.send(plugin.getServer(), plugin.respond.gatewayClosed(portal.getCustomName()));
        }
    }

    public void removePortal(String portalName) {
        // Remove portal from active portals and CMI
        activePortals.remove(portalName);
        if (plugin.cmiHook.portalModule.getPortals().containsKey(portalName))
            plugin.cmiHook.portalModule.removePortal(plugin.cmiHook.portalModule.getByName(portalName));
    }

    public Portal getPortal(String str) {
        return activePortals.get(str);
    }
}
