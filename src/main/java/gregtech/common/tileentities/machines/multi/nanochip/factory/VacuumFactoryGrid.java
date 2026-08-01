package gregtech.common.tileentities.machines.multi.nanochip.factory;

import gregtech.GTMod;
import gregtech.api.factory.standard.StandardFactoryGrid;

public class VacuumFactoryGrid
    extends StandardFactoryGrid<VacuumFactoryGrid, VacuumFactoryElement, VacuumFactoryNetwork> {

    public static final VacuumFactoryGrid INSTANCE = new VacuumFactoryGrid();

    @Override
    protected VacuumFactoryNetwork createNetwork() {
        return new VacuumFactoryNetwork();
    }

    public static void onServerClosed() {
        if (!INSTANCE.networks.isEmpty()) {
            GTMod.GT_FML_LOGGER.warn(
                "VacuumFactoryGrid had networks that weren't removed before the server stopped: this could indicate a memory leak.");
        }

        if (!INSTANCE.vertices.isEmpty()) {
            GTMod.GT_FML_LOGGER.warn(
                "VacuumFactoryGrid had vertices that weren't removed before the server stopped: this could indicate a memory leak.");
        }

        // Make sure everything is unloaded, even if something didn't remove itself properly

        INSTANCE.networks.forEach(network -> {
            network.elements.forEach(element -> { element.setNetwork(null); });

            network.elements.clear();
            network.components.clear();
        });

        INSTANCE.networks.clear();
        INSTANCE.edges.clear();
        INSTANCE.vertices.clear();
    }
}
