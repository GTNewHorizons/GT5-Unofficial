package gregtech.common.fluid;

import static gregtech.api.recipe.RecipeMaps.cannerRecipes;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.ShapeItem;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.FluidState;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.fluid.IGTFluid;
import gregtech.api.interfaces.fluid.IGTRegisteredFluid;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;

public class GTFluid extends Fluid implements IGTFluid, IGTRegisteredFluid, Runnable {

    /// Set via the `gt.dumpMaterialData` system property (see `gregtech.common.misc.MaterialDataDump`),
    /// mirroring [gregtech.api.items.MetaGeneratedItemX32#DUMP_MODE]. While active, every constructed
    /// [GTFluid] records its still-icon texture into [#DUMP_TEXTURES], regardless of whether [#addFluid]
    /// later registers it fresh or reuses an existing registration.
    public static final boolean DUMP_MODE = Boolean.getBoolean("gt.dumpMaterialData");

    /// Forge fluid name (lowercase) -> the still-icon [ResourceLocation] string a [GTFluid] with that name
    /// registered, first construction wins (mirroring legacy first-registration-wins for a name shared
    /// between two creation sites). A fluid built with [GTFluidBuilder#withIconsFrom] resolves through its
    /// source fluid's already-captured entry by name -- its own still-icon path is unused, bogus default
    /// data in that case -- rather than its own; a source fluid this map has no entry for (not itself built
    /// through a [GTFluidBuilder] this session) leaves the entry uncaptured. Only populated while
    /// [#DUMP_MODE] is active.
    public static final Map<String, String> DUMP_TEXTURES = DUMP_MODE ? new LinkedHashMap<>() : Collections.emptyMap();

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
        if (DUMP_MODE) captureDumpTexture();
    }

    /// Records this fluid's still-icon texture into [#DUMP_TEXTURES], first construction of a given name wins.
    private void captureDumpTexture() {
        if (DUMP_TEXTURES.containsKey(fluidName)) return;
        String texture = iconsFrom != null ? DUMP_TEXTURES.get(iconsFrom.getName())
            : (stillIconResourceLocation != null ? stillIconResourceLocation.toString() : null);
        if (texture != null) DUMP_TEXTURES.put(fluidName, texture);
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
        // A MaterialLib-shaped filled container (a cut-over cell prefix resolved through GTOreDictUnificator,
        // see MU) registers its own FluidContainerRegistry mapping at MaterialLib's init -- registering it
        // again here first, during GT's earlier preInit, would make that later registration a rejected
        // duplicate (FluidContainerRegistry is a flat map keyed by the filled item).
        if (fullContainer != null && fullContainer.getItem() instanceof ShapeItem) return this;
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
     * @inheritDoc
     */
    @Override
    public IGTRegisteredFluid configureMaterials(final Materials material) {
        // In dump mode the legacy builders re-run even for materials whose fluid MaterialLib already wired
        // (see GTProxy's addAutoGenerated* bypasses), purely so the constructor capture above fires; leaving
        // an already-wired slot untouched keeps the dumped fluid fields ground truth (e.g. Concrete's
        // hand-wired wet.concrete molten fluid would otherwise be clobbered by the generic molten loop's
        // freshly registered molten.concrete).
        if (material != null && !(DUMP_MODE && slotAlreadyWired(material))) {
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

    private boolean slotAlreadyWired(Materials material) {
        return switch (fluidState) {
            case SLURRY -> material.mSolid != null;
            case GAS -> material.mGas != null;
            case PLASMA -> material.mPlasma != null;
            case MOLTEN -> material.mStandardMoltenFluid != null;
            default -> material.mFluid != null;
        };
    }

    /**
     * Adds a server-side localized-name
     */
    @Override
    public IGTRegisteredFluid addLocalizedName() {
        GTLanguageManager.addStringLocalization(getUnlocalizedName(), defaultLocalName);
        return this;
    }

    private boolean addLocalizedNameHasOreprefix(IOreMaterial material, String[] oreprefixNames) {
        return Arrays.stream(oreprefixNames)
            .anyMatch(oreprefixName -> addLocalizedNameHasOreprefix(material, oreprefixName));
    }

    private boolean addLocalizedNameHasOreprefix(IOreMaterial material, String oreprefixName) {
        final String oreprefixNameRemovedFormat = oreprefixName.replace("%s", "");
        if (defaultLocalName.contains(oreprefixNameRemovedFormat)) {
            if (String.format(oreprefixName, material.getDefaultLocalName())
                .equals(defaultLocalName)) {
                localizedName = () -> OrePrefixes.getLocalizedNameForItem(oreprefixName, "%s", material);
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the localized name matches the material name, or if it matches any of the oreprefix formatted material
     * names. If neither matches, add a key for localized.
     *
     * @param material material from GT/BW/GT++
     *
     * @implNote The method checks for the following oreprefix patterns:
     *           <ul>
     *           <li>Molten %s</li>
     *           <li>%s Plasma</li>
     *           <li>Lightly Hydro-Cracked %s</li>
     *           <li>Lightly Steam-Cracked %s</li>
     *           <li>Moderately Hydro-Cracked %s</li>
     *           <li>Moderately Steam-Cracked %s</li>
     *           <li>Severely Hydro-Cracked %s</li>
     *           <li>Severely Steam-Cracked %s</li>
     *           </ul>
     *           If a pattern matches, it uses formatted localization with the appropriate oreprefix key.
     */
    @Override
    public IGTRegisteredFluid addLocalizedName(IOreMaterial material) {
        if (material == null) return addLocalizedName();
        if (!material.getDefaultLocalName()
            .equals(defaultLocalName)) {
            if (addLocalizedNameHasOreprefix(
                material,
                new String[] { "Molten %s", "%s Plasma", "Lightly Hydro-Cracked %s", "Lightly Steam-Cracked %s",
                    "Moderately Hydro-Cracked %s", "Moderately Steam-Cracked %s", "Severely Hydro-Cracked %s",
                    "Severely Steam-Cracked %s" })) {
                return this;
            }
            return addLocalizedName();
        }
        localizedName = material::getLocalizedName;
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
