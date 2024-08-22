package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.util.RecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.OreDictUnificator;
import gregtech.api.util.RecipeBuilder;

public class ProcessingRecycling implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingRecycling() {
        for (OrePrefixes tPrefix : OrePrefixes.values())
            if ((tPrefix.mIsMaterialBased) && (tPrefix.mMaterialAmount > 0L) && (tPrefix.mIsContainer))
                tPrefix.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if ((aMaterial != Materials.Empty) && (GT_Utility.getFluidForFilledItem(aStack, true) == null)
            && !aMaterial.contains(SubTag.SMELTING_TO_FLUID)
            && (OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L) != null)) {
            RecipeBuilder recipeBuilder = GT_Values.RA.stdBuilder();
            recipeBuilder.itemInputs(aStack);
            if (GT_Utility.getContainerItem(aStack, true) == null) {
                recipeBuilder.itemOutputs(
                    OreDictUnificator.get(OrePrefixes.dust, aMaterial, aPrefix.mMaterialAmount / 3628800L));
            } else {
                recipeBuilder.itemOutputs(
                    GT_Utility.getContainerItem(aStack, true),
                    OreDictUnificator.get(OrePrefixes.dust, aMaterial, aPrefix.mMaterialAmount / 3628800L));
            }
            recipeBuilder.duration(((int) Math.max(aMaterial.getMass() / 2L, 1L)) * TICKS)
                .eut(2)
                .addTo(cannerRecipes);
        }
    }
}
