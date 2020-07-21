package me.endergamingfilms.gateways.integrations;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.endergamingfilms.gateways.Gateways;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HeadDatabaseHook {
    // TODO
    private final Gateways plugin;
    public HeadDatabaseAPI hdb;

    public HeadDatabaseHook(@NotNull final Gateways instance) {
        this.plugin = instance;
    }

    public void setup() {
        hdb = new HeadDatabaseAPI();
    }

    public ItemStack parseHead(String headID) {
        ItemStack head = null;
        try {
            head = hdb.getItemHead(headID);
        } catch (NullPointerException ignored) {
        }
        return head != null ? head : new ItemStack(Material.STONE);
    }
}
