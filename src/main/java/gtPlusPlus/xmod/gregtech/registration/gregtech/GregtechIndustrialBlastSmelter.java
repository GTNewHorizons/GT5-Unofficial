package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_AlloyBlastSmelter;
import static gregtech.api.enums.MetaTileEntityIDs.Mega_AlloyBlastSmelter;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_AlloyBlastSmelter;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.mega.GregTechMetaTileEntity_MegaAlloyBlastSmelter;

public class GregtechIndustrialBlastSmelter {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Alloy Blast Smelter Multiblock.");
        if (CORE.ConfigSwitches.enableMultiblock_AlloyBlastSmelter) {
            run1();
        }
    }

    private static void run1() {
        // Industrial Alloy Blast Smelter Multiblock
        GregtechItemList.Industrial_AlloyBlastSmelter.set(
            new GregtechMetaTileEntity_AlloyBlastSmelter(
                Industrial_AlloyBlastSmelter.ID,
                "industrialsalloyamelter.controller.tier.single",
                "Alloy Blast Smelter").getStackForm(1L));
        GregtechItemList.Mega_AlloyBlastSmelter.set(
            new GregTechMetaTileEntity_MegaAlloyBlastSmelter(
                Mega_AlloyBlastSmelter.ID,
                "industrialsalloyamelter.controller.tier.mega",
                "Mega Alloy Blast Smelter").getStackForm(1L));
    }
}
