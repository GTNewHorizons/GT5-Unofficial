package gregtech.api.recipe.maps;

import static gregtech.api.recipe.check.FindRecipeResult.NOT_FOUND;

import java.util.Collection;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

/**
 * Special Class for Forming Press handling.
 */
public class FormingPressRecipeMap extends GT_Recipe.GT_Recipe_Map {

    public FormingPressRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
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
            aNotUnificated,
            aDontCheckStackSizes,
            aVoltage,
            aFluids,
            aSpecialSlot,
            aInputs);
        if (aInputs == null || aInputs.length < 2 || !GregTech_API.sPostloadFinished) return result;
        if (!result.isSuccessful()) {
            return findRenamingRecipe(aInputs);
        }
        for (ItemStack aMold : aInputs) {
            if (ItemList.Shape_Mold_Credit.isStackEqual(aMold, false, true)) {
                NBTTagCompound tNBT = aMold.getTagCompound();
                if (tNBT == null) tNBT = new NBTTagCompound();
                if (!tNBT.hasKey("credit_security_id")) tNBT.setLong("credit_security_id", System.nanoTime());
                aMold.setTagCompound(tNBT);

                GT_Recipe rRecipe = result.getRecipeNonNull();
                rRecipe = rRecipe.copy();
                rRecipe.mCanBeBuffered = false;
                rRecipe.mOutputs[0].setTagCompound(tNBT);
                return FindRecipeResult.ofSuccess(rRecipe);
            }
        }
        return result;
    }

    private ItemStack findNameMoldIndex(ItemStack[] inputs) {
        for (ItemStack stack : inputs) {
            if (ItemList.Shape_Mold_Name.isStackEqual(stack, false, true)) return stack;
        }
        return null;
    }

    private ItemStack findStackToRename(ItemStack[] inputs, ItemStack mold) {
        for (ItemStack stack : inputs) {
            if (stack == mold || stack == null) continue;
            return stack;
        }
        return null;
    }

    @Nonnull
    private FindRecipeResult findRenamingRecipe(ItemStack[] inputs) {
        ItemStack mold = findNameMoldIndex(inputs);
        if (mold == null) return NOT_FOUND;
        ItemStack input = findStackToRename(inputs, mold);
        if (input == null) return NOT_FOUND;
        ItemStack output = GT_Utility.copyAmount(1, input);
        if (output == null) return NOT_FOUND;
        output.setStackDisplayName(mold.getDisplayName());
        GT_Recipe recipe = new GT_Recipe(
            false,
            new ItemStack[] { GT_Utility.copyAmount(0, mold), GT_Utility.copyAmount(1, input) },
            new ItemStack[] { output },
            null,
            null,
            null,
            null,
            128,
            8,
            0);
        recipe.mCanBeBuffered = false;
        recipe.isNBTSensitive = true;
        return FindRecipeResult.ofSuccess(recipe);
    }
}
