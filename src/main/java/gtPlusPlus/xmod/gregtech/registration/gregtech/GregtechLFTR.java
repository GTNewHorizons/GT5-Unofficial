package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.enums.SoundResource;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_SpargeTower;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMTE_NuclearReactor;

public class GregtechLFTR {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Liquid Fluorine Thorium Reactor [LFTR].");
        if (CORE.ConfigSwitches.enableMultiblock_LiquidFluorideThoriumReactor) {
            run1();
        }
    }

    private static void run1() {
        // LFTR
        GregtechItemList.ThoriumReactor.set(
                new GregtechMTE_NuclearReactor(751, "lftr.controller.single", "Thorium Reactor [LFTR]")
                        .getStackForm(1L));
        // Reactor Processing Units
        GregtechItemList.ReactorProcessingUnit_IV.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        31031,
                        "rpu.tier.01",
                        "Reactor Processing Unit I",
                        5,
                        new String[] { "Processes Nuclear things", CORE.GT_Tooltip.get() },
                        GTPPRecipeMaps.reactorProcessingUnitRecipes,
                        2,
                        9,
                        true,
                        SoundResource.NONE,
                        SpecialEffects.NONE,
                        "REACTOR_PROCESSING_UNIT",
                        null).getStackForm(1L));
        GregtechItemList.ReactorProcessingUnit_ZPM.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        31032,
                        "rpu.tier.02",
                        "Reactor Processing Unit II",
                        7,
                        new String[] { "Processes Nuclear things", CORE.GT_Tooltip.get() },
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
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        31033,
                        "coldtrap.tier.01",
                        "Cold Trap I",
                        5,
                        new String[] { "Just like the Arctic", "Does not require ice cubes", CORE.GT_Tooltip.get() },
                        GTPPRecipeMaps.coldTrapRecipes,
                        2,
                        9,
                        true,
                        SoundResource.NONE,
                        SpecialEffects.NONE,
                        "COLD_TRAP",
                        null).getStackForm(1L));
        GregtechItemList.ColdTrap_ZPM.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        31034,
                        "coldtrap.tier.02",
                        "Cold Trap II",
                        7,
                        new String[] { "Just like the Arctic", "Does not require ice cubes", CORE.GT_Tooltip.get() },
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
                new GregtechMetaTileEntity_SpargeTower(31035, "sparge.controller.single", "Sparge Tower Controller")
                        .getStackForm(1L));
    }
}
