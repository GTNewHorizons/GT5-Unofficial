package tectech.mechanics.boseEinsteinCondensate;

import net.minecraftforge.event.world.WorldEvent;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.factory.standard.StandardFactoryGrid;

@EventBusSubscriber
public class BECFactoryGrid extends StandardFactoryGrid<BECFactoryGrid, BECFactoryElement, BECFactoryNetwork> {

    public static final BECFactoryGrid INSTANCE = new BECFactoryGrid();

    @Override
    protected BECFactoryNetwork createNetwork() {
        return new BECFactoryNetwork();
    }

    @SubscribeEvent
    public static void onServerClosed(WorldEvent.Unload event) {
        if (event.world.provider.dimensionId != 0) return;
        if (event.world.isRemote) return;

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
