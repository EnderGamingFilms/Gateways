package me.endergamingfilms.gateways.integrations;

import com.Zrips.CMI.CMI;
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

            cmiPortal.setEnabled(false);

            portal.setCmiPortal(cmiPortal);

            portalModule.addPortal(cmiPortal);
            portalModule.savePortals();
        } else
            portalModule.getByName(portal.getPortalName()).setEnabled(false);
    }
}
