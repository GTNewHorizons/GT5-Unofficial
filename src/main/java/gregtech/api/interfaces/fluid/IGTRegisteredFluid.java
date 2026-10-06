package gregtech.api.interfaces.fluid;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;

public interface IGTRegisteredFluid {

    /**
     * Registers the containers in the {@link FluidContainerRegistry} for this {@link IGTRegisteredFluid}
     *
     * @param fullContainer  The full fluid container
     * @param emptyContainer The empty fluid container
     * @param containerSize  The size of the container
     * @return The {@link IGTRegisteredFluid} for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGTRegisteredFluid registerContainers(final ItemStack fullContainer, final ItemStack emptyContainer,
        final int containerSize);

    /**
     * Registers the bucket-sized 1000L containers in the {@link FluidContainerRegistry} for this
     * {@link IGTRegisteredFluid}
     *
     * @param fullContainer  The full container to associate with this {@link IGTRegisteredFluid}
     * @param emptyContainer The empty container associate with this {@link IGTRegisteredFluid}
     * @return {@link IGTRegisteredFluid} for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGTRegisteredFluid registerBContainers(final ItemStack fullContainer, final ItemStack emptyContainer);

    /**
     * Registers the potion-sized 250L containers in the {@link FluidContainerRegistry} for this
     * {@link IGTRegisteredFluid}
     *
     * @param fullContainer  The full container to associate with this {@link IGTRegisteredFluid}
     * @param emptyContainer The empty container associate with this {@link IGTRegisteredFluid}
     * @return {@link IGTRegisteredFluid} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGTRegisteredFluid registerPContainers(final ItemStack fullContainer, final ItemStack emptyContainer);

    IGTRegisteredFluid addLocalizedName();

    /**
     * @return this {@link IGTRegisteredFluid} cast to {@link Fluid}
     */
    Fluid asFluid();
}
