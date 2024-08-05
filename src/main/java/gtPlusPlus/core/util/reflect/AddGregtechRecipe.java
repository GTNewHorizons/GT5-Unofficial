package gtPlusPlus.core.util.reflect;

import static gtPlusPlus.api.recipe.GTPPRecipeMaps.cokeOvenRecipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public final class AddGregtechRecipe {

    public static boolean importPyroRecipe(GT_Recipe aRecipe) {

        int aModifiedTime = (int) (aRecipe.mDuration * 0.8);

        if (aRecipe.mInputs == null || aRecipe.mFluidInputs == null
            || aRecipe.mFluidOutputs == null
            || aRecipe.mOutputs == null) {
            return false;
        }
        if (aRecipe.mInputs.length > 2 || aRecipe.mFluidInputs.length > 1
            || aRecipe.mFluidOutputs.length > 1
            || aRecipe.mOutputs.length > 9) {
            return false;
        } else if (aRecipe.mInputs.length <= 0) {
            return false;
        }

        int aCircuitNumber = -1;
        Item aCircuit = GT_Utility.getIntegratedCircuit(1)
            .getItem();
        boolean hasCircuit = false;

        for (ItemStack a : aRecipe.mInputs) {
            if (a != null && a.getItem() == aCircuit) {
                hasCircuit = true;
                aCircuitNumber = a.getItemDamage();
                break;
            }
        }

        ItemStack aInputItem = null;
        if (!hasCircuit || aCircuitNumber < 1) {
            return false;
        }

        for (ItemStack a : aRecipe.mInputs) {
            if (a != null && a.getItem() != aCircuit) {
                aInputItem = a;
                break;
            }
        }
        ItemStack[] inputs;
        if (aInputItem == null) {
            inputs = new ItemStack[] { GT_Utility.getIntegratedCircuit(aCircuitNumber) };
        } else {
            inputs = new ItemStack[] { GT_Utility.getIntegratedCircuit(aCircuitNumber), aInputItem };
        }

        GT_Values.RA.stdBuilder()
            .itemInputs(inputs)
            .itemOutputs(aRecipe.mOutputs)
            .fluidInputs(aRecipe.mFluidInputs)
            .fluidOutputs(aRecipe.mFluidOutputs)
            .eut(aRecipe.mEUt)
            .duration(aModifiedTime)
            .addTo(cokeOvenRecipes);

        return true;
    }
}
