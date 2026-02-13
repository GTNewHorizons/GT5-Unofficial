package gregtech.common.misc;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.api.metatileentity.implementations.MTEWirelessEnergy;

import java.util.ArrayList;
import java.util.Iterator;

import static gregtech.common.misc.WirelessNetworkManager.ticks_between_energy_addition;

@EventBusSubscriber
public class WirelessEnergyHatchManager {

    private static final ArrayList<MTEWirelessEnergy> HATCHES = new ArrayList<>();
    private static long tickTimer = 0;

    public static void addHatch(MTEWirelessEnergy e) {
        HATCHES.add(e);
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (++tickTimer % ticks_between_energy_addition != 0) return;
        Iterator<MTEWirelessEnergy> iterator = HATCHES.iterator();
        while (iterator.hasNext()) {
            MTEWirelessEnergy e = iterator.next();
            if (!e.isValid()) iterator.remove();
            e.tryFetchingEnergy();
        }
    }
}
