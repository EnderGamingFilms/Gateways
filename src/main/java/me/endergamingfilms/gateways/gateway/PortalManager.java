package me.endergamingfilms.gateways.gateway;

import me.endergamingfilms.gateways.Gateways;
import me.endergamingfilms.gateways.gateway.listeners.OnKeyBlockClick;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PortalManager {
    private final Gateways plugin;
    private final Map<String, Portal> activePortals = new HashMap<>();
    public final SelectionHandler selectionHandler;
    public final NamespacedKey keyFor;

    public PortalManager(@NotNull final Gateways instance) {
        this.plugin = instance;
        this.selectionHandler = new SelectionHandler(instance);
        this.keyFor = new NamespacedKey(instance, "keyFor");
        instance.getServer().getPluginManager().registerEvents(new OnKeyBlockClick(instance), instance);
    }

    public void addPortal(Portal portal) {
        this.activePortals.put(portal.getPortalName(), portal);
    }

    public Map<String, Portal> getActivePortals() {
        return this.activePortals;
    }

    public void beginPortalCreation(Player player, final String[] args) {
        // Check if player is already creating a portal
        if (selectionHandler.getCreationTasks().containsKey(player.getUniqueId())) {
            plugin.messageUtils.send(player, plugin.messageUtils.format("&cYou are already creating a gateway!"));
            return;
        }
        // Check if the player is holding an item (to be used as a PortalKey)
        if (player.getItemInHand().getType() == Material.AIR) {
            plugin.messageUtils.send(player, plugin.messageUtils.format("&cYou must hold an item to be used as a PortalKey!"));
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
        selectionHandler.startSelection(player, args, player.getItemInHand());
    }

    public void openPortal(Player player, Portal portal) {
        int onTime = Math.max(plugin.fileManager.defaultPortalOnTime, portal.getKeepAlive());
        // Set portal on time
        // TODO

        // Check if the portal is already open
        if (!portal.isOpened()) {
            if (plugin.cmiHook.portalModule.getByName(portal.getPortalName()) == null) return;
            // Announce portal is opening
            plugin.messageUtils.send(plugin.getServer(), plugin.respond.gatewayOpeningMessage(portal.getCustomName()));
            // Put time-limit on open portal
            portal.setIsOpen(true); // Make sure multiple keys aren't used
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.cmiHook.portalModule.getByName(portal.getPortalName()).setEnabled(true);
                    plugin.messageUtils.send(plugin.getServer(), plugin.respond.gatewayOpened(portal.getCustomName()));
                }
            }, 20L * 30);

            // Put time-limit on open portal
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    closePortal(portal.getPortalName());
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
        return this.activePortals.get(str);
    }

    public PortalKey createKey(Portal portal, ItemStack item) {
        // Create portal key
        PortalKey portalKey = new PortalKey();
        portalKey.setPortal(portal.getPortalName());
        ItemStack newItem = new ItemStack(item);
        ItemMeta itemMeta = newItem.getItemMeta();
        // Add custom data & flags
        itemMeta.getPersistentDataContainer().set(keyFor, PersistentDataType.STRING, portal.getPortalName());
        itemMeta.setUnbreakable(true);
        itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        newItem.setItemMeta(itemMeta);
        // Set the new item to the PortalKey
        portalKey.setKeyItem(newItem);
        return portalKey;
    }

    public void createPortal(Player player) {
        Portal temp = plugin.portalManager.selectionHandler.getCreationMap().get(player.getUniqueId());
        // Take selection tool
        selectionHandler.takeSelectionTool(player);
        // Remove blocks at pos1 & pos2
        temp.getPos1().getBlock().setType(Material.AIR);
        temp.getPos2().getBlock().setType(Material.AIR);
        // Add newly created portal
        addPortal(temp);
        plugin.cmiHook.createCMIPortal(temp);
        // Remove player from creation maps
        selectionHandler.getCreationMap().remove(player.getUniqueId());
        selectionHandler.getCreationTasks().remove(player.getUniqueId());
        // Send success message
        plugin.messageUtils.send(player, plugin.messageUtils.format("&7Portal has been created!"));
    }
}
