package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class ProcessingStickLong implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingStickLong INSTANCE;

    public ProcessingStickLong() {
        INSTANCE = this;
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        ProcessingStick
            .addMeltMoldRecipe(OrePrefixes.stickLong, material, stack, ItemList.Shape_Mold_Rod_Long, 15 * SECONDS);
        if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
            GTModHandler.addCraftingRecipe(
                MaterialLibAPI.findStack(material, Shapes.spring, 1),
                GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
                new Object[] { " s ", "fSx", " S ", 'S', OrePrefixes.stickLong.ingredient(material) });
        }
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {

            if (MaterialLibAPI.findStack(material, Shapes.stick, 1) != null) {

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(MaterialLibAPI.findStack(material, Shapes.stick, 2))
                    .fluidInputs(
                        GTUtility.getWater(
                            Math.max(
                                4,
                                Math.min(
                                    1000,
                                    ((int) Math.max(MaterialUtils.mass(material), 1L)) * calculateRecipeEU(material, 4)
                                        / 320))))
                    .duration(2 * ((int) Math.max(MaterialUtils.mass(material), 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(MaterialLibAPI.findStack(material, Shapes.stick, 2))
                    .fluidInputs(
                        GTModHandler.getDistilledWater(
                            Math.max(
                                3,
                                Math.min(
                                    750,
                                    ((int) Math.max(MaterialUtils.mass(material), 1L)) * calculateRecipeEU(material, 4)
                                        / 426))))
                    .duration(2 * ((int) Math.max(MaterialUtils.mass(material), 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(MaterialLibAPI.findStack(material, Shapes.stick, 2))
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(
                            Materials.Lubricant,
                            FluidShapes.fluidLiquid,
                            (int) (Math.max(
                                1,
                                Math.min(
                                    250,
                                    ((int) Math.max(MaterialUtils.mass(material), 1L)) * calculateRecipeEU(material, 4)
                                        / 1280)))))
                    .duration(((int) Math.max(MaterialUtils.mass(material), 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(MaterialLibAPI.findStack(material, Shapes.stick, 2))
                    .fluidInputs(
                        MaterialUtils.fluid(
                            Materials.dimensionallyshiftedsuperfluid,
                            Math.max(
                                1,
                                Math.min(
                                    10,
                                    ((int) Math.max(MaterialUtils.mass(material), 1L)) * calculateRecipeEU(material, 4)
                                        / 4000))))
                    .duration((int) ((Math.max(MaterialUtils.mass(material) / 2.5, 1L)) * TICKS))
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(cutterRecipes);
            }

            if (MaterialUtils.unifiable(material)) {
                if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        MaterialLibAPI.findStack(material, Shapes.stickLong, 1),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "sf", "G ", 'G', OrePrefixes.gemFlawless.ingredient(material) });
                    GTModHandler.addCraftingRecipe(
                        MaterialLibAPI.findStack(material, Shapes.stickLong, 2),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "sf", "G ", 'G', OrePrefixes.gemExquisite.ingredient(material) });
                }
            }
        }
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMASHING)) {
            // Bender recipes
            {
                if (MaterialLibAPI.findStack(material, Shapes.spring, 1) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(1)
                        .itemOutputs(MaterialLibAPI.findStack(material, Shapes.spring, 1))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(material, 16))
                        .addTo(benderRecipes);
                }
            }

            if (MaterialUtils.unifiable(material)) {
                if (MaterialUtils.processingMaterialTierEU(material) < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        MaterialLibAPI.findStack(material, Shapes.stickLong, 1),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "ShS", 'S', OrePrefixes.stick.ingredient(material) });
                }
            }
        }
    }
}
