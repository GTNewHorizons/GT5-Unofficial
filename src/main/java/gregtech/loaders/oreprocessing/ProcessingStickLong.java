package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;
import static gregtech.api.util.RecipeBuilder.SECONDS;
import static gregtech.api.util.RecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.OreDictUnificator;
import gregtech.common.GT_Proxy;

public class ProcessingStickLong implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingStickLong() {
        OrePrefixes.stickLong.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
            GT_ModHandler.addCraftingRecipe(
                OreDictUnificator.get(OrePrefixes.spring, aMaterial, 1L),
                GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { " s ", "fSx", " S ", 'S', OrePrefixes.stickLong.get(aMaterial) });
        }
        if (!aMaterial.contains(SubTag.NO_WORKING)) {

            if (OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L) != null) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(OreDictUnificator.get(OrePrefixes.stick, aMaterial, 2L))
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

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(OreDictUnificator.get(OrePrefixes.stick, aMaterial, 2L))
                    .fluidInputs(
                        GT_ModHandler.getDistilledWater(
                            Math.max(
                                3,
                                Math.min(
                                    750,
                                    ((int) Math.max(aMaterial.getMass(), 1L)) * calculateRecipeEU(aMaterial, 4)
                                        / 426))))
                    .duration(2 * ((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(cutterRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(OreDictUnificator.get(OrePrefixes.stick, aMaterial, 2L))
                    .fluidInputs(
                        Materials.Lubricant.getFluid(
                            Math.max(
                                1,
                                Math.min(
                                    250,
                                    ((int) Math.max(aMaterial.getMass(), 1L)) * calculateRecipeEU(aMaterial, 4)
                                        / 1280))))
                    .duration(((int) Math.max(aMaterial.getMass(), 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(cutterRecipes);
            }

            if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GT_ModHandler.addCraftingRecipe(
                        OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "sf", "G ", 'G', OrePrefixes.gemFlawless.get(aMaterial) });
                    GT_ModHandler.addCraftingRecipe(
                        OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 2L),
                        GT_Proxy.tBits,
                        new Object[] { "sf", "G ", 'G', OrePrefixes.gemExquisite.get(aMaterial) });
                }
            }
        }
        if (!aMaterial.contains(SubTag.NO_SMASHING)) {
            // Bender recipes
            {
                if (OreDictUnificator.get(OrePrefixes.spring, aMaterial, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(1))
                        .itemOutputs(OreDictUnificator.get(OrePrefixes.spring, aMaterial, 1L))
                        .duration(10 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, 16))
                        .addTo(benderRecipes);
                }
            }

            if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial))
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GT_ModHandler.addCraftingRecipe(
                        OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "ShS", 'S', OrePrefixes.stick.get(aMaterial) });
                }
        }
    }
}
