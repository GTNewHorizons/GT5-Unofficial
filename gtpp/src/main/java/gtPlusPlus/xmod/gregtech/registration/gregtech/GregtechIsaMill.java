package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.GT_MetaTileEntity_Hatch_MillingBalls;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IsaMill;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMTE_FrothFlotationCell;

public class GregtechIsaMill {

    public static void run() {

        Logger.INFO("Gregtech5u Content | Registering Milling Content.");

        GregtechItemList.Controller_IsaMill.set(
            new GregtechMetaTileEntity_IsaMill(31027, "gtpp.multimachine.isamill", "IsaMill Grinding Machine")
                .getStackForm(1L));
        GregtechItemList.Controller_Flotation_Cell.set(
            new GregtechMTE_FrothFlotationCell(31028, "gtpp.multimachine.flotationcell", "Flotation Cell Regulator")
                .getStackForm(1L));

        // Milling Ball Bus
        GregtechItemList.Bus_Milling_Balls
            .set((new GT_MetaTileEntity_Hatch_MillingBalls(31029, "hatch.milling", "Ball Housing")).getStackForm(1L));
    }
}
