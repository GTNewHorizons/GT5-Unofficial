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

public class ProcessingStick implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingStick() {
        OrePrefixes.stick.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
            GT_ModHandler.addCraftingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.springSmall, aMaterial, 1L),
                GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { " s ", "fPx", 'P', OrePrefixes.stick.get(aMaterial) });
        }
        if (!aMaterial.contains(gregtech.api.enums.SubTag.NO_WORKING)) {
            GT_Values.RA.addLatheRecipe(
                aMaterial.contains(SubTag.CRYSTAL) ? GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L)
                    : GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial.mMacerateInto, 2L),
                (int) Math.max(aMaterial.getMass() * 5L, 1L),
                calculateRecipeEU(aMaterial, 16));
            GT_Values.RA.addCutterRecipe(
                GT_Utility.copyAmount(1L, aStack),
                GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 4L),
                null,
                (int) Math.max(aMaterial.getMass() * 2L, 1L),
                calculateRecipeEU(aMaterial, 4));
            if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial)) {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 2L),
                        GT_Proxy.tBits,
                        new Object[] { "s", "X", 'X', OrePrefixes.stickLong.get(aMaterial) });
                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "f ", " X", 'X', OrePrefixes.ingot.get(aMaterial) });
                }
            }
        }
        if (!aMaterial.contains(gregtech.api.enums.SubTag.NO_SMASHING)) {
            // bender recipe
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1L, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.springSmall, aMaterial, 2L))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(5 * SECONDS)
                    .eut(calculateRecipeEU(aMaterial, 8))
                    .addTo(sBenderRecipes);
            }

            GT_Values.RA.addForgeHammerRecipe(
                GT_Utility.copyAmount(2L, aStack),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L),
                (int) Math.max(aMaterial.getMass(), 1L),
                calculateRecipeEU(aMaterial, 16));
        }
        GT_Values.RA.addAssemblerRecipe(
            GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 1),
            GT_Utility.getIntegratedCircuit(2),
            Materials.SeedOil.getFluid(50L),
            ItemList.FR_Stick.get(1L),
            16,
            8);
    }
}
