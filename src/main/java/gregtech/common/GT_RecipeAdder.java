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
