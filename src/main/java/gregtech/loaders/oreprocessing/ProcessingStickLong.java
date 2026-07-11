package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
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
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
            GTModHandler.addCraftingRecipe(
                GTOreDictUnificator.get(OrePrefixes.spring, aMaterial, 1L),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { " s ", "fSx", " S ", 'S', OrePrefixes.stickLong.get(aMaterial) });
        }
        if (!MU.hasFlag(aMaterial, GTMaterialFlag.NO_WORKING)) {

            if (GTOreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L) != null) {

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, aMaterial, 2L))
                    .fluidInputs(
                        Materials.Water.getFluid(
                            Math.max(
                                4,
                                Math.min(
                                    1000,
                                    ((int) Math.max(aMaterial.getMass(), 1L)) * calculateRecipeEU(aMaterial, 4)
                                        / 320))))
                    .duration(2 * ((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, aMaterial, 2L))
                    .fluidInputs(
                        GTModHandler.getDistilledWater(
                            Math.max(
                                3,
                                Math.min(
                                    750,
                                    ((int) Math.max(aMaterial.getMass(), 1L)) * calculateRecipeEU(aMaterial, 4)
                                        / 426))))
                    .duration(2 * ((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, aMaterial, 2L))
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.Lubricant,
                            Materials2FluidShapes.shapeFluidLiquid,
                            (int) (Math.max(
                                1,
                                Math.min(
                                    250,
                                    ((int) Math.max(aMaterial.getMass(), 1)) * calculateRecipeEU(aMaterial, 4)
                                        / 1280)))))
                    .duration(((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, aMaterial, 2L))
                    .fluidInputs(
                        Materials.DimensionallyShiftedSuperfluid.getFluid(
                            Math.max(
                                1,
                                Math.min(
                                    10,
                                    ((int) Math.max(aMaterial.getMass(), 1L)) * calculateRecipeEU(aMaterial, 4)
                                        / 4000))))
                    .duration((int) ((Math.max(aMaterial.getMass() / 2.5, 1L)) * TICKS))
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(cutterRecipes);
            }

            if (aMaterial.mUnifiable && (aMaterial.mMaterialInto == aMaterial)) {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "sf", "G ", 'G', OrePrefixes.gemFlawless.get(aMaterial) });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 2L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "sf", "G ", 'G', OrePrefixes.gemExquisite.get(aMaterial) });
                }
            }
        }
        if (!MU.hasFlag(aMaterial, GTMaterialFlag.NO_SMASHING)) {
            // Bender recipes
            {
                if (GTOreDictUnificator.get(OrePrefixes.spring, aMaterial, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .circuit(1)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.spring, aMaterial, 1L))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, 16))
                        .addTo(benderRecipes);
                }
            }

            if (aMaterial.mUnifiable && (aMaterial.mMaterialInto == aMaterial))
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "ShS", 'S', OrePrefixes.stick.get(aMaterial) });
                }
        }
    }
}
