package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.cannerRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;

public class ProcessingRecycling implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingRecycling() {
        for (OrePrefixes tPrefix : OrePrefixes.values())
            if ((tPrefix.mIsMaterialBased) && (tPrefix.mMaterialAmount > 0L) && (tPrefix.mIsContainer))
                tPrefix.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if ((aMaterial != Materials.Empty) && (GTUtility.getFluidForFilledItem(aStack, true) == null)
            && !aMaterial.contains(SubTag.SMELTING_TO_FLUID)
            && (GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L) != null)) {
            GTRecipeBuilder recipeBuilder = GTValues.RA.stdBuilder();
            recipeBuilder.itemInputs(aStack);
            if (GTUtility.getContainerItem(aStack, true) == null) {
                recipeBuilder.itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, aPrefix.mMaterialAmount / 3628800L));
            } else {
                recipeBuilder.itemOutputs(
                    GTUtility.getContainerItem(aStack, true),
                    GTOreDictUnificator.get(OrePrefixes.dust, aMaterial, aPrefix.mMaterialAmount / 3628800L));
            }
            recipeBuilder.duration(((int) Math.max(aMaterial.getMass() / 2L, 1L)) * TICKS)
                .eut(2)
                .addTo(cannerRecipes);
        }
    }
}
