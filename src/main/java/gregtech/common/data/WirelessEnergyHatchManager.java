package gregtech.common.data;

import static gregtech.common.misc.WirelessNetworkManager.ticks_between_energy_addition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.api.metatileentity.implementations.MTEWirelessEnergy;

public final class WirelessEnergyHatchManager {

    private final List<MTEWirelessEnergy> hatches = new ArrayList<>();
    private long tick = 0;

    public WirelessEnergyHatchManager() {}

    public void addHatch(MTEWirelessEnergy e) {
        hatches.add(e);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        tick++;
        // This is set up in a way to be as optimised as possible. If a user has a relatively plentiful energy
        // network
        // it should make no difference to them. Minimising the number of operations on BigInteger is essential.
        // Every ticks_between_energy_addition add eu_transferred_per_operation to internal EU storage from network.
        if (tick % ticks_between_energy_addition != 0L) return;
        Iterator<MTEWirelessEnergy> it = hatches.iterator();
        while (it.hasNext()) {
            MTEWirelessEnergy hatch = it.next();
            if (!hatch.isValid()) {
                it.remove();
                continue;
            }
            hatch.tryFetchingEnergy();
        }
    }
}
