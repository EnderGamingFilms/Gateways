package me.endergamingfilms.gateways.integrations;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Particl.CMIEffectManager;
import com.Zrips.CMI.Modules.Portals.CMIPortal;
import com.Zrips.CMI.Modules.Portals.CuboidArea;
import com.Zrips.CMI.Modules.Portals.PortalManager;
import me.endergamingfilms.gateways.Gateways;
import me.endergamingfilms.gateways.gateway.Portal;
import org.jetbrains.annotations.NotNull;

public class CMIHook {
    private final Gateways plugin;
    public CMI cmiInstance;
    public PortalManager portalModule;

    public CMIHook(@NotNull final Gateways instance) {
        this.plugin = instance;
    }

    public void setup() {
        cmiInstance = CMI.getInstance();
        portalModule = CMI.getInstance().getPortalManager();
    }

    public void createCMIPortal(Portal portal) {
        // If the portal doesn't exist then create it
        if (portalModule.getByName(portal.getPortalName()) == null) {
            CMIPortal cmiPortal = new CMIPortal();
            CuboidArea portalBounds = new CuboidArea(portal.getPos1(), portal.getPos2());
            cmiPortal.setArea(portalBounds);
            cmiPortal.setName(portal.getPortalName());
            cmiPortal.setActivationRange(15);
            cmiPortal.setTpLoc(portal.getDestination());
            cmiPortal.setWorld(portal.getWorld());
            // Set portal particles
            if (portal.getPortalParticles() != null) {
                cmiPortal.setEffect(CMIEffectManager.CMIParticle.getCMIParticle(portal.getPortalParticles()));
            } else {
                portal.setPortalParticles(cmiPortal.getEffect().getName());
            }
            // Portal is disabled by default
            cmiPortal.setEnabled(false);
            // Add the CMIPortal to Portal object
            portal.setCmiPortal(cmiPortal);
            portal.setParticleAmount(cmiPortal.getParticleAmount());
            // Register the portal with CMI
            portalModule.addPortal(cmiPortal);
            portalModule.savePortals();
        } else {
            updateCMIPortal(portal);
        }
    }

    public void updateCMIPortal(Portal portal) {
        CMIEffectManager.CMIParticle particle = CMIEffectManager.CMIParticle.getCMIParticle(
                portal.getPortalParticles() != null ? portal.getPortalParticles() : "reddust");
        portalModule.getByName(portal.getPortalName()).setEffect(particle);
        portalModule.getByName(portal.getPortalName()).setParticleAmount(portal.getParticleAmount() != 0 ? portal.getParticleAmount() : 20);
        portalModule.getByName(portal.getPortalName()).setEnabled(false);
        portal.setCmiPortal(portalModule.getByName(portal.getPortalName()));
        portalModule.savePortals();
    }
}
