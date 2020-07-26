package me.endergamingfilms.gateways.integrations;

import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.endergamingfilms.gateways.Gateways;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HeadDatabaseHook implements Listener {
    private final Gateways plugin;
    public HeadDatabaseAPI hdb;

    public HeadDatabaseHook(@NotNull final Gateways instance) {
        this.plugin = instance;
    }

    public void setup() {
        hdb = new HeadDatabaseAPI();
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public String getHeadID(ItemStack item) {
        if (hdb == null) return null;
        String returned = "";
        try {
            returned = hdb.getItemID(item);
        } catch (NullPointerException ignore) {

        }
        return returned;
    }

    @EventHandler
    void onDatabaseLoad(DatabaseLoadEvent event) {
        // Load Gateways
        plugin.fileManager.readGateways();
    }
}
