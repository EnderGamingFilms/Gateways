package me.endergamingfilms.gateways.gateway;

import com.Zrips.CMI.Modules.Portals.CMIPortal;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class Portal {
    private World world;
    private Location pos1;
    private Location pos2;
    private Location destination;
    private Location keyBlock;
    private final String portalName;
    private boolean isOpen;
    private CMIPortal cmiPortal;
    private String customName;
    private ItemStack tempKeyItem;

    public Portal(final String portalName, World world) {
        this.isOpen = false;
        this.world = world;
        this.portalName = portalName;
    }

    public World getWorld() {
        return this.world;
    }

    public String getPortalName() {
        return this.portalName;
    }

    public Location getPos1() {
        return this.pos1;
    }

    public Location getPos2() {
        return this.pos2;
    }

//    public Location getKeyBlock() {
//        return this.keyBlock;
//    }

    public Location getKeyBlockLocation() {
        return this.keyBlock;
    }

    public Location getDestination() {
        return this.destination;
    }

    public CMIPortal getCmiPortal() {
        return cmiPortal;
    }

    public String getCustomName() {
        return customName;
    }

    public boolean isOpened() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void setPos1(final Location pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(final Location pos2) {
        this.pos2 = pos2;
    }

    public void setKeyBlock(final Location keyBlock) {
        this.keyBlock = keyBlock;
    }

    public void setDestination(final Location destination) {
        this.destination = destination;
    }

    public void setCmiPortal(CMIPortal cmiPortal) {
        this.cmiPortal = cmiPortal;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public boolean isComplete() {
        if (pos1 == null || pos2 == null || keyBlock == null || destination == null) return false;
        return true;
    }

    public void setTempKeyItem(ItemStack keyItem) {
        this.tempKeyItem = keyItem;
    }

    public ItemStack getTempKeyItem() {
        return this.tempKeyItem;
    }
}
