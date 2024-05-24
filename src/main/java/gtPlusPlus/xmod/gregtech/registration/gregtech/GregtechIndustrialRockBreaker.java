package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_IndustrialRockBreaker;

public class GregtechIndustrialRockBreaker {

    public static void run() {
        GregtechItemList.Controller_IndustrialRockBreaker.set(
            new GregtechMetaTileEntity_IndustrialRockBreaker(
                31065,
                "industrialrockcrusher.controller.tier.single",
                "Boldarnator").getStackForm(1L));
    }
}
