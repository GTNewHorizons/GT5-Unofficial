package gregtech.common;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.Railcraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.internal.IGT_RecipeAdder;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_AssemblyLineUtils;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_AssemblyLine;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_RecipeConstants;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.extensions.ArrayExt;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;

public class GT_RecipeAdder implements IGT_RecipeAdder {

    @Override
    @Deprecated
    public boolean addFusionReactorRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration,
        int aEUt, int aStartEU) {
        return false;
    }

    @Deprecated
    @Override // Really?
    public boolean addFusionReactorRecipe(FluidStack aInput1, FluidStack aInput2, FluidStack aOutput1, int aDuration,
        int aEUt, int aStartEU) {
        if (aInput1 == null || aInput2 == null || aOutput1 == null || aDuration < 1 || aEUt < 1 || aStartEU < 1) {
            return false;
        }
        RecipeMaps.fusionRecipes.addRecipe(
            null,
            new FluidStack[] { aInput1, aInput2 },
            new FluidStack[] { aOutput1 },
            aDuration,
            aEUt,
            aStartEU);
        return true;
    }

    @Deprecated
    @Override
    public boolean addFusionReactorRecipe(FluidStack[] FluidInputArray, FluidStack[] FluidOutputArray,
        int aFusionDurationInTicks, int aFusionEnergyPerTick, int aEnergyNeededForStartingFusion) {
        if (FluidInputArray.length == 0) return false;

        if (FluidOutputArray.length == 0) return false;

        RecipeMaps.fusionRecipes.addRecipe(
            null,
            FluidInputArray,
            FluidOutputArray,
            aFusionDurationInTicks,
            aFusionEnergyPerTick,
            aEnergyNeededForStartingFusion);
        return true;
    }

    @Deprecated
    @Override
    public boolean addCentrifugeRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration) {
        return addCentrifugeRecipe(
            aInput1,
            aInput2 < 0 ? null : aInput2 > 0 ? ItemList.Cell_Empty.get(aInput2) : null,
            null,
            null,
            aOutput1,
            aOutput2,
            aOutput3,
            aOutput4,
            aOutput5,
            aOutput6,
            null,
            aDuration,
            5);
    }

    @Deprecated
    @Override
    public boolean addCentrifugeRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration, int aEUt) {
        return addCentrifugeRecipe(
            aInput1,
            aInput2 < 0 ? null : aInput2 > 0 ? ItemList.Cell_Empty.get(aInput2) : null,
            null,
            null,
            aOutput1,
            aOutput2,
            aOutput3,
            aOutput4,
            aOutput5,
            aOutput6,
            null,
            aDuration,
            aEUt);
    }

    @Deprecated
    @Override
    public boolean addCentrifugeRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
        FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4,
        ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt) {
        return addCentrifugeRecipe(
            aInput1,
            aInput2,
            aFluidInput,
            aFluidOutput,
            aOutput1,
            aOutput2,
            aOutput3,
            aOutput4,
            aOutput5,
            aOutput6,
            aChances,
            aDuration,
            aEUt,
            false);
    }

    @Deprecated
    @Override
    public boolean addCentrifugeRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
        FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4,
        ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt, boolean aCleanroom) {
        if (((aInput1 == null) && (aFluidInput == null)) || ((aOutput1 == null) && (aFluidOutput == null))) {
            return false;
        }
        if ((aInput1 != null) && (aDuration <= 0)) {
            return false;
        }
        if ((aFluidInput != null) && (aDuration <= 0)) {
            return false;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }
        RecipeMaps.centrifugeRecipes.addRecipe(
            true,
            new ItemStack[] { aInput1, aInput2 },
            new ItemStack[] { aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6, },
            null,
            aChances,
            new FluidStack[] { aFluidInput },
            new FluidStack[] { aFluidOutput },
            aDuration,
            aEUt,
            aCleanroom ? -100 : 0);
        ItemStack[] itemInputs = { aInput1, aInput2 };
        ItemStack[] itemOutputs = { aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6 };
        FluidStack[] fluidInputs = { aFluidInput, null, null };
        FluidStack[] fluidOutputs = { aFluidOutput, null, null, null, null, null, null };

        byte iNumber = 0;
        byte oNumber = 0;

        for (ItemStack item : itemInputs) {
            if (item != null) {
                if (GT_Utility.getFluidForFilledItem(aInput1, true) != null || GT_Utility.isCellEmpty(item)) {
                    fluidInputs[iNumber + 1] = GT_Utility.convertCellToFluid(item);
                    itemInputs[iNumber] = null;
                }
            }
            iNumber++;
        }

        for (ItemStack item : itemOutputs) {
            if (item != null) {
                if (GT_Utility.getFluidForFilledItem(item, true) != null || GT_Utility.isCellEmpty(item)) {
                    fluidOutputs[oNumber + 1] = GT_Utility.convertCellToFluid(item);
                    itemOutputs[oNumber] = null;
                }
            }
            oNumber++;
        }

        RecipeMaps.centrifugeNonCellRecipes.addRecipe(
            false,
            itemInputs,
            itemOutputs,
            null,
            aChances,
            fluidInputs,
            fluidOutputs,
            aDuration,
            aEUt,
            aCleanroom ? -100 : 0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addCompressorRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.compressorRecipes.addRecipe(
            true,
            new ItemStack[] { aInput1 },
            new ItemStack[] { aOutput1 },
            null,
            null,
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addElectrolyzerRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, ItemStack aOutput4, ItemStack aOutput5, ItemStack aOutput6, int aDuration, int aEUt) {
        return addElectrolyzerRecipe(
            aInput1,
            aInput2 < 0 ? null : aInput2 > 0 ? ItemList.Cell_Empty.get(aInput2) : null,
            null,
            null,
            aOutput1,
            aOutput2,
            aOutput3,
            aOutput4,
            aOutput5,
            aOutput6,
            null,
            aDuration,
            aEUt);
    }

    @Deprecated
    @Override
    public boolean addElectrolyzerRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
        FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4,
        ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt) {
        if (((aInput1 == null) && (aFluidInput == null)) || ((aOutput1 == null) && (aFluidOutput == null))) {
            return false;
        }
        if ((aInput1 != null) && (aDuration <= 0)) {
            return false;
        }
        if ((aFluidInput != null) && (aDuration <= 0)) {
            return false;
        }
        RecipeMaps.electrolyzerRecipes.addRecipe(
            true,
            new ItemStack[] { aInput1, aInput2 },
            new ItemStack[] { aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6 },
            null,
            aChances,
            new FluidStack[] { aFluidInput },
            new FluidStack[] { aFluidOutput },
            aDuration,
            aEUt,
            0);
        ItemStack[] itemInputs = { aInput1, aInput2 };
        ItemStack[] itemOutputs = { aOutput1, aOutput2, aOutput3, aOutput4, aOutput5, aOutput6 };
        FluidStack[] fluidInputs = { aFluidInput, null, null };
        FluidStack[] fluidOutputs = { aFluidOutput, null, null, null, null, null, null };

        byte iNumber = 0;
        byte oNumber = 0;

        for (ItemStack item : itemInputs) {
            if (item != null) {
                if (GT_Utility.getFluidForFilledItem(item, true) != null || GT_Utility.isCellEmpty(item)
                    || GT_Utility.isAnyIntegratedCircuit(item)) {
                    fluidInputs[iNumber + 1] = GT_Utility.convertCellToFluid(item);
                    itemInputs[iNumber] = null;
                }
            }
            iNumber++;
        }

        for (ItemStack item : itemOutputs) {
            if (item != null) {
                if (GT_Utility.getFluidForFilledItem(item, true) != null || GT_Utility.isCellEmpty(item)) {
                    fluidOutputs[oNumber + 1] = GT_Utility.convertCellToFluid(item);
                    itemOutputs[oNumber] = null;
                }
            }
            oNumber++;
        }

        RecipeMaps.electrolyzerNonCellRecipes
            .addRecipe(false, itemInputs, itemOutputs, null, aChances, fluidInputs, fluidOutputs, aDuration, aEUt, 0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput, int aDuration) {
        return addChemicalRecipe(aInput1, aInput2, null, null, aOutput, aDuration);
    }

    @Deprecated
    @Override
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput, int aDuration, int aEUt) {
        return addChemicalRecipe(aInput1, aInput2, null, null, aOutput, aDuration, aEUt);
    }

    @Deprecated
    @Override
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
        FluidStack aFluidOutput, ItemStack aOutput, int aDuration) {
        return addChemicalRecipe(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration, 30);
    }

    @Deprecated
    @Override
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
        FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration) {
        return addChemicalRecipe(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aOutput2, aDuration, 30);
    }

    @Deprecated
    @Override
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
        FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUTick) {
        return addChemicalRecipe(
            aInput1,
            aInput2,
            aFluidInput,
            aFluidOutput,
            aOutput,
            GT_Values.NI,
            aDuration,
            aEUTick);
    }

    @Deprecated
    @Override
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
        FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick) {
        return addChemicalRecipe(
            aInput1,
            aInput2,
            aFluidInput,
            aFluidOutput,
            aOutput,
            aOutput2,
            aDuration,
            aEUtick,
            false);
    }

    @Deprecated
    @Override
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
        FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick,
        boolean aCleanroom) {
        if (((aInput1 == null) && (aFluidInput == null))
            || ((aOutput == null) && (aOutput2 == null) && (aFluidOutput == null))) {
            return false;
        }
        if ((aOutput != null || aOutput2 != null) && (aDuration <= 0)) {
            return false;
        }
        if ((aFluidOutput != null) && (aDuration <= 0)) {
            return false;
        }
        if (aEUtick <= 0) {
            return false;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }
        GT_RecipeBuilder builder = stdBuilder()
            .itemInputs(ArrayExt.withoutNulls(new ItemStack[] { aInput1, aInput2 }, ItemStack[]::new))
            .itemOutputs(ArrayExt.withoutNulls(new ItemStack[] { aOutput, aOutput2 }, ItemStack[]::new));
        if (aFluidInput != null) builder.fluidInputs(aFluidInput);
        if (aFluidOutput != null) builder.fluidOutputs(aFluidOutput);
        builder.duration(aDuration)
            .eut(aEUtick)
            .metadata(GT_RecipeConstants.CLEANROOM, aCleanroom)
            .addTo(GT_RecipeConstants.UniversalChemical);
        return true;
    }

    @Deprecated
    @Override
    public boolean addMultiblockChemicalRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs,
        FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int aDuration, int aEUtick) {
        if (areItemsAndFluidsBothNull(aInputs, aFluidInputs) || areItemsAndFluidsBothNull(aOutputs, aFluidOutputs)) {
            return false;
        }
        if (aEUtick <= 0) {
            return false;
        }
        RecipeMaps.multiblockChemicalReactorRecipes
            .addRecipe(false, aInputs, aOutputs, null, null, aFluidInputs, aFluidOutputs, aDuration, aEUtick, 0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addChemicalRecipeForBasicMachineOnly(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
        FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick) {
        if (((aInput1 == null) && (aFluidInput == null))
            || ((aOutput == null) && (aOutput2 == null) && (aFluidOutput == null))) {
            return false;
        }
        if ((aOutput != null || aOutput2 != null) && (aDuration <= 0)) {
            return false;
        }
        if ((aFluidOutput != null) && (aDuration <= 0)) {
            return false;
        }
        if (aEUtick <= 0) {
            return false;
        }
        RecipeMaps.chemicalReactorRecipes.addRecipe(
            true,
            new ItemStack[] { aInput1, aInput2 },
            new ItemStack[] { aOutput, aOutput2 },
            null,
            null,
            new FluidStack[] { aFluidInput },
            new FluidStack[] { aFluidOutput },
            aDuration,
            aEUtick,
            0);
        return true;
    }

    @Deprecated
    @Override
    public void addDefaultPolymerizationRecipes(Fluid aBasicMaterial, ItemStack aBasicMaterialCell, Fluid aPolymer) {
        // Oxygen/Titaniumtetrafluoride -> +50% Output each
        addChemicalRecipe(
            ItemList.Cell_Air.get(1),
            GT_Utility.getIntegratedCircuit(1),
            new FluidStack(aBasicMaterial, 144),
            new FluidStack(aPolymer, 144),
            Materials.Empty.getCells(1),
            160);
        addChemicalRecipe(
            Materials.Oxygen.getCells(1),
            GT_Utility.getIntegratedCircuit(1),
            new FluidStack(aBasicMaterial, 144),
            new FluidStack(aPolymer, 216),
            Materials.Empty.getCells(1),
            160);
        addChemicalRecipe(
            aBasicMaterialCell,
            GT_Utility.getIntegratedCircuit(1),
            Materials.Air.getGas(14000),
            new FluidStack(aPolymer, 1000),
            Materials.Empty.getCells(1),
            1120);
        addChemicalRecipe(
            aBasicMaterialCell,
            GT_Utility.getIntegratedCircuit(1),
            Materials.Oxygen.getGas(7000),
            new FluidStack(aPolymer, 1500),
            Materials.Empty.getCells(1),
            1120);
        addMultiblockChemicalRecipe(
            new ItemStack[] { GT_Utility.getIntegratedCircuit(2) },
            new FluidStack[] { new FluidStack(aBasicMaterial, 2160), Materials.Air.getGas(7500),
                Materials.Titaniumtetrachloride.getFluid(100) },
            new FluidStack[] { new FluidStack(aPolymer, 3240) },
            null,
            800,
            30);
        addMultiblockChemicalRecipe(
            new ItemStack[] { GT_Utility.getIntegratedCircuit(2) },
            new FluidStack[] { new FluidStack(aBasicMaterial, 2160), Materials.Oxygen.getGas(7500),
                Materials.Titaniumtetrachloride.getFluid(100) },
            new FluidStack[] { new FluidStack(aPolymer, 4320) },
            null,
            800,
            30);
    }

    @Deprecated
    @Override
    public boolean addBlastRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2,
        int aDuration, int aEUt, int aLevel) {
        return addBlastRecipe(aInput1, aInput2, null, null, aOutput1, aOutput2, aDuration, aEUt, aLevel);
    }

    @Override
    @Deprecated
    public boolean addBlastRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput,
        ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.blastFurnaceRecipes.addRecipe(
            true,
            new ItemStack[] { aInput1, aInput2 },
            new ItemStack[] { aOutput1, aOutput2 },
            null,
            null,
            new FluidStack[] { aFluidInput },
            new FluidStack[] { aFluidOutput },
            aDuration,
            aEUt,
            aLevel);
        return true;
    }

    @Deprecated
    public boolean addBlastRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4,
        FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3,
        ItemStack aOutput4, int aDuration, int aEUt, int aLevel) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.blastFurnaceRecipes.addRecipe(
            true,
            new ItemStack[] { aInput1, aInput2, aInput3, aInput4 },
            new ItemStack[] { aOutput1, aOutput2, aOutput3, aOutput4 },
            null,
            null,
            new FluidStack[] { aFluidInput },
            new FluidStack[] { aFluidOutput },
            aDuration,
            aEUt,
            aLevel);
        return true;
    }

    @Deprecated
    @Override
    public boolean addPlasmaForgeRecipe(ItemStack[] ItemInputArray, FluidStack[] FluidInputArray,
        ItemStack[] OutputItemArray, FluidStack[] FluidOutputArray, int aDuration, int aEUt, int coil_heat_level) {
        RecipeMaps.plasmaForgeRecipes.addRecipe(
            false,
            ItemInputArray,
            OutputItemArray,
            null,
            null,
            FluidInputArray,
            FluidOutputArray,
            aDuration,
            aEUt,
            coil_heat_level);
        return true;
    }

    @Override
    @Deprecated
    public boolean addPrimitiveBlastRecipe(ItemStack aInput1, ItemStack aInput2, int aCoalAmount, ItemStack aOutput1,
        ItemStack aOutput2, int aDuration) {
        if ((aInput1 == null && aInput2 == null) || (aOutput1 == null && aOutput2 == null)) {
            return false;
        }
        if (aCoalAmount <= 0) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        Materials[] coals = new Materials[] { Materials.Coal, Materials.Charcoal };
        for (Materials coal : coals) {
            RecipeMaps.primitiveBlastRecipes.addRecipe(
                true,
                new ItemStack[] { aInput1, aInput2, coal.getGems(aCoalAmount) },
                new ItemStack[] { aOutput1, aOutput2, Materials.DarkAsh.getDustTiny(aCoalAmount) },
                null,
                null,
                null,
                null,
                aDuration,
                0,
                0);
            RecipeMaps.primitiveBlastRecipes.addRecipe(
                true,
                new ItemStack[] { aInput1, aInput2, coal.getDust(aCoalAmount) },
                new ItemStack[] { aOutput1, aOutput2, Materials.DarkAsh.getDustTiny(aCoalAmount) },
                null,
                null,
                null,
                null,
                aDuration,
                0,
                0);
        }
        if (Railcraft.isModLoaded()) {
            RecipeMaps.primitiveBlastRecipes.addRecipe(
                true,
                new ItemStack[] { aInput1, aInput2, RailcraftToolItems.getCoalCoke(aCoalAmount / 2) },
                new ItemStack[] { aOutput1, aOutput2, Materials.Ash.getDustTiny(aCoalAmount / 2) },
                null,
                null,
                null,
                null,
                aDuration * 2 / 3,
                0,
                0);
        }
        if (GTPlusPlus.isModLoaded()) {
            RecipeMaps.primitiveBlastRecipes.addRecipe(
                true,
                new ItemStack[] { aInput1, aInput2,
                    GT_ModHandler.getModItem(GTPlusPlus.ID, "itemCactusCoke", (aCoalAmount * 2L)) },
                new ItemStack[] { aOutput1, aOutput2, Materials.Ash.getDustTiny(aCoalAmount * 2) },
                null,
                null,
                null,
                null,
                aDuration * 2 / 3,
                0,
                0);
            RecipeMaps.primitiveBlastRecipes.addRecipe(
                true,
                new ItemStack[] { aInput1, aInput2,
                    GT_ModHandler.getModItem(GTPlusPlus.ID, "itemSugarCoke", (aCoalAmount * 2L)) },
                new ItemStack[] { aOutput1, aOutput2, Materials.Ash.getDustTiny(aCoalAmount * 2) },
                null,
                null,
                null,
                null,
                aDuration * 2 / 3,
                0,
                0);
        }
        if ((aInput1 == null || aInput1.stackSize <= 6) && (aInput2 == null || aInput2.stackSize <= 6)
            && (aOutput1 == null || aOutput1.stackSize <= 6)
            && (aOutput2 == null || aOutput2.stackSize <= 6)) {
            aInput1 = aInput1 == null ? null : GT_Utility.copyAmount(aInput1.stackSize * 10, aInput1);
            aInput2 = aInput2 == null ? null : GT_Utility.copyAmount(aInput2.stackSize * 10, aInput2);
            aOutput1 = aOutput1 == null ? null : GT_Utility.copyAmount(aOutput1.stackSize * 10, aOutput1);
            aOutput2 = aOutput2 == null ? null : GT_Utility.copyAmount(aOutput2.stackSize * 10, aOutput2);
            for (Materials coal : coals) {
                RecipeMaps.primitiveBlastRecipes.addRecipe(
                    true,
                    new ItemStack[] { aInput1, aInput2, coal.getBlocks(aCoalAmount) },
                    new ItemStack[] { aOutput1, aOutput2, Materials.DarkAsh.getDust(aCoalAmount) },
                    null,
                    null,
                    null,
                    null,
                    aDuration * 10,
                    0,
                    0);
                RecipeMaps.primitiveBlastRecipes.addRecipe(
                    true,
                    new ItemStack[] { aInput1, aInput2, coal.getBlocks(aCoalAmount) },
                    new ItemStack[] { aOutput1, aOutput2, Materials.DarkAsh.getDust(aCoalAmount) },
                    null,
                    null,
                    null,
                    null,
                    aDuration * 10,
                    0,
                    0);
            }
            if (Railcraft.isModLoaded()) {
                RecipeMaps.primitiveBlastRecipes.addRecipe(
                    true,
                    new ItemStack[] { aInput1, aInput2, EnumCube.COKE_BLOCK.getItem(aCoalAmount / 2) },
                    new ItemStack[] { aOutput1, aOutput2, Materials.Ash.getDust(aCoalAmount / 2) },
                    null,
                    null,
                    null,
                    null,
                    aDuration * 20 / 3,
                    0,
                    0);
            }
        }
        return true;
    }

    @Override
    @Deprecated
    public boolean addCannerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2,
        int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.cannerRecipes.addRecipe(
            true,
            aInput2 == null ? new ItemStack[] { aInput1 } : new ItemStack[] { aInput1, aInput2 },
            new ItemStack[] { aOutput1, aOutput2 },
            null,
            null,
            null,
            null,
            aDuration,
            Math.max(aEUt, 1),
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addAlloySmelterRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration,
        int aEUt) {
        return addAlloySmelterRecipe(aInput1, aInput2, aOutput1, aDuration, aEUt, false);
    }

    @Override
    @Deprecated
    public boolean addAlloySmelterRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration,
        int aEUt, boolean hidden) {
        if ((aInput1 == null) || (aOutput1 == null || Materials.Graphite.contains(aInput1))) {
            return false;
        }
        if ((aInput2 == null) && ((OrePrefixes.ingot.contains(aInput1)) || (OrePrefixes.dust.contains(aInput1))
            || (OrePrefixes.gem.contains(aInput1)))) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        GT_Recipe tRecipe = new GT_Recipe(
            true,
            aInput2 == null ? new ItemStack[] { aInput1 } : new ItemStack[] { aInput1, aInput2 },
            new ItemStack[] { aOutput1 },
            null,
            null,
            null,
            null,
            aDuration,
            Math.max(aEUt, 1),
            0);
        if (hidden) {
            tRecipe.mHidden = true;
        }
        RecipeMaps.alloySmelterRecipes.addRecipe(tRecipe);
        return true;
    }

    @Override
    @Deprecated
    public boolean addLatheRecipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.latheRecipes.addRecipe(
            true,
            new ItemStack[] { aInput1 },
            new ItemStack[] { aOutput1, aOutput2 },
            null,
            null,
            null,
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addCutterRecipe(ItemStack aInput, FluidStack aLubricant, ItemStack aOutput1, ItemStack aOutput2,
        int aDuration, int aEUt) {
        if ((aInput == null) || (aLubricant == null) || (aOutput1 == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.cutterRecipes.addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput1, aOutput2 },
            null,
            new FluidStack[] { aLubricant },
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addCutterRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt,
        boolean aCleanroom) {
        return addCutterRecipe(aInput, null, aOutput1, aOutput2, aDuration, aEUt, aCleanroom);
    }

    @Deprecated
    public boolean addCutterRecipe(ItemStack aInput, int aCircuit, ItemStack aOutput1, ItemStack aOutput2,
        int aDuration, int aEUt) {
        return addCutterRecipe(aInput, aCircuit, aOutput1, aOutput2, aDuration, aEUt, false);
    }

    @Deprecated
    public boolean addCutterRecipe(ItemStack aInput, int aCircuit, ItemStack aOutput1, ItemStack aOutput2,
        int aDuration, int aEUt, boolean aCleanroom) {
        return addCutterRecipe(
            aInput,
            GT_Utility.getIntegratedCircuit(aCircuit),
            aOutput1,
            aOutput2,
            aDuration,
            aEUt,
            aCleanroom);
    }

    @Override
    @Deprecated
    public boolean addCutterRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
        return addCutterRecipe(aInput, aOutput1, aOutput2, aDuration, aEUt, false);
    }

    @Override
    @Deprecated
    public boolean addCutterRecipe(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput1, ItemStack aOutput2,
        int aDuration, int aEUt) {
        return addCutterRecipe(aInput, aCircuit, aOutput1, aOutput2, aDuration, aEUt, false);
    }

    @Override
    @Deprecated
    public boolean addCutterRecipe(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput1, ItemStack aOutput2,
        int aDuration, int aEUt, boolean aCleanroom) {
        return addCutterRecipe(
            new ItemStack[] { aInput, aCircuit },
            new ItemStack[] { aOutput1, aOutput2 },
            aDuration,
            aEUt,
            aCleanroom ? -200 : 0);
    }

    @Deprecated
    public boolean addCutterRecipe(ItemStack[] aInputs, ItemStack[] aOutputs, int aDuration, int aEUt,
        boolean aCleanroom) {
        return addCutterRecipe(aInputs, aOutputs, aDuration, aEUt, aCleanroom ? -200 : 0);
    }

    @Override
    @Deprecated
    public boolean addCutterRecipe(ItemStack[] aInputs, ItemStack[] aOutputs, int aDuration, int aEUt, int aSpecial) {
        if ((aInputs == null) || (aOutputs == null) || aInputs.length == 0 || aOutputs.length == 0) {
            return false;
        }
        if (Arrays.stream(aOutputs)
            .noneMatch(Objects::nonNull)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom && aSpecial == -200) {
            aSpecial = 0;
        }
        RecipeMaps.cutterRecipes.addRecipe(
            true,
            aInputs,
            aOutputs,
            null,
            new FluidStack[] { Materials.Water.getFluid(Math.max(4, Math.min(1000, aDuration * aEUt / 320))) },
            null,
            aDuration * 2,
            aEUt,
            aSpecial);
        RecipeMaps.cutterRecipes.addRecipe(
            true,
            aInputs,
            aOutputs,
            null,
            new FluidStack[] { GT_ModHandler.getDistilledWater(Math.max(3, Math.min(750, aDuration * aEUt / 426))) },
            null,
            aDuration * 2,
            aEUt,
            aSpecial);
        RecipeMaps.cutterRecipes.addRecipe(
            true,
            aInputs,
            aOutputs,
            null,
            new FluidStack[] { Materials.Lubricant.getFluid(Math.max(1, Math.min(250, aDuration * aEUt / 1280))) },
            null,
            aDuration,
            aEUt,
            aSpecial);
        return true;
    }

    @Override
    @Deprecated
    public boolean addAssemblerRecipe(ItemStack aInput1, Object aOreDict, int aAmount, FluidStack aFluidInput,
        ItemStack aOutput1, int aDuration, int aEUt) {
        for (ItemStack tStack : GT_OreDictUnificator.getOresImmutable(aOreDict)) {
            if (GT_Utility.isStackValid(tStack)) addAssemblerRecipe(
                aInput1,
                GT_Utility.copyAmount(aAmount, tStack),
                aFluidInput,
                aOutput1,
                aDuration,
                aEUt);
        }
        return true;
    }

    @Override
    @Deprecated
    public boolean addAssemblerRecipe(ItemStack[] aInputs, Object aOreDict, int aAmount, FluidStack aFluidInput,
        ItemStack aOutput1, int aDuration, int aEUt) {
        for (ItemStack tStack : GT_OreDictUnificator.getOresImmutable(aOreDict)) {
            if (GT_Utility.isStackValid(tStack)) {
                ItemStack[] extendedInputs = new ItemStack[aInputs.length + 1];
                System.arraycopy(aInputs, 0, extendedInputs, 0, aInputs.length);
                extendedInputs[aInputs.length] = GT_Utility.copyAmount(aAmount, tStack);
                addAssemblerRecipe(extendedInputs, aFluidInput, aOutput1, aDuration, aEUt);
            }
        }
        return true;
    }

    @Override
    @Deprecated
    public boolean addAssemblerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration,
        int aEUt) {
        return addAssemblerRecipe(
            new ItemStack[] { aInput1, aInput2 == null ? aInput1 : aInput2 },
            null,
            aOutput1,
            aDuration,
            aEUt,
            false);
    }

    @Override
    @Deprecated
    public boolean addAssemblerRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, ItemStack aOutput1,
        int aDuration, int aEUt) {
        return addAssemblerRecipe(new ItemStack[] { aInput1, aInput2 }, aFluidInput, aOutput1, aDuration, aEUt);
    }

    @Override
    @Deprecated
    public boolean addAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration,
        int aEUt) {
        return addAssemblerRecipe(aInputs, aFluidInput, aOutput1, aDuration, aEUt, false);
    }

    @Override
    @Deprecated
    public boolean addAssemblerRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, ItemStack aOutput1,
        int aDuration, int aEUt, boolean aCleanroom) {
        if (aInput2 == null)
            return addAssemblerRecipe(new ItemStack[] { aInput1 }, aFluidInput, aOutput1, aDuration, aEUt, aCleanroom);
        return addAssemblerRecipe(
            new ItemStack[] { aInput1, aInput2 },
            aFluidInput,
            aOutput1,
            aDuration,
            aEUt,
            aCleanroom);
    }

    @Override
    @Deprecated
    public boolean addAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration,
        int aEUt, boolean aCleanroom) {

        if (areItemsAndFluidsBothNull(aInputs, new FluidStack[] { aFluidInput })) {
            return false;
        }

        if (aDuration <= 0) {
            return false;
        }

        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }

        if (!GT_Utility.isStackValid(aOutput1)) {
            return false;
        }

        for (int oreID : OreDictionary.getOreIDs(aOutput1)) {
            if (OreDictionary.getOreName(oreID)
                .startsWith("circuit")) {
                return this.addAssemblerRecipeNonOD(aInputs, aFluidInput, aOutput1, aDuration, aEUt, aCleanroom);
            }
        }

        for (ItemStack aInput : aInputs) {
            if (!GT_Utility.isStackValid(aInput)) {
                GT_FML_LOGGER.debug("GT_RecipeAdder: Invalid input for (" + aOutput1 + ")");
            }
        }

        RecipeMaps.assemblerRecipes.addRecipe(
            true,
            aInputs,
            new ItemStack[] { aOutput1 },
            null,
            new FluidStack[] { aFluidInput },
            null,
            aDuration,
            aEUt,
            aCleanroom ? -200 : 0);

        return true;
    }

    @Deprecated
    public boolean addAssemblerRecipeNonOD(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1,
        int aDuration, int aEUt, boolean aCleanroom) {
        if (areItemsAndFluidsBothNull(aInputs, new FluidStack[] { aFluidInput })) {
            return false;
        }

        if (aDuration <= 0) {
            return false;
        }

        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }

        if (!GT_Utility.isStackValid(aOutput1)) {
            return false;
        }

        RecipeMaps.assemblerRecipes.addRecipe(
            true,
            aInputs,
            new ItemStack[] { aOutput1 },
            null,
            new FluidStack[] { aFluidInput },
            null,
            aDuration,
            aEUt,
            aCleanroom ? -200 : 0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addWiremillRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.wiremillRecipes.addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput },
            null,
            null,
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addWiremillRecipe(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.wiremillRecipes.addRecipe(
            true,
            new ItemStack[] { aInput, aCircuit },
            new ItemStack[] { aOutput },
            null,
            null,
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addPolarizerRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.polarizerRecipes.addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput },
            null,
            null,
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addBenderRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.benderRecipes.addRecipe(
            true,
            new ItemStack[] { aInput1, ItemList.Circuit_Integrated.getWithDamage(0, aInput1.stackSize) },
            new ItemStack[] { aOutput1 },
            null,
            null,
            null,
            null,
            aDuration,
            Math.max(aEUt, 1),
            0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addBenderRecipe(ItemStack aInput1, ItemStack aCircuit, ItemStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        GT_Recipe tRecipe = new GT_Recipe(
            new ItemStack[] { aInput1, aCircuit },
            new ItemStack[] { aOutput1 },
            null,
            null,
            null,
            null,
            aDuration,
            Math.max(aEUt, 1),
            0);
        RecipeMaps.benderRecipes.addRecipe(tRecipe);
        return true;
    }

    @Override
    @Deprecated
    public boolean addExtruderRecipe(ItemStack aInput, ItemStack aShape, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aShape == null) || (aOutput == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.extruderRecipes.addRecipe(
            true,
            new ItemStack[] { aInput, aShape },
            new ItemStack[] { aOutput },
            null,
            null,
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addSlicerRecipe(ItemStack aInput, ItemStack aShape, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aShape == null) || (aOutput == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.slicerRecipes.addRecipe(
            true,
            new ItemStack[] { aInput, aShape },
            new ItemStack[] { aOutput },
            null,
            null,
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addOreWasherRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3,
        FluidStack aFluidInput, int aDuration, int aEUt) {
        if ((aInput == null) || (aFluidInput == null)
            || ((aOutput1 == null) || (aOutput2 == null) || (aOutput3 == null))) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.oreWasherRecipes.addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput1, aOutput2, aOutput3 },
            null,
            new FluidStack[] { aFluidInput },
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addOreWasherRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3,
        FluidStack aFluidInput, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aFluidInput == null)
            || ((aOutput1 == null) || (aOutput2 == null) || (aOutput3 == null))) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.oreWasherRecipes.addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput1, aOutput2, aOutput3 },
            null,
            aChances,
            new FluidStack[] { aFluidInput },
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addImplosionRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if (aInput2 <= 0) {
            return false;
        }
        int tExplosives = Math.min(aInput2, 64);
        int tGunpowder = tExplosives << 1; // Worst
        int tDynamite = Math.max(1, tExplosives >> 1); // good
        @SuppressWarnings("UnnecessaryLocalVariable")
        int tTNT = tExplosives; // Slightly better
        int tITNT = Math.max(1, tExplosives >> 2); // the best
        // new GT_Recipe(aInput1, aInput2, aOutput1, aOutput2);
        if (tGunpowder < 65) {
            RecipeMaps.implosionRecipes.addRecipe(
                true,
                new ItemStack[] { aInput1, ItemList.Block_Powderbarrel.get(tGunpowder) },
                new ItemStack[] { aOutput1, aOutput2 },
                null,
                null,
                null,
                null,
                20,
                30,
                0);
        }
        if (tDynamite < 17) {
            RecipeMaps.implosionRecipes.addRecipe(
                true,
                new ItemStack[] { aInput1, GT_ModHandler.getIC2Item("dynamite", tDynamite, null) },
                new ItemStack[] { aOutput1, aOutput2 },
                null,
                null,
                null,
                null,
                20,
                30,
                0);
        }
        RecipeMaps.implosionRecipes.addRecipe(
            true,
            new ItemStack[] { aInput1, new ItemStack(Blocks.tnt, tTNT) },
            new ItemStack[] { aOutput1, aOutput2 },
            null,
            null,
            null,
            null,
            20,
            30,
            0);
        RecipeMaps.implosionRecipes.addRecipe(
            true,
            new ItemStack[] { aInput1, GT_ModHandler.getIC2Item("industrialTnt", tITNT, null) },
            new ItemStack[] { aOutput1, aOutput2 },
            null,
            null,
            null,
            null,
            20,
            30,
            0);

        return true;
    }

    @Override
    @Deprecated
    public boolean addDistillationRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt) {
        return false;
    }

    @Override
    @Deprecated
    public boolean addUniversalDistillationRecipe(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2,
        int aDuration, int aEUt) {
        for (int i = 0; i < Math.min(aOutputs.length, 11); i++) {
            addDistilleryRecipe(i + 1, aInput, aOutputs[i], aOutput2, aDuration * 2, aEUt / 4, false);
        }

        return addDistillationTowerRecipe(aInput, aOutputs, aOutput2, aDuration, aEUt);
    }

    @Override
    @Deprecated
    public boolean addDistillationTowerRecipe(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2,
        int aDuration, int aEUt) {
        if (aInput == null || aOutputs == null || aOutputs.length < 1 || aOutputs.length > 11) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.distillationTowerRecipes.addRecipe(
            false,
            null,
            new ItemStack[] { aOutput2 },
            null,
            new FluidStack[] { aInput },
            aOutputs,
            aDuration,
            Math.max(1, aEUt),
            0);
        return false;
    }

    @Override
    @Deprecated
    public boolean addDistillationTowerRecipe(FluidStack aInput, ItemStack[] aCircuit, FluidStack[] aOutputs,
        ItemStack aOutput2, int aDuration, int aEUt) {
        if (aInput == null || aOutputs == null || aOutputs.length < 1 || aOutputs.length > 11) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.distillationTowerRecipes.addRecipe(
            false,
            aCircuit,
            new ItemStack[] { aOutput2 },
            null,
            new FluidStack[] { aInput },
            aOutputs,
            aDuration,
            Math.max(1, aEUt),
            0);
        return false;
    }

    @Override
    @Deprecated
    public boolean addUniversalDistillationRecipewithCircuit(FluidStack aInput, ItemStack[] aCircuit,
        FluidStack[] aOutputs, ItemStack aOutput2, int aDuration, int aEUt) {
        for (int i = 0; i < Math.min(aOutputs.length, 11); i++) {
            addDistilleryRecipe(i + 1, aInput, aOutputs[i], aOutput2, aDuration * 2, aEUt / 4, false);
        }
        return addDistillationTowerRecipe(aInput, aCircuit, aOutputs, aOutput2, aDuration, aEUt);
    }

    @Override
    @Deprecated
    public boolean addVacuumFreezerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.vacuumFreezerRecipes.addRecipe(
            false,
            new ItemStack[] { aInput1 },
            new ItemStack[] { aOutput1 },
            null,
            null,
            null,
            aDuration,
            aEUt,
            0);
        FluidStack tInputFluid = GT_Utility.getFluidForFilledItem(aInput1, true);
        FluidStack tOutputFluid = GT_Utility.getFluidForFilledItem(aOutput1, true);
        if (tInputFluid != null && tOutputFluid != null) {
            addVacuumFreezerRecipe(tInputFluid, tOutputFluid, aDuration, aEUt);
        }
        return true;
    }

    @Override
    @Deprecated
    public boolean addVacuumFreezerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        addVacuumFreezerRecipe(aInput1, aOutput1, aDuration, 120);
        FluidStack tInputFluid = GT_Utility.getFluidForFilledItem(aInput1, true);
        FluidStack tOutputFluid = GT_Utility.getFluidForFilledItem(aOutput1, true);
        if (tInputFluid != null && tOutputFluid != null) {
            addVacuumFreezerRecipe(tInputFluid, tOutputFluid, aDuration, 120);
        }
        return true;
    }

    @Override
    @Deprecated
    public boolean addVacuumFreezerRecipe(FluidStack aInput1, FluidStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        RecipeMaps.vacuumFreezerRecipes.addRecipe(
            false,
            null,
            null,
            null,
            new FluidStack[] { aInput1 },
            new FluidStack[] { aOutput1 },
            Math.max(1, aDuration),
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addVacuumFreezerRecipe(ItemStack[] aItemInput, FluidStack[] aFluidInput, ItemStack[] aItemOutput,
        FluidStack[] aFluidOutput, int aDuration, int aEUt) {
        RecipeMaps.vacuumFreezerRecipes.addRecipe(
            false,
            aItemInput,
            aItemOutput,
            null,
            aFluidInput,
            aFluidOutput,
            Math.max(1, aDuration),
            Math.max(1, aEUt),
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addGrinderRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, ItemStack aOutput4) {
        return false;
    }

    @Override
    @Deprecated
    public boolean addFuel(ItemStack aInput1, ItemStack aOutput1, int aEU, int aType) {
        if (aInput1 == null) {
            return false;
        }
        new GT_Recipe(aInput1, aOutput1, null, null, null, aEU, aType);
        return true;
    }

    @Override
    @Deprecated
    public boolean addSonictronSound(ItemStack aItemStack, String aSoundName) {
        if ((aItemStack == null) || (aSoundName == null) || (aSoundName.equals(""))) {
            return false;
        }
        GT_Mod.gregtechproxy.mSoundItems.add(aItemStack);
        GT_Mod.gregtechproxy.mSoundNames.add(aSoundName);
        if (aSoundName.startsWith("note.")) {
            GT_Mod.gregtechproxy.mSoundCounts.add(25);
        } else {
            GT_Mod.gregtechproxy.mSoundCounts.add(1);
        }
        return true;
    }

    @Deprecated
    @Override
    public boolean addChemicalBathRecipe(ItemStack aInput, FluidStack aBathingFluid, ItemStack aOutput1,
        ItemStack aOutput2, ItemStack aOutput3, FluidStack aFluidOutput, int[] aChances, int aDuration, int aEUt) {
        return false;
    }

    @Deprecated
    @Override
    public boolean addForgeHammerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        RecipeMaps.hammerRecipes.addRecipe(
            true,
            new ItemStack[] { aInput1 },
            new ItemStack[] { aOutput1 },
            null,
            null,
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addForgeHammerRecipe(ItemStack[] ItemInputArray, FluidStack[] FluidInputArray,
        ItemStack[] ItemOutputArray, FluidStack[] FluidOutputArray, int aDuration, int aEUt) {

        RecipeMaps.hammerRecipes.addRecipe(
            true,
            ItemInputArray,
            ItemOutputArray,
            null,
            FluidInputArray,
            FluidOutputArray,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addBoxingRecipe(ItemStack aContainedItem, ItemStack aEmptyBox, ItemStack aFullBox, int aDuration,
        int aEUt) {
        if ((aContainedItem == null) || (aFullBox == null)) {
            return false;
        }
        RecipeMaps.packagerRecipes.addRecipe(
            true,
            new ItemStack[] { aContainedItem, aEmptyBox },
            new ItemStack[] { aFullBox },
            null,
            null,
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addUnboxingRecipe(ItemStack aFullBox, ItemStack aContainedItem, ItemStack aEmptyBox, int aDuration,
        int aEUt) {
        if ((aFullBox == null) || (aContainedItem == null)) {
            return false;
        }
        RecipeMaps.unpackagerRecipes.addRecipe(
            true,
            new ItemStack[] { aFullBox },
            new ItemStack[] { aContainedItem, aEmptyBox },
            null,
            null,
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addThermalCentrifugeRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput1 == null)) {
            return false;
        }
        RecipeMaps.thermalCentrifugeRecipes.addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput1, aOutput2, aOutput3 },
            null,
            null,
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addThermalCentrifugeRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput1 == null)) {
            return false;
        }
        RecipeMaps.thermalCentrifugeRecipes.addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput1, aOutput2, aOutput3 },
            null,
            aChances,
            null,
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addAmplifier(ItemStack aAmplifierItem, int aDuration, int aAmplifierAmountOutputted) {
        if ((aAmplifierItem == null) || (aAmplifierAmountOutputted <= 0)) {
            return false;
        }
        RecipeMaps.amplifierRecipes.addRecipe(
            true,
            new ItemStack[] { aAmplifierItem },
            null,
            null,
            null,
            new FluidStack[] { Materials.UUAmplifier.getFluid(aAmplifierAmountOutputted) },
            aDuration,
            30,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addBrewingRecipe(ItemStack aIngredient, Fluid aInput, Fluid aOutput, int aDuration, int aEUt,
        boolean aHidden) {
        if ((aIngredient == null) || (aInput == null) || (aOutput == null)) {
            return false;
        }
        GT_Recipe tRecipe = RecipeMaps.brewingRecipes.addRecipe(
            false,
            new ItemStack[] { aIngredient },
            null,
            null,
            new FluidStack[] { new FluidStack(aInput, 750) },
            new FluidStack[] { new FluidStack(aOutput, 750) },
            aDuration,
            aEUt,
            0);
        if ((aHidden) && (tRecipe != null)) {
            tRecipe.mHidden = true;
        }
        return true;
    }

    @Override
    @Deprecated
    public boolean addBrewingRecipe(ItemStack aIngredient, Fluid aInput, Fluid aOutput, boolean aHidden) {
        return addBrewingRecipe(aIngredient, aInput, aOutput, 128, 4, aHidden);
    }

    @Override
    @Deprecated
    public boolean addBrewingRecipeCustom(ItemStack aIngredient, FluidStack aInput, FluidStack aOutput, int aDuration,
        int aEUt, boolean aHidden) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        GT_Recipe tRecipe = RecipeMaps.brewingRecipes.addRecipe(
            false,
            new ItemStack[] { aIngredient },
            null,
            null,
            new FluidStack[] { aInput },
            new FluidStack[] { aOutput },
            aDuration,
            aEUt,
            0);
        if ((aHidden) && (tRecipe != null)) {
            tRecipe.mHidden = true;
        }
        return true;
    }

    @Override
    @Deprecated
    public boolean addFermentingRecipe(FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt,
        boolean aHidden) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        GT_Recipe tRecipe = RecipeMaps.fermentingRecipes.addRecipe(
            false,
            null,
            null,
            null,
            new FluidStack[] { aInput },
            new FluidStack[] { aOutput },
            aDuration,
            aEUt,
            0);
        if ((aHidden) && (tRecipe != null)) {
            tRecipe.mHidden = true;
        }
        return true;
    }

    @Override
    @Deprecated
    public boolean addFermentingRecipe(FluidStack aInput, FluidStack aOutput, int aDuration, boolean aHidden) {
        return addFermentingRecipe(aInput, aOutput, aDuration, 2, aHidden);
    }

    @Override
    @Deprecated
    public boolean addDistilleryRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput,
        ItemStack aSolidOutput, int aDuration, int aEUt, boolean aHidden) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        // reduce the batch size if fluid amount is exceeding
        int tScale = (Math.max(aInput.amount, aOutput.amount) + 999) / 1000;
        if (tScale <= 0) tScale = 1;
        if (tScale > 1) {
            // trying to find whether there is a better factor
            for (int i = tScale; i <= 5; i++) {
                if (aInput.amount % i == 0 && aDuration % i == 0) {
                    tScale = i;
                    break;
                }
            }
            for (int i = tScale; i <= 5; i++) {
                if (aInput.amount % i == 0 && aDuration % i == 0 && aOutput.amount % i == 0) {
                    tScale = i;
                    break;
                }
            }
            aInput = new FluidStack(aInput.getFluid(), (aInput.amount + tScale - 1) / tScale);
            aOutput = new FluidStack(aOutput.getFluid(), aOutput.amount / tScale);
            if (aSolidOutput != null) {
                ItemData tData = GT_OreDictUnificator.getItemData(aSolidOutput);
                if (tData != null && (tData.mPrefix == OrePrefixes.dust
                    || OrePrefixes.dust.mFamiliarPrefixes.contains(tData.mPrefix))) {
                    aSolidOutput = GT_OreDictUnificator
                        .getDust(tData.mMaterial.mMaterial, tData.mMaterial.mAmount * aSolidOutput.stackSize / tScale);
                } else {
                    if (aSolidOutput.stackSize / tScale == 0) aSolidOutput = GT_Values.NI;
                    else aSolidOutput = new ItemStack(aSolidOutput.getItem(), aSolidOutput.stackSize / tScale);
                }
            }
            aDuration = (aDuration + tScale - 1) / tScale;
        }

        GT_Recipe tRecipe = RecipeMaps.distilleryRecipes.addRecipe(
            true,
            new ItemStack[] { aCircuit },
            new ItemStack[] { aSolidOutput },
            null,
            new FluidStack[] { aInput },
            new FluidStack[] { aOutput },
            aDuration,
            aEUt,
            0);
        if ((aHidden) && (tRecipe != null)) {
            tRecipe.mHidden = true;
        }
        return true;
    }

    @Override
    @Deprecated
    public boolean addDistilleryRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration,
        int aEUt, boolean aHidden) {
        return addDistilleryRecipe(aCircuit, aInput, aOutput, null, aDuration, aEUt, aHidden);
    }

    @Override
    @Deprecated
    public boolean addDistilleryRecipe(int circuitConfig, FluidStack aInput, FluidStack aOutput, ItemStack aSolidOutput,
        int aDuration, int aEUt, boolean aHidden) {
        return addDistilleryRecipe(
            GT_Utility.getIntegratedCircuit(circuitConfig),
            aInput,
            aOutput,
            aSolidOutput,
            aDuration,
            aEUt,
            aHidden);
    }

    @Override
    @Deprecated
    public boolean addDistilleryRecipe(int circuitConfig, FluidStack aInput, FluidStack aOutput, int aDuration,
        int aEUt, boolean aHidden) {
        return addDistilleryRecipe(
            GT_Utility.getIntegratedCircuit(circuitConfig),
            aInput,
            aOutput,
            aDuration,
            aEUt,
            aHidden);
    }

    @Override
    @Deprecated
    public boolean addFluidSolidifierRecipe(final ItemStack[] itemInputs, final FluidStack[] fluidInputs,
        final ItemStack[] itemOutputs, final FluidStack[] fluidOutputs, final int EUPerTick,
        final int aDurationInTicks) {
        RecipeMaps.fluidSolidifierRecipes
            .addRecipe(true, itemInputs, itemOutputs, null, fluidInputs, fluidOutputs, aDurationInTicks, EUPerTick, 0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addFluidSolidifierRecipe(ItemStack aMold, FluidStack aInput, ItemStack aOutput, int aDuration,
        int aEUt) {
        if ((aMold == null) || (aInput == null) || (aOutput == null)) {
            return false;
        }
        if (aInput.isFluidEqual(Materials.PhasedGold.getMolten(144))) {
            aInput = Materials.VibrantAlloy.getMolten(aInput.amount);
        }
        if (aInput.isFluidEqual(Materials.PhasedIron.getMolten(144))) {
            aInput = Materials.PulsatingIron.getMolten(aInput.amount);
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.fluidSolidifierRecipes.addRecipe(
            true,
            new ItemStack[] { aMold },
            new ItemStack[] { aOutput },
            null,
            new FluidStack[] { aInput },
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addFluidSmelterRecipe(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance,
        int aDuration, int aEUt) {
        return addFluidSmelterRecipe(aInput, aRemains, aOutput, aChance, aDuration, aEUt, false);
    }

    @Override
    @Deprecated
    public boolean addFluidSmelterRecipe(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance,
        int aDuration, int aEUt, boolean hidden) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        if (aOutput.isFluidEqual(Materials.PhasedGold.getMolten(1))) {
            aOutput = Materials.VibrantAlloy.getMolten(aOutput.amount);
        }
        if (aOutput.isFluidEqual(Materials.PhasedIron.getMolten(1))) {
            aOutput = Materials.PulsatingIron.getMolten(aOutput.amount);
        }
        if (aDuration <= 0) {
            return false;
        }
        GT_Recipe tRecipe = RecipeMaps.fluidExtractionRecipes.addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aRemains },
            null,
            new int[] { aChance },
            null,
            new FluidStack[] { aOutput },
            aDuration,
            aEUt,
            0);
        if ((hidden) && (tRecipe != null)) {
            tRecipe.mHidden = true;
        }
        return true;
    }

    @Deprecated
    @Override
    public boolean addFluidExtractionRecipe(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance,
        int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        if (aOutput.isFluidEqual(Materials.PhasedGold.getMolten(1))) {
            aOutput = Materials.VibrantAlloy.getMolten(aOutput.amount);
        }
        if (aOutput.isFluidEqual(Materials.PhasedIron.getMolten(1))) {
            aOutput = Materials.PulsatingIron.getMolten(aOutput.amount);
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.fluidExtractionRecipes.addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aRemains },
            null,
            new int[] { aChance },
            null,
            new FluidStack[] { aOutput },
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addFluidCannerRecipe(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput,
        FluidStack aFluidOutput) {
        int aDuration = aFluidOutput == null ? aFluidInput.amount / 62 : aFluidOutput.amount / 62;

        if (aInput == null || aOutput == null) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.fluidCannerRecipes.addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput },
            null,
            new FluidStack[] { aFluidInput },
            new FluidStack[] { aFluidOutput },
            aDuration,
            1,
            0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addFluidCannerRecipe(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput,
        FluidStack aFluidOutput, int aDuration, int aEUt) {
        if (aInput == null || aOutput == null) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.fluidCannerRecipes.addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput },
            null,
            new FluidStack[] { aFluidInput },
            new FluidStack[] { aFluidOutput },
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addChemicalBathRecipe(ItemStack aInput, FluidStack aBathingFluid, ItemStack aOutput1,
        ItemStack aOutput2, ItemStack aOutput3, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aBathingFluid == null) || (aOutput1 == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.chemicalBathRecipes.addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput1, aOutput2, aOutput3 },
            null,
            aChances,
            new FluidStack[] { aBathingFluid },
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addChemicalBathRecipe(ItemStack aInput, FluidStack aBathingFluid, FluidStack aFluidOutput,
        ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aBathingFluid == null) || (aOutput1 == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.chemicalBathRecipes.addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput1, aOutput2, aOutput3 },
            null,
            aChances,
            new FluidStack[] { aBathingFluid },
            new FluidStack[] { aFluidOutput },
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addElectromagneticSeparatorRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput1 == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.electroMagneticSeparatorRecipes.addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput1, aOutput2, aOutput3 },
            null,
            aChances,
            null,
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addExtractorRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.extractorRecipes.addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput },
            null,
            null,
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addPrinterRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aSpecialSlot, ItemStack aOutput,
        int aDuration, int aEUt) {
        if ((aInput == null) || (aFluid == null) || (aOutput == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.printerRecipes.addRecipe(
            true,
            new ItemStack[] { aInput },
            new ItemStack[] { aOutput },
            aSpecialSlot,
            null,
            new FluidStack[] { aFluid },
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addAutoclaveRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance,
        int aDuration, int aEUt, boolean aCleanroom) {
        return addAutoclaveRecipe(aInput, aFluid, aOutput, aChance, aDuration, aEUt);
    }

    @Override
    @Deprecated
    public boolean addAutoclaveRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance,
        int aDuration, int aEUt) {
        return addAutoclaveRecipe(aInput, null, aFluid, aOutput, aChance, aDuration, aEUt, false);
    }

    @Deprecated
    public boolean addAutoclaveRecipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluid, ItemStack aOutput,
        int aChance, int aDuration, int aEUt) {
        return addAutoclaveRecipe(aInput, aCircuit, aFluid, aOutput, aChance, aDuration, aEUt, false);
    }

    @Override
    @Deprecated
    public boolean addAutoclaveRecipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluidIn, ItemStack aOutput,
        int aChance, int aDuration, int aEUt, boolean aCleanroom) {
        return addAutoclaveRecipe(aInput, aCircuit, aFluidIn, null, aOutput, aChance, aDuration, aEUt, aCleanroom);
    }

    @Override
    @Deprecated
    public boolean addAutoclaveRecipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluidIn, FluidStack aFluidOut,
        ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom) {
        if ((aInput == null) || (aFluidIn == null) || (aOutput == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }
        RecipeMaps.autoclaveRecipes.addRecipe(
            true,
            new ItemStack[] { aInput, aCircuit },
            new ItemStack[] { aOutput },
            null,
            new int[] { aChance },
            new FluidStack[] { aFluidIn },
            new FluidStack[] { aFluidOut },
            aDuration,
            aEUt,
            aCleanroom ? -200 : 0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addAutoclaveSpaceRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance,
        int aDuration, int aEUt, boolean aCleanroom) {
        return addAutoclaveRecipe(aInput, aFluid, aOutput, aChance, aDuration, aEUt, aCleanroom);
    }

    @Override
    @Deprecated
    public boolean addAutoclaveSpaceRecipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluid, ItemStack aOutput,
        int aChance, int aDuration, int aEUt, boolean aCleanroom) {
        if ((aInput == null) || (aFluid == null) || (aOutput == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }
        RecipeMaps.autoclaveRecipes.addRecipe(
            true,
            new ItemStack[] { aInput, aCircuit },
            new ItemStack[] { aOutput },
            null,
            new int[] { aChance },
            new FluidStack[] { aFluid },
            null,
            aDuration,
            aEUt,
            aCleanroom ? -100 : 0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addAutoclave4Recipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluidIn, FluidStack aFluidOut,
        ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt, boolean aCleanroom) {
        if ((aInput == null) || (aFluidIn == null) || (aOutputs == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }
        RecipeMaps.autoclaveRecipes.addRecipe(
            true,
            new ItemStack[] { aInput, aCircuit },
            aOutputs,
            null,
            aChances,
            new FluidStack[] { aFluidIn },
            new FluidStack[] { aFluidOut },
            aDuration,
            aEUt,
            aCleanroom ? -200 : 0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4,
        FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt) {
        return addMixerRecipe(
            aInput1,
            aInput2,
            aInput3,
            aInput4,
            null,
            null,
            null,
            null,
            null,
            aFluidInput,
            aFluidOutput,
            aOutput,
            aDuration,
            aEUt);
    }

    @Deprecated
    @Override
    public boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4,
        ItemStack aInput5, ItemStack aInput6, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput,
        int aDuration, int aEUt) {
        return addMixerRecipe(
            aInput1,
            aInput2,
            aInput3,
            aInput4,
            aInput5,
            aInput6,
            null,
            null,
            null,
            aFluidInput,
            aFluidOutput,
            aOutput,
            aDuration,
            aEUt);
    }

    @Deprecated
    @Override
    public boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4,
        ItemStack aInput5, ItemStack aInput6, ItemStack aInput7, ItemStack aInput8, ItemStack aInput9,
        FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt) {
        if (((aInput1 == null) && (aFluidInput == null)) || ((aOutput == null) && (aFluidOutput == null))) {
            return false;
        }
        if ((aOutput != null) && (aDuration <= 0)) {
            return false;
        }
        if ((aFluidOutput != null) && (aDuration <= 0)) {
            return false;
        }
        RecipeMaps.mixerRecipes.addRecipe(
            true,
            new ItemStack[] { aInput1, aInput2, aInput3, aInput4, aInput5, aInput6, aInput7, aInput8, aInput9 },
            new ItemStack[] { aOutput },
            null,
            null,
            new FluidStack[] { aFluidInput },
            new FluidStack[] { aFluidOutput },
            aDuration,
            aEUt,
            0);
        ItemStack[] itemInputs = { aInput1, aInput2, aInput3, aInput4, aInput5, aInput6, aInput7, aInput8, aInput9 };
        ItemStack[] itemOutputs = { aOutput };
        FluidStack[] fluidInputs = { aFluidInput, null, null, null, null, null, null, null, null, null };
        FluidStack[] fluidOutputs = { aFluidOutput, null };

        byte iNumber = 0;
        byte oNumber = 0;

        for (ItemStack item : itemInputs) {
            if (item != null) {
                if (GT_Utility.getFluidForFilledItem(aInput1, true) != null || GT_Utility.isCellEmpty(item)) {
                    fluidInputs[iNumber + 1] = GT_Utility.convertCellToFluid(item);
                    itemInputs[iNumber] = null;
                }
            }
            iNumber++;
        }

        for (ItemStack item : itemOutputs) {
            if (item != null) {
                if (GT_Utility.getFluidForFilledItem(item, true) != null || GT_Utility.isCellEmpty(item)) {
                    fluidOutputs[oNumber + 1] = GT_Utility.convertCellToFluid(item);
                    itemOutputs[oNumber] = null;
                }
            }
            oNumber++;
        }

        RecipeMaps.mixerNonCellRecipes
            .addRecipe(false, itemInputs, itemOutputs, null, null, fluidInputs, fluidOutputs, aDuration, aEUt, 0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addMixerRecipe(ItemStack[] ItemInputArray, FluidStack[] FluidInputArray, ItemStack[] ItemOutputArray,
        FluidStack[] FluidOutputArray, int aDuration, int aEUt) {
        RecipeMaps.mixerRecipes.addRecipe(
            false,
            ItemInputArray,
            ItemOutputArray,
            null,
            null,
            FluidInputArray,
            FluidOutputArray,
            aDuration,
            aEUt,
            0);
        List<ItemStack> tItemInputList;
        if (ItemInputArray == null) {
            tItemInputList = new ArrayList<>(1);
        } else {
            tItemInputList = new ArrayList<>(Arrays.asList(ItemInputArray));
        }
        List<FluidStack> tFluidInputList;
        if (FluidInputArray != null) {
            tFluidInputList = new ArrayList<>(Arrays.asList(FluidInputArray));
        } else {
            tFluidInputList = new ArrayList<>(1);
        }
        for (int i = 0; i < tItemInputList.size(); i++) {
            if (tItemInputList.get(i) != null) {
                if (GT_Utility.getFluidForFilledItem(tItemInputList.get(i), true) != null
                    || GT_Utility.isCellEmpty(tItemInputList.get(i))) {
                    tFluidInputList.add(GT_Utility.convertCellToFluid(tItemInputList.get(i)));
                    tItemInputList.set(i, null);
                }
            }
        }
        List<ItemStack> tItemOutputList;
        if (ItemOutputArray == null) {
            tItemOutputList = new ArrayList<>(1);
        } else {
            tItemOutputList = new ArrayList<>(Arrays.asList(ItemOutputArray));
        }
        List<FluidStack> tFluidOutputList;
        if (FluidOutputArray != null) {
            tFluidOutputList = new ArrayList<>(Arrays.asList(FluidOutputArray));
        } else {
            tFluidOutputList = new ArrayList<>(1);
        }
        for (int i = 0; i < tItemOutputList.size(); i++) {
            if (tItemOutputList.get(i) != null) {
                if (GT_Utility.getFluidForFilledItem(tItemOutputList.get(i), true) != null
                    || GT_Utility.isCellEmpty(tItemOutputList.get(i))) {
                    tFluidInputList.add(GT_Utility.convertCellToFluid(tItemOutputList.get(i)));
                    tItemOutputList.set(i, null);
                }
            }
        }

        ItemStack[] tItemInputArray = new ItemStack[tItemInputList.size()];
        for (int i = 0; i < tItemInputArray.length; i++) tItemInputArray[i] = tItemInputList.get(i);

        FluidStack[] tFluidInputArray = new FluidStack[tFluidInputList.size()];
        for (int i = 0; i < tFluidInputArray.length; i++) tFluidInputArray[i] = tFluidInputList.get(i);

        ItemStack[] tItemOutputArray = new ItemStack[tItemOutputList.size()];
        for (int i = 0; i < tItemOutputArray.length; i++) tItemOutputArray[i] = tItemOutputList.get(i);

        FluidStack[] tFluidOutputArray = new FluidStack[tFluidOutputList.size()];
        for (int i = 0; i < tFluidOutputArray.length; i++) tFluidOutputArray[i] = tFluidOutputList.get(i);

        RecipeMaps.mixerNonCellRecipes.addRecipe(
            false,
            tItemInputArray,
            tItemOutputArray,
            null,
            null,
            tFluidInputArray,
            tFluidOutputArray,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4,
        ItemStack aInput5, ItemStack aInput6, ItemStack aInput7, ItemStack aInput8, ItemStack aInput9,
        FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3,
        ItemStack aOutput4, int aDuration, int aEUt) {
        if (((aInput1 == null) && (aFluidInput == null)) || ((aOutput1 == null) && (aFluidOutput == null))) {
            return false;
        }
        if ((aOutput1 != null) && (aDuration <= 0)) {
            return false;
        }
        if ((aFluidOutput != null) && (aDuration <= 0)) {
            return false;
        }
        RecipeMaps.mixerRecipes.addRecipe(
            true,
            new ItemStack[] { aInput1, aInput2, aInput3, aInput4, aInput5, aInput6, aInput7, aInput8, aInput9 },
            new ItemStack[] { aOutput1, aOutput2, aOutput3, aOutput4 },
            null,
            null,
            new FluidStack[] { aFluidInput },
            new FluidStack[] { aFluidOutput },
            aDuration,
            aEUt,
            0);
        ItemStack[] itemInputs = { aInput1, aInput2, aInput3, aInput4, aInput5, aInput6, aInput7, aInput8, aInput9 };
        ItemStack[] itemOutputs = { aOutput1, aOutput2, aOutput3, aOutput4 };
        FluidStack[] fluidInputs = { aFluidInput, null, null, null, null, null, null, null, null, null };
        FluidStack[] fluidOutputs = { aFluidOutput, null, null, null, null };

        byte iNumber = 0;
        byte oNumber = 0;

        for (ItemStack item : itemInputs) {
            if (item != null) {
                if (GT_Utility.getFluidForFilledItem(aInput1, true) != null || GT_Utility.isCellEmpty(item)) {
                    fluidInputs[iNumber + 1] = GT_Utility.convertCellToFluid(item);
                    itemInputs[iNumber] = null;
                }
            }
            iNumber++;
        }

        for (ItemStack item : itemOutputs) {
            if (item != null) {
                if (GT_Utility.getFluidForFilledItem(item, true) != null || GT_Utility.isCellEmpty(item)) {
                    fluidOutputs[oNumber + 1] = GT_Utility.convertCellToFluid(item);
                    itemOutputs[oNumber] = null;
                }
            }
            oNumber++;
        }

        RecipeMaps.mixerNonCellRecipes
            .addRecipe(false, itemInputs, itemOutputs, null, null, fluidInputs, fluidOutputs, aDuration, aEUt, 0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addLaserEngraverRecipe(ItemStack aItemToEngrave, ItemStack aLens, ItemStack aEngravedItem,
        int aDuration, int aEUt) {
        return addLaserEngraverRecipe(aItemToEngrave, aLens, aEngravedItem, aDuration, aEUt, false);
    }

    @Deprecated
    @Override
    public boolean addLaserEngraverRecipe(ItemStack aItemToEngrave, ItemStack aLens, ItemStack aEngravedItem,
        int aDuration, int aEUt, boolean aCleanroom) {
        if ((aItemToEngrave == null) || (aLens == null) || (aEngravedItem == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }
        RecipeMaps.laserEngraverRecipes.addRecipe(
            true,
            new ItemStack[] { aItemToEngrave, aLens },
            new ItemStack[] { aEngravedItem },
            null,
            null,
            null,
            aDuration,
            aEUt,
            aCleanroom ? -200 : 0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addLaserEngraverRecipe(ItemStack[] ItemInputArray, FluidStack[] FluidInputArray,
        ItemStack[] OutputItemArray, FluidStack[] FluidOutputArray, int aDuration, int aEUt, boolean aCleanroom) {
        RecipeMaps.laserEngraverRecipes.addRecipe(
            false,
            ItemInputArray,
            OutputItemArray,
            null,
            null,
            FluidInputArray,
            FluidOutputArray,
            aDuration,
            aEUt,
            aCleanroom ? -200 : 0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addFormingPressRecipe(ItemStack aItemToImprint, ItemStack aForm, ItemStack aImprintedItem,
        int aDuration, int aEUt) {
        if ((aItemToImprint == null) || (aForm == null) || (aImprintedItem == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.formingPressRecipes.addRecipe(
            true,
            new ItemStack[] { aItemToImprint, aForm },
            new ItemStack[] { aImprintedItem },
            null,
            null,
            null,
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addFormingPressRecipe(ItemStack[] ItemInputArray, ItemStack[] OutputItemArray, int aDuration,
        int aEUt) {
        if ((ItemInputArray == null) || (OutputItemArray == null)) {
            return false;
        }
        RecipeMaps.formingPressRecipes
            .addRecipe(true, ItemInputArray, OutputItemArray, null, null, null, aDuration, aEUt, 0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addFluidHeaterRecipe(ItemStack aItem, FluidStack aOutput, int aDuration, int aEUt) {
        if ((aItem == null) || (aOutput == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.fluidHeaterRecipes.addRecipe(
            true,
            new ItemStack[] { aItem },
            null,
            null,
            null,
            new FluidStack[] { aOutput },
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addFluidHeaterRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration,
        int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.fluidHeaterRecipes.addRecipe(
            true,
            new ItemStack[] { aCircuit },
            null,
            null,
            new FluidStack[] { aInput },
            new FluidStack[] { aOutput },
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addSifterRecipe(ItemStack aItemToSift, ItemStack[] aSiftedItems, int[] aChances, int aDuration,
        int aEUt) {
        if ((aItemToSift == null) || (aSiftedItems == null)) {
            return false;
        }
        for (ItemStack tStack : aSiftedItems) {
            if (tStack != null) {
                if (aDuration <= 0) {
                    return false;
                }
                RecipeMaps.sifterRecipes.addRecipe(
                    true,
                    new ItemStack[] { aItemToSift },
                    aSiftedItems,
                    null,
                    aChances,
                    null,
                    null,
                    aDuration,
                    aEUt,
                    0);
                return true;
            }
        }
        return false;
    }

    @Deprecated
    @Override
    public boolean addSifterRecipe(ItemStack[] ItemInputArray, FluidStack[] FluidInputArray,
        ItemStack[] OutputItemArray, FluidStack[] FluidOutputArray, int[] aChances, int aDuration, int aEUt,
        boolean aCleanroom) {
        RecipeMaps.sifterRecipes.addRecipe(
            false,
            ItemInputArray,
            OutputItemArray,
            null,
            aChances,
            FluidInputArray,
            FluidOutputArray,
            aDuration,
            aEUt,
            aCleanroom ? -200 : 0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addArcFurnaceRecipe(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration,
        int aEUt) {
        return addArcFurnaceRecipe(aInput, aOutputs, aChances, aDuration, aEUt, false);
    }

    @Deprecated
    @Override
    public boolean addArcFurnaceRecipe(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt,
        boolean hidden) {
        if ((aInput == null) || (aOutputs == null)) {
            return false;
        }
        for (ItemStack tStack : aOutputs) {
            if (tStack != null) {
                if (aDuration <= 0) {
                    return false;
                }
                GT_Recipe sRecipe = RecipeMaps.arcFurnaceRecipes.addRecipe(
                    true,
                    new ItemStack[] { aInput },
                    aOutputs,
                    null,
                    aChances,
                    new FluidStack[] { Materials.Oxygen.getGas(aDuration) },
                    null,
                    aDuration,
                    Math.max(1, aEUt),
                    0);
                if ((hidden) && (sRecipe != null)) {
                    sRecipe.mHidden = true;
                }
                for (Materials tMaterial : new Materials[] { Materials.Argon, Materials.Nitrogen }) {
                    if (tMaterial.mPlasma != null) {
                        int tPlasmaAmount = (int) Math.max(1L, aDuration / (tMaterial.getMass() * 16L));
                        GT_Recipe tRecipe = RecipeMaps.plasmaArcFurnaceRecipes.addRecipe(
                            true,
                            new ItemStack[] { aInput },
                            aOutputs,
                            null,
                            aChances,
                            new FluidStack[] { tMaterial.getPlasma(tPlasmaAmount) },
                            new FluidStack[] { tMaterial.getGas(tPlasmaAmount) },
                            Math.max(1, aDuration / 16),
                            Math.max(1, aEUt / 3),
                            0);
                        if ((hidden) && (tRecipe != null)) {
                            tRecipe.mHidden = true;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    @Deprecated
    public boolean addSimpleArcFurnaceRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs,
        int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutputs == null) || aFluidInput == null) {
            return false;
        }
        for (ItemStack tStack : aOutputs) {
            if (tStack != null) {
                if (aDuration <= 0) {
                    return false;
                }
                RecipeMaps.arcFurnaceRecipes.addRecipe(
                    true,
                    new ItemStack[] { aInput },
                    aOutputs,
                    null,
                    aChances,
                    new FluidStack[] { aFluidInput },
                    null,
                    aDuration,
                    Math.max(1, aEUt),
                    0);
                return true;
            }
        }
        return false;
    }

    @Override
    @Deprecated
    public boolean addPlasmaArcFurnaceRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs,
        int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutputs == null) || aFluidInput == null) {
            return false;
        }
        for (ItemStack tStack : aOutputs) {
            if (tStack != null) {
                if (aDuration <= 0) {
                    return false;
                }
                RecipeMaps.plasmaArcFurnaceRecipes.addRecipe(
                    true,
                    new ItemStack[] { aInput },
                    aOutputs,
                    null,
                    aChances,
                    new FluidStack[] { aFluidInput },
                    null,
                    aDuration,
                    Math.max(1, aEUt),
                    0);
                return true;
            }
        }
        return false;
    }

    @Override
    @Deprecated
    public boolean addPlasmaArcFurnaceRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs,
        FluidStack aFluidOutput, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutputs == null) || aFluidInput == null) {
            return false;
        }
        for (ItemStack tStack : aOutputs) {
            if (tStack != null) {
                if (aDuration <= 0) {
                    return false;
                }
                RecipeMaps.plasmaArcFurnaceRecipes.addRecipe(
                    true,
                    new ItemStack[] { aInput },
                    aOutputs,
                    null,
                    aChances,
                    new FluidStack[] { aFluidInput },
                    new FluidStack[] { aFluidOutput },
                    aDuration,
                    Math.max(1, aEUt),
                    0);
                return true;
            }
        }
        return false;
    }

    @Deprecated
    @Override
    public boolean addPulveriserRecipe(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration,
        int aEUt) {
        return addPulveriserRecipe(aInput, aOutputs, aChances, aDuration, aEUt, false);
    }

    @Deprecated
    @Override
    public boolean addPulveriserRecipe(ItemStack aInput, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt,
        boolean hidden) {
        if ((aInput == null) || (aOutputs == null)) {
            return false;
        }
        for (ItemStack tStack : aOutputs) {
            if (tStack != null) {
                if (aDuration <= 0) {
                    return false;
                }
                GT_Recipe tRecipe = RecipeMaps.maceratorRecipes.addRecipe(
                    true,
                    new ItemStack[] { aInput },
                    aOutputs,
                    null,
                    aChances,
                    null,
                    null,
                    aDuration,
                    aEUt,
                    0);
                if ((hidden) && (tRecipe != null)) {
                    tRecipe.mHidden = true;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    @Deprecated
    public boolean addPyrolyseRecipe(ItemStack aInput, FluidStack aFluidInput, int intCircuit, ItemStack aOutput,
        FluidStack aFluidOutput, int aDuration, int aEUt) {
        if (aInput == null) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.pyrolyseRecipes.addRecipe(
            false,
            new ItemStack[] { aInput, ItemList.Circuit_Integrated.getWithDamage(0L, intCircuit) },
            new ItemStack[] { aOutput },
            null,
            null,
            new FluidStack[] { aFluidInput },
            new FluidStack[] { aFluidOutput },
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addCrackingRecipe(FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt) {
        return false;
    }

    @Override
    @Deprecated
    public boolean addCrackingRecipe(int circuitConfig, FluidStack aInput, FluidStack aInput2, FluidStack aOutput,
        int aDuration, int aEUt) {
        if ((aInput == null && aInput2 == null) || (aOutput == null)) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        RecipeMaps.crackingRecipes.addRecipe(
            false,
            new ItemStack[] { GT_Utility.getIntegratedCircuit(circuitConfig) },
            null,
            null,
            null,
            new FluidStack[] { aInput, aInput2 },
            new FluidStack[] { aOutput },
            aDuration,
            aEUt,
            0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addAssemblylineRecipe(ItemStack aResearchItem, int aResearchTime, ItemStack[] aInputs,
        FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aResearchItem == null) || (aResearchTime <= 0)
            || (aInputs == null)
            || (aOutput == null)
            || aInputs.length > 15
            || aInputs.length < 4) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        for (ItemStack tItem : aInputs) {
            if (tItem == null) {
                GT_FML_LOGGER.info(
                    "addAssemblingLineRecipe " + aResearchItem.getDisplayName()
                        + " --> "
                        + aOutput.getUnlocalizedName()
                        + " there is some null item in that recipe");
            }
        }
        RecipeMaps.scannerFakeRecipes.addFakeRecipe(
            false,
            new ItemStack[] { aResearchItem },
            new ItemStack[] { aOutput },
            new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Writes Research result") },
            null,
            null,
            aResearchTime,
            30,
            -201);
        RecipeMaps.assemblylineVisualRecipes.addFakeRecipe(
            false,
            aInputs,
            new ItemStack[] { aOutput },
            new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Reads Research result") },
            aFluidInputs,
            null,
            aDuration,
            aEUt,
            0,
            false);
        GT_Recipe_AssemblyLine tRecipe = new GT_Recipe_AssemblyLine(
            aResearchItem,
            aResearchTime,
            aInputs,
            aFluidInputs,
            aOutput,
            aDuration,
            aEUt);
        GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.add(tRecipe);
        GT_AssemblyLineUtils.addRecipeToCache(tRecipe);
        return true;
    }

    @Override
    @Deprecated
    public boolean addAssemblylineRecipe(ItemStack aResearchItem, int aResearchTime, Object[] aInputs,
        FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aResearchItem == null) || (aResearchTime <= 0)
            || (aInputs == null)
            || (aOutput == null)
            || aInputs.length > 15
            || aInputs.length < 4) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        ItemStack[] tInputs = new ItemStack[aInputs.length];
        ItemStack[][] tAlts = new ItemStack[aInputs.length][];
        int tPersistentHash = 1;
        for (int i = 0; i < aInputs.length; i++) {
            Object obj = aInputs[i];
            if (obj instanceof ItemStack) {
                tInputs[i] = (ItemStack) obj;
                tAlts[i] = null;
                tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(tInputs[i], true, false);
                continue;
            } else if (obj instanceof ItemStack[]aStacks) {
                if (aStacks.length > 0) {
                    tInputs[i] = aStacks[0];
                    tAlts[i] = Arrays.copyOf(aStacks, aStacks.length);
                    for (ItemStack tAlt : tAlts[i]) {
                        tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(tAlt, true, false);
                    }
                    tPersistentHash *= 31;
                    continue;
                }
            } else if (obj instanceof Object[]objs) {
                List<ItemStack> tList;
                if (objs.length >= 2 && !(tList = GT_OreDictUnificator.getOres(objs[0])).isEmpty()) {
                    try {
                        // sort the output, so the hash code is stable across launches
                        tList.sort(
                            Comparator
                                .<ItemStack, String>comparing(
                                    s -> GameRegistry.findUniqueIdentifierFor(s.getItem()).modId)
                                .thenComparing(s -> GameRegistry.findUniqueIdentifierFor(s.getItem()).name)
                                .thenComparingInt(Items.feather::getDamage)
                                .thenComparingInt(s -> s.stackSize));
                        int tAmount = ((Number) objs[1]).intValue();
                        List<ItemStack> uList = new ArrayList<>();
                        for (ItemStack tStack : tList) {
                            ItemStack uStack = GT_Utility.copyAmount(tAmount, tStack);
                            if (GT_Utility.isStackValid(uStack)) {
                                uList.add(uStack);
                                if (tInputs[i] == null) tInputs[i] = uStack;
                            }
                        }
                        tAlts[i] = uList.toArray(new ItemStack[0]);
                        tPersistentHash = tPersistentHash * 31 + (objs[0] == null ? "" : objs[0].toString()).hashCode();
                        tPersistentHash = tPersistentHash * 31 + tAmount;
                        continue;
                    } catch (Exception ignored) {}
                }
            }
            GT_FML_LOGGER.info(
                "addAssemblingLineRecipe " + aResearchItem.getDisplayName()
                    + " --> "
                    + aOutput.getUnlocalizedName()
                    + " there is some null item in that recipe");
        }
        tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(aResearchItem, true, false);
        tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(aOutput, true, false);
        for (FluidStack tFluidInput : aFluidInputs) {
            if (tFluidInput == null) continue;
            tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(tFluidInput, true, false);
        }
        tPersistentHash = tPersistentHash * 31 + aResearchTime;
        tPersistentHash = tPersistentHash * 31 + aDuration;
        tPersistentHash = tPersistentHash * 31 + aEUt;
        RecipeMaps.scannerFakeRecipes.addFakeRecipe(
            false,
            new ItemStack[] { aResearchItem },
            new ItemStack[] { aOutput },
            new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Writes Research result") },
            null,
            null,
            aResearchTime,
            30,
            -201);
        RecipeMaps.assemblylineVisualRecipes.addFakeRecipe(
            false,
            tInputs,
            new ItemStack[] { aOutput },
            new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Reads Research result") },
            aFluidInputs,
            null,
            aDuration,
            aEUt,
            0,
            tAlts,
            false);
        GT_Recipe_AssemblyLine tRecipe = new GT_Recipe_AssemblyLine(
            aResearchItem,
            aResearchTime,
            tInputs,
            aFluidInputs,
            aOutput,
            aDuration,
            aEUt,
            tAlts);
        tRecipe.setPersistentHash(tPersistentHash);
        GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.add(tRecipe);
        GT_AssemblyLineUtils.addRecipeToCache(tRecipe);
        return true;
    }

    @Override
    @Deprecated
    public boolean addCircuitAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput,
        int aDuration, int aEUt) {
        return addCircuitAssemblerRecipe(aInputs, aFluidInput, aOutput, aDuration, aEUt, false);
    }

    @Override
    @Deprecated
    public boolean addCircuitAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput,
        int aDuration, int aEUt, boolean aCleanroom) {

        if (this.areItemsAndFluidsBothNull(aInputs, new FluidStack[] { aFluidInput })) {
            return false;
        }

        if (aDuration <= 0) {
            return false;
        }

        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }

        if (!GT_Utility.isStackValid(aOutput)) {
            return false;
        }

        for (int oreID : OreDictionary.getOreIDs(aOutput)) {
            if (OreDictionary.getOreName(oreID)
                .startsWith("circuit")) {
                return this
                    .addCircuitAssemblerRecipeNonOredicted(aInputs, aFluidInput, aOutput, aDuration, aEUt, aCleanroom);
            }
        }

        RecipeMaps.circuitAssemblerRecipes.addRecipe(
            true,
            aInputs,
            new ItemStack[] { aOutput },
            null,
            new FluidStack[] { aFluidInput },
            null,
            aDuration,
            aEUt,
            aCleanroom ? -200 : 0);
        return true;
    }

    @Deprecated
    public boolean addCircuitAssemblerRecipeNonOredicted(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput,
        int aDuration, int aEUt, boolean aCleanroom) {
        if ((aInputs == null) || (aOutput == null) || aInputs.length > 6 || aInputs.length < 1) {
            return false;
        }
        if (aDuration <= 0) {
            return false;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }
        RecipeMaps.circuitAssemblerRecipes.addRecipe(
            true,
            aInputs,
            new ItemStack[] { aOutput },
            null,
            null,
            new FluidStack[] { aFluidInput },
            null,
            aDuration,
            aEUt,
            aCleanroom ? -200 : 0);
        return true;
    }

    @Override
    @Deprecated
    public boolean addNanoForgeRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack[] aOutputs,
        FluidStack[] aFluidOutputs, int[] aChances, int aDuration, int aEUt, int aSpecialValue) {
        if (aInputs == null || aOutputs == null || aSpecialValue == 0) return false;

        RecipeMaps.nanoForgeRecipes.addRecipe(
            new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                null,
                aChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue));
        return true;
    }

    @Override
    public GT_Recipe addIC2ReactorBreederCell(ItemStack input, ItemStack output, boolean reflector, int heatStep,
        int heatMultiplier, int requiredPulses) {
        return GT_Values.RA.stdBuilder()
            .itemInputs(input)
            .itemOutputs(output)
            .setNEIDesc(
                reflector ? "Neutron reflecting Breeder" : "Heat neutral Breeder",
                String.format("Every %d reactor hull heat", heatStep),
                String.format("increase speed by %d00%%", heatMultiplier),
                String.format("Required pulses: %d", requiredPulses))
            .duration(0)
            .eut(0)
            .addTo(RecipeMaps.ic2NuclearFakeRecipes)
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public GT_Recipe addIC2ReactorFuelCell(ItemStack input, ItemStack output, boolean aMox, float aHeat, float aEnergy,
        int aCells) {
        // for the mysterious constant 5.0f,
        // see ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric.getOfferedEnergy
        // don't ask, just accept
        int pulses = aCells / 2 + 1;
        float nukePowerMult = 5.0f * ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/nuclear");
        return GT_Values.RA.stdBuilder()
            .itemInputs(input)
            .itemOutputs(output)
            .setNEIDesc(
                aMox ? "MOX Model" : "Uranium Model",
                "Neutron Pulse: " + aCells,
                aCells == 1 ? String.format("Heat: %.1f * n1 * (n1 + 1)", aHeat / 2f)
                    : String.format("Heat: %.1f * (%d + n1) * (%d + n1)", aHeat * aCells / 2f, aCells, aCells + 1),
                String.format(
                    "Energy: %.1f + n2 * %.1f EU/t",
                    aEnergy * aCells * pulses * nukePowerMult,
                    aEnergy * nukePowerMult))
            .duration(0)
            .eut(0)
            .addTo(RecipeMaps.ic2NuclearFakeRecipes)
            .stream()
            .findFirst()
            .orElse(null);
    }

    private boolean areItemsAndFluidsBothNull(ItemStack[] items, FluidStack[] fluids) {
        boolean itemsNull = true;
        if (items != null) {
            for (ItemStack itemStack : items) {
                if (itemStack != null) {
                    itemsNull = false;
                    break;
                }
            }
        }
        boolean fluidsNull = true;
        if (fluids != null) {
            for (FluidStack fluidStack : fluids) {
                if (fluidStack != null) {
                    fluidsNull = false;
                    break;
                }
            }
        }
        return itemsNull && fluidsNull;
    }

    @Override
    public GT_RecipeBuilder stdBuilder() {
        return GT_RecipeBuilder.builder();
    }
}
