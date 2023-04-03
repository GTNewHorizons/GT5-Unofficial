package gregtech.common.fluid;

import static gregtech.api.enums.Mods.GregTech;

import java.util.Locale;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.FluidState;
import gregtech.api.interfaces.fluid.IGT_Fluid;
import gregtech.api.interfaces.fluid.IGT_FluidBuilder;
import gregtech.api.interfaces.fluid.IGT_RegisteredFluid;

public class GT_FluidBuilder implements IGT_FluidBuilder {

    final String fluidName;
    String localizedName;
    ResourceLocation stillIconResourceLocation = null, flowingIconResourceLocation = null;
    short[] colorRGBA = Dyes._NULL.getRGBA();
    Block fluidBlock = null;
    FluidState fluidState;
    int temperature;
    IIcon stillIcon;
    IIcon flowingIcon;
    Fluid iconsFrom;

    public GT_FluidBuilder(final String fluidName) {
        this.fluidName = fluidName.toLowerCase(Locale.ENGLISH);
    }

    /**
     * @inheritDoc
     */
    @Override
    public IGT_FluidBuilder withColorRGBA(final short[] colorRGBA) {
        this.colorRGBA = colorRGBA;
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public IGT_FluidBuilder withLocalizedName(final String localizedName) {
        this.localizedName = localizedName;
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public IGT_FluidBuilder withStateAndTemperature(final FluidState fluidState, final int temperature) {
        this.fluidState = fluidState;
        this.temperature = temperature;
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public IGT_FluidBuilder withStillIconResourceLocation(final ResourceLocation stillIconResourceLocation) {
        this.stillIconResourceLocation = stillIconResourceLocation;
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public IGT_FluidBuilder withFlowingIconResourceLocation(final ResourceLocation flowingIconResourceLocation) {
        this.flowingIconResourceLocation = flowingIconResourceLocation;
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public IGT_FluidBuilder withTextureName(final String textureName) {
        this.stillIconResourceLocation = new ResourceLocation(GregTech.ID, "fluids/fluid." + textureName);
        this.flowingIconResourceLocation = null;
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public IGT_FluidBuilder withIconsFrom(@Nonnull final Fluid fromFluid) {
        this.iconsFrom = fromFluid;
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public IGT_FluidBuilder withFluidBlock(final Block fluidBlock) {
        this.fluidBlock = fluidBlock;
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public IGT_FluidBuilder withTextures(final ResourceLocation stillIconResourceLocation,
            final ResourceLocation flowingIconResourceLocation) {
        this.stillIconResourceLocation = stillIconResourceLocation;
        this.flowingIconResourceLocation = flowingIconResourceLocation;
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public IGT_Fluid build() {
        if (colorRGBA == null) {
            colorRGBA = Dyes._NULL.getRGBA();
        }
        if (stillIconResourceLocation == null) {
            withTextureName(fluidName.toLowerCase(Locale.ENGLISH));
        }
        if (localizedName == null) {
            localizedName = fluidName;
        }
        return new GT_Fluid(this);
    }

    /**
     * @inheritDoc
     */
    @Override
    public IGT_RegisteredFluid buildAndRegister() {
        return build().addFluid();
    }
}
