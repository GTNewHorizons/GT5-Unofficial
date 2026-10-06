package gregtech.common.fluid;

import static gregtech.api.recipe.RecipeMaps.cannerRecipes;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.ShapeFluidInContainer;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.FluidState;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.fluid.IGTFluid;
import gregtech.api.interfaces.fluid.IGTRegisteredFluid;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;

public class GTFluid extends Fluid implements IGTFluid, IGTRegisteredFluid, Runnable {

    private final String defaultLocalName;
    private final ResourceLocation stillIconResourceLocation;
    private final ResourceLocation flowingIconResourceLocation;
    private final short[] colorRGBA;
    private final FluidState fluidState;
    private final Fluid iconsFrom;
    private Fluid registeredFluid;
    private Supplier<String> localizedName;

    /**
     * Constructs this {@link IGTFluid} implementation from an {@link GTFluidBuilder} instance
     *
     * @param builder The {@link GTFluidBuilder} instance to construct this {@link IGTFluid} implementation
     */
    protected GTFluid(@Nonnull final GTFluidBuilder builder) {
        super(builder.fluidName);
        this.defaultLocalName = builder.defaultLocalName;
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
     * Adjusts this {@link Fluid}'s settings based on this {@link IGTFluid}'s state
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

    // ----- IGTFluid interface implementations -----

    @Override
    public IGTRegisteredFluid addFluid() {
        if (FluidRegistry.registerFluid(GTFluid.this)) {
            // Registered as a new Fluid
            registeredFluid = this;
        } else {
            // Fluid already registered, get it from the registry
            registeredFluid = FluidRegistry.getFluid(GTFluid.this.fluidName);
            // Sets temperature of already registered fluids if they use the default (temperature = 300)
            if (registeredFluid.getTemperature() == new Fluid("test").getTemperature()) {
                registeredFluid.setTemperature(GTFluid.this.temperature);
            }
        }
        // Schedules the fluid for the block icons loader run() tasks
        GregTechAPI.sGTBlockIconload.add(this);
        return this;
    }

    // ----- IGT_RegisteredFluid interface implementations -----

    /**
     * @inheritDoc
     */
    @Override
    public IGTRegisteredFluid registerContainers(final ItemStack fullContainer, final ItemStack emptyContainer,
        final int containerSize) {
        // A MaterialLib fluid-in-container shape registers its own mapping at MaterialLib's init, and
        // FluidContainerRegistry keeps only one entry per filled item.
        if (fullContainer != null && fullContainer.getItem() instanceof ShapeFluidInContainer) return this;
        if (fullContainer != null && emptyContainer != null) {
            final FluidStack fluidStack = new FluidStack(registeredFluid, containerSize);
            if (!FluidContainerRegistry.registerFluidContainer(fluidStack, fullContainer, emptyContainer)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(fullContainer)
                    .itemOutputs(GTUtility.getContainerItem(fullContainer, false))
                    .fluidOutputs(fluidStack)
                    .duration(fluidStack.amount / 62)
                    .eut(1)
                    .addTo(cannerRecipes);
            }
        }
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public IGTRegisteredFluid registerBContainers(final ItemStack fullContainer, final ItemStack emptyContainer) {
        return registerContainers(fullContainer, emptyContainer, 1000);
    }

    /**
     * @inheritDoc
     */
    @Override
    public IGTRegisteredFluid registerPContainers(final ItemStack fullContainer, final ItemStack emptyContainer) {
        return registerContainers(fullContainer, emptyContainer, 250);
    }

    /**
     * Adds a server-side localized-name
     */
    @Override
    public IGTRegisteredFluid addLocalizedName() {
        GTLanguageManager.addStringLocalization(getUnlocalizedName(), defaultLocalName);
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
     * This {@link Runnable#run()} implementation is scheduled within the {@link GregTechAPI#sGTBlockIconload} to
     * load
     * this {@link IGTFluid}'s texture icons.
     *
     * @see Runnable#run()
     */
    @Override
    public void run() {
        if (iconsFrom instanceof GTFluid) {
            // Needs the GTFluid to have registered its icons
            ((GTFluid) iconsFrom).run();
            stillIcon = iconsFrom.getStillIcon();
            flowingIcon = iconsFrom.getFlowingIcon();
        } else {
            if (stillIconResourceLocation != null) {
                stillIcon = GregTechAPI.sBlockIcons.registerIcon(stillIconResourceLocation.toString());
            }
            if (flowingIconResourceLocation != null) {
                flowingIcon = GregTechAPI.sBlockIcons.registerIcon(flowingIconResourceLocation.toString());
            }
        }
    }

    @Override
    public String toString() {
        return "GTFluid{" + "fluidName='" + fluidName + '\'' + '}';
    }

    @Override
    public String getLocalizedName() {
        if (localizedName == null) return super.getLocalizedName();
        return localizedName.get();
    }
}
