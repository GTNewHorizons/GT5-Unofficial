package gregtech.common.fluid;

import static gregtech.api.enums.GT_Values.MOD_ID;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.FluidState;
import gregtech.api.interfaces.fluid.IGT_Fluid;
import gregtech.api.interfaces.fluid.IGT_FluidBuilder;
import java.util.Locale;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class GT_FluidBuilder implements IGT_FluidBuilder {
    protected final String fluidName;
    protected String localizedName;
    protected ResourceLocation stillIconResourceLocation = null, flowingIconResourceLocation = null;
    protected short[] colorRGBA = Dyes._NULL.getRGBA();
    protected Block fluidBlock = null;
    protected FluidState fluidState;
    protected int temperature;

    public GT_FluidBuilder(final String fluidName) {
        this.fluidName = fluidName;
    }

    /**
     * @inheritDoc from {@link IGT_FluidBuilder#withColorRGBA(short[])}
     */
    @Override
    public IGT_FluidBuilder withColorRGBA(final short[] colorRGBA) {
        this.colorRGBA = colorRGBA;
        return this;
    }

    /**
     * @inheritDoc from {@link IGT_FluidBuilder#withLocalizedName(String)}
     */
    @Override
    public IGT_FluidBuilder withLocalizedName(final String localizedName) {
        this.localizedName = localizedName;
        return this;
    }

    /**
     * @inheritDoc from {@link IGT_FluidBuilder#withStateTemperature(FluidState, int)}
     */
    @Override
    public IGT_FluidBuilder withStateTemperature(final FluidState fluidState, final int temperature) {
        this.fluidState = fluidState;
        this.temperature = temperature;
        return this;
    }

    /**
     * @inheritDoc from {@link IGT_FluidBuilder#withStillIconResourceLocation(ResourceLocation)}
     */
    @Override
    public IGT_FluidBuilder withStillIconResourceLocation(final ResourceLocation stillIconResourceLocation) {
        this.stillIconResourceLocation = stillIconResourceLocation;
        return this;
    }

    /**
     * @inheritDoc from {@link IGT_FluidBuilder#withFlowingIconResourceLocation(ResourceLocation)}
     */
    @Override
    public IGT_FluidBuilder withFlowingIconResourceLocation(final ResourceLocation flowingIconResourceLocation) {
        this.flowingIconResourceLocation = flowingIconResourceLocation;
        return this;
    }

    /**
     * @inheritDoc from {@link IGT_FluidBuilder#withTextureName(String)}
     */
    @Override
    public IGT_FluidBuilder withTextureName(final String textureName) {
        this.stillIconResourceLocation = new ResourceLocation(MOD_ID, "fluids/fluid." + textureName);
        this.flowingIconResourceLocation = null;
        return this;
    }

    /**
     * @inheritDoc from {@link IGT_FluidBuilder#withTextureFrom(IGT_Fluid)}
     */
    @Override
    public IGT_FluidBuilder withTextureFrom(final IGT_Fluid fromGTFluid) {
        this.stillIconResourceLocation = fromGTFluid.getStillIconResourceLocation();
        this.flowingIconResourceLocation = fromGTFluid.getFlowingIconResourceLocation();
        return this;
    }

    /**
     * @inheritDoc from {@link IGT_FluidBuilder#withFluidBlock}
     */
    @Override
    public IGT_FluidBuilder withFluidBlock(final Block fluidBlock) {
        this.fluidBlock = fluidBlock;
        return this;
    }

    /**
     * @inheritDoc from {@link IGT_FluidBuilder#withTextures(ResourceLocation, ResourceLocation)}
     */
    @Override
    public IGT_FluidBuilder withTextures(
            final ResourceLocation stillIconResourceLocation, final ResourceLocation flowingIconResourceLocation) {
        this.stillIconResourceLocation = stillIconResourceLocation;
        this.flowingIconResourceLocation = flowingIconResourceLocation;
        return this;
    }

    /**
     * @inheritDoc from {@link IGT_FluidBuilder#build()
     */
    @Override
    public IGT_Fluid build() {
        if (stillIconResourceLocation == null) {
            withTextureName(fluidName.toLowerCase(Locale.ENGLISH));
        }
        return new GT_Fluid(this);
    }

    /**
     * @inheritDoc from {@link IGT_FluidBuilder#buildAddFluid()
     */
    @Override
    public IGT_Fluid buildAddFluid() {
        if (stillIconResourceLocation == null) {
            withTextureName(fluidName.toLowerCase(Locale.ENGLISH));
        }
        return build().addFluid();
    }
}
