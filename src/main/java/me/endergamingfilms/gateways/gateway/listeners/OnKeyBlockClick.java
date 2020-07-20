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
import org.jetbrains.annotations.NotNull;

import java.util.Map;

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
        Player player = event.getPlayer();
        if ((player.getItemInHand().getType() != Material.FEATHER)) return;
        // If the item has no item meta then return
//        if (player.getItemInHand().getItemMeta() == null) return

        System.out.println("arePortalsEmpty? " + plugin.portalManager.getActivePortals().isEmpty());
        if (!plugin.portalManager.getActivePortals().isEmpty()) {
            System.out.println("rightclick? " + event.getAction().equals(Action.RIGHT_CLICK_BLOCK));
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                for (Map.Entry<String, Portal> entry : plugin.portalManager.getActivePortals().entrySet()) {
                    String name = entry.getKey();
                    Portal portal = entry.getValue();
                    Block clickedBlock = event.getClickedBlock();
                    System.out.println("portalName: " + name);
                    if (clickedBlock != null) {
                        System.out.println("---->isKeyBlock? " + clickedBlock.getLocation().equals(portal.getKeyBlockLocation()));
                        if (clickedBlock.getLocation().equals(portal.getKeyBlockLocation()))
                            plugin.portalManager.openPortal(player, name);
                    }
                }
            }
        }
    }
}
