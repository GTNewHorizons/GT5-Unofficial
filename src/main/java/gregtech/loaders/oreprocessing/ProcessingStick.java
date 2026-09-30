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

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;

public class ProcessingStick implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingStick INSTANCE;

    public ProcessingStick() {
        INSTANCE = this;
        OrePrefixes.stick.add(this);
    }

    /// Molds `stack`, `material`'s `prefix` part, when [ProcessingShaping] does not shape that material from
    /// ingots: from its molten fluid, or else from the liquid or gas its dust converts to, taking the part's share
    /// of the fluid a whole ingot or dust holds, and melts the part back into the same fluid unless
    /// [GTRecipeRegistrator] recycling already melts it. Skipped for any stack other than the material's own part,
    /// and for a part another mod's item stands in for, whose material amount need not match its own recipes.
    static void addMeltMoldRecipe(OrePrefixes prefix, Material material, ItemStack stack, ItemList mold, int duration) {
        if (ProcessingShaping.shapesFromIngots(material) || !MaterialParts.isMinted(prefix, material)) return;
        ItemStack part = MaterialParts.stack(prefix, material, 1L);
        if (part == null || !GTUtility.areStacksEqual(part, stack)) return;
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
            .addTo(fluidSolidifierRecipes);
        if (GTRecipeRegistrator.hasReverseFluidSmeltingRecipe(material)) return;
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, part))
            .fluidOutputs(fluid.copy())
            .duration((int) Math.max(1, 24 * prefix.getMaterialAmount() / M))
            .eut(calculateRecipeEU(material, 16))
            .recipeCategory(RecipeCategories.fluidExtractorRecycling)
            .addTo(fluidExtractionRecipes);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        addMeltMoldRecipe(OrePrefixes.stick, material, stack, ItemList.Shape_Mold_Rod, 7 * SECONDS + 10 * TICKS);
        {
            if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                GTModHandler.addCraftingRecipe(
                    MaterialParts.stack(OrePrefixes.springSmall, material, 1L),
                    GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
                    new Object[] { " s ", "fPx", 'P', MaterialParts.craftIngredient(OrePrefixes.stick, material) });
            }
        }
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {

            if ((MaterialUtils.hasFlag(material, GTMaterialFlag.CRYSTAL)
                ? MaterialParts.stack(OrePrefixes.gem, material, 1L)
                : MaterialParts.stack(OrePrefixes.ingot, material, 1L)) != null
                && MaterialParts.stack(OrePrefixes.dustSmall, MaterialUtils.macerateInto(material), 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        MaterialUtils.hasFlag(material, GTMaterialFlag.CRYSTAL)
                            ? MaterialParts.stack(OrePrefixes.gem, material, 1L)
                            : MaterialParts.stack(OrePrefixes.ingot, material, 1L))
                    .itemOutputs(
                        MaterialParts.stack(OrePrefixes.stick, material, 1L),
                        MaterialParts.stack(OrePrefixes.dustSmall, MaterialUtils.macerateInto(material), 2L))
                    .duration(((int) Math.max(MaterialUtils.mass(material) * 5L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 16))
                    .addTo(latheRecipes);
            }

            if (MaterialParts.stack(OrePrefixes.bolt, material, 1L) != null) {

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(MaterialParts.stack(OrePrefixes.bolt, material, 4L))
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
                    .itemOutputs(MaterialParts.stack(OrePrefixes.bolt, material, 4L))
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
                    .itemOutputs(MaterialParts.stack(OrePrefixes.bolt, material, 4L))
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
                    .itemOutputs(MaterialParts.stack(OrePrefixes.bolt, material, 4L))
                    .fluidInputs(
                        MaterialUtils.fluid(
                            Materials.dimensionallyshiftedsuperfluid,
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
                        MaterialParts.stack(OrePrefixes.stick, material, 2L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "s", "X", 'X', MaterialParts.craftIngredient(OrePrefixes.stickLong, material) });
                    GTModHandler.addCraftingRecipe(
                        MaterialParts.stack(OrePrefixes.stick, material, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "f ", " X", 'X', MaterialParts.craftIngredient(OrePrefixes.ingot, material) });
                }
            }
        }
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMASHING)) {
            // bender recipe
            {
                if (MaterialParts.stack(OrePrefixes.springSmall, material, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(1)
                        .itemOutputs(MaterialParts.stack(OrePrefixes.springSmall, material, 2L))
                        .duration(5 * SECONDS)
                        .eut(calculateRecipeEU(material, 8))
                        .addTo(benderRecipes);
                }
            }

            if (MaterialParts.stack(OrePrefixes.stickLong, material, 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(2, stack))
                    .itemOutputs(MaterialParts.stack(OrePrefixes.stickLong, material, 1L))
                    .duration(Math.max(MaterialUtils.mass(material), 1L))
                    .eut(calculateRecipeEU(material, 16))
                    .addTo(hammerRecipes);
            }
        }
    }
}
