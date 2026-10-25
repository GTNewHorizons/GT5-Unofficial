package gregtech.api.recipe.maps;

import static gregtech.api.util.GTRecipeConstants.SPARGE_MAX_BYPRODUCT;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.StackInfo;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.nei.GTNEIDefaultHandler;

public class SpargeTowerFrontend extends FluidOnlyFrontend {

    public SpargeTowerFrontend(@NotNull BasicUIPropertiesBuilder uiPropertiesBuilder,
        @NotNull NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    protected List<String> handleNEIByproductTooltip(@NotNull ItemStack stack, @NotNull List<String> currentTip,
        @NotNull GTRecipe recipe) {

        FluidStack hoveredFluid = StackInfo.getFluid(stack);
        if (hoveredFluid == null) return currentTip;

        FluidStack spargeGas = recipe.mFluidInputs[0];
        if (hoveredFluid.isFluidEqual(spargeGas)) {
            currentTip.add(StatCollector.translateToLocal("GT5U.nei.sparge_tower.remainder"));
            currentTip
                .add(StatCollector.translateToLocalFormatted("GT5U.nei.sparge_tower.maximum_output", spargeGas.amount));
            return currentTip;
        }

        int maximumByproducts = recipe.getMetadataOrDefault(SPARGE_MAX_BYPRODUCT, 0);
        for (int i = 2; i < recipe.mFluidOutputs.length; i++) {
            if (hoveredFluid.isFluidEqual(recipe.mFluidOutputs[i])) {
                currentTip.add(
                    StatCollector.translateToLocalFormatted("GT5U.nei.sparge_tower.maximum_output", maximumByproducts));
            }
        }

        return currentTip;
    }

    @Override
    public @NotNull List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GTNEIDefaultHandler.@NotNull CachedDefaultRecipe neiCachedRecipe) {
        GTRecipe recipe = neiCachedRecipe.mRecipe;
        for (PositionedStack pStack : neiCachedRecipe.mInputs) {
            if (pStack.containsWithNBT(stack)) {
                if (pStack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed) {
                    currentTip = handleNEIItemInputTooltip(currentTip, fixed);
                }
                break;
            }
        }
        for (PositionedStack pStack : neiCachedRecipe.mOutputs) {
            if (pStack.containsWithNBT(stack)) {
                if (pStack instanceof GTNEIDefaultHandler.FixedPositionedStack) {
                    currentTip = handleNEIByproductTooltip(stack, currentTip, recipe);
                }
                break;
            }
        }
        return currentTip;
    }

}
