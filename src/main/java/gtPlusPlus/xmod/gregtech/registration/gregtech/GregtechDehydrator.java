package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Controller_Vacuum_Furnace;
import static gregtech.api.enums.MetaTileEntityIDs.GT_Dehydrator_EV;
import static gregtech.api.enums.MetaTileEntityIDs.GT_Dehydrator_HV;
import static gregtech.api.enums.MetaTileEntityIDs.GT_Dehydrator_IV;
import static gregtech.api.enums.MetaTileEntityIDs.GT_Dehydrator_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.GT_Dehydrator_MV;
import static gregtech.api.enums.MetaTileEntityIDs.GT_Dehydrator_ZPM;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.common.tileentities.machines.basic.MTEBasicMachineWithRecipe.X.CIRCUIT;
import static gregtech.common.tileentities.machines.basic.MTEBasicMachineWithRecipe.X.HULL;
import static gregtech.common.tileentities.machines.basic.MTEBasicMachineWithRecipe.X.ROBOT_ARM;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.tileentities.machines.basic.MTEBasicMachineWithRecipeBuilder;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialDehydrator;

public class GregtechDehydrator {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Chemical Dehydrators.");
        registerMTEs();
        registerRecipes();
    }

    private static void registerMTEs() {
        // Basic
        GregtechItemList.GT_Dehydrator_MV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(GT_Dehydrator_MV.ID)
                .setName("machine.dehydrator.tier.00", "Basic Dehydrator I")
                .setTier(2)
                .setDescription(new String[] { "This dehydrates your Grapes into Raisins.", GTPPCore.GT_Tooltip.get() })
                .setRecipes(GTPPRecipeMaps.chemicalDehydratorRecipes)
                .setSlotsCount(2, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("DEHYDRATOR")
                .build()
                .setRecipeCatalystPriority(6)
                .getStackForm(1L));

        GregtechItemList.GT_Dehydrator_HV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(GT_Dehydrator_HV.ID)
                .setName("machine.dehydrator.tier.01", "Basic Dehydrator II")
                .setTier(3)
                .setDescription(new String[] { "This dehydrates your Grapes into Raisins.", GTPPCore.GT_Tooltip.get() })
                .setRecipes(GTPPRecipeMaps.chemicalDehydratorRecipes)
                .setSlotsCount(2, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("DEHYDRATOR")
                .build()
                .setRecipeCatalystPriority(5)
                .getStackForm(1L));

        // Chemical
        GregtechItemList.GT_Dehydrator_EV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(GT_Dehydrator_EV.ID)
                .setName("advancedmachine.dehydrator.tier.01", "Chemical Dehydrator I")
                .setTier(4)
                .setDescription(
                    new String[] { "A hangover is the way your body reacts to dehydration.",
                        GTPPCore.GT_Tooltip.get() })
                .setRecipes(GTPPRecipeMaps.chemicalDehydratorRecipes)
                .setSlotsCount(2, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("DEHYDRATOR")
                .build()
                .setRecipeCatalystPriority(4)
                .getStackForm(1L));

        GregtechItemList.GT_Dehydrator_IV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(GT_Dehydrator_IV.ID)
                .setName("advancedmachine.dehydrator.tier.02", "Chemical Dehydrator II")
                .setTier(5)
                .setDescription(
                    new String[] { "A hangover is the way your body reacts to dehydration.",
                        GTPPCore.GT_Tooltip.get() })
                .setRecipes(GTPPRecipeMaps.chemicalDehydratorRecipes)
                .setSlotsCount(2, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("DEHYDRATOR")
                .build()
                .setRecipeCatalystPriority(3)
                .getStackForm(1L));

        GregtechItemList.GT_Dehydrator_LuV.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(GT_Dehydrator_LuV.ID)
                .setName("advancedmachine.dehydrator.tier.03", "Chemical Dehydrator III")
                .setTier(6)
                .setDescription(
                    new String[] { "You could probably make space icecream with this..", GTPPCore.GT_Tooltip.get() })
                .setRecipes(GTPPRecipeMaps.chemicalDehydratorRecipes)
                .setSlotsCount(2, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("DEHYDRATOR")
                .build()
                .setRecipeCatalystPriority(2)
                .getStackForm(1L));

        GregtechItemList.GT_Dehydrator_ZPM.set(
            MTEBasicMachineWithRecipeBuilder.builder()
                .setId(GT_Dehydrator_ZPM.ID)
                .setName("advancedmachine.dehydrator.tier.04", "Chemical Dehydrator IV")
                .setTier(7)
                .setDescription(
                    new String[] { "You can definitely make space icecream with this..", GTPPCore.GT_Tooltip.get() })
                .setRecipes(GTPPRecipeMaps.chemicalDehydratorRecipes)
                .setSlotsCount(2, 9)
                .setFluidSlots(true, true)
                .setSound(SoundResource.GTCEU_LOOP_CHEMICAL)
                .setOverlays("DEHYDRATOR")
                .build()
                .setRecipeCatalystPriority(1)
                .getStackForm(1L));

        // Advanced
        GregtechItemList.Controller_Vacuum_Furnace.set(
            new MTEIndustrialDehydrator(Controller_Vacuum_Furnace.ID, "multimachine.adv.vacuumfurnace", "Utupu-Tanuri")
                .getStackForm(1L));
    }

    private static void registerRecipes() {
        // Make some coils by wrapping wire around a spool.
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.DehydratorCoilWireEV.get(4),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Bronze, 1))
            .itemOutputs(GregtechItemList.DehydratorCoilEV.get(1))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.DehydratorCoilWireIV.get(4),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Steel, 1))
            .itemOutputs(GregtechItemList.DehydratorCoilIV.get(1))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.DehydratorCoilWireLuV.get(4),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.StainlessSteel, 1))
            .itemOutputs(GregtechItemList.DehydratorCoilLuV.get(1))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.DehydratorCoilWireZPM.get(4),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Titanium, 1))
            .itemOutputs(GregtechItemList.DehydratorCoilZPM.get(1))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .addTo(assemblerRecipes);

        GTModHandler.addMachineCraftingRecipe(
            GregtechItemList.GT_Dehydrator_MV.get(1),
            new Object[] { "ECE", "WMW", "GPG", 'M', HULL, 'P', ROBOT_ARM, 'E',
                OrePrefixes.wireFine.get(Materials.RedAlloy), 'C', CIRCUIT, 'W',
                OrePrefixes.cableGt04.get(Materials.Copper), 'G', OrePrefixes.gearGt.get(Materials.Steel) },
            2);

        GTModHandler.addMachineCraftingRecipe(
            GregtechItemList.GT_Dehydrator_HV.get(1),
            new Object[] { "ECE", "WMW", "GPG", 'M', HULL, 'P', ROBOT_ARM, 'E',
                OrePrefixes.wireFine.get(Materials.Electrum), 'C', CIRCUIT, 'W',
                OrePrefixes.cableGt04.get(Materials.Silver), 'G', MaterialsAlloy.POTIN.getGear(1) },
            3);

        GTModHandler.addMachineCraftingRecipe(
            GregtechItemList.GT_Dehydrator_EV.get(1),
            new Object[] { "ECE", "WMW", "GPG", 'M', HULL, 'P', ROBOT_ARM, 'E', GregtechItemList.DehydratorCoilEV, 'C',
                CIRCUIT, 'W', OrePrefixes.cableGt04.get(Materials.Aluminium), 'G', MaterialsAlloy.TUMBAGA.getGear(1) },
            4);

        GTModHandler.addMachineCraftingRecipe(
            GregtechItemList.GT_Dehydrator_IV.get(1),
            new Object[] { "ECE", "WMW", "GPG", 'M', HULL, 'P', ROBOT_ARM, 'E', GregtechItemList.DehydratorCoilIV, 'C',
                CIRCUIT, 'W', OrePrefixes.cableGt04.get(Materials.Tungsten), 'G',
                MaterialsAlloy.INCONEL_690.getGear(1) },
            5);

        GTModHandler.addMachineCraftingRecipe(
            GregtechItemList.GT_Dehydrator_LuV.get(1),
            new Object[] { "ECE", "WMW", "GPG", 'M', HULL, 'P', ROBOT_ARM, 'E', GregtechItemList.DehydratorCoilLuV, 'C',
                CIRCUIT, 'W', OrePrefixes.cableGt04.get(Materials.Naquadah), 'G',
                MaterialsAlloy.HASTELLOY_N.getGear(1) },
            6);

        GTModHandler.addMachineCraftingRecipe(
            GregtechItemList.GT_Dehydrator_ZPM.get(1),
            new Object[] { "ECE", "WMW", "GPG", 'M', HULL, 'P', ROBOT_ARM, 'E', GregtechItemList.DehydratorCoilZPM, 'C',
                CIRCUIT, 'W', OrePrefixes.cableGt04.get(Materials.Osmium), 'G', MaterialsAlloy.ZERON_100.getGear(1) },
            7);
    }
}
