package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT;
import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL;
import static gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TierEU;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialDehydrator;

public class GregtechDehydrator {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Chemical Dehydrators.");
        if (CORE.ConfigSwitches.enableMachine_Dehydrators) {
            run1();
        }
    }

    private static void run1() {
        ItemStack coilT1 = new ItemStack(ModItems.itemDehydratorCoil, 1, 0);
        ItemStack coilT2 = new ItemStack(ModItems.itemDehydratorCoil, 1, 1);
        ItemStack coilT3 = new ItemStack(ModItems.itemDehydratorCoil, 1, 2);
        ItemStack coilT4 = new ItemStack(ModItems.itemDehydratorCoil, 1, 3);

        // Make some coils by wrapping wire around a spool.
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        new ItemStack(ModItems.itemDehydratorCoilWire, 4, 0),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Bronze, 1))
                .itemOutputs(coilT1).duration(8 * SECONDS).eut(TierEU.RECIPE_MV).addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        new ItemStack(ModItems.itemDehydratorCoilWire, 4, 1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Steel, 1))
                .itemOutputs(coilT2).duration(8 * SECONDS).eut(TierEU.RECIPE_HV / 2).addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        new ItemStack(ModItems.itemDehydratorCoilWire, 4, 2),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.StainlessSteel, 1))
                .itemOutputs(coilT3).duration(8 * SECONDS).eut(TierEU.RECIPE_HV).addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        new ItemStack(ModItems.itemDehydratorCoilWire, 4, 3),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Titanium, 1))
                .itemOutputs(coilT4).duration(8 * SECONDS).eut(TierEU.RECIPE_EV / 2).addTo(assemblerRecipes);

        // Basic
        GregtechItemList.GT_Dehydrator_MV.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        911,
                        "machine.dehydrator.tier.00",
                        "Basic Dehydrator I",
                        2,
                        new String[] { "This dehydrates your Grapes into Raisins.", CORE.GT_Tooltip.get() },
                        GTPPRecipeMaps.chemicalDehydratorRecipes,
                        2,
                        9,
                        true,
                        SoundResource.NONE,
                        SpecialEffects.NONE,
                        "DEHYDRATOR",
                        new Object[] { "ECE", "WMW", "GPG", 'M', HULL, 'P', ROBOT_ARM, 'E',
                                OrePrefixes.wireFine.get(Materials.RedAlloy), 'C', CIRCUIT, 'W',
                                OrePrefixes.cableGt04.get(Materials.Copper), 'G',
                                OrePrefixes.gearGt.get(Materials.Steel) }).setRecipeCatalystPriority(6)
                                        .getStackForm(1L));

        GregtechItemList.GT_Dehydrator_HV.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        912,
                        "machine.dehydrator.tier.01",
                        "Basic Dehydrator II",
                        3,
                        new String[] { "This dehydrates your Grapes into Raisins.", CORE.GT_Tooltip.get() },
                        GTPPRecipeMaps.chemicalDehydratorRecipes,
                        2,
                        9,
                        true,
                        SoundResource.NONE,
                        SpecialEffects.NONE,
                        "DEHYDRATOR",
                        new Object[] { "ECE", "WMW", "GPG", 'M', HULL, 'P', ROBOT_ARM, 'E',
                                OrePrefixes.wireFine.get(Materials.Electrum), 'C', CIRCUIT, 'W',
                                OrePrefixes.cableGt04.get(Materials.Silver), 'G', ALLOY.POTIN.getGear(1) })
                                        .setRecipeCatalystPriority(5).getStackForm(1L));

        // Chemical
        GregtechItemList.GT_Dehydrator_EV.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        813,
                        "advancedmachine.dehydrator.tier.01",
                        "Chemical Dehydrator I",
                        4,
                        new String[] { "A hangover is the way your body reacts to dehydration.",
                                CORE.GT_Tooltip.get() },
                        GTPPRecipeMaps.chemicalDehydratorRecipes,
                        2,
                        9,
                        true,
                        SoundResource.NONE,
                        SpecialEffects.NONE,
                        "DEHYDRATOR",
                        new Object[] { "ECE", "WMW", "GPG", 'M', HULL, 'P', ROBOT_ARM, 'E', coilT1, 'C', CIRCUIT, 'W',
                                OrePrefixes.cableGt04.get(Materials.Aluminium), 'G', ALLOY.TUMBAGA.getGear(1) })
                                        .setRecipeCatalystPriority(4).getStackForm(1L));

        GregtechItemList.GT_Dehydrator_IV.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        814,
                        "advancedmachine.dehydrator.tier.02",
                        "Chemical Dehydrator II",
                        5,
                        new String[] { "A hangover is the way your body reacts to dehydration.",
                                CORE.GT_Tooltip.get() },
                        GTPPRecipeMaps.chemicalDehydratorRecipes,
                        2,
                        9,
                        true,
                        SoundResource.NONE,
                        SpecialEffects.NONE,
                        "DEHYDRATOR",
                        new Object[] { "ECE", "WMW", "GPG", 'M', HULL, 'P', ROBOT_ARM, 'E', coilT2, 'C', CIRCUIT, 'W',
                                OrePrefixes.cableGt04.get(Materials.Tungsten), 'G', ALLOY.INCONEL_690.getGear(1) })
                                        .setRecipeCatalystPriority(3).getStackForm(1L));

        GregtechItemList.GT_Dehydrator_LuV.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        815,
                        "advancedmachine.dehydrator.tier.03",
                        "Chemical Dehydrator III",
                        6,
                        new String[] { "You could probably make space icecream with this..", CORE.GT_Tooltip.get() },
                        GTPPRecipeMaps.chemicalDehydratorRecipes,
                        2,
                        9,
                        true,
                        SoundResource.NONE,
                        SpecialEffects.NONE,
                        "DEHYDRATOR",
                        new Object[] { "ECE", "WMW", "GPG", 'M', HULL, 'P', ROBOT_ARM, 'E', coilT3, 'C', CIRCUIT, 'W',
                                OrePrefixes.cableGt04.get(Materials.Naquadah), 'G', ALLOY.HASTELLOY_N.getGear(1) })
                                        .setRecipeCatalystPriority(2).getStackForm(1L));

        GregtechItemList.GT_Dehydrator_ZPM.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        816,
                        "advancedmachine.dehydrator.tier.04",
                        "Chemical Dehydrator IV",
                        7,
                        new String[] { "You can definitely make space icecream with this..", CORE.GT_Tooltip.get() },
                        GTPPRecipeMaps.chemicalDehydratorRecipes,
                        2,
                        9,
                        true,
                        SoundResource.NONE,
                        SpecialEffects.NONE,
                        "DEHYDRATOR",
                        new Object[] { "ECE", "WMW", "GPG", 'M', HULL, 'P', ROBOT_ARM, 'E', coilT4, 'C', CIRCUIT, 'W',
                                OrePrefixes.cableGt04.get(Materials.Osmium), 'G', ALLOY.ZERON_100.getGear(1) })
                                        .setRecipeCatalystPriority(1).getStackForm(1L));

        // Advanced
        GregtechItemList.Controller_Vacuum_Furnace.set(
                new GregtechMetaTileEntity_IndustrialDehydrator(995, "multimachine.adv.vacuumfurnace", "Utupu-Tanuri")
                        .getStackForm(1L));
    }
}
