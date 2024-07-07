package gregtech.common;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import gregtech.api.enums.TierEU;
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
