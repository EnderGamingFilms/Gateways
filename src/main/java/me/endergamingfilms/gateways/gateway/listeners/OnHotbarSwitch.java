package me.endergamingfilms.gateways.gateway.listeners;

import me.endergamingfilms.gateways.Gateways;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OnHotbarSwitch implements Listener {
    private final Gateways plugin;

    public OnHotbarSwitch(@NotNull final Gateways instance) {
        this.plugin = instance;
    }

    @EventHandler
    void onHotBarSwitch(PlayerItemHeldEvent event) {
        if (event.getPlayer().getInventory().getItem(event.getPreviousSlot()) == null) return;

        PersistentDataContainer container = Objects.requireNonNull(event.getPlayer().getInventory()
                .getItem(event.getPreviousSlot())).getItemMeta().getPersistentDataContainer();
        // Check if held item is the selectionTool
        if (Objects.equals(container.get(plugin.portalManager.selectionHandler.key, PersistentDataType.STRING),
                Objects.requireNonNull(plugin.portalManager.selectionHandler.getSelectionTool().getItemMeta())
                        .getPersistentDataContainer().get(plugin.portalManager.selectionHandler.key, PersistentDataType.STRING))) {
            // Begin creation cancellation
            int taskID = plugin.portalManager.selectionHandler.getCreationTasks().get(event.getPlayer().getUniqueId());
            plugin.portalManager.selectionHandler.cancelCreation(event.getPlayer());
            // Cancel runnable task
            plugin.getServer().getScheduler().cancelTask(taskID);
            // Send message
            plugin.messageUtils.send(event.getPlayer(), plugin.messageUtils.format("&cGateway creation has been cancelled."));
        }
    }
}
