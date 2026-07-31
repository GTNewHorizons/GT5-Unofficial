package gregtech.common.tileentities.machines.multi.nanochip.factory;

import net.minecraftforge.event.world.WorldEvent;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.factory.standard.StandardFactoryGrid;

@EventBusSubscriber
public class VacuumFactoryGrid
    extends StandardFactoryGrid<VacuumFactoryGrid, VacuumFactoryElement, VacuumFactoryNetwork> {

    public static final VacuumFactoryGrid INSTANCE = new VacuumFactoryGrid();

    @Override
    protected VacuumFactoryNetwork createNetwork() {
        return new VacuumFactoryNetwork();
    }

    @SubscribeEvent
    public static void onServerClosed(WorldEvent.Unload event) {
        if (event.world.provider.dimensionId != 0) return;
        if (event.world.isRemote) return;

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
