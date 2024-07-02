package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_WaterPump;

public class GregtechWaterPump {

    public static void run() {
        // Water Pump Multiblock
        GregtechItemList.WaterPump.set(
            new GregtechMetaTileEntity_WaterPump(31085, "waterpump.controller.tier.single", "Water Pump")
                .getStackForm(1L));
    }
}
