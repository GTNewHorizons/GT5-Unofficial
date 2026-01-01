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

import static gregtech.api.enums.ItemList.FluidExtractorUHV;
import static gregtech.api.enums.MetaTileEntityIDs.DraconicEvolutionFusionCrafterController;
import static gregtech.api.enums.MetaTileEntityIDs.ExtremeEntityCrusherController;
import static gregtech.api.enums.MetaTileEntityIDs.ExtremeIndustrialApiaryController;
import static gregtech.api.enums.MetaTileEntityIDs.ExtremeIndustrialGreenhouseController;
import static gregtech.api.enums.MetaTileEntityIDs.HighTemperatureGasCooledReactorController;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.DraconicEvolution;
import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.MobsInfo;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.OpenBlocks;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static kubatech.api.enums.ItemList.BlackTea;
import static kubatech.api.enums.ItemList.BlackTeaLeaf;
import static kubatech.api.enums.ItemList.BruisedTeaLeaf;
import static kubatech.api.enums.ItemList.DraconicEvolutionFusionCrafter;
import static kubatech.api.enums.ItemList.EarlGrayTea;
import static kubatech.api.enums.ItemList.ExtremeEntityCrusher;
import static kubatech.api.enums.ItemList.ExtremeIndustrialApiary;
import static kubatech.api.enums.ItemList.ExtremeIndustrialGreenhouse;
import static kubatech.api.enums.ItemList.FermentedTeaLeaf;
import static kubatech.api.enums.ItemList.GreenTea;
import static kubatech.api.enums.ItemList.GreenTeaLeaf;
import static kubatech.api.enums.ItemList.HighTemperatureGasCooledReactor;
import static kubatech.api.enums.ItemList.LegendaryUltimateTea;
import static kubatech.api.enums.ItemList.LemonTea;
import static kubatech.api.enums.ItemList.MilkTea;
import static kubatech.api.enums.ItemList.OolongTea;
import static kubatech.api.enums.ItemList.OolongTeaLeaf;
import static kubatech.api.enums.ItemList.OxidizedTeaLeaf;
import static kubatech.api.enums.ItemList.PartiallyOxidizedTeaLeaf;
import static kubatech.api.enums.ItemList.PeppermintTea;
import static kubatech.api.enums.ItemList.PuerhTea;
import static kubatech.api.enums.ItemList.PuerhTeaLeaf;
import static kubatech.api.enums.ItemList.RolledTeaLeaf;
import static kubatech.api.enums.ItemList.SteamedTeaLeaf;
import static kubatech.api.enums.ItemList.TeaAcceptor;
import static kubatech.api.enums.ItemList.TeaAcceptorResearchNote;
import static kubatech.api.enums.ItemList.TeaLeafDehydrated;
import static kubatech.api.enums.ItemList.WhiteTea;
import static kubatech.api.enums.ItemList.WhiteTeaLeaf;
import static kubatech.api.enums.ItemList.YellowTea;
import static kubatech.api.enums.ItemList.YellowTeaLeaf;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.material.MaterialsAlloy;
import kubatech.api.enums.ItemList;
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

        RegisterTeaLine();
    }

    private static boolean lateRecipesInitialized = false;

    public static void addRecipesLate() {
        // Runs on server start
        if (lateRecipesInitialized) return;
        lateRecipesInitialized = true;
    }

    private static void RegisterTeaLine() {
        // TEA LINE //
        if (PamsHarvestCraft.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(GameRegistry.findItemStack("harvestcraft", "tealeafItem", 1))
                .itemOutputs(TeaLeafDehydrated.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(TeaLeafDehydrated.get(1))
                .itemOutputs(WhiteTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(TeaLeafDehydrated.get(1))
                .itemOutputs(SteamedTeaLeaf.get(1))
                .fluidInputs(Materials.Water.getFluid(50))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(SteamedTeaLeaf.get(1))
                .itemOutputs(YellowTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(TeaLeafDehydrated.get(1))
                .circuit(1)
                .itemOutputs(RolledTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(benderRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(RolledTeaLeaf.get(1))
                .itemOutputs(GreenTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(RolledTeaLeaf.get(1))
                .circuit(1)
                .itemOutputs(OxidizedTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(OxidizedTeaLeaf.get(1))
                .itemOutputs(BlackTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(RolledTeaLeaf.get(1))
                .circuit(2)
                .itemOutputs(FermentedTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(10 * SECONDS)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(FermentedTeaLeaf.get(1))
                .itemOutputs(PuerhTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(TeaLeafDehydrated.get(1))
                .itemOutputs(BruisedTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(cutterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(BruisedTeaLeaf.get(1))
                .circuit(1)
                .itemOutputs(PartiallyOxidizedTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(2 * SECONDS + 10 * TICKS)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(PartiallyOxidizedTeaLeaf.get(1))
                .itemOutputs(OolongTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

            // Tea Assembly
            GameRegistry.addSmelting(BlackTeaLeaf.get(1), BlackTea.get(1), 10);

            GTValues.RA.stdBuilder()
                .itemInputs(BlackTea.get(1), GameRegistry.findItemStack("harvestcraft", "limejuiceItem", 1))
                .itemOutputs(EarlGrayTea.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(mixerRecipes);

            GameRegistry.addSmelting(GreenTeaLeaf.get(1), GreenTea.get(1), 10);

            GTValues.RA.stdBuilder()
                .itemInputs(BlackTea.get(1))
                .itemOutputs(LemonTea.get(1))
                .fluidInputs(FluidRegistry.getFluidStack("potion.lemonjuice", 10))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(mixerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(BlackTea.get(1))
                .itemOutputs(MilkTea.get(1))
                .fluidInputs(Materials.Milk.getFluid(100))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(mixerRecipes);

            GameRegistry.addSmelting(OolongTeaLeaf.get(1), OolongTea.get(1), 10);

            GTValues.RA.stdBuilder()
                .itemInputs(GameRegistry.findItemStack("harvestcraft", "peppermintItem", 1))
                .itemOutputs(PeppermintTea.get(1))
                .fluidInputs(Materials.Water.getFluid(1_000))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(mixerRecipes);

            GameRegistry.addSmelting(PuerhTeaLeaf.get(1), PuerhTea.get(1), 10);
            GameRegistry.addSmelting(WhiteTeaLeaf.get(1), WhiteTea.get(1), 10);
            GameRegistry.addSmelting(YellowTeaLeaf.get(1), YellowTea.get(1), 10);
        }
        if (Avaritia.isModLoaded() && NewHorizonsCoreMod.isModLoaded()) {
            // Tea Acceptor
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, TeaAcceptorResearchNote.get(1))
                .metadata(SCANNING, new Scanning(1 * MINUTES + 40 * SECONDS, TierEU.RECIPE_UV))
                .itemInputs(
                    LegendaryUltimateTea.get(0),
                    gregtech.api.enums.ItemList.Machine_Multi_NeutroniumCompressor.get(1),
                    gregtech.api.enums.ItemList.Quantum_Tank_EV.get(1),
                    FluidExtractorUHV.get(10),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 16L })
                .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(3 * STACKS + 8 * INGOTS))
                .itemOutputs(TeaAcceptor.get(1))
                .eut(TierEU.RECIPE_UHV)
                .duration(5 * MINUTES)
                .addTo(AssemblyLine);
        }
    }
}
