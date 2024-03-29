package me.endergamingfilms.gateways.gateway.listeners;

import me.endergamingfilms.gateways.Gateways;
import org.bukkit.Location;
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

public class OnSelectionToolUse implements Listener {
    private final Gateways plugin;

    public OnSelectionToolUse(@NotNull final Gateways instance) {
        this.plugin = instance;
    }

    @EventHandler
    void onBlockInteract(PlayerInteractEvent event) {
        // Only Run Code for main hand
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        // If no item in hand then return
        Player player = event.getPlayer();
        if ((player.getItemInHand().getType() == Material.AIR)) return;
        // If the item has no item meta then return
        if (player.getItemInHand().getItemMeta() == null) return;
        // Only Check Right Hand (main hand)
        PersistentDataContainer container = player.getItemInHand().getItemMeta().getPersistentDataContainer();
        // Check if held item is the selectionTool
        if (container.has(plugin.portalManager.selectionHandler.key, PersistentDataType.STRING)) {
            // Always cancel the event for the selection tool
            event.setCancelled(true);
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Block clickedBlock = event.getClickedBlock();
                if (clickedBlock != null) {
                    // Return if there are currently no creations going on
                    if (plugin.portalManager.selectionHandler.getCreationMap().isEmpty()) return;
                    plugin.portalManager.selectionHandler.getCreationMap().forEach((k, v) -> {
                        if (player.getUniqueId() == k) {
                            if (v.getPos1() == null) {
                                v.setPos1(clickedBlock.getLocation());
                                plugin.messageUtils.send(player, plugin.messageUtils.format("&7Please select pos2"));
                            } else if (v.getPos2() == null) {
                                v.setPos2(clickedBlock.getLocation());
                                plugin.messageUtils.send(player, plugin.messageUtils.format("&7Please select keyBlock"));
                            } else if (v.getKeyBlockLocation() == null) {
                                v.setKeyBlockLocation(clickedBlock.getLocation());
                                plugin.messageUtils.send(player, plugin.messageUtils.format("&7Please select destination (left-click)"));
                            }
                        }
                    });
                }
            } else {
                // Set destination with left-click
                plugin.portalManager.selectionHandler.getCreationMap().forEach((k, v) -> {
                    if (player.getUniqueId() == k) {
                        if (v.getKeyBlockLocation() == null) return; // Don't run unless ready
                        if (v.getDestination() == null) {
                            Location playerLoc = event.getPlayer().getLocation();
                            Location loc = new Location(event.getPlayer().getWorld(),
                                    playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(),
                                    playerLoc.getYaw(), playerLoc.getPitch());
                            v.setDestination(loc);
                        }
                    }
                });
            }
            // Check if portal is complete take tool
            if (plugin.portalManager.selectionHandler.getCreationMap().get(player.getUniqueId()).isComplete()) {
                plugin.portalManager.createPortal(event.getPlayer());
            }
        }
    }
}
