package me.endergamingfilms.gateways.gateway;

import com.Zrips.CMI.Modules.Portals.CMIPortal;
import me.endergamingfilms.gateways.Gateways;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.print.attribute.standard.Destination;

public class Portal {
    private World world;
    private Location pos1;
    private Location pos2;
    private Location destination;
    private Block keyBlock;
    private final String portalName;
    private boolean isOpen;
    private CMIPortal cmiPortal;

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

    public Block getKeyBlock() {
        return this.keyBlock;
    }

    public Location getKeyBlockLocation() {
        return this.keyBlock.getLocation();
    }

    public Location getDestination() {
        return this.destination;
    }

    public CMIPortal getCmiPortal() {
        return cmiPortal;
    }

    public boolean isOpened() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void setPos1(final Block pos1) {
        this.pos1 = pos1.getLocation();
    }

    public void setPos2(final Block pos2) {
        this.pos2 = pos2.getLocation();
    }

    public void setKeyBlock(final Block keyBlock) {
        this.keyBlock = keyBlock;
    }

    public void setDestination(final Block destination) {
        this.destination = destination.getLocation();
    }

    public void setCmiPortal(CMIPortal cmiPortal) {
        this.cmiPortal = cmiPortal;
    }

    public boolean isComplete() {
        if (pos1 == null || pos2 == null || keyBlock == null || destination == null) return false;
        return true;
    }
}
