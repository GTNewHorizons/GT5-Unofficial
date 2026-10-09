package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.polarizerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTUtility;

/// Chemical-bath and polarizer recipes for one shape, turning a material's part into the same shape of its treated
/// or magnetic counterpart.
public class ProcessingTransforming implements PartRecipeGenerator {

    private final Shape shape;

    public ProcessingTransforming(Shape shape) {
        this.shape = shape;
    }

    @Override
    public void generate(OrePrefixes prefix, Material material, String oreDictName, ItemStack stack) {
        switch (MaterialUtils.internalName(material)) {
            case "Wood" ->
            // Chemical bath recipes
            {
                if (Materials.WoodSealed.hasShape(shape)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(MaterialLibAPI.getStack(Materials.WoodSealed, shape, 1))
                        .fluidInputs(
                            MaterialUtils.fluid(
                                Materials.SeedOil,
                                GTUtility.translateMaterialToAmount(prefix.getMaterialAmount(), 120L, true)))
                        .duration(5 * SECONDS)
                        .eut(TierEU.RECIPE_ULV)
                        .addTo(chemicalBathRecipes);
                }

            }
            case "Iron" -> {
                // Chemical bath recipes
                {
                    if (Materials.FierySteel.hasShape(shape)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(MaterialLibAPI.getStack(Materials.FierySteel, shape, 1))
                            .fluidInputs(
                                MaterialLibAPI.getFluidStack(
                                    Materials.FierySteel,
                                    FluidShapes.fluidLiquid,
                                    (int) (GTUtility.translateMaterialToAmount(prefix.getMaterialAmount(), 250, true))))
                            .duration(5 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(chemicalBathRecipes);
                    }
                }

                // Polarizer recipes
                {
                    if (Materials.IronMagnetic.hasShape(shape)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(MaterialLibAPI.getStack(Materials.IronMagnetic, shape, 1))
                            .duration(((int) Math.max(16L, prefix.getMaterialAmount() * 128L / GTValues.M)) * TICKS)
                            .eut((int) TierEU.RECIPE_LV / 2)
                            .addTo(polarizerRecipes);
                    }
                }
            }
            case "CastIron" -> {
                // Chemical bath recipes
                {
                    if (Materials.FierySteel.hasShape(shape)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(MaterialLibAPI.getStack(Materials.FierySteel, shape, 1))
                            .fluidInputs(
                                MaterialLibAPI.getFluidStack(
                                    Materials.FierySteel,
                                    FluidShapes.fluidLiquid,
                                    (int) (GTUtility.translateMaterialToAmount(prefix.getMaterialAmount(), 225, true))))
                            .duration(5 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(chemicalBathRecipes);
                    }
                }

                // Polarizer recipes
                {
                    if (Materials.IronMagnetic.hasShape(shape)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(MaterialLibAPI.getStack(Materials.IronMagnetic, shape, 1))
                            .duration(((int) Math.max(16L, prefix.getMaterialAmount() * 128L / GTValues.M)) * TICKS)
                            .eut((int) TierEU.RECIPE_LV / 2)
                            .addTo(polarizerRecipes);
                    }
                }
            }
            case "Steel" -> {
                // Chemical Bath recipes
                {
                    if (Materials.FierySteel.hasShape(shape)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(MaterialLibAPI.getStack(Materials.FierySteel, shape, 1))
                            .fluidInputs(
                                MaterialLibAPI.getFluidStack(
                                    Materials.FierySteel,
                                    FluidShapes.fluidLiquid,
                                    (int) (GTUtility.translateMaterialToAmount(prefix.getMaterialAmount(), 200, true))))
                            .duration(5 * SECONDS)
                            .eut(TierEU.RECIPE_ULV)
                            .addTo(chemicalBathRecipes);
                    }
                }

                // Polarizer recipes
                {
                    if (Materials.SteelMagnetic.hasShape(shape)) {
                        GTValues.RA.stdBuilder()
                            .itemInputs(GTUtility.copyAmount(1, stack))
                            .itemOutputs(MaterialLibAPI.getStack(Materials.SteelMagnetic, shape, 1))
                            .duration(((int) Math.max(16L, prefix.getMaterialAmount() * 128L / GTValues.M)) * TICKS)
                            .eut((int) TierEU.RECIPE_LV / 2)
                            .addTo(polarizerRecipes);
                    }
                }
            }
            case "Neodymium" ->
            // Polarizer recipes
            {
                if (Materials.NeodymiumMagnetic.hasShape(shape)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(MaterialLibAPI.getStack(Materials.NeodymiumMagnetic, shape, 1))
                        .duration(((int) Math.max(16L, prefix.getMaterialAmount() * 128L / GTValues.M)) * TICKS)
                        .eut((int) TierEU.RECIPE_HV / 2)
                        .addTo(polarizerRecipes);
                }
            }
            case "Samarium" ->
            // Polarizer recipes
            {
                if (Materials.SamariumMagnetic.hasShape(shape)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(MaterialLibAPI.getStack(Materials.SamariumMagnetic, shape, 1))
                        .duration(((int) Math.max(16L, prefix.getMaterialAmount() * 128L / GTValues.M)) * TICKS)
                        .eut((int) TierEU.RECIPE_IV / 2)
                        .addTo(polarizerRecipes);
                }
            }

            case "TengamPurified" ->
            // Polarizer recipes
            {
                if (Materials.TengamAttuned.hasShape(shape)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(MaterialLibAPI.getStack(Materials.TengamAttuned, shape, 1))
                        .duration(((int) Math.max(16L, prefix.getMaterialAmount() * 128L / GTValues.M)) * TICKS)
                        .eut((int) TierEU.RECIPE_UHV)
                        .addTo(polarizerRecipes);
                }
            }

            default -> { /* NO-OP */ }
        }
    }
}
