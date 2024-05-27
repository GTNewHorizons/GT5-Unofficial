package gregtech.api.interfaces.fluid;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import gregtech.api.enums.FluidState;

@SuppressWarnings("unused") // API might legitimately expose unused methods within this local project's scope
public interface IGT_FluidBuilder {

    /**
     * @param colorRGBA The {@code short[]} RGBA color of the {@link Fluid} or {@code null} for no defined RGBA color
     * @return {@link IGT_FluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGT_FluidBuilder withColorRGBA(final short[] colorRGBA);

    /**
     * @param localizedName The localized name of this {@link IGT_FluidBuilder}
     * @return {@link IGT_FluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGT_FluidBuilder withLocalizedName(final String localizedName);

    /**
     * @param fluidState  The {@link FluidState} of this {@link IGT_FluidBuilder}
     * @param temperature The Kelvin temperature of this {@link IGT_FluidBuilder}
     * @return {@link IGT_FluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGT_FluidBuilder withStateAndTemperature(final FluidState fluidState, final int temperature);

    /**
     * @param stillIconResourceLocation the {@link ResourceLocation} of the still fluid icon
     * @return {@link IGT_FluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGT_FluidBuilder withStillIconResourceLocation(final ResourceLocation stillIconResourceLocation);

    /**
     * @param flowingIconResourceLocation the {@link ResourceLocation} of the flowing fluid icon
     * @return {@link IGT_FluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGT_FluidBuilder withFlowingIconResourceLocation(final ResourceLocation flowingIconResourceLocation);

    /**
     * @param textureName The name of the GregTech mod texture of this {@link IGT_FluidBuilder}
     * @return {@link IGT_FluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGT_FluidBuilder withTextureName(final String textureName);

    /**
     * @param fluidBlock the {@link Block} implementation of the {@link IGT_Fluid}
     * @return {@link IGT_FluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGT_FluidBuilder withFluidBlock(final Block fluidBlock);

    /**
     * @param fromFluid the {@link Fluid} to copy the icons from
     * @return {@link IGT_FluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGT_FluidBuilder withIconsFrom(@Nonnull final Fluid fromFluid);

    /**
     * @param stillIconResourceLocation   The {@link ResourceLocation} of the still fluid texture
     * @param flowingIconResourceLocation The {@link ResourceLocation} of the flowing fluid texture
     * @return {@link IGT_FluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGT_FluidBuilder withTextures(final ResourceLocation stillIconResourceLocation,
        final ResourceLocation flowingIconResourceLocation);

    /**
     * Builds the {@link IGT_Fluid}
     *
     * @return the built {@link IGT_Fluid}
     */
    IGT_Fluid build();

    /**
     * Builds, then adds the {@link IGT_Fluid} to the {@link FluidRegistry}
     *
     * @return the {@link IGT_Fluid}
     * @see #build()
     * @see IGT_Fluid#addFluid()
     */
    IGT_RegisteredFluid buildAndRegister();
}
