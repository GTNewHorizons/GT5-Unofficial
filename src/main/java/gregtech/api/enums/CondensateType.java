package gregtech.api.enums;

import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.QFT_CATALYST;
import static gregtech.api.util.GTRecipeConstants.QFT_FOCUS_TIER;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.quantumForceTransformerRecipes;

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
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import tectech.recipe.TecTechRecipeMaps;

/// Each of these entries maps to a type of condensate used by the BEC multis. Condensate itself is just a fluid, but
/// this class autogens the recipes, cells, and fluids for each type. This isn't a material because I didn't want to
/// contaminate the material system with a bunch of BEC-specific code, especially since the logic for this class is so
/// minimal.
/// Note that 'entangled' condensate should never be something you can manufacture outside the BEC network system. It's
/// meant to be a special fluid that you cannot get normally. Prepared condensate is manufactured by a QFT in practice,
/// but it can be produced by anything if the mechanics warrant it. The entangled condensate cell isn't obtainable, it's
/// just used to tell the player that the fluid exists. Prepared condensate is a real fluid that can be used in any way.
public enum CondensateType {

    // Both of these condensates are temporary
    // spotless:off
    Quantium(
        "quantium",
        () -> Materials.Quantium,
        144,
        prepare -> prepare.fluidInputs(Materials.Quantium.getMolten(144)),
        generate -> generate.duration(20).eut(TierEU.RECIPE_UHV)),
    Infinity(
        "infinity",
        () -> Materials.Infinity,
        144,
        prepare -> prepare.fluidInputs(Materials.Infinity.getMolten(144)),
        generate -> generate.duration(40).eut(TierEU.RECIPE_UEV)),
    // spotless:on
    ;

    private final String id;
    private final Lazy<IOreMaterial> material;
    private final int unit;
    private final Consumer<GTRecipeBuilder> preparation;
    private final Consumer<GTRecipeBuilder> entanglement;
    private Fluid preparedFluid, entangledFluid;
    private GTItemCell preparedCell, entangledCell;

    CondensateType(String id, Supplier<IOreMaterial> mat, int unit, Consumer<GTRecipeBuilder> preparation,
        Consumer<GTRecipeBuilder> entanglement) {
        this.id = id;
        this.material = new Lazy<>(mat);
        this.unit = unit;
        this.preparation = preparation;
        this.entanglement = entanglement;
    }

    public IOreMaterial getMaterial() {
        return material.get();
    }

    public FluidStack getPrepared(int amount) {
        return new FluidStack(preparedFluid, amount);
    }

    public FluidStack getEntangled(int amount) {
        return new FluidStack(entangledFluid, amount);
    }

    public String getAbbrevName() {
        return GTUtility.translate("abbrev.entangled_" + id);
    }

    public static void registerFluids() {
        for (CondensateType type : values()) {
            type.preparedFluid = GTFluidFactory.builder("prepared_" + type.id)
                .withTextures(new ResourceLocation("gregtech:fluids/condensate/fluid." + type.id + "_prepared"), null)
                .withColorRGBA(
                    type.getMaterial()
                        .getRGBA())
                .withStateAndTemperature(FluidState.GAS, 1)
                .buildAndRegister()
                .asFluid();

            type.entangledFluid = GTFluidFactory.builder("entangled_" + type.id)
                .withTextures(new ResourceLocation("gregtech:fluids/condensate/fluid." + type.id + "_entangled"), null)
                .withColorRGBA(
                    type.getMaterial()
                        .getRGBA())
                .withStateAndTemperature(FluidState.GAS, 0)
                .buildAndRegister()
                .asFluid();

            type.preparedCell = new GTItemCell("prepared_" + type.id, "prepared_condensate", type.preparedFluid);
            type.entangledCell = new GTItemCell("entangled_" + type.id, "entangled_condensate", type.entangledFluid);
        }
    }

    public static void registerRecipes() {
        for (CondensateType type : values()) {
            GTRecipeBuilder prepare = GTValues.RA.stdBuilder()
                .fluidOutputs(new FluidStack(type.preparedFluid, type.unit))
                .metadata(QFT_CATALYST, GregtechItemList.SimpleNaquadahCatalyst.get(0))
                .metadata(QFT_FOCUS_TIER, 2)
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UIV);

            type.preparation.accept(prepare);

            prepare.addTo(quantumForceTransformerRecipes);

            GTRecipeBuilder generate = GTValues.RA.stdBuilder()
                .fluidInputs(new FluidStack(type.preparedFluid, type.unit))
                .fluidOutputs(new FluidStack(type.entangledFluid, type.unit));

            type.entanglement.accept(generate);

            generate.addTo(TecTechRecipeMaps.condensateGeneratorRecipes);
        }
    }

    public static CondensateType getCondensateType(Fluid fluid) {
        for (CondensateType type : values()) {
            if (fluid == type.preparedFluid || fluid == type.entangledFluid) {
                return type;
            }
        }

        return null;
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
