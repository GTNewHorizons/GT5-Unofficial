package gregtech.api.recipe.maps;

import java.util.Collection;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.ItemList;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;

/**
 * Special Class for Unboxinator handling.
 */
public class UnboxinatorRecipeMap extends GT_Recipe.GT_Recipe_Map {

    public UnboxinatorRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
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
        if (aInputs == null || aInputs.length == 0 || !ItemList.IC2_Scrapbox.isStackEqual(aInputs[0], false, true))
            return super.findRecipeWithResult(
                aRecipe,
                aIsValidRecipe,
                aNotUnificated,
                aDontCheckStackSizes,
                aVoltage,
                aFluids,
                aSpecialSlot,
                aInputs);
        ItemStack tOutput = GT_ModHandler.getRandomScrapboxDrop();
        if (tOutput == null) return super.findRecipeWithResult(
            aRecipe,
            aIsValidRecipe,
            aNotUnificated,
            aDontCheckStackSizes,
            aVoltage,
            aFluids,
            aSpecialSlot,
            aInputs);
        GT_Recipe rRecipe = new GT_Recipe(
            false,
            new ItemStack[] { ItemList.IC2_Scrapbox.get(1) },
            new ItemStack[] { tOutput },
            null,
            null,
            null,
            null,
            16,
            1,
            0);
        // It is not allowed to be buffered due to the random Output
        rRecipe.mCanBeBuffered = false;
        // Due to its randomness it is not good if there are Items in the Output Slot, because those Items could
        // manipulate the outcome.
        rRecipe.mNeedsEmptyOutput = true;
        return FindRecipeResult.ofSuccess(rRecipe);
    }

    @Override
    public boolean containsInput(ItemStack aStack) {
        return ItemList.IC2_Scrapbox.isStackEqual(aStack, false, true) || super.containsInput(aStack);
    }
}
