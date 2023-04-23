package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sBenderRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
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
                GT_OreDictUnificator.get(OrePrefixes.spring, aMaterial, 1L),
                GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { " s ", "fSx", " S ", 'S', OrePrefixes.stickLong.get(aMaterial) });
        }
        if (!aMaterial.contains(SubTag.NO_WORKING)) {
            GT_Values.RA.addCutterRecipe(
                GT_Utility.copyAmount(1L, aStack),
                GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 2L),
                null,
                (int) Math.max(aMaterial.getMass(), 1L),
                calculateRecipeEU(aMaterial, 4));
            if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "sf", "G ", 'G', OrePrefixes.gemFlawless.get(aMaterial) });
                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 2L),
                        GT_Proxy.tBits,
                        new Object[] { "sf", "G ", 'G', OrePrefixes.gemExquisite.get(aMaterial) });
                }
            }
        }
        if (!aMaterial.contains(SubTag.NO_SMASHING)) {
            // Bender recipes
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1L, aStack), GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.spring, aMaterial, 1L))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(10 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 16))
                    .addTo(sBenderRecipes);
            }

            if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial))
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "ShS", 'S', OrePrefixes.stick.get(aMaterial) });
                }
        }
    }
}
