package gtPlusPlus.core.util.reflect;

import static gtPlusPlus.api.recipe.GTPPRecipeMaps.cokeOvenRecipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;

public final class AddGregtechRecipe {

    public static boolean importPyroRecipe(GTRecipe aRecipe) {

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
        Item aCircuit = GTUtility.getIntegratedCircuit(1)
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
            inputs = new ItemStack[] { GTUtility.getIntegratedCircuit(aCircuitNumber) };
        } else {
            inputs = new ItemStack[] { GTUtility.getIntegratedCircuit(aCircuitNumber), aInputItem };
        }

        GTValues.RA.stdBuilder()
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
