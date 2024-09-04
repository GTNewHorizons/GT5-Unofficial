package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_AlloyBlastSmelter;
import static gregtech.api.enums.MetaTileEntityIDs.Mega_AlloyBlastSmelter;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEAlloyBlastSmelter;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.mega.MTEMegaAlloyBlastSmelter;

public class GregtechIndustrialBlastSmelter {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Alloy Blast Smelter Multiblock.");
        if (GTPPCore.ConfigSwitches.enableMultiblock_AlloyBlastSmelter) {
            run1();
        }
    }

    private static void run1() {
        // Industrial Alloy Blast Smelter Multiblock
        GregtechItemList.Industrial_AlloyBlastSmelter.set(
            new MTEAlloyBlastSmelter(
                Industrial_AlloyBlastSmelter.ID,
                "industrialsalloyamelter.controller.tier.single",
                "Alloy Blast Smelter").getStackForm(1L));
        GregtechItemList.Mega_AlloyBlastSmelter.set(
            new MTEMegaAlloyBlastSmelter(
                Mega_AlloyBlastSmelter.ID,
                "industrialsalloyamelter.controller.tier.mega",
                "Mega Alloy Blast Smelter").getStackForm(1L));
    }
}
