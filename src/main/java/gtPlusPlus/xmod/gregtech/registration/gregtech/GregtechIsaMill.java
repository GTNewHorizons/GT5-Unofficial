package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Bus_Milling_Balls;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_Flotation_Cell;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_IsaMill;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.GT_MetaTileEntity_Hatch_MillingBalls;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IsaMill;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMTE_FrothFlotationCell;

public class GregtechIsaMill {

    public static void run() {

        Logger.INFO("Gregtech5u Content | Registering Milling Content.");

        GregtechItemList.Controller_IsaMill.set(
            new GregtechMetaTileEntity_IsaMill(
                Controller_IsaMill.ID,
                "gtpp.multimachine.isamill",
                "IsaMill Grinding Machine").getStackForm(1L));
        GregtechItemList.Controller_Flotation_Cell.set(
            new GregtechMTE_FrothFlotationCell(
                Controller_Flotation_Cell.ID,
                "gtpp.multimachine.flotationcell",
                "Flotation Cell Regulator").getStackForm(1L));

        // Milling Ball Bus
        GregtechItemList.Bus_Milling_Balls.set(
            (new GT_MetaTileEntity_Hatch_MillingBalls(Bus_Milling_Balls.ID, "hatch.milling", "Ball Housing"))
                .getStackForm(1L));
    }
}
