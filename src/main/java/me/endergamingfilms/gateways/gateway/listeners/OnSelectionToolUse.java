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

import java.util.Objects;

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
        if (Objects.equals(container.get(plugin.portalManager.selectionHandler.key, PersistentDataType.STRING),
                Objects.requireNonNull(plugin.portalManager.selectionHandler.getSelectionTool().getItemMeta())
                        .getPersistentDataContainer().get(plugin.portalManager.selectionHandler.key, PersistentDataType.STRING))) {
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
                                v.setPos1(clickedBlock);
                                plugin.messageUtils.send(player, plugin.messageUtils.format("&7Please select pos2"));
                            } else if (v.getPos2() == null) {
                                v.setPos2(clickedBlock);
                                plugin.messageUtils.send(player, plugin.messageUtils.format("&7Please select keyBlock"));
                            } else if (v.getKeyBlock() == null) {
                                v.setKeyBlock(clickedBlock);
                                plugin.messageUtils.send(player, plugin.messageUtils.format("&7Please select destination"));
                            } else if (v.getDestination() == null) {
                                clickedBlock.getLocation().setY(clickedBlock.getLocation().getY() + 1);
                                v.setDestination(clickedBlock);
                            }
                        }
                    });
                    // Check if portal is complete take tool
                    if (plugin.portalManager.selectionHandler.getCreationMap().get(player.getUniqueId()).isComplete()) {
                        Portal temp = plugin.portalManager.selectionHandler.getCreationMap().get(player.getUniqueId());
                        // Take selection tool
                        plugin.portalManager.selectionHandler.takeSelectionTool(player);
                        // Remove blocks at pos1 & pos2
                        temp.getPos1().getBlock().setType(Material.AIR);
                        temp.getPos2().getBlock().setType(Material.AIR);
                        // Add newly created portal
                        plugin.portalManager.addPortal(temp);
                        plugin.cmiHook.createCMIPortal(temp);
                        // Remove player from creation maps
                        plugin.portalManager.selectionHandler.getCreationMap().remove(player.getUniqueId());
                        plugin.portalManager.selectionHandler.getCreationTasks().remove(player.getUniqueId());
                        // Send success message
                        plugin.messageUtils.send(player, plugin.messageUtils.format("&7Portal has been created!"));
                    }
                }
            }
        }
    }
}
