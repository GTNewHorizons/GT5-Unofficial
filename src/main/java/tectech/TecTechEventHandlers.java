package tectech;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import tectech.mechanics.tesla.ITeslaConnectable.TeslaUtil;

public class TecTechEventHandlers {

    public static void init() {
        FMLCommonHandler.instance()
            .bus()
            .register(new TecTechEventHandlers());
    }

    @SubscribeEvent
    public void onServerTickEnd(ServerTickEvent event) {
        if (event.phase == Phase.END) {
            TeslaUtil.housekeep();
        }
    }
}
