package me.endergamingfilms.gateways.gateway;

import me.endergamingfilms.gateways.Gateways;
import me.endergamingfilms.gateways.gateway.listeners.OnSelectionToolUse;
import me.endergamingfilms.gateways.utils.FileManager;
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
    private ItemStack selectionTool;

    public SelectionHandler(@NotNull final Gateways instance) {
        this.plugin = instance;
        this.key = new NamespacedKey(instance, "selectionTool");

        instance.getServer().getPluginManager().registerEvents(new OnSelectionToolUse(instance), instance);
        makeSelectionTool();
    }

    public void makeSelectionTool() {
        Material toolMat = Material.getMaterial(plugin.messageUtils.grabConfig("SelectionTool.type", FileManager.STRING));
        String matName = plugin.messageUtils.grabConfig("SelectionTool.name", FileManager.STRING);
        List<String> matLore = (List<String>) plugin.messageUtils.grabConfig("SelectionTool.lore", FileManager.LIST);

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

    public void startSelection(Player player, final String portal) {
        creationMap.put(player.getUniqueId(), new Portal(portal, player.getWorld()));
        giveSelectionTool(player);
        plugin.messageUtils.send(player, plugin.messageUtils.format("&7Please select pos1"));
        // Put time-limit on portal creation
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (player.getInventory().contains(selectionTool)) {
                    // Take selection tool after 20 seconds
                    plugin.portalManager.selectionHandler.takeSelectionTool(player);
                    // Clear creationMap
                    creationMap.remove(player.getUniqueId());
                    // Send failed message
                    plugin.messageUtils.send(player, plugin.messageUtils.format("&cCreation was cancelled for taking too long."));
                }
            }
        }, 20*20L);
    }

    public void takeSelectionTool(Player player) {
        player.getInventory().remove(selectionTool);
    }

    public Map<UUID, Portal> getCreationMap() {
        return creationMap;
    }
}
