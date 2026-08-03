package gregtech.api.enums;

import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.fluid.GTFluidFactory;
import gregtech.api.interfaces.fluid.IGTFluidBuilder;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTUtility;
import gregtech.api.util.Lazy;
import gregtech.common.fluid.GTFluid;
import gregtech.common.items.GTItemCell;
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
    Neutronium(
        "neutronium",
        () -> Materials.Neutronium,
        144,
        () -> MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidMolten, 144), 20, TierEU.RECIPE_UHV),
    CosmicNeutronium(
        "cosmicneutronium",
        () -> Materials.CosmicNeutronium,
        144,
        () -> MaterialLibAPI.getFluidStack(Materials.CosmicNeutronium, FluidShapes.fluidMolten, 144), 20, TierEU.RECIPE_UHV),
    Bedrockium(
        "bedrockium",
        () -> Materials.Bedrockium,
        144,
        () -> MaterialLibAPI.getFluidStack(Materials.Bedrockium, FluidShapes.fluidMolten, 144), 20, TierEU.RECIPE_UEV),
    ChromaticGlass(
        "chromaticglass",
        () -> Materials.ChromaticGlass,
        144,
        () -> MaterialUtils.anyFluid(Materials.ChromaticGlass, 144), 20, TierEU.RECIPE_UEV),
    CelestialTungsten(
        "celestialtungsten",
        () -> Materials.CelestialTungsten,
        144,
        () -> MaterialUtils.anyFluid(Materials.CelestialTungsten, 144), 20, TierEU.RECIPE_UEV),
    Infinity(
        "infinity",
        () -> Materials.Infinity,
        144,
        () -> MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidMolten, 144), 20, TierEU.RECIPE_UEV),
    Hypogen(
        "hypogen",
        () -> Materials.Hypogen,
        144,
        () -> MaterialUtils.anyFluid(Materials.Hypogen, 144), 40, TierEU.RECIPE_UIV),
    TranscendentMetal(
        "transcendentmetal",
        () -> Materials.TranscendentMetal,
        144,
        () -> MaterialLibAPI.getFluidStack(Materials.TranscendentMetal, FluidShapes.fluidMolten, 144), 40, TierEU.RECIPE_UIV),
    DimensionallyShiftedSuperfluid(
        "dimshiftedsuperfluid",
        () -> Materials.dimensionallyshiftedsuperfluid,
        1000,
        () -> MaterialUtils.fluid(Materials.dimensionallyshiftedsuperfluid, 1000), 120, TierEU.RECIPE_UIV),
    PhononMedium(
        "phononmedium",
        () -> Materials.PhononMedium,
        1000,
        () -> MaterialLibAPI.getFluidStack(Materials.PhononMedium, FluidShapes.fluidLiquid, 1000), 120, TierEU.RECIPE_UIV),
    QuarkGluonPlasma(
        "quarkgluonplasma",
        () -> Materials.QuarkGluonPlasma,
        1000,
        () -> MaterialLibAPI.getFluidStack(Materials.QuarkGluonPlasma, FluidShapes.fluidLiquid, 1000), 120, TierEU.RECIPE_UIV),
    SpaceTime(
        "spacetime",
        () -> Materials.SpaceTime,
        144,
        () -> MaterialLibAPI.getFluidStack(Materials.SpaceTime, FluidShapes.fluidMolten, 144), 60, TierEU.RECIPE_UIV),
    Time(
        "time",
        () -> Materials.temporalFluid,
        144,
        () -> MaterialUtils.molten(Materials.temporalFluid, 144), 60, TierEU.RECIPE_UMV),
    Space(
        "space",
        () -> Materials.spatialFluid,
        144,
        () -> MaterialUtils.molten(Materials.spatialFluid, 144), 60, TierEU.RECIPE_UMV),
    BoundlessCosmicSolder(
        "cosmicsolder",
        () -> Materials.BoundlessCosmicSolder,
        1000,
        () -> MaterialLibAPI.getFluidStack(Materials.BoundlessCosmicSolder, FluidShapes.fluidLiquid, 1000), 160, TierEU.RECIPE_UMV),
    MHDCSM(
        "mhdcsm",
        () -> Materials.MagnetohydrodynamicallyConstrainedStarMatter,
        144,
        () -> MaterialUtils.molten(Materials.MagnetohydrodynamicallyConstrainedStarMatter, 144), 80, TierEU.RECIPE_UXV),
    MagMatter(
        "magmatter",
        () -> Materials.Magmatter,
        144,
        () -> MaterialUtils.molten(Materials.Magmatter, 144), 80, TierEU.RECIPE_UXV),
    Universium(
        "universium",
        () -> Materials.Universium,
        144,
        () -> MaterialLibAPI.getFluidStack(Materials.Universium, FluidShapes.fluidMolten, 144), 80, TierEU.RECIPE_UXV),
    Eternity(
        "eternity",
        () -> Materials.Eternity,
        144,
        () -> MaterialLibAPI.getFluidStack(Materials.Eternity, FluidShapes.fluidMolten, 144), 80, TierEU.RECIPE_UXV),
    // spotless:on
    ;

    private final String id;
    private final Lazy<Material> material;
    private final int unit;
    private final Supplier<FluidStack> source;
    private final int duration;
    private final long eut;
    private Fluid entangledFluid;
    private GTItemCell entangledCell;

    CondensateType(String id, Supplier<Material> mat, int unit, Supplier<FluidStack> source, int duration, long eut) {
        this.id = id;
        this.material = new Lazy<>(mat);
        this.unit = unit;
        this.source = source;
        this.duration = duration;
        this.eut = eut;
    }

    public Material getMaterial() {
        return material.get();
    }

    public FluidStack getEntangled(int amount) {
        // Half the unit allows for some niche balance cases while being easy to handle on the player side,
        // but any other non-multiple is needlessly complicated and bad player experience.
        if (amount % (unit / 2) != 0) throw new IllegalArgumentException(
            "amount " + amount
                + " of condensate "
                + id
                + " is not cleanly divisible by its unit amount "
                + unit
                + " or half that");
        return new FluidStack(entangledFluid, amount);
    }

    public int getUnit() {
        return unit;
    }

    public String getAbbrevName() {
        return GTUtility.translate("abbrev.entangled_" + id);
    }

    public static void registerFluids() {
        for (CondensateType type : values()) {
            Material material = type.getMaterial();
            IGTFluidBuilder builder = GTFluidFactory.builder("entangled_" + type.id)
                .withColorRGBA(MaterialUtils.rgba(material))
                .withStateAndTemperature(FluidState.GAS, 0);

            FluidStack src = type.source.get();
            if (src == null) {
                // The backing fluid (e.g. "molten.hypogen") isn't registered yet at this point in the load
                // order, so build as addGTFluidMolten/FluidGT6.run() does instead of reading its icons.
                builder.withTextures(
                    new ResourceLocation(
                        "miscutils",
                        "fluids/fluid.molten." + MaterialUtils.customTextureSetName(material)),
                    null);
            } else {
                Fluid fluid = src.getFluid();
                if (fluid instanceof GTFluid) {
                    builder.withIconsFrom(fluid);
                } else {
                    builder.withTextures(
                        new ResourceLocation("gregtech:fluids/condensate/fluid." + type.id + "_entangled"),
                        null);
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

    /// Resolves the color to render a condensate fluid with. Condensate textures are baked (untinted), so this
    /// matches the source fluid's own render color, and returns 0xFFFFFF for unknown fluids.
    public static int getRenderColor(Fluid fluid) {
        CondensateType type = getCondensateType(fluid);
        if (type == null) {
            return 0xFFFFFF;
        }
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
