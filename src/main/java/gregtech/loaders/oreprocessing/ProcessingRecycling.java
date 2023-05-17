package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sCannerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

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
            && (GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L) != null)) {
            GT_Values.RA.stdBuilder()
                .itemInputs(aStack)
                .itemOutputs(
                    GT_Utility.getContainerItem(aStack, true),
                    GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, aPrefix.mMaterialAmount / 3628800L))
                .noFluidInputs()
                .noFluidOutputs()
                .duration(((int) Math.max(aMaterial.getMass() / 2L, 1L)) * TICKS)
                .eut(2)
                .addTo(sCannerRecipes);
        }
    }
}
