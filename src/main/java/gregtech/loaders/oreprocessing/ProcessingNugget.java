package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;

public class ProcessingNugget implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingNugget() {
        OrePrefixes.nugget.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {

        if (aMaterial.contains(SubTag.SMELTING_TO_GEM)
            && GTOreDictUnificator.get(OrePrefixes.gem, aMaterial.mSmeltInto, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(9, aStack), ItemList.Shape_Mold_Ball.get(0L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, aMaterial.mSmeltInto, 1L))
                .duration(10 * SECONDS)
                .eut(calculateRecipeEU(aMaterial, 2))
                .addTo(alloySmelterRecipes);
        }

        if ((!aMaterial.contains(SubTag.SMELTING_TO_GEM))
            && GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L) != null
            && aMaterial != Materials.Aluminium) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(9, aStack), ItemList.Shape_Mold_Ingot.get(0L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L))
                .duration(10 * SECONDS)
                .eut(calculateRecipeEU(aMaterial, 2))
                .recipeCategory(RecipeCategories.alloySmelterMolding)
                .addTo(alloySmelterRecipes);
        }

        if (aMaterial.mStandardMoltenFluid != null) {
            if (!(aMaterial == Materials.AnnealedCopper || aMaterial == Materials.WroughtIron)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Nugget.get(0L))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nugget, aMaterial, 1L))
                    .fluidInputs(aMaterial.getMolten(16L))
                    .duration(16 * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(fluidSolidifierRecipes);
            }
        }

        GTRecipeRegistrator.registerReverseFluidSmelting(aStack, aMaterial, aPrefix.mMaterialAmount, null);
        GTRecipeRegistrator
            .registerReverseMacerating(aStack, aMaterial, aPrefix.mMaterialAmount, null, null, null, false);
        if (!aMaterial.contains(SubTag.NO_SMELTING)
            && GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L),
                    ItemList.Shape_Mold_Nugget.get(0L))
                .itemOutputs(GTUtility.copyAmount(9, aStack))
                .duration(5 * SECONDS)
                .eut(calculateRecipeEU(aMaterial, 1))
                .recipeCategory(RecipeCategories.alloySmelterMolding)
                .addTo(alloySmelterRecipes);
            if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.nugget, aMaterial, 8L),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "sI ", 'I', OrePrefixes.ingot.get(aMaterial) });
            }
        }
    }
}
