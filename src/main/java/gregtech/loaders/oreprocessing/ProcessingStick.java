package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.GTValues.M;
import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;

public class ProcessingStick implements PartRecipeGenerator {

    public static final ProcessingStick INSTANCE = new ProcessingStick();

    private ProcessingStick() {}

    /// Adds recipes that mold `stack`, `material`'s `prefix` part, from fluid and melt it back, for a material
    /// [ProcessingShaping] does not shape from ingots. The fluid is the material's molten fluid, else the liquid or
    /// gas its dust converts to, at the part's share of what a whole ingot or dust holds. The melting recipe is
    /// skipped when [GTRecipeRegistrator] recycling already melts the material. Both are skipped for a part another
    /// mod's item stands in for.
    static void addMeltMoldRecipe(OrePrefixes prefix, Material material, ItemStack stack, ItemList mold, int duration) {
        if (ProcessingShaping.shapesFromIngots(material) || !MaterialParts.isMinted(prefix, material)) return;
        ItemStack part = GTUtility.copyAmount(1, stack);
        FluidStack fluid;
        if (MaterialUtils.hasMolten(material)) {
            fluid = MaterialUtils.molten(material, INGOTS * prefix.getMaterialAmount() / M);
        } else {
            fluid = ProcessingDust.dustFluid(material);
            if (fluid == null) return;
            fluid.amount = (int) (fluid.amount * prefix.getMaterialAmount() / M);
        }
        GTValues.RA.stdBuilder()
            .itemInputs(mold.get(0L))
            .itemOutputs(part)
            .fluidInputs(fluid)
            .duration(duration)
            .eut(calculateRecipeEU(material, 16))
            .requiresEnvironment(material.getProperty(GTMaterialProperties.MOLTEN_PROCESSING_ENVIRONMENTS))
            .addTo(fluidSolidifierRecipes);
        if (GTRecipeRegistrator.hasReverseFluidSmeltingRecipe(material)) return;
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, part))
            .fluidOutputs(fluid.copy())
            .duration((int) Math.max(1, 24 * prefix.getMaterialAmount() / M))
            .eut(calculateRecipeEU(material, 16))
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .requiresEnvironment(material.getProperty(GTMaterialProperties.MOLTEN_PROCESSING_ENVIRONMENTS))
            .addTo(fluidExtractionRecipes);
    }

    @Override
    public void generate(OrePrefixes prefix, Material material, Shape shape, ItemStack stack) {
        addMeltMoldRecipe(OrePrefixes.stick, material, stack, ItemList.Shape_Mold_Rod, 7 * SECONDS + 10 * TICKS);
        {
            if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV && material.hasShape(Shapes.springSmall)) {
                GTModHandler.addCraftingRecipe(
                    MaterialLibAPI.getStack(material, Shapes.springSmall, 1),
                    GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
                    new Object[] { " s ", "fPx", 'P', OrePrefixes.stick.ingredient(material) });
            }
        }
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {

            Shape feedstock = MaterialUtils.hasFlag(material, GTMaterialFlag.CRYSTAL) ? Shapes.gem : Shapes.ingot;
            Material dustTarget = MaterialUtils.macerateInto(material);
            if (material.hasShape(feedstock) && dustTarget.hasShape(Shapes.dustSmall)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.getStack(material, feedstock, 1))
                    .itemOutputs(
                        MaterialLibAPI.getStack(material, Shapes.stick, 1),
                        MaterialLibAPI.getStack(dustTarget, Shapes.dustSmall, 2))
                    .duration(((int) Math.max(MaterialUtils.mass(material) * 5L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 16))
                    .addTo(latheRecipes);
            }

            if (material.hasShape(Shapes.bolt)) {

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(MaterialLibAPI.getStack(material, Shapes.bolt, 4))
                    .fluidInputs(
                        GTUtility.getWater(
                            Math.max(
                                4,
                                Math.min(
                                    1000,
                                    2 * ((int) Math.max(MaterialUtils.mass(material) * 2L, 1L))
                                        * calculateRecipeEU(material, 4)
                                        / 320))))
                    .duration(2 * ((int) Math.max(MaterialUtils.mass(material) * 2L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(MaterialLibAPI.getStack(material, Shapes.bolt, 4))
                    .fluidInputs(
                        GTModHandler.getDistilledWater(
                            Math.max(
                                3,
                                Math.min(
                                    750,
                                    2 * ((int) Math.max(MaterialUtils.mass(material) * 2L, 1L))
                                        * calculateRecipeEU(material, 4)
                                        / 426))))
                    .duration(2 * ((int) Math.max(MaterialUtils.mass(material) * 2L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(MaterialLibAPI.getStack(material, Shapes.bolt, 4))
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(
                            Materials.Lubricant,
                            FluidShapes.fluidLiquid,
                            (int) (Math.max(
                                1,
                                Math.min(
                                    250,
                                    ((int) Math.max(MaterialUtils.mass(material) * 2, 1))
                                        * calculateRecipeEU(material, 4)
                                        / 1280)))))
                    .duration(((int) Math.max(MaterialUtils.mass(material) * 2L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(MaterialLibAPI.getStack(material, Shapes.bolt, 4))
                    .fluidInputs(
                        MaterialUtils.fluid(
                            Materials.DimensionallyShiftedSuperfluid,
                            Math.max(
                                1,
                                Math.min(
                                    10,
                                    ((int) Math.max(MaterialUtils.mass(material) * 2L, 1L))
                                        * calculateRecipeEU(material, 4)
                                        / 4000))))
                    .duration(((int) Math.max(MaterialUtils.mass(material) * 2L / 2.5, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(cutterRecipes);
            }

            if (MaterialUtils.unifiable(material)) {
                if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        MaterialLibAPI.getStack(material, Shapes.stick, 2),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "s", "X", 'X', OrePrefixes.stickLong.ingredient(material) });
                    GTModHandler.addCraftingRecipe(
                        MaterialLibAPI.getStack(material, Shapes.stick, 1),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "f ", " X", 'X', OrePrefixes.ingot.ingredient(material) });
                }
            }
        }
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMASHING)) {
            // bender recipe
            {
                if (material.hasShape(Shapes.springSmall)) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(1)
                        .itemOutputs(MaterialLibAPI.getStack(material, Shapes.springSmall, 2))
                        .duration(5 * SECONDS)
                        .eut(calculateRecipeEU(material, 8))
                        .addTo(benderRecipes);
                }
            }

            if (material.hasShape(Shapes.stickLong)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(2, stack))
                    .itemOutputs(MaterialLibAPI.getStack(material, Shapes.stickLong, 1))
                    .duration(Math.max(MaterialUtils.mass(material), 1L))
                    .eut(calculateRecipeEU(material, 16))
                    .addTo(hammerRecipes);
            }
        }
    }
}
