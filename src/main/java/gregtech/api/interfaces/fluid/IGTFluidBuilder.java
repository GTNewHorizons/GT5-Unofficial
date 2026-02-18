package gregtech.api.interfaces.fluid;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import gregtech.api.enums.FluidState;

@SuppressWarnings("unused") // API might legitimately expose unused methods within this local project's scope
public interface IGTFluidBuilder {

    /**
     * @param colorRGBA The {@code short[]} RGBA color of the {@link Fluid} or {@code null} for no defined RGBA color
     * @return {@link IGTFluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGTFluidBuilder withColorRGBA(final short[] colorRGBA);

    /**
     * @param localizedName The localized name of this {@link IGTFluidBuilder}
     * @return {@link IGTFluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGTFluidBuilder withDefaultLocalName(final String localizedName);

    /**
     * Deprecated, use {@link #withDefaultLocalName(String)}.
     * 
     * @param localizedName The localized name of this {@link IGTFluidBuilder}
     * @return {@link IGTFluidBuilder} self for call chaining
     */
    @Deprecated
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGTFluidBuilder withLocalizedName(final String localizedName);

    /**
     * @param fluidState  The {@link FluidState} of this {@link IGTFluidBuilder}
     * @param temperature The Kelvin temperature of this {@link IGTFluidBuilder}
     * @return {@link IGTFluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGTFluidBuilder withStateAndTemperature(final FluidState fluidState, final int temperature);

    /**
     * @param stillIconResourceLocation the {@link ResourceLocation} of the still fluid icon
     * @return {@link IGTFluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGTFluidBuilder withStillIconResourceLocation(final ResourceLocation stillIconResourceLocation);

    /**
     * @param flowingIconResourceLocation the {@link ResourceLocation} of the flowing fluid icon
     * @return {@link IGTFluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGTFluidBuilder withFlowingIconResourceLocation(final ResourceLocation flowingIconResourceLocation);

    /**
     * @param textureName The name of the GregTech mod texture of this {@link IGTFluidBuilder}
     * @return {@link IGTFluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGTFluidBuilder withTextureName(final String textureName);

    /**
     * @param fluidBlock the {@link Block} implementation of the {@link IGTFluid}
     * @return {@link IGTFluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGTFluidBuilder withFluidBlock(final Block fluidBlock);

    /**
     * @param fromFluid the {@link Fluid} to copy the icons from
     * @return {@link IGTFluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGTFluidBuilder withIconsFrom(@Nonnull final Fluid fromFluid);

    /**
     * @param stillIconResourceLocation   The {@link ResourceLocation} of the still fluid texture
     * @param flowingIconResourceLocation The {@link ResourceLocation} of the flowing fluid texture
     * @return {@link IGTFluidBuilder} self for call chaining
     */
    @SuppressWarnings("UnusedReturnValue") // Last call in chain, may not use this returned value
    IGTFluidBuilder withTextures(final ResourceLocation stillIconResourceLocation,
        final ResourceLocation flowingIconResourceLocation);

    /**
     * Builds the {@link IGTFluid}
     *
     * @return the built {@link IGTFluid}
     */
    IGTFluid build();

    /**
     * Builds, then adds the {@link IGTFluid} to the {@link FluidRegistry}
     *
     * @return the {@link IGTFluid}
     * @see #build()
     * @see IGTFluid#addFluid()
     */
    IGTRegisteredFluid buildAndRegister();
}
