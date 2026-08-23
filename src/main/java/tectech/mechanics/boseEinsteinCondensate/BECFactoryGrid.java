package tectech.mechanics.boseEinsteinCondensate;

import gregtech.GTMod;
import gregtech.api.factory.standard.StandardFactoryGrid;

public class BECFactoryGrid extends StandardFactoryGrid<BECFactoryGrid, BECFactoryElement, BECFactoryNetwork> {

    public static final BECFactoryGrid INSTANCE = new BECFactoryGrid();

    @Override
    protected BECFactoryNetwork createNetwork() {
        return new BECFactoryNetwork();
    }

    public static void onServerClosed() {
        if (!INSTANCE.networks.isEmpty()) {
            GTMod.GT_FML_LOGGER.warn(
                "BECFactoryGrid had networks that weren't removed before the server stopped: this could indicate a memory leak.");
        }

        if (!INSTANCE.vertices.isEmpty()) {
            GTMod.GT_FML_LOGGER.warn(
                "BECFactoryGrid had vertices that weren't removed before the server stopped: this could indicate a memory leak.");
        }

        // Make sure everything is unloaded, even if something didn't remove itself properly

        INSTANCE.networks.forEach(network -> {
            network.elements.forEach(element -> { element.setNetwork(null); });

            network.elements.clear();
            network.components.clear();
            network.routeTracker.clear();
        });

        INSTANCE.networks.clear();
        INSTANCE.edges.clear();
        INSTANCE.vertices.clear();
    }
}
