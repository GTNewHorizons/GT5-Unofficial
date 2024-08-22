package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;
import static gregtech.api.util.RecipeBuilder.SECONDS;
import static gregtech.api.util.RecipeBuilder.TICKS;

import gregtech.api.util.RecipeRegistrator;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.OreDictUnificator;

public class ProcessingNugget implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingNugget() {
        OrePrefixes.nugget.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {

        if (aMaterial.contains(SubTag.SMELTING_TO_GEM)
            && OreDictUnificator.get(OrePrefixes.gem, aMaterial.mSmeltInto, 1L) != null) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(9, aStack), ItemList.Shape_Mold_Ball.get(0L))
                .itemOutputs(OreDictUnificator.get(OrePrefixes.gem, aMaterial.mSmeltInto, 1L))
                .duration(10 * SECONDS)
                .eut(calculateRecipeEU(aMaterial, 2))
                .addTo(alloySmelterRecipes);
        }

        if ((!aMaterial.contains(SubTag.SMELTING_TO_GEM))
            && OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L) != null
            && aMaterial != Materials.Aluminium) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(9, aStack), ItemList.Shape_Mold_Ingot.get(0L))
                .itemOutputs(OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L))
                .duration(10 * SECONDS)
                .eut(calculateRecipeEU(aMaterial, 2))
                .recipeCategory(RecipeCategories.alloySmelterMolding)
                .addTo(alloySmelterRecipes);
        }

        if (aMaterial.mStandardMoltenFluid != null) {
            if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Nugget.get(0L))
                    .itemOutputs(OreDictUnificator.get(OrePrefixes.nugget, aMaterial, 1L))
                    .fluidInputs(aMaterial.getMolten(16L))
                    .duration(16 * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(fluidSolidifierRecipes);
            }
        }

        RecipeRegistrator.registerReverseFluidSmelting(aStack, aMaterial, aPrefix.mMaterialAmount, null);
        RecipeRegistrator
            .registerReverseMacerating(aStack, aMaterial, aPrefix.mMaterialAmount, null, null, null, false);
        if (!aMaterial.contains(SubTag.NO_SMELTING)
            && OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L) != null) {
            GT_Values.RA.stdBuilder()
                .itemInputs(OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L), ItemList.Shape_Mold_Nugget.get(0L))
                .itemOutputs(GT_Utility.copyAmount(9, aStack))
                .duration(5 * SECONDS)
                .eut(calculateRecipeEU(aMaterial, 1))
                .recipeCategory(RecipeCategories.alloySmelterMolding)
                .addTo(alloySmelterRecipes);
            if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                GT_ModHandler.addCraftingRecipe(
                    OreDictUnificator.get(OrePrefixes.nugget, aMaterial, 8L),
                    GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "sI ", 'I', OrePrefixes.ingot.get(aMaterial) });
            }
        }
    }
}
