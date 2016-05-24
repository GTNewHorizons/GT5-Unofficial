package miscutil.gregtech.common;

import gregtech.api.GregTech_API;
import miscutil.core.util.Utils;
import miscutil.gregtech.api.interfaces.internal.IGregtech_RecipeAdder;
import miscutil.gregtech.api.util.GregtechRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GregtechRecipeAdder implements IGregtech_RecipeAdder {

	@SuppressWarnings("static-method")
	private void debug1(){
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("Walking Through CokeOven Recipe Creation.");
		Utils.LOG_INFO("My name is Ralph and I will be your humble host.");
	}
	@SuppressWarnings("static-method")
	private void debug2(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt){
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("Taking a step forward.");
		Utils.LOG_INFO("aInput1 == null && aFluidInput == null || aOutput == null && aFluidOutput == null");
		Utils.LOG_INFO("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Utils.LOG_INFO("Passed.");
	}
	@SuppressWarnings("static-method")
	private void debug3(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt){
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("Taking a step forward.");
		Utils.LOG_INFO("(aOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get(cokeoven, aOutput, aDuration)) <= 0)");
		Utils.LOG_INFO("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Utils.LOG_INFO("Passed.");
	}
	@SuppressWarnings("static-method")
	private void debug4(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt){
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("Taking a step forward.");
		Utils.LOG_INFO("(aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get(cokeoven, aFluidOutput.getFluid().getName(), aDuration)) <= 0)");
		Utils.LOG_INFO("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Utils.LOG_INFO("Passed.");
		Utils.LOG_INFO("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);

	}
	@SuppressWarnings("static-method")
	private void debug5(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt){
		Utils.LOG_INFO("Successfully added a Coke Oven recipe for: "+aOutput.getDisplayName()+" & "+aFluidOutput.getFluid().getName()+", Using "+aInput1.getDisplayName()+" & "+aInput2.getDisplayName()+" & liquid "+aFluidInput.getFluid().getName()+". This took "+(aDuration/20)+" seconds for "+aEUt+"eu/t.");
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("==================================================================================");
		Utils.LOG_INFO("==================================================================================");
	}

	@Override
	public boolean addCokeOvenRecipe(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt) {
		try {
			try {
				debug1();
				if (((aInput1 == null) /*&& (aFluidInput == null)*/) || ((aOutput == null) || (aFluidOutput == null))) {
					Utils.LOG_INFO("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
					Utils.LOG_INFO("Something was null, returning false");
					return false;
				}

			} catch (NullPointerException e){e.getStackTrace();}
			try {
				debug2(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt);
				if ((aOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("cokeoven", aOutput, aDuration)) <= 0)) {
					Utils.LOG_INFO("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
					Utils.LOG_INFO("Something was null, returning false");
					return false;
				}

			} catch (NullPointerException e){e.getStackTrace();}
			try {

				debug3(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt);
				if ((aFluidOutput == null) && ((aDuration = GregTech_API.sRecipeFile.get("cokeoven", aFluidOutput.getFluid().getName(), aDuration)) <= 0)) {
					Utils.LOG_INFO("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
					Utils.LOG_INFO("Something was null, returning false");
					return false;
				}

			} catch (NullPointerException e){e.getStackTrace();}
			try {
				debug4(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt);
				if (aFluidInput == null){
					GregtechRecipe.Gregtech_Recipe_Map.sCokeOvenRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2}, new ItemStack[]{aOutput}, null, null, null, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
				}
				else {
					GregtechRecipe.Gregtech_Recipe_Map.sCokeOvenRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2}, new ItemStack[]{aOutput}, null, null, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
				}
				debug5(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt);

				return true;

			} catch (NullPointerException e){
				return false;
			}
		} catch (Throwable e){
			Utils.LOG_INFO("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
			Utils.LOG_INFO("Failed.");
			e.getStackTrace();
			return false;
		}
	}



}
