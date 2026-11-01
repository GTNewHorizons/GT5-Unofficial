package gregtech.api.recipe.maps;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.getFluidUnit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.RecipeDisplayInfo;

/**
 * Display fuels on NEI as 1L of fluid, showing filled fluid containers as their fluid.
 * Fluid fuel values are shown in EU/L and item fuel values in EU/item.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FuelFrontend extends RecipeMapFrontend {

    private final int fuelValueMultiplier;

    public FuelFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder, NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        this(uiPropertiesBuilder, neiPropertiesBuilder, 1);
    }

    public FuelFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder, NEIRecipePropertiesBuilder neiPropertiesBuilder,
        int fuelValueMultiplier) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
        this.fuelValueMultiplier = fuelValueMultiplier;
    }

    @Override
    protected NEIRecipePropertiesBuilder modifyNEIProperties(NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        return neiPropertiesBuilder.neiSpecialInfoFormatter(this::formatFuelValue)
            .itemInputsGetter(FuelFrontend::getItemInputs)
            .fluidInputsGetter(FuelFrontend::getFluidInputs)
            .itemOutputsGetter(FuelFrontend::getItemOutputs);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return super.getItemInputPositions(Math.min(itemInputCount, uiProperties.maxItemInputs));
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return super.getItemOutputPositions(Math.min(itemOutputCount, uiProperties.maxItemOutputs));
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        if (uiProperties.maxItemInputs > 0) return super.getFluidInputPositions(fluidInputCount);
        return UIHelper.getItemInputPositions(fluidInputCount);
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        if (uiProperties.maxItemOutputs > 0) return super.getFluidOutputPositions(fluidOutputCount);
        return UIHelper.getItemOutputPositions(fluidOutputCount);
    }

    private List<String> formatFuelValue(RecipeDisplayInfo recipeInfo) {
        GTRecipe recipe = recipeInfo.recipe;
        long fuelValue = (long) recipe.mSpecialValue * fuelValueMultiplier;
        if (isItemFuel(recipe)) {
            return Collections.singletonList(
                StatCollector.translateToLocalFormatted("GT5U.nei.fuel_per_item", formatNumber(fuelValue * 1000)));
        }
        return Collections.singletonList(
            StatCollector.translateToLocalFormatted("GT5U.nei.fuel", formatNumber(fuelValue), getFluidUnit()));
    }

    private static boolean isItemFuel(GTRecipe recipe) {
        return GTUtility.getNonnullElementCount(recipe.mFluidInputs) == 0 && Arrays.stream(recipe.mInputs)
            .noneMatch(input -> GTUtility.getFluidForFilledItem(input, true) != null);
    }

    private static ItemStack[] getItemInputs(GTRecipe recipe) {
        ItemStack[] inputs = recipe.mInputs.clone();
        for (int i = 0; i < inputs.length; i++) {
            if (GTUtility.getFluidForFilledItem(inputs[i], true) != null) inputs[i] = null;
        }
        return inputs;
    }

    private static FluidStack[] getFluidInputs(GTRecipe recipe) {
        List<FluidStack> fluids = new ArrayList<>();
        for (FluidStack fluid : recipe.mFluidInputs) {
            if (fluid != null) fluids.add(new FluidStack(fluid, 1));
        }
        for (ItemStack input : recipe.mInputs) {
            FluidStack fluid = GTUtility.getFluidForFilledItem(input, true);
            if (fluid != null) fluids.add(new FluidStack(fluid, 1));
        }
        return fluids.toArray(new FluidStack[0]);
    }

    private static ItemStack[] getItemOutputs(GTRecipe recipe) {
        ItemStack[] outputs = recipe.mOutputs.clone();
        for (ItemStack input : recipe.mInputs) {
            ItemStack container = GTUtility.getContainerForFilledItem(input, true);
            if (container == null) continue;
            for (int i = 0; i < outputs.length; i++) {
                if (GTUtility.areStacksEqual(outputs[i], container)) outputs[i] = null;
            }
        }
        return outputs;
    }
}
