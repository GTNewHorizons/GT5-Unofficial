package tectech.mechanics.boseEinsteinCondensate;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.factory.standard.StandardFactoryGrid;

@EventBusSubscriber
public class BECFactoryGrid extends StandardFactoryGrid<BECFactoryGrid, BECFactoryElement, BECFactoryNetwork> {

    public static final BECFactoryGrid INSTANCE = new BECFactoryGrid();

    @Override
    protected BECFactoryNetwork createNetwork() {
        return new BECFactoryNetwork();
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.side != Side.SERVER) return;

        for (BECFactoryNetwork network : INSTANCE.networks) {
            network.onPostTick();
        }
    }
}
