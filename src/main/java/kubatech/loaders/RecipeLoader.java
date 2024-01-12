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

import java.lang.reflect.InvocationTargetException;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import com.dreammaster.gthandler.CustomItemList;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import kubatech.api.LoaderReference;
import kubatech.api.enums.ItemList;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_DEFusionCrafter;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeEntityCrusher;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeIndustrialGreenhouse;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_MegaIndustrialApiary;

public class RecipeLoader {

    protected static final long bitsd = GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE
        | GT_ModHandler.RecipeBits.BUFFERED
        | GT_ModHandler.RecipeBits.DISMANTLEABLE;

    private static int MTEID = 14201;
    private static final int MTEIDMax = 14300;

    public static void addRecipes() {
        if (registerMTE(
            ExtremeEntityCrusher,
            GT_MetaTileEntity_ExtremeEntityCrusher.class,
            "multimachine.entitycrusher",
            "Extreme Entity Crusher",
            LoaderReference.EnderIO)) {
            GT_ModHandler.addCraftingRecipe(
                ItemList.ExtremeEntityCrusher.get(1),
                bitsd,
                new Object[] { "RCR", "CHC", "VVV", 'R', gregtech.api.enums.ItemList.Robot_Arm_EV, 'C',
                    OrePrefixes.circuit.get(Materials.Data), 'H', gregtech.api.enums.ItemList.Hull_EV, 'V',
                    GT_ModHandler.getModItem("OpenBlocks", "vacuumhopper", 1, new ItemStack(Blocks.hopper)) });
        }
        if (registerMTE(
            ExtremeIndustrialApiary,
            GT_MetaTileEntity_MegaIndustrialApiary.class,
            "multimachine.extremeapiary",
            "Industrial Apicultural Acclimatiser and Drone Domestication Station",
            LoaderReference.Forestry)) {
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, gregtech.api.enums.ItemList.Machine_IndustrialApiary.get(1))
                .metadata(RESEARCH_TIME, 8 * MINUTES + 20 * SECONDS)
                .itemInputs(
                    gregtech.api.enums.ItemList.Machine_IndustrialApiary.get(64L),
                    gregtech.api.enums.ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64L),
                    gregtech.api.enums.ItemList.IndustrialApiary_Upgrade_STABILIZER.get(64L),
                    gregtech.api.enums.ItemList.Robot_Arm_UV.get(16L),
                    new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L },
                    new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L },
                    new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L },
                    new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L })
                .fluidInputs(
                    FluidRegistry.getFluidStack("molten.indalloy140", 28800),
                    FluidRegistry.getFluidStack("for.honey", 20000))
                .itemOutputs(ExtremeIndustrialApiary.get(1))
                .eut(2_048_000)
                .duration(5 * MINUTES)
                .addTo(AssemblyLine);
        }
        if (registerMTEUsingID(
            12_792,
            ExtremeIndustrialGreenhouse,
            GT_MetaTileEntity_ExtremeIndustrialGreenhouse.class,
            "multimachine.extremegreenhouse",
            "Extreme Industrial Greenhouse",
            true /* IC2 is always loaded */)) {
            GT_ModHandler.addCraftingRecipe(
                ExtremeIndustrialGreenhouse.get(1),
                bitsd,
                new Object[] { "AZA", "BRB", "AZA", 'B', gregtech.api.enums.ItemList.Casing_CleanStainlessSteel, 'R',
                    GT_ModHandler.getModItem("EnderIO", "blockFarmStation", 1, new ItemStack(Items.diamond_hoe)), 'A',
                    LoaderReference.GTNHCoreMod ? CustomItemList.AcceleratorIV.get(1)
                        : gregtech.api.enums.ItemList.Robot_Arm_IV,
                    'Z', OrePrefixes.circuit.get(Materials.Ultimate) });
        }
        if (registerMTEUsingID(
            5_001,
            DraconicEvolutionFusionCrafter,
            GT_MetaTileEntity_DEFusionCrafter.class,
            "multimachine.defusioncrafter",
            "Draconic Evolution Fusion Crafter",
            LoaderReference.DraconicEvolution)) {
            // Controller recipe added in TecTech
            DEFCRecipes.addRecipes();
        }
        RegisterTeaLine();
        if (MTEID > MTEIDMax + 1) throw new RuntimeException("MTE ID's");
    }

    private static boolean registerMTE(ItemList item, Class<? extends MetaTileEntity> mte, String aName,
        String aNameRegional, boolean... deps) {
        if (MTEID > MTEIDMax) throw new RuntimeException("MTE ID's");
        boolean dep = registerMTEUsingID(MTEID, item, mte, aName, aNameRegional, deps);
        MTEID++;
        return dep;
    }

    private static boolean registerMTEUsingID(int ID, ItemList item, Class<? extends MetaTileEntity> mte, String aName,
        String aNameRegional, boolean... deps) {
        boolean dep = true;
        for (boolean b : deps) {
            if (!b) {
                dep = false;
                break;
            }
        }
        if (dep) {
            try {
                item.set(
                    mte.getConstructor(int.class, String.class, String.class)
                        .newInstance(ID, aName, aNameRegional)
                        .getStackForm(1));
            } catch (InvocationTargetException ex) {
                Throwable original_ex = ex.getCause();
                if (original_ex instanceof RuntimeException) throw (RuntimeException) original_ex;
                throw new RuntimeException(original_ex.getMessage());
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
        return dep;
    }

    private static boolean lateRecipesInitialized = false;

    public static void addRecipesLate() {
        // Runs on server start
        if (lateRecipesInitialized) return;
        lateRecipesInitialized = true;
    }

    private static void RegisterTeaLine() {
        // TEA LINE //
        if (LoaderReference.GTPlusPlus && LoaderReference.HarvestCraft) {
            CORE.RA.addDehydratorRecipe(
                new ItemStack[] { GameRegistry.findItemStack("harvestcraft", "tealeafItem", 1) },
                null,
                null,
                new ItemStack[] { TeaLeafDehydrated.get(1) },
                null,
                100,
                32);
            CORE.RA.addDehydratorRecipe(
                new ItemStack[] { TeaLeafDehydrated.get(1) },
                null,
                null,
                new ItemStack[] { WhiteTeaLeaf.get(1) },
                null,
                100,
                32);
            GT_Values.RA.stdBuilder()
                .itemInputs(TeaLeafDehydrated.get(1))
                .itemOutputs(SteamedTeaLeaf.get(1))
                .fluidInputs(FluidRegistry.getFluidStack("water", 50))
                .eut(32)
                .duration(5 * SECONDS)
                .addTo(mixerRecipes);
            CORE.RA.addDehydratorRecipe(
                new ItemStack[] { SteamedTeaLeaf.get(1) },
                null,
                null,
                new ItemStack[] { YellowTeaLeaf.get(1) },
                null,
                100,
                32);
            GT_Values.RA.stdBuilder()
                .itemInputs(TeaLeafDehydrated.get(1))
                .itemOutputs(RolledTeaLeaf.get(1))
                .eut(32)
                .duration(5 * SECONDS)
                .addTo(benderRecipes);
            CORE.RA.addDehydratorRecipe(
                new ItemStack[] { RolledTeaLeaf.get(1) },
                null,
                null,
                new ItemStack[] { GreenTeaLeaf.get(1) },
                null,
                100,
                32);
            GT_Values.RA.stdBuilder()
                .itemInputs(RolledTeaLeaf.get(1), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(OxidizedTeaLeaf.get(1))
                .eut(32)
                .duration(5 * SECONDS)
                .addTo(UniversalChemical);
            CORE.RA.addDehydratorRecipe(
                new ItemStack[] { OxidizedTeaLeaf.get(1) },
                null,
                null,
                new ItemStack[] { BlackTeaLeaf.get(1) },
                null,
                100,
                32);
            GT_Values.RA.stdBuilder()
                .itemInputs(RolledTeaLeaf.get(1), GT_Utility.getIntegratedCircuit(2))
                .itemOutputs(FermentedTeaLeaf.get(1))
                .eut(32)
                .duration(10 * SECONDS)
                .addTo(UniversalChemical);
            CORE.RA.addDehydratorRecipe(
                new ItemStack[] { FermentedTeaLeaf.get(1) },
                null,
                null,
                new ItemStack[] { PuerhTeaLeaf.get(1) },
                null,
                100,
                32);
            GT_Values.RA.stdBuilder()
                .itemInputs(TeaLeafDehydrated.get(1))
                .itemOutputs(BruisedTeaLeaf.get(1))
                .eut(32)
                .duration(5 * SECONDS)
                .addTo(cutterRecipes);
            GT_Values.RA.stdBuilder()
                .itemInputs(BruisedTeaLeaf.get(1), GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(PartiallyOxidizedTeaLeaf.get(1))
                .eut(32)
                .duration(2 * SECONDS + 10 * TICKS)
                .addTo(UniversalChemical);
            CORE.RA.addDehydratorRecipe(
                new ItemStack[] { PartiallyOxidizedTeaLeaf.get(1) },
                null,
                null,
                new ItemStack[] { OolongTeaLeaf.get(1) },
                null,
                100,
                32);

            // Tea Assembly
            GameRegistry.addSmelting(BlackTeaLeaf.get(1), BlackTea.get(1), 10);
            GT_Values.RA.stdBuilder()
                .itemInputs(BlackTea.get(1), GameRegistry.findItemStack("harvestcraft", "limejuiceItem", 1))
                .itemOutputs(EarlGrayTea.get(1))
                .eut(32)
                .duration(5 * SECONDS)
                .addTo(mixerRecipes);
            GameRegistry.addSmelting(GreenTeaLeaf.get(1), GreenTea.get(1), 10);
            GT_Values.RA.stdBuilder()
                .itemInputs(BlackTea.get(1))
                .itemOutputs(LemonTea.get(1))
                .fluidInputs(FluidRegistry.getFluidStack("potion.lemonjuice", 1000))
                .eut(32)
                .duration(5 * SECONDS)
                .addTo(mixerRecipes);
            GT_Values.RA.stdBuilder()
                .itemInputs(BlackTea.get(1))
                .itemOutputs(MilkTea.get(1))
                .fluidInputs(FluidRegistry.getFluidStack("milk", 1000))
                .eut(32)
                .duration(5 * SECONDS)
                .addTo(mixerRecipes);
            GameRegistry.addSmelting(OolongTeaLeaf.get(1), OolongTea.get(1), 10);
            GT_Values.RA.stdBuilder()
                .itemInputs(GameRegistry.findItemStack("harvestcraft", "peppermintItem", 1))
                .itemOutputs(PeppermintTea.get(1))
                .fluidInputs(FluidRegistry.getFluidStack("water", 1000))
                .eut(32)
                .duration(5 * SECONDS)
                .addTo(mixerRecipes);
            GameRegistry.addSmelting(PuerhTeaLeaf.get(1), PuerhTea.get(1), 10);
            GameRegistry.addSmelting(WhiteTeaLeaf.get(1), WhiteTea.get(1), 10);
            GameRegistry.addSmelting(YellowTeaLeaf.get(1), YellowTea.get(1), 10);
        }
        if (LoaderReference.Avaritia && LoaderReference.GTNHCoreMod) {
            GT_Values.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, TeaAcceptorResearchNote.get(1))
                .metadata(RESEARCH_TIME, 8 * MINUTES + 20 * SECONDS)
                .itemInputs(
                    LegendaryUltimateTea.get(0),
                    GameRegistry.findItemStack("Avaritia", "Neutronium_Compressor", 1),
                    gregtech.api.enums.ItemList.Quantum_Tank_EV.get(1),
                    CustomItemList.FluidExtractorUHV.get(10),
                    new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L },
                    new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L },
                    new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L },
                    new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4L })
                .fluidInputs(FluidRegistry.getFluidStack("molten.indalloy140", 28800))
                .itemOutputs(TeaAcceptor.get(1))
                .eut(2_048_000)
                .duration(5 * MINUTES)
                .addTo(AssemblyLine);
        }
    }
}
