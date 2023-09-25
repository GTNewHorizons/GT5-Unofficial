package gregtech.api.recipe.maps;

import static gregtech.api.recipe.check.FindRecipeResult.NOT_FOUND;

import java.util.Collection;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

/**
 * Special Class for Furnace Recipe handling.
 */
public class FurnaceRecipeMap extends NonGTRecipeMap {

    public FurnaceRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
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
        if (aInputs == null || aInputs.length == 0 || aInputs[0] == null) return NOT_FOUND;
        if (aRecipe != null && aRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
            return FindRecipeResult.ofSuccess(aRecipe);
        ItemStack tOutput = GT_ModHandler.getSmeltingOutput(aInputs[0], false, null);
        return tOutput == null ? NOT_FOUND
            : FindRecipeResult.ofSuccess(
                new GT_Recipe(
                    false,
                    new ItemStack[] { GT_Utility.copyAmount(1, aInputs[0]) },
                    new ItemStack[] { tOutput },
                    null,
                    null,
                    null,
                    null,
                    128,
                    4,
                    0));
    }

    @Override
    public boolean containsInput(ItemStack aStack) {
        return GT_ModHandler.getSmeltingOutput(aStack, false, null) != null;
    }
}
