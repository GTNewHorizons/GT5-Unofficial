package miscutil.gregtech.common;

import gregtech.api.GregTech_API;
import miscutil.core.util.Utils;
import miscutil.gregtech.api.interfaces.internal.IGregtech_RecipeAdder;
import miscutil.gregtech.api.util.GregtechRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GregtechRecipeAdder implements IGregtech_RecipeAdder {

	/*@Override
	public boolean addCokeOvenRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
		if (bInput1 == null || aOutput1 == null || bOutput1 == null || aDuration < 1 || aEUt < 1) {
			return false;
		}
		GregtechRecipe.Gregtech_Recipe_Map.sCokeOvenRecipes.addRecipe(aOptimize, aInputs, aOutputs, aSpecial, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue);
		return true;
	}*/

	@Override
	public boolean addCokeOvenRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt) {
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("Walking Through CokeOven Recipe Creation.");
		Utils.LOG_INFO("My name is Ralph and I will be your humble host.");
		if (((aInput1 == null) /*&& (aFluidInput == null)*/) || ((aOutput == null) && (aFluidOutput == null))) {
			Utils.LOG_INFO("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
			Utils.LOG_INFO("Something was null, returning false");
			return false;
		}
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("Taking a step forward.");
		Utils.LOG_INFO("aInput1 == null && aFluidInput == null || aOutput == null && aFluidOutput == null");
		Utils.LOG_INFO("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Utils.LOG_INFO("Passed.");
		if ((aOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("cokeoven", aOutput, aDuration)) <= 0)) {
			Utils.LOG_INFO("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
			Utils.LOG_INFO("Something was null, returning false");
			return false;
		}
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("Taking a step forward.");
		Utils.LOG_INFO("(aOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get(cokeoven, aOutput, aDuration)) <= 0)");
		Utils.LOG_INFO("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Utils.LOG_INFO("Passed.");
		if ((aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("cokeoven", aFluidOutput.getFluid().getName(), aDuration)) <= 0)) {
			Utils.LOG_INFO("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
			Utils.LOG_INFO("Something was null, returning false");
			return false;
		}
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("Taking a step forward.");
		Utils.LOG_INFO("(aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get(cokeoven, aFluidOutput.getFluid().getName(), aDuration)) <= 0)");
		Utils.LOG_INFO("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Utils.LOG_INFO("Passed.");
		Utils.LOG_INFO("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		GregtechRecipe.Gregtech_Recipe_Map.sCokeOvenRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2}, new ItemStack[]{aOutput}, null, null, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
		Utils.LOG_INFO("Successfully added a Coke Oven recipe for: "+aOutput.getDisplayName()+" & "+aFluidOutput.getFluid().getName()+", Using "+aInput1.getDisplayName()+", "+aInput2.getDisplayName()+", "+aFluidInput.getFluid().getName());
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("==================================================================================");
		return true;
	}
}
