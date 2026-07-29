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
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingStickLong implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingStickLong INSTANCE;

    public ProcessingStickLong() {
        INSTANCE = this;
        OrePrefixes.stickLong.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        {
            Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
            if ((processingTierEU == null ? 0 : processingTierEU) < TierEU.IV) {
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.spring, material, 1L),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { " s ", "fSx", " S ", 'S',
                        MaterialParts.craftIngredient(OrePrefixes.stickLong, material) });
            }
        }
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_WORKING)) {

            if (GTOreDictUnificator.get(OrePrefixes.stick, material, 1L) != null) {

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, material, 2L))
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
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, material, 2L))
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
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, material, 2L))
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.Lubricant,
                            Materials2FluidShapes.fluidLiquid,
                            (int) (Math.max(
                                1,
                                Math.min(
                                    250,
                                    ((int) Math.max(MaterialUtils.mass(material), 1)) * calculateRecipeEU(material, 4)
                                        / 1280)))))
                    .duration(((int) Math.max(MaterialUtils.mass(material), 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, material, 2L))
                    .fluidInputs(
                        MaterialUtils.fluid(
                            Materials2Materials.dimensionallyshiftedsuperfluid,
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

            if (!Boolean.FALSE.equals(material.getProperty(GTMaterialProperties.UNIFIABLE))) {
                Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
                if ((processingTierEU == null ? 0 : processingTierEU) < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "sf", "G ", 'G',
                            MaterialParts.craftIngredient(OrePrefixes.gemFlawless, material) });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.stickLong, material, 2L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "sf", "G ", 'G',
                            MaterialParts.craftIngredient(OrePrefixes.gemExquisite, material) });
                }
            }
        }
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMASHING)) {
            // Bender recipes
            {
                if (GTOreDictUnificator.get(OrePrefixes.spring, material, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(1)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.spring, material, 1L))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(material, 16))
                        .addTo(benderRecipes);
                }
            }

            if (!Boolean.FALSE.equals(material.getProperty(GTMaterialProperties.UNIFIABLE))) {
                Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
                if ((processingTierEU == null ? 0 : processingTierEU) < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "ShS", 'S', MaterialParts.craftIngredient(OrePrefixes.stick, material) });
                }
            }
        }
    }
}
