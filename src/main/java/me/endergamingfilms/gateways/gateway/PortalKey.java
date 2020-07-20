package me.endergamingfilms.gateways.gateway;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PortalKey {
    private Portal portal;
    private ItemStack keyItem;
    private ItemMeta keyMeta;

    public Portal getPortal() {
        return portal;
    }

    public ItemStack getKeyItem() {
        return keyItem;
    }

    public ItemMeta getKeyMeta() {
        return keyMeta;
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }

    public void setKeyItem(ItemStack keyItem) {
        this.keyItem = keyItem;
    }

    public void setKeyMeta(ItemMeta keyMeta) {
        this.keyMeta = keyMeta;
    }
}
