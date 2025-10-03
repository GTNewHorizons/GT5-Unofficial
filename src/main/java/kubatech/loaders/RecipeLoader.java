/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2024  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.loaders;

import static gregtech.api.enums.MetaTileEntityIDs.DraconicEvolutionFusionCrafterController;
import static gregtech.api.enums.MetaTileEntityIDs.ExtremeEntityCrusherController;
import static gregtech.api.enums.MetaTileEntityIDs.ExtremeIndustrialApiaryController;
import static gregtech.api.enums.MetaTileEntityIDs.ExtremeIndustrialGreenhouseController;
import static gregtech.api.enums.MetaTileEntityIDs.HighTemperatureGasCooledReactorController;
import static gregtech.api.enums.Mods.DraconicEvolution;
import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.MobsInfo;
import static gregtech.api.enums.Mods.OpenBlocks;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;
import static kubatech.api.enums.ItemList.DraconicEvolutionFusionCrafter;
import static kubatech.api.enums.ItemList.ExtremeEntityCrusher;
import static kubatech.api.enums.ItemList.ExtremeIndustrialApiary;
import static kubatech.api.enums.ItemList.ExtremeIndustrialGreenhouse;
import static kubatech.api.enums.ItemList.HighTemperatureGasCooledReactor;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.material.MaterialsAlloy;
import kubatech.api.enums.ItemList;
import kubatech.loaders.tea.TeaLoader;
import kubatech.tileentity.gregtech.multiblock.MTEDEFusionCrafter;
import kubatech.tileentity.gregtech.multiblock.MTEExtremeEntityCrusher;
import kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse;
import kubatech.tileentity.gregtech.multiblock.MTEHighTempGasCooledReactor;
import kubatech.tileentity.gregtech.multiblock.MTEMegaIndustrialApiary;

public class RecipeLoader {

    protected static final long bitsd = GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
        | GTModHandler.RecipeBits.BUFFERED
        | GTModHandler.RecipeBits.DISMANTLEABLE;

    public static void registerMTEs() {
        if (MobsInfo.isModLoaded() && EnderIO.isModLoaded()) {
            ExtremeEntityCrusher.set(
                new MTEExtremeEntityCrusher(
                    ExtremeEntityCrusherController.ID,
                    "multimachine.entitycrusher",
                    "Extreme Entity Crusher").getStackForm(1));
        }

        if (Forestry.isModLoaded()) {
            ExtremeIndustrialApiary.set(
                new MTEMegaIndustrialApiary(
                    ExtremeIndustrialApiaryController.ID,
                    "multimachine.extremeapiary",
                    "Industrial Apicultural Acclimatiser and Drone Domestication Station").getStackForm(1));
        }

        ExtremeIndustrialGreenhouse.set(
            new MTEExtremeIndustrialGreenhouse(
                ExtremeIndustrialGreenhouseController.ID,
                "multimachine.extremegreenhouse",
                "Extreme Industrial Greenhouse").getStackForm(1));

        if (DraconicEvolution.isModLoaded()) {
            DraconicEvolutionFusionCrafter.set(
                new MTEDEFusionCrafter(
                    DraconicEvolutionFusionCrafterController.ID,
                    "multimachine.defusioncrafter",
                    "Draconic Evolution Fusion Crafter").getStackForm(1));
        }

        HighTemperatureGasCooledReactor.set(
            new MTEHighTempGasCooledReactor(
                HighTemperatureGasCooledReactorController.ID,
                "HTGR",
                "High Temperature Gas-cooled Reactor").getStackForm(1L));
    }

    public static void addRecipes() {

        if (MobsInfo.isModLoaded() && EnderIO.isModLoaded()) {
            GTModHandler.addCraftingRecipe(
                ItemList.ExtremeEntityCrusher.get(1),
                bitsd,
                new Object[] { "RCR", "CHC", "VVV", 'R', gregtech.api.enums.ItemList.Robot_Arm_EV, 'C',
                    OrePrefixes.circuit.get(Materials.EV), 'H', gregtech.api.enums.ItemList.Hull_EV, 'V',
                    GTModHandler.getModItem(OpenBlocks.ID, "vacuumhopper", 1, new ItemStack(Blocks.hopper)) });
        }

        if (Forestry.isModLoaded()) {
            // Industrial Apicultural Acclimatiser and Drone Domestication Station
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, gregtech.api.enums.ItemList.Machine_IndustrialApiary.get(1))
                .metadata(SCANNING, new Scanning(2 * MINUTES + 50 * SECONDS, TierEU.RECIPE_ZPM))
                .itemInputs(
                    gregtech.api.enums.ItemList.Machine_IndustrialApiary.get(64L),
                    gregtech.api.enums.ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64L),
                    gregtech.api.enums.ItemList.IndustrialApiary_Upgrade_STABILIZER.get(64L),
                    gregtech.api.enums.ItemList.Robot_Arm_UV.get(16L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 16L })
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(3 * STACKS + 8 * INGOTS),
                    Materials.Honey.getFluid(20_000))
                .itemOutputs(ExtremeIndustrialApiary.get(1))
                .eut(TierEU.RECIPE_UHV)
                .duration(5 * MINUTES)
                .addTo(AssemblyLine);

            MTEExtremeIndustrialGreenhouse.addFertilizerItem(gregtech.api.enums.ItemList.FR_Fertilizer.get(1));
        }

        GTModHandler.addCraftingRecipe(
            ExtremeIndustrialGreenhouse.get(1),
            bitsd,
            new Object[] { "AZA", "BRB", "AZA", 'B', gregtech.api.enums.ItemList.Casing_CleanStainlessSteel, 'R',
                GTModHandler.getModItem(EnderIO.ID, "blockFarmStation", 1, new ItemStack(Items.diamond_hoe)), 'A',
                gregtech.api.enums.ItemList.AcceleratorIV.get(1), 'Z', OrePrefixes.circuit.get(Materials.ZPM) });

        // Vanilla should always be loaded
        MTEExtremeIndustrialGreenhouse.addFertilizerItem(new ItemStack(Items.dye, 1, 15));
        // will need to be updated when ic2 goes the way of the dodo
        MTEExtremeIndustrialGreenhouse.addFertilizerItem(gregtech.api.enums.ItemList.IC2_Fertilizer.get(1));

        if (DraconicEvolution.isModLoaded()) {
            // Controller recipe added in TecTech
            DEFCRecipes.addRecipes();
        }

        GTModHandler.addCraftingRecipe(
            HighTemperatureGasCooledReactor.get(1),
            bitsd,
            new Object[] { "BZB", "ZRZ", "BZB", 'B', gregtech.api.enums.ItemList.Casing_IV.get(1), 'R',
                GTModHandler.getModItem(IndustrialCraft2.ID, "blockGenerator", 1, 5), 'Z', "circuitUltimate" });

        TeaLoader.registerTeaLine();
    }

    private static boolean lateRecipesInitialized = false;

    public static void addRecipesLate() {
        // Runs on server start
        if (lateRecipesInitialized) return;
        lateRecipesInitialized = true;
    }
}
