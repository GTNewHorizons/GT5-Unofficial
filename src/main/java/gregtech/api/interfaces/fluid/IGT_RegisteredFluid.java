package gregtech.api.interfaces.fluid;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;

import gregtech.api.enums.FluidState;
import gregtech.api.enums.Materials;

public interface IGT_RegisteredFluid {

    /**
     * Registers the containers in the {@link FluidContainerRegistry} for this {@link IGT_RegisteredFluid}
     *
     * @param fullContainer  The full fluid container
     * @param emptyContainer The empty fluid container
     * @param containerSize  The size of the container
     * @return The {@link IGT_RegisteredFluid} for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGT_RegisteredFluid registerContainers(final ItemStack fullContainer, final ItemStack emptyContainer,
        final int containerSize);

    /**
     * Registers the bucket-sized 1000L containers in the {@link FluidContainerRegistry} for this
     * {@link IGT_RegisteredFluid}
     *
     * @param fullContainer  The full container to associate with this {@link IGT_RegisteredFluid}
     * @param emptyContainer The empty container associate with this {@link IGT_RegisteredFluid}
     * @return {@link IGT_RegisteredFluid} for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGT_RegisteredFluid registerBContainers(final ItemStack fullContainer, final ItemStack emptyContainer);

    /**
     * Registers the potion-sized 250L containers in the {@link FluidContainerRegistry} for this
     * {@link IGT_RegisteredFluid}
     *
     * @param fullContainer  The full container to associate with this {@link IGT_RegisteredFluid}
     * @param emptyContainer The empty container associate with this {@link IGT_RegisteredFluid}
     * @return {@link IGT_RegisteredFluid} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGT_RegisteredFluid registerPContainers(final ItemStack fullContainer, final ItemStack emptyContainer);

    /**
     * Updates the {@link Materials}'s fluids from this {@link IGT_RegisteredFluid}'s state
     *
     * @param material the {@link Materials} to configure based on this {@link IGT_RegisteredFluid} and
     *                 {@link FluidState}
     * @return The {@link IGT_RegisteredFluid} for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGT_RegisteredFluid configureMaterials(final Materials material);

    /**
     * @return this {@link IGT_RegisteredFluid} cast to {@link Fluid}
     */
    Fluid asFluid();
}
