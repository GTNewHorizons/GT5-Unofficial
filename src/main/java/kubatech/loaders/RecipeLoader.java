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
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.DraconicEvolution;
import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.OpenBlocks;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.AssemblyLine;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;
import static gregtech.api.util.GT_RecipeConstants.UniversalChemical;
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
import forestry.plugins.PluginCore;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import ic2.core.Ic2Items;
import kubatech.api.enums.ItemList;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_DEFusionCrafter;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeEntityCrusher;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeIndustrialGreenhouse;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_MegaIndustrialApiary;

public class RecipeLoader {

    protected static final long bitsd = GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE
        | GT_ModHandler.RecipeBits.BUFFERED
        | GT_ModHandler.RecipeBits.DISMANTLEABLE;

    public static void registerMTEs() {
        if (EnderIO.isModLoaded()) {
            ExtremeEntityCrusher.set(
                new GT_MetaTileEntity_ExtremeEntityCrusher(
                    ExtremeEntityCrusherController.ID,
                    "multimachine.entitycrusher",
                    "Extreme Entity Crusher").getStackForm(1));
        }

        if (Forestry.isModLoaded()) {
            ExtremeIndustrialApiary.set(
                new GT_MetaTileEntity_MegaIndustrialApiary(
                    ExtremeIndustrialApiaryController.ID,
                    "multimachine.extremeapiary",
                    "Industrial Apicultural Acclimatiser and Drone Domestication Station").getStackForm(1));
        }

        ExtremeIndustrialGreenhouse.set(
            new GT_MetaTileEntity_ExtremeIndustrialGreenhouse(
                ExtremeIndustrialGreenhouseController.ID,
                "multimachine.extremegreenhouse",
                "Extreme Industrial Greenhouse").getStackForm(1));

        if (DraconicEvolution.isModLoaded()) {
            DraconicEvolutionFusionCrafter.set(
                new GT_MetaTileEntity_DEFusionCrafter(
                    DraconicEvolutionFusionCrafterController.ID,
                    "multimachine.defusioncrafter",
                    "Draconic Evolution Fusion Crafter").getStackForm(1));
        }
    }

    public static void addRecipes() {

        if (EnderIO.isModLoaded()) {
            GT_ModHandler.addCraftingRecipe(
                ItemList.ExtremeEntityCrusher.get(1),
                bitsd,
                new Object[] { "RCR", "CHC", "VVV", 'R', gregtech.api.enums.ItemList.Robot_Arm_EV, 'C',
                    OrePrefixes.circuit.get(Materials.EV), 'H', gregtech.api.enums.ItemList.Hull_EV, 'V',
                    GT_ModHandler.getModItem(OpenBlocks.ID, "vacuumhopper", 1, new ItemStack(Blocks.hopper)) });
        }

        if (Forestry.isModLoaded()) {
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, gregtech.api.enums.ItemList.Machine_IndustrialApiary.get(1))
                .metadata(RESEARCH_TIME, 8 * MINUTES + 20 * SECONDS)
                .itemInputs(
                    gregtech.api.enums.ItemList.Machine_IndustrialApiary.get(64L),
                    gregtech.api.enums.ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64L),
                    gregtech.api.enums.ItemList.IndustrialApiary_Upgrade_STABILIZER.get(64L),
                    gregtech.api.enums.ItemList.Robot_Arm_UV.get(16L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4L },
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4L },
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4L },
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4L })
                .fluidInputs(
                    FluidRegistry.getFluidStack("molten.indalloy140", 28800),
                    FluidRegistry.getFluidStack("for.honey", 20000))
                .itemOutputs(ExtremeIndustrialApiary.get(1))
                .eut(TierEU.RECIPE_UHV)
                .duration(5 * MINUTES)
                .addTo(AssemblyLine);

            GT_MetaTileEntity_ExtremeIndustrialGreenhouse
                .addFertilizerItem(PluginCore.items.fertilizerCompound.getItemStack(1));
        }

        GT_ModHandler.addCraftingRecipe(
            ExtremeIndustrialGreenhouse.get(1),
            bitsd,
            new Object[] { "AZA", "BRB", "AZA", 'B', gregtech.api.enums.ItemList.Casing_CleanStainlessSteel, 'R',
                GT_ModHandler.getModItem(EnderIO.ID, "blockFarmStation", 1, new ItemStack(Items.diamond_hoe)), 'A',
                gregtech.api.enums.ItemList.AcceleratorIV.get(1), 'Z', OrePrefixes.circuit.get(Materials.ZPM) });

        // Vanilla should always be loaded
        GT_MetaTileEntity_ExtremeIndustrialGreenhouse.addFertilizerItem(new ItemStack(Items.dye, 1, 15));
        // IC2 should always be loaded
        GT_MetaTileEntity_ExtremeIndustrialGreenhouse.addFertilizerItem(Ic2Items.fertilizer);

        if (DraconicEvolution.isModLoaded()) {
            // Controller recipe added in TecTech
            DEFCRecipes.addRecipes();
        }
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
            GT_Values.RA.stdBuilder()
                .itemInputs(GameRegistry.findItemStack("harvestcraft", "tealeafItem", 1))
                .itemOutputs(TeaLeafDehydrated.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(TeaLeafDehydrated.get(1))
                .itemOutputs(WhiteTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(TeaLeafDehydrated.get(1))
                .itemOutputs(SteamedTeaLeaf.get(1))
                .fluidInputs(FluidRegistry.getFluidStack("water", 50))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(mixerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(SteamedTeaLeaf.get(1))
                .itemOutputs(YellowTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(TeaLeafDehydrated.get(1), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(RolledTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(benderRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(RolledTeaLeaf.get(1))
                .itemOutputs(GreenTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(RolledTeaLeaf.get(1), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(OxidizedTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(UniversalChemical);

            GT_Values.RA.stdBuilder()
                .itemInputs(OxidizedTeaLeaf.get(1))
                .itemOutputs(BlackTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(RolledTeaLeaf.get(1), GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(FermentedTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(10 * SECONDS)
                .addTo(UniversalChemical);

            GT_Values.RA.stdBuilder()
                .itemInputs(FermentedTeaLeaf.get(1))
                .itemOutputs(PuerhTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(TeaLeafDehydrated.get(1))
                .itemOutputs(BruisedTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(cutterRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(BruisedTeaLeaf.get(1), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(PartiallyOxidizedTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(2 * SECONDS + 10 * TICKS)
                .addTo(UniversalChemical);

            GT_Values.RA.stdBuilder()
                .itemInputs(PartiallyOxidizedTeaLeaf.get(1))
                .itemOutputs(OolongTeaLeaf.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

            // Tea Assembly
            GameRegistry.addSmelting(BlackTeaLeaf.get(1), BlackTea.get(1), 10);

            GT_Values.RA.stdBuilder()
                .itemInputs(BlackTea.get(1), GameRegistry.findItemStack("harvestcraft", "limejuiceItem", 1))
                .itemOutputs(EarlGrayTea.get(1))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(mixerRecipes);

            GameRegistry.addSmelting(GreenTeaLeaf.get(1), GreenTea.get(1), 10);

            GT_Values.RA.stdBuilder()
                .itemInputs(BlackTea.get(1))
                .itemOutputs(LemonTea.get(1))
                .fluidInputs(FluidRegistry.getFluidStack("potion.lemonjuice", 1000))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(mixerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(BlackTea.get(1))
                .itemOutputs(MilkTea.get(1))
                .fluidInputs(FluidRegistry.getFluidStack("milk", 1000))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(mixerRecipes);

            GameRegistry.addSmelting(OolongTeaLeaf.get(1), OolongTea.get(1), 10);

            GT_Values.RA.stdBuilder()
                .itemInputs(GameRegistry.findItemStack("harvestcraft", "peppermintItem", 1))
                .itemOutputs(PeppermintTea.get(1))
                .fluidInputs(FluidRegistry.getFluidStack("water", 1000))
                .eut(TierEU.RECIPE_LV)
                .duration(5 * SECONDS)
                .addTo(mixerRecipes);

            GameRegistry.addSmelting(PuerhTeaLeaf.get(1), PuerhTea.get(1), 10);
            GameRegistry.addSmelting(WhiteTeaLeaf.get(1), WhiteTea.get(1), 10);
            GameRegistry.addSmelting(YellowTeaLeaf.get(1), YellowTea.get(1), 10);
        }
        if (Avaritia.isModLoaded() && NewHorizonsCoreMod.isModLoaded()) {
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, TeaAcceptorResearchNote.get(1))
                .metadata(RESEARCH_TIME, 8 * MINUTES + 20 * SECONDS)
                .itemInputs(
                    LegendaryUltimateTea.get(0),
                    GameRegistry.findItemStack("Avaritia", "Neutronium_Compressor", 1),
                    gregtech.api.enums.ItemList.Quantum_Tank_EV.get(1),
                    FluidExtractorUHV.get(10),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4L },
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4L },
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4L },
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4L })
                .fluidInputs(FluidRegistry.getFluidStack("molten.indalloy140", 28800))
                .itemOutputs(TeaAcceptor.get(1))
                .eut(TierEU.RECIPE_UHV)
                .duration(5 * MINUTES)
                .addTo(AssemblyLine);
        }
    }
}
