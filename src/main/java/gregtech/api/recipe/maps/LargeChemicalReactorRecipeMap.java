package gregtech.api.recipe.maps;

import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.Mods.GregTech;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.extensions.ArrayExt;

public class LargeChemicalReactorRecipeMap extends LargeNEIDisplayRecipeMap {

    private static final int TOTAL_INPUT_COUNT = 6;
    private static final int OUTPUT_COUNT = 6;

    public LargeChemicalReactorRecipeMap() {
        super(
            new HashSet<>(1000),
            "gt.recipe.largechemicalreactor",
            "Large Chemical Reactor",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
            TOTAL_INPUT_COUNT,
            OUTPUT_COUNT,
            0,
            0,
            1,
            E,
            1,
            E,
            true,
            true);
    }

    @Override
    public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial,
        int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue) {
        aOptimize = false;
        ArrayList<ItemStack> adjustedInputs = new ArrayList<>();
        ArrayList<ItemStack> adjustedOutputs = new ArrayList<>();
        ArrayList<FluidStack> adjustedFluidInputs = new ArrayList<>();
        ArrayList<FluidStack> adjustedFluidOutputs = new ArrayList<>();

        if (aInputs == null) {
            aInputs = new ItemStack[0];
        } else {
            aInputs = ArrayExt.withoutTrailingNulls(aInputs, ItemStack[]::new);
        }

        for (ItemStack input : aInputs) {
            FluidStack inputFluidContent = FluidContainerRegistry.getFluidForFilledItem(input);
            if (inputFluidContent != null) {
                inputFluidContent.amount *= input.stackSize;
                if (inputFluidContent.getFluid()
                    .getName()
                    .equals("ic2steam")) {
                    inputFluidContent = GT_ModHandler.getSteam(inputFluidContent.amount);
                }
                adjustedFluidInputs.add(inputFluidContent);
            } else {
                ItemData itemData = GT_OreDictUnificator.getItemData(input);
                if (itemData != null && itemData.hasValidPrefixMaterialData()
                    && itemData.mMaterial.mMaterial == Materials.Empty) {
                    continue;
                } else {
                    if (itemData != null && itemData.hasValidPrefixMaterialData()
                        && itemData.mPrefix == OrePrefixes.cell) {
                        ItemStack dustStack = itemData.mMaterial.mMaterial.getDust(input.stackSize);
                        if (dustStack != null) {
                            adjustedInputs.add(dustStack);
                        } else {
                            adjustedInputs.add(input);
                        }
                    } else {
                        adjustedInputs.add(input);
                    }
                }
            }

            if (aFluidInputs == null) {
                aFluidInputs = new FluidStack[0];
            }
        }
        Collections.addAll(adjustedFluidInputs, aFluidInputs);
        aInputs = adjustedInputs.toArray(new ItemStack[0]);
        aFluidInputs = adjustedFluidInputs.toArray(new FluidStack[0]);

        if (aOutputs == null) {
            aOutputs = new ItemStack[0];
        } else {
            aOutputs = ArrayExt.withoutTrailingNulls(aOutputs, ItemStack[]::new);
        }

        for (ItemStack output : aOutputs) {
            FluidStack outputFluidContent = FluidContainerRegistry.getFluidForFilledItem(output);
            if (outputFluidContent != null) {
                outputFluidContent.amount *= output.stackSize;
                if (outputFluidContent.getFluid()
                    .getName()
                    .equals("ic2steam")) {
                    outputFluidContent = GT_ModHandler.getSteam(outputFluidContent.amount);
                }
                adjustedFluidOutputs.add(outputFluidContent);
            } else {
                ItemData itemData = GT_OreDictUnificator.getItemData(output);
                if (!(itemData != null && itemData.hasValidPrefixMaterialData()
                    && itemData.mMaterial.mMaterial == Materials.Empty)) {
                    adjustedOutputs.add(output);
                }
            }
        }
        if (aFluidOutputs == null) {
            aFluidOutputs = new FluidStack[0];
        }
        Collections.addAll(adjustedFluidOutputs, aFluidOutputs);
        aOutputs = adjustedOutputs.toArray(new ItemStack[0]);
        aFluidOutputs = adjustedFluidOutputs.toArray(new FluidStack[0]);

        return super.addRecipe(
            aOptimize,
            aInputs,
            aOutputs,
            aSpecial,
            aOutputChances,
            aFluidInputs,
            aFluidOutputs,
            aDuration,
            aEUt,
            aSpecialValue);
    }
}
