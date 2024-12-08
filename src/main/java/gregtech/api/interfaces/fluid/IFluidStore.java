package gregtech.api.interfaces.fluid;

import javax.annotation.Nonnull;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

/**
 * Objects implementing this interface can be used for storing certain fluid, especially for recipe output.
 */
public interface IFluidStore extends IFluidTank {

    /**
     * @return If this does not have partially filled fluid nor have restriction on what fluid to accept.
     */
    boolean isEmptyAndAcceptsAnyFluid();

    /**
     * @return Whether to allow given fluid to be inserted into this.
     */
    boolean canStoreFluid(@Nonnull FluidStack fluidStack);
}
