package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.ColdTrap_IV;
import static gregtech.api.enums.MetaTileEntityIDs.ColdTrap_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_Sparge_Tower;
import static gregtech.api.enums.MetaTileEntityIDs.ReactorProcessingUnit_IV;
import static gregtech.api.enums.MetaTileEntityIDs.ReactorProcessingUnit_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.ThoriumReactor;

import gregtech.api.enums.SoundResource;
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipeBuilder;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTESpargeTower;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTENuclearReactor;

public class GregtechLFTR {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Liquid Fluorine Thorium Reactor [LFTR].");
        run1();
    }

    private static void run1() {
        // LFTR
        GregtechItemList.ThoriumReactor.set(
            new MTENuclearReactor(ThoriumReactor.ID, "lftr.controller.single", "Thorium Reactor [LFTR]")
                .getStackForm(1L));
        // Reactor Processing Units
        GregtechItemList.ReactorProcessingUnit_IV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ReactorProcessingUnit_IV.ID)
                .setName("rpu.tier.01", "Reactor Processing Unit I")
                .setTier(5)
                .setDescription(new String[] { "Processes Nuclear things", GTPPCore.GT_Tooltip.get() })
                .setRecipes(GTPPRecipeMaps.reactorProcessingUnitRecipes)
                .setSlotsCount(2, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.NONE)
                .setOverlays("REACTOR_PROCESSING_UNIT")
                .build()
                .getStackForm(1L));
        GregtechItemList.ReactorProcessingUnit_ZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ReactorProcessingUnit_ZPM.ID)
                .setName("rpu.tier.02", "Reactor Processing Unit II")
                .setTier(7)
                .setDescription(new String[] { "Processes Nuclear things", GTPPCore.GT_Tooltip.get() })
                .setRecipes(GTPPRecipeMaps.reactorProcessingUnitRecipes)
                .setSlotsCount(2, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.NONE)
                .setOverlays("REACTOR_PROCESSING_UNIT")
                .build()
                .getStackForm(1L));
        // Cold Traps
        GregtechItemList.ColdTrap_IV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ColdTrap_IV.ID)
                .setName("coldtrap.tier.01", "Cold Trap I")
                .setTier(5)
                .setDescription(
                    new String[] { "Just like the Arctic", "Does not require ice cubes", GTPPCore.GT_Tooltip.get() })
                .setRecipes(GTPPRecipeMaps.coldTrapRecipes)
                .setSlotsCount(2, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.NONE)
                .setOverlays("COLD_TRAP")
                .build()
                .getStackForm(1L));
        GregtechItemList.ColdTrap_ZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(ColdTrap_ZPM.ID)
                .setName("coldtrap.tier.02", "Cold Trap II")
                .setTier(7)
                .setDescription(
                    new String[] { "Just like the Arctic", "Does not require ice cubes", GTPPCore.GT_Tooltip.get() })
                .setRecipes(GTPPRecipeMaps.coldTrapRecipes)
                .setSlotsCount(2, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.NONE)
                .setOverlays("COLD_TRAP")
                .build()
                .getStackForm(1L));
        // Sparge Tower
        GregtechItemList.Controller_Sparge_Tower.set(
            new MTESpargeTower(Controller_Sparge_Tower.ID, "sparge.controller.single", "Sparge Tower Controller")
                .getStackForm(1L));
    }
}
