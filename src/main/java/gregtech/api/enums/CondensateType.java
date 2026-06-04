package gregtech.api.enums;

import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.fluid.GTFluidFactory;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.fluid.IGTFluidBuilder;
import gregtech.api.util.GTUtility;
import gregtech.api.util.Lazy;
import gregtech.common.fluid.GTFluid;
import gregtech.common.items.GTItemCell;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialsElements;
import tectech.recipe.TecTechRecipeMaps;

/// Each of these entries maps to a type of condensate used by the BEC multis. Condensate itself is just a fluid, but
/// this class autogens the recipes, cells, and fluids for each type. This isn't a material because I didn't want to
/// contaminate the material system with a bunch of BEC-specific code, especially since the logic for this class is so
/// minimal.
/// Note that 'entangled' condensate should never be something you can manufacture outside the BEC network system. It's
/// meant to be a special fluid that you cannot get normally. It's generated in the condensate generator directly from
/// the source material's molten fluid (or its standard fluid form, for materials that have no molten). The entangled
/// condensate cell isn't obtainable, it's just used to tell the player that the fluid exists.
public enum CondensateType {

    // spotless:off
    NETHERITE(
        "netherite",
        () -> Materials.ActivatedNetherite,
        144,
        () -> Materials.ActivatedNetherite.getFluid(144), 20, TierEU.RECIPE_UHV),
    NEUTRONIUM(
        "neutronium",
        () -> Materials.Neutronium,
        144,
        () -> Materials.Neutronium.getMolten(144), 20, TierEU.RECIPE_UHV),
    BEDROCKIUM(
        "bedrockium",
        () -> Materials.Bedrockium,
        144,
        () -> Materials.Bedrockium.getMolten(144), 20, TierEU.RECIPE_UEV),
    ChromaticGlass(
        "chromaticglass",
        () -> MaterialsElements.STANDALONE.CHRONOMATIC_GLASS,
        144,
        () -> MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(144), 20, TierEU.RECIPE_UEV),
    CelestialTungsten(
        "celestialtungsten",
        () -> MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN,
        144,
        () -> MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(144), 20, TierEU.RECIPE_UEV),
    Infinity(
        "infinity",
        () -> Materials.Infinity,
        144,
        () -> Materials.Infinity.getMolten(144), 20, TierEU.RECIPE_UEV),
    Hypogen(
        "hypogen",
        () -> MaterialsElements.STANDALONE.HYPOGEN,
        144,
        () -> MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(144), 40, TierEU.RECIPE_UIV),
    TranscendentMetal(
        "transcendentmetal",
        () -> Materials.TranscendentMetal,
        144,
        () -> Materials.TranscendentMetal.getMolten(144), 40, TierEU.RECIPE_UIV),
    DimensionallyShiftedSuperfluid(
        "dimshiftedsuperfluid",
        () -> Materials.DimensionallyShiftedSuperfluid,
        144,
        () -> Materials.DimensionallyShiftedSuperfluid.getFluid(144), 40, TierEU.RECIPE_UIV),
    SpaceTime(
        "spacetime",
        () -> Materials.SpaceTime,
        144,
        () -> Materials.SpaceTime.getMolten(144), 40, TierEU.RECIPE_UIV),
    Time(
        "time",
        () -> Materials.Time,
        144,
        () -> Materials.Time.getMolten(144), 60, TierEU.RECIPE_UMV),
    Space(
        "space",
        () -> Materials.Space,
        144,
        () -> Materials.Space.getMolten(144), 60, TierEU.RECIPE_UMV),
    BoundlessCosmicSolder(
        "cosmicsolder",
        () -> Materials.BoundlessCosmicSolder,
        144,
        () -> Materials.BoundlessCosmicSolder.getFluid(144), 60, TierEU.RECIPE_UMV),
    MHDCSM(
        "mhdcsm",
        () -> Materials.MHDCSM,
        144,
        () -> Materials.MHDCSM.getMolten(144), 80, TierEU.RECIPE_UXV),
    MagMatter(
        "magmatter",
        () -> Materials.MagMatter,
        144,
        () -> Materials.MagMatter.getMolten(144), 80, TierEU.RECIPE_UXV),
    Universium(
        "universium",
        () -> Materials.Universium,
        144,
        () -> Materials.Universium.getMolten(144), 80, TierEU.RECIPE_UXV),
    Eternity(
        "eternity",
        () -> Materials.Eternity,
        144,
        () -> Materials.Eternity.getMolten(144), 80, TierEU.RECIPE_UXV),
    // spotless:on
    ;

    private final String id;
    private final Lazy<IOreMaterial> material;
    private final int unit;
    private final Supplier<FluidStack> source;
    private final int duration;
    private final long eut;
    private Fluid entangledFluid;
    private GTItemCell entangledCell;

    CondensateType(String id, Supplier<IOreMaterial> mat, int unit, Supplier<FluidStack> source, int duration,
        long eut) {
        this.id = id;
        this.material = new Lazy<>(mat);
        this.unit = unit;
        this.source = source;
        this.duration = duration;
        this.eut = eut;
    }

    public IOreMaterial getMaterial() {
        return material.get();
    }

    public FluidStack getEntangled(int amount) {
        return new FluidStack(entangledFluid, amount);
    }

    public String getAbbrevName() {
        return GTUtility.translate("abbrev.entangled_" + id);
    }

    public static void registerFluids() {
        for (CondensateType type : values()) {
            IGTFluidBuilder builder = GTFluidFactory.builder("entangled_" + type.id)
                .withColorRGBA(
                    type.getMaterial()
                        .getRGBA())
                .withStateAndTemperature(FluidState.GAS, 0);

            ResourceLocation ownTexture = new ResourceLocation(
                "gregtech:fluids/condensate/fluid." + type.id + "_entangled");

            switch (type) {
                // Chromatic glass and celestial tungsten keep their bespoke condensate textures.
                case ChromaticGlass, CelestialTungsten -> builder.withTextures(ownTexture, null);
                // Hypogen's source is a gtPlusPlus fluid (can't share via withIconsFrom), so point at its molten
                // texture.
                case Hypogen -> builder
                    .withTextures(new ResourceLocation("miscutils", "fluids/fluid.molten.hypogen"), null);
                // Everything else reuses the source fluid's stitched icon when it's a GTFluid.
                default -> {
                    FluidStack src = type.source.get();
                    if (src != null && src.getFluid() instanceof GTFluid) {
                        builder.withIconsFrom(src.getFluid());
                    } else {
                        builder.withTextures(ownTexture, null);
                    }
                }
            }

            type.entangledFluid = builder.buildAndRegister()
                .asFluid();

            type.entangledCell = new GTItemCell("entangled_" + type.id, "entangled_condensate", type.entangledFluid);
        }
    }

    public static void registerRecipes() {
        for (CondensateType type : values()) {
            GTValues.RA.stdBuilder()
                .fluidInputs(type.source.get())
                .fluidOutputs(new FluidStack(type.entangledFluid, type.unit))
                .duration(type.duration)
                .eut(type.eut)
                .addTo(TecTechRecipeMaps.condensateGeneratorRecipes);
        }
    }

    public static CondensateType getCondensateType(Fluid fluid) {
        for (CondensateType type : values()) {
            if (fluid == type.entangledFluid) {
                return type;
            }
        }

        return null;
    }

    /// Resolves the color to render a condensate fluid with. Used for chromatic glass and celestial tungsten
    /// to use their custom tinting methods, matching other items/fluids of the same material.
    /// </p>
    /// Returns 0xFFFFFF for all other materials.
    public static int getRenderColor(Fluid fluid) {
        CondensateType type = getCondensateType(fluid);
        if (type == null) {
            return 0xFFFFFF;
        }
        if (type.getMaterial() instanceof Material gtppMaterial && gtppMaterial.getRGBA()[3] > 1) {
            return BaseItemComponent.getMaterialCustomColor(gtppMaterial);
        }
        // Shared fluid textures are tinted, so match the source fluid's own render color.
        FluidStack src = type.source.get();
        return src != null ? src.getFluid()
            .getColor() : 0xFFFFFF;
    }

    /// Gets the name for a given fluid. This will usually be the condensate name, but non-condensate fluids will
    /// return their localized name. This is for future proofing, in case we ever want to include non-condensate in the
    /// condensate network for some reason.
    public static String getCondensateName(Fluid fluid) {
        CondensateType condensate = getCondensateType(fluid);

        if (condensate != null) {
            return condensate.getAbbrevName();
        } else {
            return new FluidStack(fluid, 1).getLocalizedName();
        }
    }
}
