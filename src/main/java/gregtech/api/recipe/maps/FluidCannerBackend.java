package gregtech.api.recipe.maps;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FluidCannerBackend extends RecipeMapBackend {

    public FluidCannerBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    protected GTRecipe findFallback(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot) {
        for (ItemStack item : items) {
            if (item == null) continue;
            // Try to fill a container
            for (FluidStack fluid : fluids) {
                if (fluid == null) continue;
                ItemStack filledItem = GTUtility.fillFluidContainer(fluid, item, false, true);
                FluidStack fluidToTake = GTUtility.getFluidForFilledItem(filledItem, true);
                if (fluidToTake != null) {
                    return GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, item))
                        .itemOutputs(filledItem)
                        .fluidInputs(fluidToTake)
                        .duration(Math.max(fluidToTake.amount / 64, 16))
                        .eut(1)
                        .nbtSensitive()
                        .noBuffer()
                        .build()
                        .orElse(null);
                }
            }

            // Try to empty a container
            FluidStack drainedFluid = GTUtility.getFluidForFilledItem(item, true);
            if (drainedFluid != null) {
                GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
                recipeBuilder.itemInputs(GTUtility.copyAmount(1, item))
                    .itemOutputs(GTUtility.getContainerItem(item, true));
                if (drainedFluid.amount > 0) {
                    recipeBuilder.fluidOutputs(drainedFluid);
                }
                return recipeBuilder.duration(Math.max(drainedFluid.amount / 64, 16))
                    .eut(1)
                    .nbtSensitive()
                    .noBuffer()
                    .build()
                    .orElse(null);
            }
        }
        return null;
    }

    @Override
    public boolean containsInput(ItemStack item) {
        return super.containsInput(item) || (item.getItem() instanceof IFluidContainerItem
            && ((IFluidContainerItem) item.getItem()).getCapacity(item) > 0);
    }

    @Override
    public boolean containsInput(Fluid fluid) {
        return true;
    }
}
