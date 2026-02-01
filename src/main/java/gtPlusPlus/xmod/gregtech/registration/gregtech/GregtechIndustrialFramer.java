package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.GT_Framer_HV;
import static gregtech.api.enums.MetaTileEntityIDs.GT_Framer_LV;
import static gregtech.api.enums.MetaTileEntityIDs.GT_Framer_MV;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.MTEDrawerFramer;

public class GregtechIndustrialFramer {

    public static void run() {
        GregtechItemList.GT_Framer_LV
            .set(new MTEDrawerFramer(GT_Framer_LV.ID, "framer.tier.01", "Basic Drawer Framer", 1).getStackForm(1L));
        GregtechItemList.GT_Framer_MV
            .set(new MTEDrawerFramer(GT_Framer_MV.ID, "framer.tier.02", "Advanced Drawer Framer", 2).getStackForm(1L));
        GregtechItemList.GT_Framer_HV
            .set(new MTEDrawerFramer(GT_Framer_HV.ID, "framer.tier.03", "Precise Drawer Framer", 3).getStackForm(1L));
    }

}
