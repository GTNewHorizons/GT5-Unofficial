package gregtech.common.misc;

import static gregtech.common.misc.WirelessNetworkManager.ticks_between_energy_addition;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.api.metatileentity.implementations.MTEWirelessEnergy;

@EventBusSubscriber
public class WirelessEnergyHatchManager {

    private static final ArrayList<WeakReference<MTEWirelessEnergy>> HATCHES = new ArrayList<>();
    private static long tickTimer = 0;

    public static void addHatch(MTEWirelessEnergy e) {
        HATCHES.add(new WeakReference<>(e));
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (++tickTimer % ticks_between_energy_addition != 0) return;
        Iterator<WeakReference<MTEWirelessEnergy>> iterator = HATCHES.iterator();
        while (iterator.hasNext()) {
            MTEWirelessEnergy e = iterator.next()
                .get();
            if (e == null || !e.isValid()) {
                iterator.remove();
                continue;
            }
            e.tryFetchingEnergy();
        }
    }
}
