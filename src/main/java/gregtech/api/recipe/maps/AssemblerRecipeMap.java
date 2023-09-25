package gregtech.api.recipe.maps;

import java.util.Collection;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_Recipe;

/**
 * Special Class for Assembler handling.
 */
public class AssemblerRecipeMap extends RecipeMap {

    public AssemblerRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
        String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
        int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
        String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
        super(
            aRecipeList,
            aUnlocalizedName,
            aLocalName,
            aNEIName,
            aNEIGUIPath,
            aUsualInputCount,
            aUsualOutputCount,
            aMinimalInputItems,
            aMinimalInputFluids,
            aAmperage,
            aNEISpecialValuePre,
            aNEISpecialValueMultiplier,
            aNEISpecialValuePost,
            aShowVoltageAmperageInNEI,
            aNEIAllowed);
    }

    @Nonnull
    @Override
    public FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, Predicate<GT_Recipe> aIsValidRecipe,
        boolean aNotUnificated, boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids,
        ItemStack aSpecialSlot, ItemStack... aInputs) {

        FindRecipeResult result = super.findRecipeWithResult(
            aRecipe,
            aIsValidRecipe,
            true,
            aDontCheckStackSizes,
            aVoltage,
            aFluids,
            aSpecialSlot,
            aInputs);
        /*
         * Doesnt work, keep it as a reminder tho if (rRecipe == null){ Set<ItemStack> aInputs2 = new
         * TreeSet<ItemStack>(); for (ItemStack aInput : aInputs) { aInputs2.add(aInput); } for (ItemStack aInput :
         * aInputs) { aInputs2.remove(aInput); int[] oredictIDs = OreDictionary.getOreIDs(aInput); if (
         * oredictIDs.length > 1){ for (final int i : oredictIDs){ final ItemStack[] oredictIS = (ItemStack[])
         * OreDictionary.getOres(OreDictionary.getOreName(i)).toArray(); if (oredictIS != null && oredictIS.length >
         * 1){ for (final ItemStack IS : oredictIS){ aInputs2.add(IS); ItemStack[] temp = (ItemStack[])
         * aInputs2.toArray(); rRecipe = super.findRecipe(aTileEntity, aRecipe, aNotUnificated, aVoltage, aFluids,
         * aSpecialSlot,temp); if(rRecipe!= null){ break; } else { aInputs2.remove(IS); } } if(rRecipe!= null)
         * break; } } if(rRecipe!= null) break; }else aInputs2.add(aInput); if(rRecipe!= null) break; } }
         */
        if (aInputs == null || aInputs.length == 0
            || aInputs[0] == null
            || !result.isSuccessful()
            || !GregTech_API.sPostloadFinished) return result;

        GT_Recipe rRecipe = result.getRecipeNonNull();
        for (ItemStack aInput : aInputs) {
            if (ItemList.Paper_Printed_Pages.isStackEqual(aInput, false, true)) {
                rRecipe = rRecipe.copy();
                rRecipe.mCanBeBuffered = false;
                rRecipe.mOutputs[0].setTagCompound(aInput.getTagCompound());
            }
        }
        return FindRecipeResult.ofSuccess(rRecipe);
    }
}
