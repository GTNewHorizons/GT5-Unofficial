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

public enum CondensateType {
    Quantium(
        "quantium",
        () -> Materials.Quantium,
        144,
        prepare -> prepare.fluidInputs(Materials.Quantium.getMolten(144)),
        generate -> generate.duration(20).eut(TierEU.RECIPE_UHV)),
    //
    ;

    private final String id;
    private final Lazy<IOreMaterial> material;
    private final int unit;
    private final Consumer<GTRecipeBuilder> preparation;
    private final Consumer<GTRecipeBuilder> generation;
    private Fluid preparedFluid, entangledFluid;
    private GTItemCell preparedCell, entangledCell;

    CondensateType(String id, Supplier<IOreMaterial> mat, int unit, Consumer<GTRecipeBuilder> preparation, Consumer<GTRecipeBuilder> generation) {
        this.id = id;
        this.material = new Lazy<>(mat);
        this.unit = unit;
        this.preparation = preparation;
        this.generation = generation;
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
        return GTUtility.translate("abbrev.entangled_" + id + ".name");
    }

    public static void registerFluids() {
        for (CondensateType type : values()) {
            type.preparedFluid = GTFluidFactory.builder("prepared_" + type.id)
                .withTextures(new ResourceLocation("gregtech:fluids/condensate/fluid." + type.id + "_prepared"), null)
                .withLocalizedName(GTUtility.translate("fluid.prepared_" + type.id + ".name"))
                .withColorRGBA(type.getMaterial().getRGBA())
                .withStateAndTemperature(FluidState.GAS, 1)
                .buildAndRegister()
                .asFluid();

            type.entangledFluid = GTFluidFactory.builder("entangled_" + type.id)
                .withTextures(new ResourceLocation("gregtech:fluids/condensate/fluid." + type.id + "_entangled"), null)
                .withLocalizedName(GTUtility.translate("fluid.entangled_" + type.id + ".name"))
                .withColorRGBA(type.getMaterial().getRGBA())
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

            type.generation.accept(generate);

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
}
