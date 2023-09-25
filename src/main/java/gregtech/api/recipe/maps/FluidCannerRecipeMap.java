package gregtech.api.recipe.maps;

import static gregtech.api.recipe.check.FindRecipeResult.NOT_FOUND;

import java.util.Collection;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import gregtech.api.GregTech_API;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

/**
 * Special Class for Fluid Canner handling.
 */
public class FluidCannerRecipeMap extends GT_Recipe.GT_Recipe_Map {

    public FluidCannerRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
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
        if (aInputs == null || aInputs.length == 0
            || aInputs[0] == null
            || result.isSuccessful()
            || !GregTech_API.sPostloadFinished) return result;

        if (aFluids != null && aFluids.length > 0 && aFluids[0] != null) {
            ItemStack tOutput = GT_Utility.fillFluidContainer(aFluids[0], aInputs[0], false, true);
            FluidStack tFluid = GT_Utility.getFluidForFilledItem(tOutput, true);
            if (tFluid != null) {
                GT_Recipe recipe = new GT_Recipe(
                    false,
                    new ItemStack[] { GT_Utility.copyAmount(1, aInputs[0]) },
                    new ItemStack[] { tOutput },
                    null,
                    null,
                    new FluidStack[] { tFluid },
                    null,
                    Math.max(tFluid.amount / 64, 16),
                    1,
                    0);
                recipe.mCanBeBuffered = false;
                return FindRecipeResult.ofSuccess(recipe);
            }
        }
        FluidStack tFluid = GT_Utility.getFluidForFilledItem(aInputs[0], true);
        if (tFluid != null) {
            GT_Recipe recipe = new GT_Recipe(
                false,
                new ItemStack[] { GT_Utility.copyAmount(1, aInputs[0]) },
                new ItemStack[] { GT_Utility.getContainerItem(aInputs[0], true) },
                null,
                null,
                null,
                new FluidStack[] { tFluid },
                Math.max(tFluid.amount / 64, 16),
                1,
                0);
            recipe.mCanBeBuffered = false;
            return FindRecipeResult.ofSuccess(recipe);
        }
        return NOT_FOUND;
    }

    @Override
    public boolean containsInput(ItemStack aStack) {
        return aStack != null && (super.containsInput(aStack) || (aStack.getItem() instanceof IFluidContainerItem
            && ((IFluidContainerItem) aStack.getItem()).getCapacity(aStack) > 0));
    }

    @Override
    public boolean containsInput(FluidStack aFluid) {
        return true;
    }

    @Override
    public boolean containsInput(Fluid aFluid) {
        return true;
    }
}
