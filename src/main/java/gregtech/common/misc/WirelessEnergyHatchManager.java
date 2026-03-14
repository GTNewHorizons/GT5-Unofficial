package gregtech.common.misc;

import static gregtech.common.misc.WirelessNetworkManager.ticks_between_energy_addition;

import java.util.ArrayList;
import java.util.Iterator;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEWirelessEnergy;

public class WirelessEnergyHatchManager {

    private final ArrayList<MTEWirelessEnergy> HATCHES = new ArrayList<>();
    private long tickTimer = 0;

    public void addHatch(MTEWirelessEnergy e) {
        HATCHES.add(e);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (++tickTimer % ticks_between_energy_addition != 0) return;
        Iterator<MTEWirelessEnergy> iterator = HATCHES.iterator();
        while (iterator.hasNext()) {
            MTEWirelessEnergy e = iterator.next();
            if (e == null || !e.isValid()) {
                iterator.remove();
                continue;
            }
            BaseMetaTileEntity base = (BaseMetaTileEntity) e.getBaseMetaTileEntity();
            if (!base.isTickDisabled()) {
                iterator.remove();
                continue;
            }
            e.tryFetchingEnergy();
        }
    }
}
