package gtPlusPlus.api.recipe;

import static gregtech.api.util.GTRecipeConstants.SPARGE_MAX_BYPRODUCT;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.maps.FluidOnlyFrontend;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.nei.GTNEIDefaultHandler;

public class SpargeTowerFrontend extends FluidOnlyFrontend {

    public SpargeTowerFrontend(@NotNull BasicUIPropertiesBuilder uiPropertiesBuilder,
        @NotNull NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    protected List<String> handleNEIByproductTooltip(@NotNull ItemStack stack, @NotNull List<String> currentTip,
        @NotNull GTRecipe recipe) {
        int maximumByproducts = recipe.getMetadataOrDefault(SPARGE_MAX_BYPRODUCT, 0);
        FluidStack spargeGas = recipe.mFluidInputs[0];
        if (stack.isItemEqual(GTUtility.getFluidDisplayStack(spargeGas.getFluid()))) {
            currentTip.add("The amount returned is the remainder after all other outputs.");
            currentTip.add("Maximum Output: " + spargeGas.amount + "L");
            return currentTip;
        }
        for (int i = 2; i < recipe.mFluidOutputs.length; i++) {
            if (stack.isItemEqual(GTUtility.getFluidDisplayStack(recipe.mFluidOutputs[i].getFluid()))) {
                currentTip.add("Maximum Output: " + maximumByproducts + "L");
            }
        }
        return currentTip;
    }

    @Override
    public @NotNull List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GTNEIDefaultHandler.@NotNull CachedDefaultRecipe neiCachedRecipe) {
        GTRecipe recipe = neiCachedRecipe.mRecipe;
        for (PositionedStack pStack : neiCachedRecipe.mInputs) {
            if (stack == pStack.item) {
                if (pStack instanceof GTNEIDefaultHandler.FixedPositionedStack) {
                    currentTip = handleNEIItemInputTooltip(
                        currentTip,
                        (GTNEIDefaultHandler.FixedPositionedStack) pStack);
                }
                break;
            }
        }
        for (PositionedStack pStack : neiCachedRecipe.mOutputs) {
            if (stack == pStack.item) {
                if (pStack instanceof GTNEIDefaultHandler.FixedPositionedStack) {
                    currentTip = handleNEIByproductTooltip(stack, currentTip, recipe);
                }
                break;
            }
        }
        return currentTip;
    }

}
