package gregtech.api.recipe.maps;

import java.util.Collection;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GT_Recipe;

/**
 * Abstract Class for general Recipe Handling of non GT Recipes
 */
public abstract class NonGTRecipeMap extends GT_Recipe.GT_Recipe_Map {

    public NonGTRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
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

    @Override
    public boolean containsInput(ItemStack aStack) {
        return false;
    }

    @Override
    public boolean containsInput(FluidStack aFluid) {
        return false;
    }

    @Override
    public boolean containsInput(Fluid aFluid) {
        return false;
    }

    @Override
    public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial,
        int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue) {
        return null;
    }

    @Override
    public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial,
        FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        return null;
    }

    @Override
    public GT_Recipe addRecipe(GT_Recipe aRecipe) {
        return null;
    }

    @Override
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
        Object aSpecial, int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration,
        int aEUt, int aSpecialValue) {
        return null;
    }

    @Override
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
        Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue) {
        return null;
    }

    @Override
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
        Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue, boolean hidden) {
        return null;
    }

    @Override
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, GT_Recipe aRecipe) {
        return null;
    }

    @Override
    public GT_Recipe add(GT_Recipe aRecipe) {
        return null;
    }

    @Override
    public void reInit() {
        /**/
    }

    @Override
    protected GT_Recipe addToItemMap(GT_Recipe aRecipe) {
        return null;
    }
}
