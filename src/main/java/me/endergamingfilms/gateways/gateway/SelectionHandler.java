package me.endergamingfilms.gateways.gateway;

import me.endergamingfilms.gateways.Gateways;
import me.endergamingfilms.gateways.gateway.listeners.OnHotbarSwitch;
import me.endergamingfilms.gateways.gateway.listeners.OnSelectionToolUse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SelectionHandler implements Listener {
    private final Gateways plugin;
    public final NamespacedKey key;
    private final Map<UUID, Portal> creationMap = new HashMap<>();
    private final Map<UUID, Integer> creationTasks = new HashMap<>();
    private ItemStack selectionTool;

    public SelectionHandler(@NotNull final Gateways instance) {
        this.plugin = instance;
        this.key = new NamespacedKey(instance, "selectionTool");
        // Setup Selection Tool Item
        makeSelectionTool();
        // Register SelectionToolListeners
        instance.getServer().getPluginManager().registerEvents(new OnSelectionToolUse(instance), instance);
        instance.getServer().getPluginManager().registerEvents(new OnHotbarSwitch(instance), instance);
    }

    public void makeSelectionTool() {
        Material toolMat = Material.getMaterial(plugin.fileManager.selectionToolType);
        String matName = plugin.fileManager.selectionToolName;
        List<String> matLore = plugin.fileManager.selectionToolLore;

        if (!matLore.isEmpty()) {
            List<String> colorized = new ArrayList<>();
            for (String s : matLore) {
                colorized.add(plugin.messageUtils.colorize(s));
            }
            matLore = colorized;
        }

        if (toolMat != null) {
            selectionTool = new ItemStack(toolMat);
            ItemMeta meta = selectionTool.getItemMeta();
            assert meta != null;
            meta.setDisplayName(plugin.messageUtils.colorize(matName));
            meta.setLore(matLore);
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "true");
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
            selectionTool.setItemMeta(meta);
        }
    }

    public ItemStack getSelectionTool() {
        return this.selectionTool;
    }

    public void giveSelectionTool(Player player) {
        player.getInventory().addItem(selectionTool);
    }

    public void startSelection(Player player, final String[] args, final ItemStack keyItem) {
        int cancellationTime = 60;
        String passedName = "gateway_" + args[1];
        // Create new Portal object
        Portal portal = new Portal(passedName, player.getWorld());
        // Create new PortalKey
        portal.setPortalKey(plugin.portalManager.createKey(portal, player.getItemInHand()));
        // Set default portal keepAlive (change in config)
        portal.setKeepAlive(plugin.fileManager.defaultPortalOnTime);
        // Set the Portal DisplayName
        portal.setCustomName(args[2]);
        // Add player & portal to creaitionMap
        creationMap.put(player.getUniqueId(), portal);
        // Give player the selection too & state creation process
        giveSelectionTool(player);
        plugin.messageUtils.send(player, plugin.messageUtils.format("&7Please select pos1"));
        // Put time-limit on portal creation
        int creationTaskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (player.getInventory().contains(selectionTool)) {
                    cancelCreation(player);
                    // Send timeout message
                    plugin.messageUtils.send(player, plugin.respond.gatewayCreationTimeout());
                }
            }
        }, cancellationTime * 20L);
        creationTasks.put(player.getUniqueId(), creationTaskID);
    }

    public void cancelCreation(Player player) {
        // Take selection tool
        plugin.portalManager.selectionHandler.takeSelectionTool(player);
        // Remove player from creation maps
        creationMap.remove(player.getUniqueId());
        creationTasks.remove(player.getUniqueId());
    }

    public void takeSelectionTool(Player player) {
        player.getInventory().remove(selectionTool);
    }

    public Map<UUID, Portal> getCreationMap() {
        return this.creationMap;
    }

    public Map<UUID, Integer> getCreationTasks() {
        return this.creationTasks;
    }
}
