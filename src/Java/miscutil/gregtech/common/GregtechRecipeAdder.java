package miscutil.gregtech.common;

import gregtech.api.GregTech_API;
import gregtech.api.util.GT_Recipe;
import miscutil.gregtech.api.interfaces.internal.IGregtech_RecipeAdder;
import miscutil.gregtech.api.util.GregtechRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GregtechRecipeAdder implements IGregtech_RecipeAdder {

	@Override
	public boolean addCokeOvenRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
		/*if (bInput1 == null || aOutput1 == null || bOutput1 == null || aDuration < 1 || aEUt < 1) {
			return false;
		}*/
		GregtechRecipe.Gregtech_Recipe_Map.sCokeOvenRecipes.addRecipe(aOptimize, aInputs, aOutputs, aSpecial, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue);
		return true;
	}
	
	public boolean addCokeOvenRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration) {
        if (((aInput1 == null) && (aFluidInput == null)) || ((aOutput == null) && (aFluidOutput == null))) {
            return false;
        }
        if ((aOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("chemicalreactor", aOutput, aDuration)) <= 0)) {
            return false;
        }
        if ((aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("chemicalreactor", aFluidOutput.getFluid().getName(), aDuration)) <= 0)) {
            return false;
        }
        GT_Recipe.GT_Recipe_Map.sChemicalRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2}, new ItemStack[]{aOutput}, null, null, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, 30, 0);
        return true;
    }
}
