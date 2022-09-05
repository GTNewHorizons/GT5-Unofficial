package gregtech.common.fluid;

import gregtech.api.GregTech_API;
import gregtech.api.enums.FluidState;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.fluid.IGT_Fluid;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GT_Fluid extends Fluid implements IGT_Fluid, Runnable {
    private final String localizedName;
    private final ResourceLocation stillIconResourceLocation;
    private final ResourceLocation flowingIconResourceLocation;
    private final short[] colorRGBA;
    private final FluidState fluidState;
    private final int temperature;

    /**
     * Constructs this {@link IGT_Fluid} implementation from an {@link GT_FluidBuilder} instance
     *
     * @param builder The {@link GT_FluidBuilder} instance to construct this {@link IGT_Fluid} implementation
     */
    public GT_Fluid(final GT_FluidBuilder builder) {
        super(builder.fluidName);
        this.localizedName = builder.localizedName;
        this.stillIconResourceLocation = builder.stillIconResourceLocation;
        this.flowingIconResourceLocation = builder.flowingIconResourceLocation;
        this.block = builder.fluidBlock;
        this.colorRGBA = builder.colorRGBA;
        this.fluidState = builder.fluidState;
        this.temperature = builder.temperature;
        configureFromStateTemperature();
    }

    /**
     * @inheritDoc from {@link Fluid#getColor()}
     */
    @Override
    public int getColor() {
        return (Math.max(0, Math.min(255, colorRGBA[0])) << 16)
                | (Math.max(0, Math.min(255, colorRGBA[1])) << 8)
                | Math.max(0, Math.min(255, colorRGBA[2]));
    }

    /**
     * This {@link Runnable#run()} implementation is scheduled within the {@link GregTech_API#sGTBlockIconload}
     * to load this {@link IGT_Fluid}'s texture icons.
     *
     * @inheritDoc from {@link Runnable#run()}
     */
    @Override
    public void run() {
        final IIcon stillIcon = GregTech_API.sBlockIcons.registerIcon(stillIconResourceLocation.toString());
        if (flowingIconResourceLocation == null) {
            setIcons(stillIcon);
        } else {
            final IIcon flowingIcon = GregTech_API.sBlockIcons.registerIcon(flowingIconResourceLocation.toString());
            setIcons(stillIcon, flowingIcon);
        }
    }

    /**
     * @inheritDoc from {@link IGT_Fluid#addFluid()}
     */
    @Override
    public IGT_Fluid addFluid() {
        // Adds self the block icons loader run() tasks
        GregTech_API.sGTBlockIconload.add(this);
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName(), localizedName);

        final Fluid registeredFluid = registerFluid();
        if (registeredFluid.getTemperature() == new Fluid("test").getTemperature()) {
            registeredFluid.setTemperature(temperature);
        }
        return this;
    }

    /**
     * @inheritDoc from {@link IGT_Fluid#registerContainers(ItemStack, ItemStack, int)}
     */
    @Override
    public IGT_Fluid registerContainers(
            final ItemStack fullContainer, final ItemStack emptyContainer, final int containerSize) {
        if (fullContainer == null || emptyContainer == null) return this;
        final FluidStack fluidStack = new FluidStack(this, containerSize);
        if (!FluidContainerRegistry.registerFluidContainer(fluidStack, fullContainer, emptyContainer)) {
            GT_Values.RA.addFluidCannerRecipe(
                    fullContainer, GT_Utility.getContainerItem(fullContainer, false), null, fluidStack);
        }
        return this;
    }

    /**
     * @inheritDoc from {@link IGT_Fluid#registerBContainers(ItemStack, ItemStack)}
     */
    @Override
    public IGT_Fluid registerBContainers(final ItemStack fullContainer, final ItemStack emptyContainer) {
        return registerContainers(fullContainer, emptyContainer, 1000);
    }

    /**
     * @inheritDoc from {@link IGT_Fluid#registerPContainers(ItemStack, ItemStack)}
     */
    @Override
    public IGT_Fluid registerPContainers(final ItemStack fullContainer, final ItemStack emptyContainer) {
        return registerContainers(fullContainer, emptyContainer, 250);
    }

    /**
     * @inheritDoc from {@link IGT_Fluid#getStillIconResourceLocation()}
     */
    @Override
    public ResourceLocation getStillIconResourceLocation() {
        return stillIconResourceLocation;
    }

    /**
     * @inheritDoc from {@link IGT_Fluid#getFlowingIconResourceLocation()}
     */
    @Override
    public ResourceLocation getFlowingIconResourceLocation() {
        return flowingIconResourceLocation;
    }

    /**
     * @throws IllegalStateException if {@link FluidState} in invalid
     * @inheritDoc from {@link IGT_Fluid#configureMaterials(Materials)}
     */
    @Override
    public IGT_Fluid configureMaterials(final Materials material) {
        switch (fluidState) {
            case SOLID:
                material.mSolid = this;
                break;
            case LIQUID:
                material.mFluid = this;
                break;
            case GAS:
                material.mGas = this;
                break;
            case PLASMA:
                material.mPlasma = this;
                break;
            case MOLTEN:
                material.mStandardMoltenFluid = this;
                break;
            default:
                throw new IllegalStateException("Unexpected FluidState: " + fluidState);
        }
        return this;
    }

    /**
     * @inheritDoc from {@link IGT_Fluid#asFluid()}
     */
    @Override
    public Fluid asFluid() {
        return this;
    }

    /**
     * Adjusts this {@link Fluid}'s settings based on this {@link IGT_Fluid}'s state
     *
     * @throws IllegalStateException if {@link FluidState} in invalid
     */
    protected void configureFromStateTemperature() {
        switch (fluidState) {
            case SOLID: // Solid
                setGaseous(false).setViscosity(10000);
                break;
            case LIQUID: // Fluid
            case MOLTEN: // Molten
                setGaseous(false)
                        .setViscosity(1000)
                        .setLuminosity(
                                temperature >= 5000
                                        ? 15
                                        : temperature < 1000 ? 0 : 14 * (temperature - 1000) / 4000 + 1);
                break;
            case GAS: // Gas
                setGaseous(true).setDensity(-100).setViscosity(200);
                break;
            case PLASMA: // Plasma
                setGaseous(true).setDensity(55536).setViscosity(10).setLuminosity(15);
                break;
            default:
                throw new IllegalStateException("Unexpected FluidState: " + fluidState);
        }
    }

    /**
     * Registers this {@link IGT_Fluid} to the {@link FluidRegistry}
     *
     * @return the {@link Fluid} from the {@link FluidRegistry}
     */
    protected Fluid registerFluid() {
        return FluidRegistry.registerFluid(this) ? this : FluidRegistry.getFluid(this.fluidName);
    }
}
