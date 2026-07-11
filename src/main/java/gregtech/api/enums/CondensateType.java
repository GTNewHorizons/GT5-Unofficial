package gregtech.api.enums;

import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.fluid.GTFluidFactory;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.fluid.IGTFluidBuilder;
import gregtech.api.util.GTUtility;
import gregtech.api.util.Lazy;
import gregtech.common.fluid.GTFluid;
import gregtech.common.items.GTItemCell;
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
    Neutronium(
        "neutronium",
        () -> Materials.Neutronium,
        144,
        () -> MaterialLibAPI.getFluidStack(Materials2Materials.Neutronium, Materials2FluidShapes.shapeFluidMolten, (int) (144)), 20, TierEU.RECIPE_UHV),
    CosmicNeutronium(
        "cosmicneutronium",
        () -> Materials.CosmicNeutronium,
        144,
        () -> MaterialLibAPI.getFluidStack(Materials2Materials.CosmicNeutronium, Materials2FluidShapes.shapeFluidMolten, (int) (144)), 20, TierEU.RECIPE_UHV),
    Bedrockium(
        "bedrockium",
        () -> Materials.Bedrockium,
        144,
        () -> MaterialLibAPI.getFluidStack(Materials2Materials.Bedrockium, Materials2FluidShapes.shapeFluidMolten, (int) (144)), 20, TierEU.RECIPE_UEV),
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
        () -> MaterialLibAPI.getFluidStack(Materials2Materials.Infinity, Materials2FluidShapes.shapeFluidMolten, (int) (144)), 20, TierEU.RECIPE_UEV),
    Hypogen(
        "hypogen",
        () -> MaterialsElements.STANDALONE.HYPOGEN,
        144,
        () -> MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(144), 40, TierEU.RECIPE_UIV),
    TranscendentMetal(
        "transcendentmetal",
        () -> Materials.TranscendentMetal,
        144,
        () -> MaterialLibAPI.getFluidStack(Materials2Materials.TranscendentMetal, Materials2FluidShapes.shapeFluidMolten, (int) (144)), 40, TierEU.RECIPE_UIV),
    DimensionallyShiftedSuperfluid(
        "dimshiftedsuperfluid",
        () -> Materials.DimensionallyShiftedSuperfluid,
        1000,
        () -> Materials.DimensionallyShiftedSuperfluid.getFluid(1000), 120, TierEU.RECIPE_UIV),
    PhononMedium(
        "phononmedium",
        () -> Materials.PhononMedium,
        1000,
        () -> MaterialLibAPI.getFluidStack(Materials2Materials.PhononMedium, Materials2FluidShapes.shapeFluidLiquid, (int) (1000)), 120, TierEU.RECIPE_UIV),
    QuarkGluonPlasma(
        "quarkgluonplasma",
        () -> Materials.QuarkGluonPlasma,
        1000,
        () -> MaterialLibAPI.getFluidStack(Materials2Materials.QuarkGluonPlasma, Materials2FluidShapes.shapeFluidLiquid, (int) (1000)), 120, TierEU.RECIPE_UIV),
    SpaceTime(
        "spacetime",
        () -> Materials.SpaceTime,
        144,
        () -> MaterialLibAPI.getFluidStack(Materials2Materials.SpaceTime, Materials2FluidShapes.shapeFluidMolten, (int) (144)), 60, TierEU.RECIPE_UIV),
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
        1000,
        () -> MaterialLibAPI.getFluidStack(Materials2Materials.BoundlessCosmicSolder, Materials2FluidShapes.shapeFluidLiquid, (int) (1000)), 160, TierEU.RECIPE_UMV),
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
        () -> MaterialLibAPI.getFluidStack(Materials2Materials.Universium, Materials2FluidShapes.shapeFluidMolten, (int) (144)), 80, TierEU.RECIPE_UXV),
    Eternity(
        "eternity",
        () -> Materials.Eternity,
        144,
        () -> MaterialLibAPI.getFluidStack(Materials2Materials.Eternity, Materials2FluidShapes.shapeFluidMolten, (int) (144)), 80, TierEU.RECIPE_UXV),
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
            IOreMaterial material = type.getMaterial();
            IGTFluidBuilder builder = GTFluidFactory.builder("entangled_" + type.id)
                .withColorRGBA(material.getRGBA())
                .withStateAndTemperature(FluidState.GAS, 0);

            if (material instanceof Material) {
                // GTPP fluids aren't registered yet at this point, so build as addGTFluidMolten/FluidGT6.run() does.
                builder.withTextures(
                    new ResourceLocation(
                        "miscutils",
                        "fluids/fluid.molten." + material.getTextureSet().aTextCustomAutogenerated),
                    null);
            } else {
                Fluid fluid = type.source.get()
                    .getFluid();
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
