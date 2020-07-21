package me.endergamingfilms.gateways.gateway.listeners;

import me.endergamingfilms.gateways.Gateways;
import me.endergamingfilms.gateways.gateway.Portal;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class OnKeyBlockClick implements Listener {
    private final Gateways plugin;

    public OnKeyBlockClick(@NotNull final Gateways instance) {
        this.plugin = instance;
    }

    @EventHandler
    void onBlockInteract(PlayerInteractEvent event) {
        // Only Run Code for main hand
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        // If no item in hand then return
        if ((event.getPlayer().getItemInHand().getType() == Material.AIR)) return;
        // If the item has no item meta then return
        if (event.getPlayer().getItemInHand().getItemMeta() == null) return;
        // If the item is a PortalKey
        Player player = event.getPlayer();
        PersistentDataContainer container = player.getItemInHand().getItemMeta().getPersistentDataContainer();
        if (container.has(plugin.portalManager.keyFor, PersistentDataType.STRING)) {
            // Cancel all interactions with this "PortalKey"
            event.setCancelled(true);
            if (!plugin.portalManager.getActivePortals().isEmpty()) {
                if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    Portal portal = plugin.portalManager.getPortal(container.get(plugin.portalManager.keyFor, PersistentDataType.STRING));
                    if (portal == null) return;
                    Block clickedBlock = event.getClickedBlock();
                    if (clickedBlock != null) {
                        // Check if the location clicked is the KeyBlock for the portal
                        if (Objects.equals(clickedBlock.getLocation(), portal.getKeyBlockLocation())) {
                            player.getInventory().remove(Objects.requireNonNull(event.getItem()));
                            plugin.messageUtils.send(player, plugin.respond.gatewayKeyUsed());
                            plugin.portalManager.openPortal(player, portal);
                        } else {
                            plugin.messageUtils.send(player, plugin.respond.gatewayWrongKey());
                        }
                    }
                }
            }
        }
    }
}
