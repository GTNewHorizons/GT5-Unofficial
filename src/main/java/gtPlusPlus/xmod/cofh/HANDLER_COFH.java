package gtPlusPlus.xmod.cofh;

import static gregtech.api.enums.Mods.COFHCore;

import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.general.RF2EU_Battery;

public class HANDLER_COFH {

    public static void initItems() {
        if (COFHCore.isModLoaded()) {
            ModItems.RfEuBattery = new RF2EU_Battery();
        }
    }
}
