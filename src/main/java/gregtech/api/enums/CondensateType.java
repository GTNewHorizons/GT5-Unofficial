package gregtech.api.enums;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.fluid.GTFluidFactory;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.api.util.Lazy;
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
    ChromaticGlass(
        "chromaticglass",
        () -> MaterialsElements.STANDALONE.CHRONOMATIC_GLASS,
        144,
        recipe -> recipe.fluidInputs(MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(144)).duration(20).eut(TierEU.RECIPE_UEV)),
    CelestialTungsten(
        "celestialtungsten",
        () -> MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN,
        144,
        recipe -> recipe.fluidInputs(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(144)).duration(20).eut(TierEU.RECIPE_UEV)),
    Infinity(
        "infinity",
        () -> Materials.Infinity,
        144,
        recipe -> recipe.fluidInputs(Materials.Infinity.getMolten(144)).duration(20).eut(TierEU.RECIPE_UEV)),
    Hypogen(
        "hypogen",
        () -> MaterialsElements.STANDALONE.HYPOGEN,
        144,
        recipe -> recipe.fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(144)).duration(40).eut(TierEU.RECIPE_UIV)),
    TranscendentMetal(
        "transcendentmetal",
        () -> Materials.TranscendentMetal,
        144,
        recipe -> recipe.fluidInputs(Materials.TranscendentMetal.getMolten(144)).duration(40).eut(TierEU.RECIPE_UIV)),
    DimensionallyShiftedSuperfluid(
        "dimshiftedsuperfluid",
        () -> Materials.DimensionallyShiftedSuperfluid,
        144,
        recipe -> recipe.fluidInputs(Materials.DimensionallyShiftedSuperfluid.getFluid(144)).duration(40).eut(TierEU.RECIPE_UIV)),
    SpaceTime(
        "spacetime",
        () -> Materials.SpaceTime,
        144,
        recipe -> recipe.fluidInputs(Materials.SpaceTime.getMolten(144)).duration(40).eut(TierEU.RECIPE_UIV)),
    Time(
        "time",
        () -> Materials.Time,
        144,
        recipe -> recipe.fluidInputs(Materials.Time.getMolten(144)).duration(60).eut(TierEU.RECIPE_UMV)),
    Space(
        "space",
        () -> Materials.Space,
        144,
        recipe -> recipe.fluidInputs(Materials.Space.getMolten(144)).duration(60).eut(TierEU.RECIPE_UMV)),
    Hexanite(
        "hexanite",
        () -> Materials.Hexanite,
        144,
        recipe -> recipe.fluidInputs(Materials.Hexanite.getMolten(144)).duration(60).eut(TierEU.RECIPE_UMV)),
    BoundlessCosmicSolder(
        "cosmicsolder",
        () -> Materials.BoundlessCosmicSolder,
        144,
        recipe -> recipe.fluidInputs(Materials.BoundlessCosmicSolder.getFluid(144)).duration(60).eut(TierEU.RECIPE_UMV)),
    MHDCSM(
        "mhdcsm",
        () -> Materials.MHDCSM,
        144,
        recipe -> recipe.fluidInputs(Materials.MHDCSM.getMolten(144)).duration(80).eut(TierEU.RECIPE_UXV)),
    MagMatter(
        "magmatter",
        () -> Materials.MagMatter,
        144,
        recipe -> recipe.fluidInputs(Materials.MagMatter.getMolten(144)).duration(80).eut(TierEU.RECIPE_UXV)),
    Universium(
        "universium",
        () -> Materials.Universium,
        144,
        recipe -> recipe.fluidInputs(Materials.Universium.getMolten(144)).duration(80).eut(TierEU.RECIPE_UXV)),
    Eternity(
        "eternity",
        () -> Materials.Eternity,
        144,
        recipe -> recipe.fluidInputs(Materials.Eternity.getMolten(144)).duration(80).eut(TierEU.RECIPE_UXV)),
    // spotless:on
    ;

    private final String id;
    private final Lazy<IOreMaterial> material;
    private final int unit;
    private final Consumer<GTRecipeBuilder> recipe;
    private Fluid entangledFluid;
    private GTItemCell entangledCell;

    CondensateType(String id, Supplier<IOreMaterial> mat, int unit, Consumer<GTRecipeBuilder> recipe) {
        this.id = id;
        this.material = new Lazy<>(mat);
        this.unit = unit;
        this.recipe = recipe;
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
            type.entangledFluid = GTFluidFactory.builder("entangled_" + type.id)
                .withTextures(new ResourceLocation("gregtech:fluids/condensate/fluid." + type.id + "_entangled"), null)
                .withColorRGBA(
                    type.getMaterial()
                        .getRGBA())
                .withStateAndTemperature(FluidState.GAS, 0)
                .buildAndRegister()
                .asFluid();

            type.entangledCell = new GTItemCell("entangled_" + type.id, "entangled_condensate", type.entangledFluid);
        }
    }

    public static void registerRecipes() {
        for (CondensateType type : values()) {
            GTRecipeBuilder generate = GTValues.RA.stdBuilder()
                .fluidOutputs(new FluidStack(type.entangledFluid, type.unit));

            type.recipe.accept(generate);

            generate.addTo(TecTechRecipeMaps.condensateGeneratorRecipes);
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

    /// Resolves the color to render a condensate fluid with. Condensates of a gtPlusPlus material that uses a dynamic
    /// render mode (the glow or rainbow hue cycle, e.g. Celestial Tungsten and Chromatic Glass) reuse that material's
    /// animated color, so the condensate matches how the material is drawn on its blocks/items/fluids. This mirrors
    /// the approach added for those materials in PR #6649. Any other fluid keeps the provided fallback color.
    public static int getRenderColor(Fluid fluid, int fallbackColor) {
        CondensateType type = getCondensateType(fluid);
        if (type != null && type.getMaterial() instanceof Material gtppMaterial && gtppMaterial.getRGBA()[3] > 1) {
            return BaseItemComponent.getMaterialCustomColor(gtppMaterial);
        }
        return fallbackColor;
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
