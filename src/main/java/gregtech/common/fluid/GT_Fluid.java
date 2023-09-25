package gregtech.common.fluid;

import static gregtech.api.recipe.RecipeMaps.fluidCannerRecipes;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.FluidState;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.fluid.IGT_Fluid;
import gregtech.api.interfaces.fluid.IGT_RegisteredFluid;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;

public class GT_Fluid extends Fluid implements IGT_Fluid, IGT_RegisteredFluid, Runnable {

    private final String localizedName;
    private final ResourceLocation stillIconResourceLocation;
    private final ResourceLocation flowingIconResourceLocation;
    private final short[] colorRGBA;
    private final FluidState fluidState;
    private final Fluid iconsFrom;
    private Fluid registeredFluid;

    /**
     * Constructs this {@link IGT_Fluid} implementation from an {@link GT_FluidBuilder} instance
     *
     * @param builder The {@link GT_FluidBuilder} instance to construct this {@link IGT_Fluid} implementation
     */
    protected GT_Fluid(@Nonnull final GT_FluidBuilder builder) {
        super(builder.fluidName);
        this.localizedName = builder.localizedName;
        this.stillIconResourceLocation = builder.stillIconResourceLocation;
        this.flowingIconResourceLocation = builder.flowingIconResourceLocation;
        this.iconsFrom = builder.iconsFrom;
        this.block = builder.fluidBlock;
        this.colorRGBA = builder.colorRGBA;
        this.fluidState = builder.fluidState;
        this.temperature = builder.temperature;
        configureFromStateTemperature();
    }

    /**
     * Adjusts this {@link Fluid}'s settings based on this {@link IGT_Fluid}'s state
     */
    protected void configureFromStateTemperature() {
        switch (fluidState) {
            case SLURRY:
                setGaseous(false).setViscosity(10000);
                break;
            case GAS:
                setGaseous(true).setDensity(-100)
                    .setViscosity(200);
                break;
            case PLASMA:
                setGaseous(true).setDensity(55536)
                    .setViscosity(10)
                    .setLuminosity(15);
                break;
            case MOLTEN:
                final int luminosity;
                if (temperature >= 3500) {
                    luminosity = 15;
                } else {
                    luminosity = temperature < 1000 ? 0 : 14 * (temperature - 1000) / 2500 + 1;
                }
                setLuminosity(luminosity);
            case LIQUID:
            default:
                setGaseous(false).setViscosity(1000);
                break;
        }
    }

    // ----- Fluid implementations -----

    /**
     * @inheritDoc
     */
    @Override
    public int getColor() {
        return (Math.max(0, Math.min(255, colorRGBA[0])) << 16) | (Math.max(0, Math.min(255, colorRGBA[1])) << 8)
            | Math.max(0, Math.min(255, colorRGBA[2]));
    }

    // ----- IGT_Fluid interface implementations -----

    public IGT_RegisteredFluid addFluid() {
        if (FluidRegistry.registerFluid(GT_Fluid.this)) {
            // Registered as a new Fluid
            registeredFluid = this;
            // Adds a server-side localized-name
            GT_LanguageManager.addStringLocalization(getUnlocalizedName(), localizedName);
        } else {
            // Fluid already registered, get it from the registry
            registeredFluid = FluidRegistry.getFluid(GT_Fluid.this.fluidName);
            // Sets temperature of already registered fluids if they use the default (temperature = 300)
            if (registeredFluid.getTemperature() == new Fluid("test").getTemperature()) {
                registeredFluid.setTemperature(GT_Fluid.this.temperature);
            }
        }
        // Schedules the fluid for the block icons loader run() tasks
        GregTech_API.sGTBlockIconload.add(this);
        return this;
    }

    // ----- IGT_RegisteredFluid interface implementations -----

    /**
     * @inheritDoc
     */
    @Override
    public IGT_RegisteredFluid registerContainers(final ItemStack fullContainer, final ItemStack emptyContainer,
        final int containerSize) {
        if (fullContainer != null && emptyContainer != null) {
            final FluidStack fluidStack = new FluidStack(registeredFluid, containerSize);
            if (!FluidContainerRegistry.registerFluidContainer(fluidStack, fullContainer, emptyContainer)) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(fullContainer)
                    .itemOutputs(GT_Utility.getContainerItem(fullContainer, false))
                    .fluidOutputs(fluidStack)
                    .duration(fluidStack.amount / 62)
                    .eut(1)
                    .addTo(fluidCannerRecipes);
            }
        }
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public IGT_RegisteredFluid registerBContainers(final ItemStack fullContainer, final ItemStack emptyContainer) {
        return registerContainers(fullContainer, emptyContainer, 1000);
    }

    /**
     * @inheritDoc
     */
    @Override
    public IGT_RegisteredFluid registerPContainers(final ItemStack fullContainer, final ItemStack emptyContainer) {
        return registerContainers(fullContainer, emptyContainer, 250);
    }

    /**
     * @inheritDoc
     */
    @Override
    public IGT_RegisteredFluid configureMaterials(final Materials material) {
        if (material != null) {
            switch (fluidState) {
                case SLURRY -> material.mSolid = registeredFluid;
                case GAS -> material.mGas = registeredFluid;
                case PLASMA -> material.mPlasma = registeredFluid;
                case MOLTEN -> material.mStandardMoltenFluid = registeredFluid;
                default -> material.mFluid = registeredFluid;
            }
            Materials.FLUID_MAP.put(registeredFluid, material);
        }
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Fluid asFluid() {
        return registeredFluid;
    }

    // ----- Runnable interface implementations -----

    /**
     * This {@link Runnable#run()} implementation is scheduled within the {@link GregTech_API#sGTBlockIconload} to load
     * this {@link IGT_Fluid}'s texture icons.
     *
     * @see Runnable#run()
     */
    @Override
    public void run() {
        if (iconsFrom instanceof GT_Fluid) {
            // Needs the GT_Fluid to have registered its icons
            ((GT_Fluid) iconsFrom).run();
            stillIcon = iconsFrom.getStillIcon();
            flowingIcon = iconsFrom.getFlowingIcon();
        } else {
            if (stillIconResourceLocation != null) {
                stillIcon = GregTech_API.sBlockIcons.registerIcon(stillIconResourceLocation.toString());
            }
            if (flowingIconResourceLocation != null) {
                flowingIcon = GregTech_API.sBlockIcons.registerIcon(flowingIconResourceLocation.toString());
            }
        }
    }
}
