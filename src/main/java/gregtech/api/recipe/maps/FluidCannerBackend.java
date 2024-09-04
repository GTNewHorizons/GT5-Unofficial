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
        if (items.length == 0 || items[0] == null) {
            return null;
        }

        if (fluids.length > 0 && fluids[0] != null) {
            ItemStack filledItem = GTUtility.fillFluidContainer(fluids[0], items[0], false, true);
            FluidStack fluidToTake = GTUtility.getFluidForFilledItem(filledItem, true);
            if (fluidToTake != null) {
                return GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, items[0]))
                    .itemOutputs(filledItem)
                    .fluidInputs(fluidToTake)
                    .duration(Math.max(fluidToTake.amount / 64, 16))
                    .eut(1)
                    .noOptimize()
                    .noBuffer()
                    .build()
                    .orElse(null);
            }
        }
        FluidStack drainedFluid = GTUtility.getFluidForFilledItem(items[0], true);
        if (drainedFluid != null) {
            return GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, items[0]))
                .itemOutputs(GTUtility.getContainerItem(items[0], true))
                .fluidOutputs(drainedFluid)
                .duration(Math.max(drainedFluid.amount / 64, 16))
                .eut(1)
                .noBuffer()
                .build()
                .orElse(null);
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
