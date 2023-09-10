package gtPlusPlus.core.util.reflect;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;

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
        Item aCircuit = CI.getNumberedCircuit(1).getItem();
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

        return CORE.RA.addCokeOvenRecipe(
                aCircuitNumber,
                aInputItem,
                aRecipe.mFluidInputs,
                aRecipe.mFluidOutputs,
                aRecipe.mOutputs,
                aModifiedTime,
                aRecipe.mEUt);
    }

    @Deprecated
    public static boolean addCokeAndPyrolyseRecipes(ItemStack input1, int circuitNumber, FluidStack inputFluid1,
            ItemStack output1, FluidStack outputFluid1, int timeInSeconds, int euTick) {
        // Seconds Conversion
        int TIME = timeInSeconds * 20;
        int TIMEPYRO = TIME + (TIME / 5);
        // Even though it says coke and pyrolyse, ICO recipes are imported from pyrolyse by #importPyroRecipe
        GT_Values.RA.addPyrolyseRecipe(input1, inputFluid1, circuitNumber, output1, outputFluid1, TIMEPYRO, euTick);

        return false;
    }
}
