package gregtech.common;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.Railcraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.internal.IGT_RecipeAdder;
import gregtech.api.objects.GT_FluidStack;
import gregtech.api.objects.ItemData;
import gregtech.api.util.*;
import gregtech.api.util.GT_Recipe.GT_Recipe_AssemblyLine;
import gregtech.common.items.GT_IntegratedCircuit_Item;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;

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
        if ((aDuration = GregTech_API.sRecipeFile.get(
            "fusion",
            aOutput1.getFluid()
                .getName(),
            aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sFusionRecipes.addRecipe(
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

        // If the recipe has more than 2 inputs or 2 outputs it is added to a different recipe map.
        // This is so NEI can function properly and understand the recipe. Otherwise, it gets cut off.
        if ((FluidInputArray.length > 2) || (FluidOutputArray.length > 2)) {
            GT_Recipe.GT_Recipe_Map.sComplexFusionRecipes.addRecipe(
                null,
                FluidInputArray,
                FluidOutputArray,
                aFusionDurationInTicks,
                aFusionEnergyPerTick,
                aEnergyNeededForStartingFusion);
            return true;
        }

        GT_Recipe.GT_Recipe_Map.sFusionRecipes.addRecipe(
            null,
            FluidInputArray,
            FluidOutputArray,
            aFusionDurationInTicks,
            aFusionEnergyPerTick,
            aEnergyNeededForStartingFusion);
        return true;
    }

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

    @Override
    public boolean addCentrifugeRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
        FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4,
        ItemStack aOutput5, ItemStack aOutput6, int[] aChances, int aDuration, int aEUt, boolean aCleanroom) {
        if (((aInput1 == null) && (aFluidInput == null)) || ((aOutput1 == null) && (aFluidOutput == null))) {
            return false;
        }
        if ((aInput1 != null) && ((aDuration = GregTech_API.sRecipeFile.get("centrifuge", aInput1, aDuration)) <= 0)) {
            return false;
        }
        if ((aFluidInput != null) && ((aDuration = GregTech_API.sRecipeFile.get(
            "centrifuge",
            aFluidInput.getFluid()
                .getName(),
            aDuration)) <= 0)) {
            return false;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }
        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.addRecipe(
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

        GT_Recipe.GT_Recipe_Map.sMultiblockCentrifugeRecipes.addRecipe(
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
        if ((aDuration = GregTech_API.sRecipeFile.get("compressor", aInput1, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sCompressorRecipes.addRecipe(
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
        if ((aInput1 != null)
            && ((aDuration = GregTech_API.sRecipeFile.get("electrolyzer", aInput1, aDuration)) <= 0)) {
            return false;
        }
        if ((aFluidInput != null) && ((aDuration = GregTech_API.sRecipeFile.get(
            "electrolyzer",
            aFluidInput.getFluid()
                .getName(),
            aDuration)) <= 0)) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.addRecipe(
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

        GT_Recipe.GT_Recipe_Map.sMultiblockElectrolyzerRecipes
            .addRecipe(false, itemInputs, itemOutputs, null, aChances, fluidInputs, fluidOutputs, aDuration, aEUt, 0);
        return true;
    }

    @Override
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput, int aDuration) {
        return addChemicalRecipe(aInput1, aInput2, null, null, aOutput, aDuration);
    }

    @Override
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput, int aDuration, int aEUt) {
        return addChemicalRecipe(aInput1, aInput2, null, null, aOutput, aDuration, aEUt);
    }

    @Override
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
        FluidStack aFluidOutput, ItemStack aOutput, int aDuration) {
        return addChemicalRecipe(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration, 30);
    }

    @Override
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
        FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration) {
        return addChemicalRecipe(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aOutput2, aDuration, 30);
    }

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

    @Override
    public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
        FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick,
        boolean aCleanroom) {
        if (((aInput1 == null) && (aFluidInput == null))
            || ((aOutput == null) && (aOutput2 == null) && (aFluidOutput == null))) {
            return false;
        }
        if ((aOutput != null || aOutput2 != null)
            && ((aDuration = GregTech_API.sRecipeFile.get("chemicalreactor", aOutput, aDuration)) <= 0)) {
            return false;
        }
        if ((aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get(
            "chemicalreactor",
            aFluidOutput.getFluid()
                .getName(),
            aDuration)) <= 0)) {
            return false;
        }
        if (aEUtick <= 0) {
            return false;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }
        GT_Recipe.GT_Recipe_Map.sChemicalRecipes.addRecipe(
            true,
            new ItemStack[] { aInput1, aInput2 },
            new ItemStack[] { aOutput, aOutput2 },
            null,
            null,
            new FluidStack[] { aFluidInput },
            new FluidStack[] { aFluidOutput },
            aDuration,
            aEUtick,
            aCleanroom ? -200 : 0);
        if (!(aInput1 != null && aInput1.getItem() instanceof GT_IntegratedCircuit_Item
            && aInput1.getItemDamage() >= 10)
            && !(aInput2 != null && aInput2.getItem() instanceof GT_IntegratedCircuit_Item
                && aInput2.getItemDamage() >= 10)) {
            GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.addRecipe(
                false,
                new ItemStack[] { aInput1, aInput2 },
                new ItemStack[] { aOutput, aOutput2 },
                null,
                null,
                new FluidStack[] { aFluidInput },
                new FluidStack[] { aFluidOutput },
                aDuration,
                aEUtick,
                0);
        }
        return true;
    }

    @Override
    public boolean addMultiblockChemicalRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs,
        FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int aDuration, int aEUtick) {
        if (areItemsAndFluidsBothNull(aInputs, aFluidInputs) || areItemsAndFluidsBothNull(aOutputs, aFluidOutputs)) {
            return false;
        }
        if (aEUtick <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes
            .addRecipe(false, aInputs, aOutputs, null, null, aFluidInputs, aFluidOutputs, aDuration, aEUtick, 0);
        return true;
    }

    @Override
    public boolean addChemicalRecipeForBasicMachineOnly(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput,
        FluidStack aFluidOutput, ItemStack aOutput, ItemStack aOutput2, int aDuration, int aEUtick) {
        if (((aInput1 == null) && (aFluidInput == null))
            || ((aOutput == null) && (aOutput2 == null) && (aFluidOutput == null))) {
            return false;
        }
        if ((aOutput != null || aOutput2 != null)
            && ((aDuration = GregTech_API.sRecipeFile.get("chemicalreactor", aOutput, aDuration)) <= 0)) {
            return false;
        }
        if ((aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get(
            "chemicalreactor",
            aFluidOutput.getFluid()
                .getName(),
            aDuration)) <= 0)) {
            return false;
        }
        if (aEUtick <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sChemicalRecipes.addRecipe(
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

    @Override
    public void addDefaultPolymerizationRecipes(Fluid aBasicMaterial, ItemStack aBasicMaterialCell, Fluid aPolymer) {
        // Oxygen/Titaniumtetrafluoride -> +50% Output each
        addChemicalRecipe(
            ItemList.Cell_Air.get(1),
            GT_Utility.getIntegratedCircuit(1),
            new GT_FluidStack(aBasicMaterial, 144),
            new GT_FluidStack(aPolymer, 144),
            Materials.Empty.getCells(1),
            160);
        addChemicalRecipe(
            Materials.Oxygen.getCells(1),
            GT_Utility.getIntegratedCircuit(1),
            new GT_FluidStack(aBasicMaterial, 144),
            new GT_FluidStack(aPolymer, 216),
            Materials.Empty.getCells(1),
            160);
        addChemicalRecipe(
            aBasicMaterialCell,
            GT_Utility.getIntegratedCircuit(1),
            Materials.Air.getGas(14000),
            new GT_FluidStack(aPolymer, 1000),
            Materials.Empty.getCells(1),
            1120);
        addChemicalRecipe(
            aBasicMaterialCell,
            GT_Utility.getIntegratedCircuit(1),
            Materials.Oxygen.getGas(7000),
            new GT_FluidStack(aPolymer, 1500),
            Materials.Empty.getCells(1),
            1120);
        addMultiblockChemicalRecipe(
            new ItemStack[] { GT_Utility.getIntegratedCircuit(2) },
            new FluidStack[] { new GT_FluidStack(aBasicMaterial, 2160), Materials.Air.getGas(7500),
                Materials.Titaniumtetrachloride.getFluid(100) },
            new FluidStack[] { new GT_FluidStack(aPolymer, 3240) },
            null,
            800,
            30);
        addMultiblockChemicalRecipe(
            new ItemStack[] { GT_Utility.getIntegratedCircuit(2) },
            new FluidStack[] { new GT_FluidStack(aBasicMaterial, 2160), Materials.Oxygen.getGas(7500),
                Materials.Titaniumtetrachloride.getFluid(100) },
            new FluidStack[] { new GT_FluidStack(aPolymer, 4320) },
            null,
            800,
            30);
    }

    @Override
    public boolean addBlastRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2,
        int aDuration, int aEUt, int aLevel) {
        return addBlastRecipe(aInput1, aInput2, null, null, aOutput1, aOutput2, aDuration, aEUt, aLevel);
    }

    @Override
    public boolean addBlastRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput,
        ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("blastfurnace", aInput1, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sBlastRecipes.addRecipe(
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

    public boolean addBlastRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4,
        FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3,
        ItemStack aOutput4, int aDuration, int aEUt, int aLevel) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("blastfurnace", aInput1, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sBlastRecipes.addRecipe(
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

    @Override
    public boolean addPlasmaForgeRecipe(ItemStack[] ItemInputArray, FluidStack[] FluidInputArray,
        ItemStack[] OutputItemArray, FluidStack[] FluidOutputArray, int aDuration, int aEUt, int coil_heat_level) {
        GT_Recipe.GT_Recipe_Map.sPlasmaForgeRecipes.addRecipe(
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
    public boolean addPrimitiveBlastRecipe(ItemStack aInput1, ItemStack aInput2, int aCoalAmount, ItemStack aOutput1,
        ItemStack aOutput2, int aDuration) {
        if ((aInput1 == null && aInput2 == null) || (aOutput1 == null && aOutput2 == null)) {
            return false;
        }
        if (aCoalAmount <= 0) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("primitiveblastfurnace", aInput1, aDuration)) <= 0) {
            return false;
        }
        Materials[] coals = new Materials[] { Materials.Coal, Materials.Charcoal };
        for (Materials coal : coals) {
            GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.addRecipe(
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
            GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.addRecipe(
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
            GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.addRecipe(
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
            GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.addRecipe(
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
            GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.addRecipe(
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
            aInput1 = aInput1 == null ? null : GT_Utility.copyAmount(aInput1.stackSize * 10L, aInput1);
            aInput2 = aInput2 == null ? null : GT_Utility.copyAmount(aInput2.stackSize * 10L, aInput2);
            aOutput1 = aOutput1 == null ? null : GT_Utility.copyAmount(aOutput1.stackSize * 10L, aOutput1);
            aOutput2 = aOutput2 == null ? null : GT_Utility.copyAmount(aOutput2.stackSize * 10L, aOutput2);
            for (Materials coal : coals) {
                GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.addRecipe(
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
                GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.addRecipe(
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
                GT_Recipe.GT_Recipe_Map.sPrimitiveBlastRecipes.addRecipe(
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
    public boolean addCannerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2,
        int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("canning", aInput1, aDuration)) <= 0) {
            return false;
        }
        new GT_Recipe(aInput1, aEUt, aInput2, aDuration, aOutput1, aOutput2);
        return true;
    }

    @Override
    public boolean addAlloySmelterRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration,
        int aEUt) {
        return addAlloySmelterRecipe(aInput1, aInput2, aOutput1, aDuration, aEUt, false);
    }

    @Override
    public boolean addAlloySmelterRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration,
        int aEUt, boolean hidden) {
        if ((aInput1 == null) || (aOutput1 == null || Materials.Graphite.contains(aInput1))) {
            return false;
        }
        if ((aInput2 == null) && ((OrePrefixes.ingot.contains(aInput1)) || (OrePrefixes.dust.contains(aInput1))
            || (OrePrefixes.gem.contains(aInput1)))) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("alloysmelting", aInput2 == null ? aInput1 : aOutput1, aDuration))
            <= 0) {
            return false;
        }
        GT_Recipe tRecipe = new GT_Recipe(aInput1, aInput2, aEUt, aDuration, aOutput1);
        if (hidden) {
            tRecipe.mHidden = true;
        }
        return true;
    }

    @Override
    @Deprecated
    public boolean addCNCRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        return GregTech_API.sRecipeFile.get("cnc", aOutput1, aDuration) > 0;
    }

    @Override
    public boolean addLatheRecipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("lathe", aInput1, aDuration)) <= 0) {
            return false;
        }
        new GT_Recipe(aInput1, aOutput1, aOutput2, aDuration, aEUt);
        return true;
    }

    @Override
    public boolean addCutterRecipe(ItemStack aInput, FluidStack aLubricant, ItemStack aOutput1, ItemStack aOutput2,
        int aDuration, int aEUt) {
        if ((aInput == null) || (aLubricant == null) || (aOutput1 == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("cutting", aInput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sCutterRecipes.addRecipe(
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
    public boolean addCutterRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt,
        boolean aCleanroom) {
        return addCutterRecipe(aInput, null, aOutput1, aOutput2, aDuration, aEUt, aCleanroom);
    }

    public boolean addCutterRecipe(ItemStack aInput, int aCircuit, ItemStack aOutput1, ItemStack aOutput2,
        int aDuration, int aEUt) {
        return addCutterRecipe(aInput, aCircuit, aOutput1, aOutput2, aDuration, aEUt, false);
    }

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
    public boolean addCutterRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt) {
        return addCutterRecipe(aInput, aOutput1, aOutput2, aDuration, aEUt, false);
    }

    @Override
    public boolean addCutterRecipe(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput1, ItemStack aOutput2,
        int aDuration, int aEUt) {
        return addCutterRecipe(aInput, aCircuit, aOutput1, aOutput2, aDuration, aEUt, false);
    }

    @Override
    public boolean addCutterRecipe(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput1, ItemStack aOutput2,
        int aDuration, int aEUt, boolean aCleanroom) {
        return addCutterRecipe(
            new ItemStack[] { aInput, aCircuit },
            new ItemStack[] { aOutput1, aOutput2 },
            aDuration,
            aEUt,
            aCleanroom ? -200 : 0);
    }

    public boolean addCutterRecipe(ItemStack[] aInputs, ItemStack[] aOutputs, int aDuration, int aEUt,
        boolean aCleanroom) {
        return addCutterRecipe(aInputs, aOutputs, aDuration, aEUt, aCleanroom ? -200 : 0);
    }

    @Override
    public boolean addCutterRecipe(ItemStack[] aInputs, ItemStack[] aOutputs, int aDuration, int aEUt, int aSpecial) {
        if ((aInputs == null) || (aOutputs == null) || aInputs.length == 0 || aOutputs.length == 0) {
            return false;
        }
        if (Arrays.stream(aOutputs)
            .noneMatch(Objects::nonNull)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("cutting", aInputs[0], aDuration)) <= 0) {
            return false;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom && aSpecial == -200) {
            aSpecial = 0;
        }
        GT_Recipe.GT_Recipe_Map.sCutterRecipes.addRecipe(
            true,
            aInputs,
            aOutputs,
            null,
            new FluidStack[] { Materials.Water.getFluid(Math.max(4, Math.min(1000, aDuration * aEUt / 320))) },
            null,
            aDuration * 2,
            aEUt,
            aSpecial);
        GT_Recipe.GT_Recipe_Map.sCutterRecipes.addRecipe(
            true,
            aInputs,
            aOutputs,
            null,
            new FluidStack[] { GT_ModHandler.getDistilledWater(Math.max(3, Math.min(750, aDuration * aEUt / 426))) },
            null,
            aDuration * 2,
            aEUt,
            aSpecial);
        GT_Recipe.GT_Recipe_Map.sCutterRecipes.addRecipe(
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
    public boolean addAssemblerRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, ItemStack aOutput1,
        int aDuration, int aEUt) {
        return addAssemblerRecipe(new ItemStack[] { aInput1, aInput2 }, aFluidInput, aOutput1, aDuration, aEUt);
    }

    @Override
    public boolean addAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration,
        int aEUt) {
        return addAssemblerRecipe(aInputs, aFluidInput, aOutput1, aDuration, aEUt, false);
    }

    @Override
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
    public boolean addAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration,
        int aEUt, boolean aCleanroom) {

        if (areItemsAndFluidsBothNull(aInputs, new FluidStack[] { aFluidInput })) {
            return false;
        }

        if ((aDuration = GregTech_API.sRecipeFile.get("assembling", aOutput1, aDuration)) <= 0) {
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

        GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.addRecipe(
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

    public boolean addAssemblerRecipeNonOD(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1,
        int aDuration, int aEUt, boolean aCleanroom) {
        if (areItemsAndFluidsBothNull(aInputs, new FluidStack[] { aFluidInput })) {
            return false;
        }

        if ((aDuration = GregTech_API.sRecipeFile.get("assembling", aOutput1, aDuration)) <= 0) {
            return false;
        }

        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }

        if (!GT_Utility.isStackValid(aOutput1)) {
            return false;
        }

        GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.addRecipe(
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
    public boolean addWiremillRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("wiremill", aInput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sWiremillRecipes.addRecipe(
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
    public boolean addWiremillRecipe(ItemStack aInput, ItemStack aCircuit, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("wiremill", aInput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sWiremillRecipes.addRecipe(
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
    public boolean addPolarizerRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("polarizer", aInput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sPolarizerRecipes.addRecipe(
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
        if ((aDuration = GregTech_API.sRecipeFile.get("bender", aInput1, aDuration)) <= 0) {
            return false;
        }
        new GT_Recipe(aEUt, aDuration, aInput1, aOutput1);
        return true;
    }

    @Deprecated
    @Override
    public boolean addBenderRecipe(ItemStack aInput1, ItemStack aCircuit, ItemStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("bender", aInput1, aDuration)) <= 0) {
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
        GT_Recipe.GT_Recipe_Map.sBenderRecipes.addRecipe(tRecipe);
        return true;
    }

    @Override
    public boolean addExtruderRecipe(ItemStack aInput, ItemStack aShape, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aShape == null) || (aOutput == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("extruder", aOutput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sExtruderRecipes.addRecipe(
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
    public boolean addSlicerRecipe(ItemStack aInput, ItemStack aShape, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aShape == null) || (aOutput == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("slicer", aOutput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sSlicerRecipes.addRecipe(
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
    public boolean addOreWasherRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3,
        FluidStack aFluidInput, int aDuration, int aEUt) {
        if ((aInput == null) || (aFluidInput == null)
            || ((aOutput1 == null) || (aOutput2 == null) || (aOutput3 == null))) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("orewasher", aInput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sOreWasherRecipes.addRecipe(
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
    public boolean addOreWasherRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3,
        FluidStack aFluidInput, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aFluidInput == null)
            || ((aOutput1 == null) || (aOutput2 == null) || (aOutput3 == null))) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("orewasher", aInput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sOreWasherRecipes.addRecipe(
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
    public boolean addImplosionRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if ((aInput2 = GregTech_API.sRecipeFile.get("implosion", aInput1, aInput2)) <= 0) {
            return false;
        }
        int tExplosives = Math.min(aInput2, 64);
        int tGunpowder = tExplosives << 1; // Worst
        int tDynamite = Math.max(1, tExplosives >> 1); // good
        int tTNT = tExplosives; // Slightly better
        int tITNT = Math.max(1, tExplosives >> 2); // the best
        // new GT_Recipe(aInput1, aInput2, aOutput1, aOutput2);
        if (tGunpowder < 65) {
            GT_Recipe.GT_Recipe_Map.sImplosionRecipes.addRecipe(
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
            GT_Recipe.GT_Recipe_Map.sImplosionRecipes.addRecipe(
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
        GT_Recipe.GT_Recipe_Map.sImplosionRecipes.addRecipe(
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
        GT_Recipe.GT_Recipe_Map.sImplosionRecipes.addRecipe(
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
    public boolean addUniversalDistillationRecipe(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2,
        int aDuration, int aEUt) {
        for (int i = 0; i < Math.min(aOutputs.length, 11); i++) {
            addDistilleryRecipe(i + 1, aInput, aOutputs[i], aOutput2, aDuration * 2, aEUt / 4, false);
        }

        return addDistillationTowerRecipe(aInput, aOutputs, aOutput2, aDuration, aEUt);
    }

    @Override
    public boolean addDistillationTowerRecipe(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2,
        int aDuration, int aEUt) {
        if (aInput == null || aOutputs == null || aOutputs.length < 1 || aOutputs.length > 11) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("distillation", aInput.getUnlocalizedName(), aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sDistillationRecipes.addRecipe(
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
    public boolean addDistillationTowerRecipe(FluidStack aInput, ItemStack[] aCircuit, FluidStack[] aOutputs,
        ItemStack aOutput2, int aDuration, int aEUt) {
        if (aInput == null || aOutputs == null || aOutputs.length < 1 || aOutputs.length > 11) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("distillation", aInput.getUnlocalizedName(), aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sDistillationRecipes.addRecipe(
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
    public boolean addUniversalDistillationRecipewithCircuit(FluidStack aInput, ItemStack[] aCircuit,
        FluidStack[] aOutputs, ItemStack aOutput2, int aDuration, int aEUt) {
        for (int i = 0; i < Math.min(aOutputs.length, 11); i++) {
            addDistilleryRecipe(i + 1, aInput, aOutputs[i], aOutput2, aDuration * 2, aEUt / 4, false);
        }
        return addDistillationTowerRecipe(aInput, aCircuit, aOutputs, aOutput2, aDuration, aEUt);
    }

    @Override
    public boolean addVacuumFreezerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("vacuumfreezer", aInput1, aDuration)) <= 0) {
            return false;
        }
        new GT_Recipe(aInput1, aOutput1, aDuration, aEUt, 0); // Since all other methods are taken
        FluidStack tInputFluid = GT_Utility.getFluidForFilledItem(aInput1, true);
        FluidStack tOutputFluid = GT_Utility.getFluidForFilledItem(aOutput1, true);
        if (tInputFluid != null && tOutputFluid != null) {
            addVacuumFreezerRecipe(tInputFluid, tOutputFluid, aDuration, aEUt);
        }
        return true;
    }

    @Override
    public boolean addVacuumFreezerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("vacuumfreezer", aInput1, aDuration)) <= 0) {
            return false;
        }
        new GT_Recipe(aInput1, aOutput1, aDuration);
        FluidStack tInputFluid = GT_Utility.getFluidForFilledItem(aInput1, true);
        FluidStack tOutputFluid = GT_Utility.getFluidForFilledItem(aOutput1, true);
        if (tInputFluid != null && tOutputFluid != null) {
            addVacuumFreezerRecipe(tInputFluid, tOutputFluid, aDuration, 120);
        }
        return true;
    }

    @Override
    public boolean addVacuumFreezerRecipe(FluidStack aInput1, FluidStack aOutput1, int aDuration, int aEUt) {
        if ((aInput1 == null) || (aOutput1 == null)) {
            return false;
        }

        new GT_Recipe(aInput1, aOutput1, aDuration, aEUt);
        return true;
    }

    @Override
    public boolean addVacuumFreezerRecipe(ItemStack[] aItemInput, FluidStack[] aFluidInput, ItemStack[] aItemOutput,
        FluidStack[] aFluidOutput, int aDuration, int aEUt) {
        GT_Recipe.GT_Recipe_Map.sVacuumRecipes.addRecipe(
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
    public boolean addFuel(ItemStack aInput1, ItemStack aOutput1, int aEU, int aType) {
        if (aInput1 == null) {
            return false;
        }
        new GT_Recipe(aInput1, aOutput1, GregTech_API.sRecipeFile.get("fuel_" + aType, aInput1, aEU), aType);
        return true;
    }

    @Override
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
        if (!GregTech_API.sRecipeFile.get("forgehammer", aOutput1, true)) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sHammerRecipes.addRecipe(
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

        GT_Recipe.GT_Recipe_Map.sHammerRecipes.addRecipe(
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
    public boolean addBoxingRecipe(ItemStack aContainedItem, ItemStack aEmptyBox, ItemStack aFullBox, int aDuration,
        int aEUt) {
        if ((aContainedItem == null) || (aFullBox == null)) {
            return false;
        }
        if (!GregTech_API.sRecipeFile.get("boxing", aFullBox, true)) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes.addRecipe(
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
    public boolean addUnboxingRecipe(ItemStack aFullBox, ItemStack aContainedItem, ItemStack aEmptyBox, int aDuration,
        int aEUt) {
        if ((aFullBox == null) || (aContainedItem == null)) {
            return false;
        }
        if (!GregTech_API.sRecipeFile.get("unboxing", aFullBox, true)) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes.addRecipe(
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
        if (!GregTech_API.sRecipeFile.get("thermalcentrifuge", aInput, true)) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes.addRecipe(
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
        if (!GregTech_API.sRecipeFile.get("thermalcentrifuge", aInput, true)) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes.addRecipe(
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
    public boolean addAmplifier(ItemStack aAmplifierItem, int aDuration, int aAmplifierAmountOutputted) {
        if ((aAmplifierItem == null) || (aAmplifierAmountOutputted <= 0)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("amplifier", aAmplifierItem, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sAmplifiers.addRecipe(
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
    public boolean addBrewingRecipe(ItemStack aIngredient, Fluid aInput, Fluid aOutput, int aDuration, int aEUt,
        boolean aHidden) {
        if ((aIngredient == null) || (aInput == null) || (aOutput == null)) {
            return false;
        }
        if (!GregTech_API.sRecipeFile.get("brewing", aOutput.getUnlocalizedName(), true)) {
            return false;
        }
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sBrewingRecipes.addRecipe(
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
    public boolean addBrewingRecipe(ItemStack aIngredient, Fluid aInput, Fluid aOutput, boolean aHidden) {
        return addBrewingRecipe(aIngredient, aInput, aOutput, 128, 4, aHidden);
    }

    @Override
    public boolean addBrewingRecipeCustom(ItemStack aIngredient, FluidStack aInput, FluidStack aOutput, int aDuration,
        int aEUt, boolean aHidden) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        if (!GregTech_API.sRecipeFile.get("brewing", aOutput.getUnlocalizedName(), true)) {
            return false;
        }
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sBrewingRecipes.addRecipe(
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
    public boolean addFermentingRecipe(FluidStack aInput, FluidStack aOutput, int aDuration, int aEUt,
        boolean aHidden) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get(
            "fermenting",
            aOutput.getFluid()
                .getUnlocalizedName(),
            aDuration)) <= 0) {
            return false;
        }
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sFermentingRecipes.addRecipe(
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
    public boolean addFermentingRecipe(FluidStack aInput, FluidStack aOutput, int aDuration, boolean aHidden) {
        return addFermentingRecipe(aInput, aOutput, aDuration, 2, aHidden);
    }

    @Override
    public boolean addDistilleryRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput,
        ItemStack aSolidOutput, int aDuration, int aEUt, boolean aHidden) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get(
            "distillery",
            aOutput.getFluid()
                .getUnlocalizedName(),
            aDuration)) <= 0) {
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

        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sDistilleryRecipes.addRecipe(
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
    public boolean addDistilleryRecipe(ItemStack aCircuit, FluidStack aInput, FluidStack aOutput, int aDuration,
        int aEUt, boolean aHidden) {
        return addDistilleryRecipe(aCircuit, aInput, aOutput, null, aDuration, aEUt, aHidden);
    }

    @Override
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
    public boolean addFluidSolidifierRecipe(final ItemStack[] itemInputs, final FluidStack[] fluidInputs,
        final ItemStack[] itemOutputs, final FluidStack[] fluidOutputs, final int EUPerTick,
        final int aDurationInTicks) {
        GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes
            .addRecipe(true, itemInputs, itemOutputs, null, fluidInputs, fluidOutputs, aDurationInTicks, EUPerTick, 0);
        return true;
    }

    @Override
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
        if ((aDuration = GregTech_API.sRecipeFile.get("fluidsolidifier", aOutput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes.addRecipe(
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
    public boolean addFluidSmelterRecipe(ItemStack aInput, ItemStack aRemains, FluidStack aOutput, int aChance,
        int aDuration, int aEUt) {
        return addFluidSmelterRecipe(aInput, aRemains, aOutput, aChance, aDuration, aEUt, false);
    }

    @Override
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
        if ((aDuration = GregTech_API.sRecipeFile.get("fluidsmelter", aInput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes.addRecipe(
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
        if ((aDuration = GregTech_API.sRecipeFile.get("fluidextractor", aInput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes.addRecipe(
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

    @Override
    public boolean addFluidCannerRecipe(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput,
        FluidStack aFluidOutput) {
        int aDuration = aFluidOutput == null ? aFluidInput.amount / 62 : aFluidOutput.amount / 62;

        if (aInput == null || aOutput == null) {
            return false;
        }
        if (!GregTech_API.sRecipeFile.get("fluidcanner", aOutput, true)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("fluidcanner", aInput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes.addRecipe(
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

    @Override
    public boolean addFluidCannerRecipe(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput,
        FluidStack aFluidOutput, int aDuration, int aEUt) {
        if (aInput == null || aOutput == null) {
            return false;
        }
        if (!GregTech_API.sRecipeFile.get("fluidcanner", aOutput, true)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("fluidcanner", aInput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes.addRecipe(
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

    @Override
    public boolean addChemicalBathRecipe(ItemStack aInput, FluidStack aBathingFluid, ItemStack aOutput1,
        ItemStack aOutput2, ItemStack aOutput3, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aBathingFluid == null) || (aOutput1 == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("chemicalbath", aInput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes.addRecipe(
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

    @Override
    public boolean addChemicalBathRecipe(ItemStack aInput, FluidStack aBathingFluid, FluidStack aFluidOutput,
        ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aBathingFluid == null) || (aOutput1 == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("chemicalbath", aInput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes.addRecipe(
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
    public boolean addElectromagneticSeparatorRecipe(ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2,
        ItemStack aOutput3, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput1 == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("electromagneticseparator", aInput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes.addRecipe(
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
    public boolean addExtractorRecipe(ItemStack aInput, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutput == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("extractor", aInput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sExtractorRecipes.addRecipe(
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
    public boolean addPrinterRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aSpecialSlot, ItemStack aOutput,
        int aDuration, int aEUt) {
        if ((aInput == null) || (aFluid == null) || (aOutput == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("printer", aInput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sPrinterRecipes.addRecipe(
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
    public boolean addAutoclaveRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance,
        int aDuration, int aEUt, boolean aCleanroom) {
        return addAutoclaveRecipe(aInput, aFluid, aOutput, aChance, aDuration, aEUt);
    }

    @Override
    public boolean addAutoclaveRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance,
        int aDuration, int aEUt) {
        return addAutoclaveRecipe(aInput, null, aFluid, aOutput, aChance, aDuration, aEUt, false);
    }

    public boolean addAutoclaveRecipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluid, ItemStack aOutput,
        int aChance, int aDuration, int aEUt) {
        return addAutoclaveRecipe(aInput, aCircuit, aFluid, aOutput, aChance, aDuration, aEUt, false);
    }

    @Override
    public boolean addAutoclaveRecipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluidIn, ItemStack aOutput,
        int aChance, int aDuration, int aEUt, boolean aCleanroom) {
        return addAutoclaveRecipe(aInput, aCircuit, aFluidIn, null, aOutput, aChance, aDuration, aEUt, aCleanroom);
    }

    @Override
    public boolean addAutoclaveRecipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluidIn, FluidStack aFluidOut,
        ItemStack aOutput, int aChance, int aDuration, int aEUt, boolean aCleanroom) {
        if ((aInput == null) || (aFluidIn == null) || (aOutput == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("autoclave", aInput, aDuration)) <= 0) {
            return false;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }
        GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes.addRecipe(
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
    public boolean addAutoclaveSpaceRecipe(ItemStack aInput, FluidStack aFluid, ItemStack aOutput, int aChance,
        int aDuration, int aEUt, boolean aCleanroom) {
        return addAutoclaveRecipe(aInput, aFluid, aOutput, aChance, aDuration, aEUt, aCleanroom);
    }

    @Override
    public boolean addAutoclaveSpaceRecipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluid, ItemStack aOutput,
        int aChance, int aDuration, int aEUt, boolean aCleanroom) {
        if ((aInput == null) || (aFluid == null) || (aOutput == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("autoclave", aInput, aDuration)) <= 0) {
            return false;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }
        GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes.addRecipe(
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
    public boolean addAutoclave4Recipe(ItemStack aInput, ItemStack aCircuit, FluidStack aFluidIn, FluidStack aFluidOut,
        ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUt, boolean aCleanroom) {
        if ((aInput == null) || (aFluidIn == null) || (aOutputs == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("autoclave", aInput, aDuration)) <= 0) {
            return false;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }
        GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes.addRecipe(
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
        if ((aOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("mixer", aOutput, aDuration)) <= 0)) {
            return false;
        }
        if ((aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get(
            "mixer",
            aFluidOutput.getFluid()
                .getName(),
            aDuration)) <= 0)) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sMixerRecipes.addRecipe(
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

        GT_Recipe.GT_Recipe_Map.sMultiblockMixerRecipes
            .addRecipe(false, itemInputs, itemOutputs, null, null, fluidInputs, fluidOutputs, aDuration, aEUt, 0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addMixerRecipe(ItemStack[] ItemInputArray, FluidStack[] FluidInputArray, ItemStack[] ItemOutputArray,
        FluidStack[] FluidOutputArray, int aDuration, int aEUt) {
        GT_Recipe.GT_Recipe_Map.sMixerRecipes.addRecipe(
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

        GT_Recipe.GT_Recipe_Map.sMultiblockMixerRecipes.addRecipe(
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
        if ((aOutput1 != null) && ((aDuration = GregTech_API.sRecipeFile.get("mixer", aOutput1, aDuration)) <= 0)) {
            return false;
        }
        if ((aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get(
            "mixer",
            aFluidOutput.getFluid()
                .getName(),
            aDuration)) <= 0)) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sMixerRecipes.addRecipe(
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

        GT_Recipe.GT_Recipe_Map.sMultiblockMixerRecipes
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
        if ((aDuration = GregTech_API.sRecipeFile.get("laserengraving", aEngravedItem, aDuration)) <= 0) {
            return false;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }
        GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes.addRecipe(
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
        GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes.addRecipe(
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
        if ((aDuration = GregTech_API.sRecipeFile.get("press", aImprintedItem, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sPressRecipes.addRecipe(
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
        GT_Recipe.GT_Recipe_Map.sPressRecipes
            .addRecipe(true, ItemInputArray, OutputItemArray, null, null, null, aDuration, aEUt, 0);
        return true;
    }

    @Deprecated
    @Override
    public boolean addFluidHeaterRecipe(ItemStack aItem, FluidStack aOutput, int aDuration, int aEUt) {
        if ((aItem == null) || (aOutput == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get(
            "fluidheater",
            aOutput.getFluid()
                .getUnlocalizedName(),
            aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes.addRecipe(
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
        if ((aDuration = GregTech_API.sRecipeFile.get(
            "fluidheater",
            aOutput.getFluid()
                .getUnlocalizedName(),
            aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes.addRecipe(
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
                if ((aDuration = GregTech_API.sRecipeFile.get("sifter", aItemToSift, aDuration)) <= 0) {
                    return false;
                }
                GT_Recipe.GT_Recipe_Map.sSifterRecipes.addRecipe(
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
        GT_Recipe.GT_Recipe_Map.sSifterRecipes.addRecipe(
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
                if ((aDuration = GregTech_API.sRecipeFile.get("arcfurnace", aInput, aDuration)) <= 0) {
                    return false;
                }
                GT_Recipe sRecipe = GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes.addRecipe(
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
                        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes.addRecipe(
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
    public boolean addSimpleArcFurnaceRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs,
        int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutputs == null) || aFluidInput == null) {
            return false;
        }
        for (ItemStack tStack : aOutputs) {
            if (tStack != null) {
                if ((aDuration = GregTech_API.sRecipeFile.get("arcfurnace", aInput, aDuration)) <= 0) {
                    return false;
                }
                GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes.addRecipe(
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
    public boolean addPlasmaArcFurnaceRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs,
        int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutputs == null) || aFluidInput == null) {
            return false;
        }
        for (ItemStack tStack : aOutputs) {
            if (tStack != null) {
                if ((aDuration = GregTech_API.sRecipeFile.get("arcfurnace", aInput, aDuration)) <= 0) {
                    return false;
                }
                GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes.addRecipe(
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
    public boolean addPlasmaArcFurnaceRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs,
        FluidStack aFluidOutput, int[] aChances, int aDuration, int aEUt) {
        if ((aInput == null) || (aOutputs == null) || aFluidInput == null) {
            return false;
        }
        for (ItemStack tStack : aOutputs) {
            if (tStack != null) {
                if ((aDuration = GregTech_API.sRecipeFile.get("arcfurnace", aInput, aDuration)) <= 0) {
                    return false;
                }
                GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes.addRecipe(
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
                if ((aDuration = GregTech_API.sRecipeFile.get("pulveriser", aInput, aDuration)) <= 0) {
                    return false;
                }
                GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.addRecipe(
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
    public boolean addPyrolyseRecipe(ItemStack aInput, FluidStack aFluidInput, int intCircuit, ItemStack aOutput,
        FluidStack aFluidOutput, int aDuration, int aEUt) {
        if (aInput == null) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("pyrolyse", aInput, aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sPyrolyseRecipes.addRecipe(
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
    public boolean addCrackingRecipe(int circuitConfig, FluidStack aInput, FluidStack aInput2, FluidStack aOutput,
        int aDuration, int aEUt) {
        if ((aInput == null && aInput2 == null) || (aOutput == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("cracking", aInput.getUnlocalizedName(), aDuration)) <= 0) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sCrackingRecipes.addRecipe(
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
    public boolean addAssemblylineRecipe(ItemStack aResearchItem, int aResearchTime, ItemStack[] aInputs,
        FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aResearchItem == null) || (aResearchTime <= 0)
            || (aInputs == null)
            || (aOutput == null)
            || aInputs.length > 15
            || aInputs.length < 4) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("assemblingline", aOutput, aDuration)) <= 0) {
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
        GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
            false,
            new ItemStack[] { aResearchItem },
            new ItemStack[] { aOutput },
            new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Writes Research result") },
            null,
            null,
            aResearchTime,
            30,
            -201);
        GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.addFakeRecipe(
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
    public boolean addAssemblylineRecipe(ItemStack aResearchItem, int aResearchTime, Object[] aInputs,
        FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt) {
        if ((aResearchItem == null) || (aResearchTime <= 0)
            || (aInputs == null)
            || (aOutput == null)
            || aInputs.length > 15
            || aInputs.length < 4) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("assemblingline", aOutput, aDuration)) <= 0) {
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
        GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
            false,
            new ItemStack[] { aResearchItem },
            new ItemStack[] { aOutput },
            new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Writes Research result") },
            null,
            null,
            aResearchTime,
            30,
            -201);
        GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.addFakeRecipe(
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
    public boolean addCircuitAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput,
        int aDuration, int aEUt) {
        return addCircuitAssemblerRecipe(aInputs, aFluidInput, aOutput, aDuration, aEUt, false);
    }

    @Override
    public boolean addCircuitAssemblerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput,
        int aDuration, int aEUt, boolean aCleanroom) {

        if (this.areItemsAndFluidsBothNull(aInputs, new FluidStack[] { aFluidInput })) {
            return false;
        }

        if ((aDuration = GregTech_API.sRecipeFile.get("circuitassembler", aOutput, aDuration)) <= 0) {
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

        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.addRecipe(
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

    public boolean addCircuitAssemblerRecipeNonOredicted(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput,
        int aDuration, int aEUt, boolean aCleanroom) {
        if ((aInputs == null) || (aOutput == null) || aInputs.length > 6 || aInputs.length < 1) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("circuitassembler", aOutput, aDuration)) <= 0) {
            return false;
        }
        if (!GT_Mod.gregtechproxy.mEnableCleanroom) {
            aCleanroom = false;
        }
        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes.addRecipe(
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
    public boolean addNanoForgeRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack[] aOutputs,
        FluidStack[] aFluidOutputs, int[] aChances, int aDuration, int aEUt, int aSpecialValue) {
        if (aInputs == null || aOutputs == null || aSpecialValue == 0) return false;

        GT_Recipe.GT_Recipe_Map.sNanoForge.addRecipe(
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
    public boolean addPCBFactoryRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack[] aOutputs,
        int aDuration, int aEUt, int aSpecialValue) {

        if (aInputs == null || aFluidInputs == null || aOutputs == null) {
            return false;
        }

        if (aSpecialValue <= 0 || aEUt < 0 || aDuration < 0) {
            return false;
        }

        GT_Recipe.GT_Recipe_Map.sPCBFactory.addRecipe(
            new GT_Recipe(false, aInputs, aOutputs, null, null, aFluidInputs, null, aDuration, aEUt, aSpecialValue));

        return true;
    }

    @Override
    public GT_Recipe addIC2ReactorBreederCell(ItemStack input, ItemStack output, boolean reflector, int heatStep,
        int heatMultiplier, int requiredPulses) {
        return GT_Recipe.GT_Recipe_Map.sIC2NuclearFakeRecipe.addFakeRecipe(
            input,
            output,
            reflector ? "Neutron reflecting Breeder" : "Heat neutral Breeder",
            String.format("Every %d reactor hull heat", heatStep),
            String.format("increase speed by %d00%%", heatMultiplier),
            String.format("Required pulses: %d", requiredPulses));
    }

    @Override
    public GT_Recipe addIC2ReactorFuelCell(ItemStack input, ItemStack output, boolean aMox, float aHeat, float aEnergy,
        int aCells) {
        // for the mysterious constant 5.0f,
        // see ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric.getOfferedEnergy
        // don't ask, just accept
        int pulses = aCells / 2 + 1;
        float nukePowerMult = 5.0f * ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/nuclear");
        return GT_Recipe.GT_Recipe_Map.sIC2NuclearFakeRecipe.addFakeRecipe(
            input,
            output,
            aMox ? "MOX Model" : "Uranium Model",
            "Neutron Pulse: " + aCells,
            aCells == 1 ? String.format("Heat: %.1f * n1 * (n1 + 1)", aHeat / 2f)
                : String.format("Heat: %.1f * (%d + n1) * (%d + n1)", aHeat * aCells / 2f, aCells, aCells + 1),
            String.format(
                "Energy: %.1f + n2 * %.1f EU/t",
                aEnergy * aCells * pulses * nukePowerMult,
                aEnergy * nukePowerMult));
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
