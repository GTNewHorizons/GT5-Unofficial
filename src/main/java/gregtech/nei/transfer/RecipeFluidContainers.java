package gregtech.nei.transfer;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import gregtech.api.util.GTUtility;
import gregtech.nei.transfer.RecipeTransferPlanner.FluidContainers;

/**
 * Fluid containers as Forge knows them: items that hold any amount ({@link IFluidContainerItem}, like the GT large
 * fluid cells) can be partly drained, registered containers (cells, buckets) only as a whole.
 */
class RecipeFluidContainers implements FluidContainers {

    private final FluidStack fluid;

    RecipeFluidContainers(FluidStack fluid) {
        this.fluid = fluid;
    }

    @Override
    public int amountIn(ItemStack container) {
        FluidStack content = GTUtility.getFluidForFilledItem(container, true);
        return content != null && content.isFluidEqual(fluid) ? content.amount : 0;
    }

    @Override
    public Drained drain(ItemStack container, int amount) {
        ItemStack single = GTUtility.copyAmount(1, container);
        int content = amountIn(single);
        if (content <= 0 || amount <= 0 || amount > content) return null;

        if (single.getItem() instanceof IFluidContainerItem item && item.getCapacity(single) > 0) {
            FluidStack drained = item.drain(single, amount, true);
            if (drained == null || drained.amount != amount) return null;
            return new Drained(single.stackSize > 0 ? single : null);
        }

        if (amount < content) return null;
        return new Drained(FluidContainerRegistry.drainFluidContainer(single));
    }
}
