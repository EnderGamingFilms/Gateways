package me.endergamingfilms.gateways.gateway;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PortalKey {
    private String portalName;
    private ItemStack keyItem;
    private ItemMeta keyMeta;

    public String getPortalName() {
        return portalName;
    }

    public ItemStack getKeyItem() {
        return keyItem;
    }

    public ItemMeta getKeyMeta() {
        return keyMeta;
    }

    public void setPortal(String portalName) {
        this.portalName = portalName;
    }

    public void setKeyItem(ItemStack keyItem) {
        this.keyItem = keyItem;
        this.keyMeta = keyItem.getItemMeta();
    }

    public void setKeyMeta(ItemMeta keyMeta) {
        this.keyMeta = keyMeta;
    }

    public void update() {
        this.keyItem.setItemMeta(keyMeta);
    }
}
