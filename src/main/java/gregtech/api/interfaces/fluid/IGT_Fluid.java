package gregtech.api.interfaces.fluid;

import gregtech.api.enums.FluidState;
import gregtech.api.enums.Materials;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

public interface IGT_Fluid {

    /**
     * Registers this {@link IGT_Fluid} to ths {@link FluidRegistry}
     *
     * @return {@link IGT_Fluid} self for call chaining
     */
    IGT_Fluid addFluid();

    /**
     * Registers the containers in the {@link FluidContainerRegistry} for this {@link IGT_Fluid}
     *
     * @param fullContainer  The full fluid container
     * @param emptyContainer The empty fluid container
     * @param containerSize  The size of the container
     * @return The {@link IGT_Fluid} for chaining
     */
    IGT_Fluid registerContainers(
            final ItemStack fullContainer, final ItemStack emptyContainer, final int containerSize);

    /**
     * Registers the bucket-sized 1000L containers in the {@link FluidContainerRegistry} for this {@link IGT_Fluid}
     *
     * @param fullContainer  The full container to associate with this {@link IGT_Fluid}
     * @param emptyContainer The empty container associate with this {@link IGT_Fluid}
     * @return {@link IGT_Fluid} self for call chaining
     */
    IGT_Fluid registerBContainers(final ItemStack fullContainer, final ItemStack emptyContainer);

    /**
     * Registers the potion-sized 250L containers in the {@link FluidContainerRegistry} for this {@link IGT_Fluid}
     *
     * @param fullContainer  The full container to associate with this {@link IGT_Fluid}
     * @param emptyContainer The empty container associate with this {@link IGT_Fluid}
     * @return {@link IGT_Fluid} self for call chaining
     */
    IGT_Fluid registerPContainers(final ItemStack fullContainer, final ItemStack emptyContainer);

    /**
     * Updates the {@link Materials}'s fluids from this {@link IGT_Fluid}'s state
     *
     * @param material the {@link Materials} to configure based on this {@link IGT_Fluid} and {@link FluidState}
     * @return The {@link IGT_Fluid} for chaining
     */
    IGT_Fluid configureMaterials(final Materials material);

    /**
     * @return this {@link IGT_Fluid} cast to {@link Fluid}
     */
    Fluid asFluid();

    /**
     * @return the {@link ResourceLocation} of the still fluid texture
     */
    ResourceLocation getStillIconResourceLocation();

    /**
     * @return the {@link ResourceLocation} of the flowing fluid texture
     */
    ResourceLocation getFlowingIconResourceLocation();
}
