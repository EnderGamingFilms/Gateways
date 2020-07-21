package me.endergamingfilms.gateways.gateway;

import com.Zrips.CMI.Modules.Portals.CMIPortal;
import org.bukkit.Location;
import org.bukkit.World;

public class Portal {
    private final World world;
    private Location pos1;
    private Location pos2;
    private Location destination;
    private Location keyBlockLocation;
    private final String portalName;
    private boolean isOpen;
    private CMIPortal cmiPortal;
    private String customName;
    private PortalKey portalKey;
    private int keepAlive;
    private String portalParticles;
    private int particleAmount;

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

    public Location getKeyBlockLocation() {
        return this.keyBlockLocation;
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

    public PortalKey getPortalKey() {
        return this.portalKey;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public String getPortalParticles() {
        return portalParticles;
    }

    public int getParticleAmount() {
        return particleAmount;
    }

    public boolean isOpened() {
        return isOpen;
    }

    public void setIsOpen(final boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void setPos1(final Location pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(final Location pos2) {
        this.pos2 = pos2;
    }

    public void setKeyBlockLocation(final Location keyBlockLocation) {
        this.keyBlockLocation = keyBlockLocation;
    }

    public void setDestination(final Location destination) {
        this.destination = destination;
    }

    public void setCmiPortal(CMIPortal cmiPortal) {
        this.cmiPortal = cmiPortal;
    }

    public void setCustomName(final String customName) {
        this.customName = customName;
    }

    public void setPortalKey(PortalKey portalKey) {
        this.portalKey = portalKey;
    }

    public void setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
    }

    public void setPortalParticles(String portalParticles) {
        this.portalParticles = portalParticles;
    }

    public void setParticleAmount(int particleAmount) {
        this.particleAmount = particleAmount;
    }

    public boolean isComplete() {
        if (pos1 == null || pos2 == null || keyBlockLocation == null || destination == null) return false;
        return true;
    }
}
