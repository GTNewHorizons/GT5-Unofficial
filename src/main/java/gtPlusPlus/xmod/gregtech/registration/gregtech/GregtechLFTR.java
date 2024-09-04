package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.ColdTrap_IV;
import static gregtech.api.enums.MetaTileEntityIDs.ColdTrap_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_Sparge_Tower;
import static gregtech.api.enums.MetaTileEntityIDs.ReactorProcessingUnit_IV;
import static gregtech.api.enums.MetaTileEntityIDs.ReactorProcessingUnit_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.ThoriumReactor;

import gregtech.api.enums.SoundResource;
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe;
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe.SpecialEffects;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTESpargeTower;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTENuclearReactor;

public class GregtechLFTR {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Liquid Fluorine Thorium Reactor [LFTR].");
        if (GTPPCore.ConfigSwitches.enableMultiblock_LiquidFluorideThoriumReactor) {
            run1();
        }
    }

    private static void run1() {
        // LFTR
        GregtechItemList.ThoriumReactor.set(
            new MTENuclearReactor(ThoriumReactor.ID, "lftr.controller.single", "Thorium Reactor [LFTR]")
                .getStackForm(1L));
        // Reactor Processing Units
        GregtechItemList.ReactorProcessingUnit_IV.set(
            new MTEBasicMachineWithRecipe(
                ReactorProcessingUnit_IV.ID,
                "rpu.tier.01",
                "Reactor Processing Unit I",
                5,
                new String[] { "Processes Nuclear things", GTPPCore.GT_Tooltip.get() },
                GTPPRecipeMaps.reactorProcessingUnitRecipes,
                2,
                9,
                true,
                SoundResource.NONE,
                SpecialEffects.NONE,
                "REACTOR_PROCESSING_UNIT",
                null).getStackForm(1L));
        GregtechItemList.ReactorProcessingUnit_ZPM.set(
            new MTEBasicMachineWithRecipe(
                ReactorProcessingUnit_ZPM.ID,
                "rpu.tier.02",
                "Reactor Processing Unit II",
                7,
                new String[] { "Processes Nuclear things", GTPPCore.GT_Tooltip.get() },
                GTPPRecipeMaps.reactorProcessingUnitRecipes,
                2,
                9,
                true,
                SoundResource.NONE,
                SpecialEffects.NONE,
                "REACTOR_PROCESSING_UNIT",
                null).getStackForm(1L));
        // Cold Traps
        GregtechItemList.ColdTrap_IV.set(
            new MTEBasicMachineWithRecipe(
                ColdTrap_IV.ID,
                "coldtrap.tier.01",
                "Cold Trap I",
                5,
                new String[] { "Just like the Arctic", "Does not require ice cubes", GTPPCore.GT_Tooltip.get() },
                GTPPRecipeMaps.coldTrapRecipes,
                2,
                9,
                true,
                SoundResource.NONE,
                SpecialEffects.NONE,
                "COLD_TRAP",
                null).getStackForm(1L));
        GregtechItemList.ColdTrap_ZPM.set(
            new MTEBasicMachineWithRecipe(
                ColdTrap_ZPM.ID,
                "coldtrap.tier.02",
                "Cold Trap II",
                7,
                new String[] { "Just like the Arctic", "Does not require ice cubes", GTPPCore.GT_Tooltip.get() },
                GTPPRecipeMaps.coldTrapRecipes,
                2,
                9,
                true,
                SoundResource.NONE,
                SpecialEffects.NONE,
                "COLD_TRAP",
                null).getStackForm(1L));
        // Sparge Tower
        GregtechItemList.Controller_Sparge_Tower.set(
            new MTESpargeTower(Controller_Sparge_Tower.ID, "sparge.controller.single", "Sparge Tower Controller")
                .getStackForm(1L));
    }
}
