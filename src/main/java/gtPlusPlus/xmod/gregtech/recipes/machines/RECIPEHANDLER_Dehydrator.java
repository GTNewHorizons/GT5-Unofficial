package gtPlusPlus.xmod.gregtech.recipes.machines;

import net.minecraft.item.ItemStack;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraftforge.fluids.FluidStack;

public class RECIPEHANDLER_Dehydrator {

	public static void debug1(){
		Logger.WARNING("==================================================================================");
		Logger.WARNING("==================================================================================");
		Logger.WARNING("==================================================================================");
		Logger.WARNING("Walking Through Chemical Dehydrator Recipe Creation.");
		Logger.WARNING("My name is Willus and I will be your humble host.");
	}
	public static void debug2(final ItemStack aInput1, final ItemStack aInput2, final FluidStack aFluidInput, final FluidStack aFluidOutput, final ItemStack aOutput, final int aDuration, final int aEUt){
		Logger.WARNING("==================================================================================");
		Logger.WARNING("Taking a step forward.");
		Logger.WARNING("aInput1 == null && aFluidInput == null || aOutput == null && aFluidOutput == null");
		Logger.WARNING("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Logger.WARNING("Passed.");
	}
	public static void debug3(final ItemStack aInput1, final ItemStack aInput2, final FluidStack aFluidInput, final FluidStack aFluidOutput, final ItemStack aOutput, final int aDuration, final int aEUt){
		Logger.WARNING("==================================================================================");
		Logger.WARNING("Taking a step forward.");
		Logger.WARNING("(aOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get(cokeoven, aOutput, aDuration)) <= 0)");
		Logger.WARNING("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Logger.WARNING("Passed.");
	}
	public static void debug4(final ItemStack aInput1, final ItemStack aInput2, final FluidStack aFluidInput, final FluidStack aFluidOutput, final ItemStack aOutput, final int aDuration, final int aEUt){
		Logger.WARNING("==================================================================================");
		Logger.WARNING("Taking a step forward.");
		Logger.WARNING("(aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get(cokeoven, aFluidOutput.getFluid().getName(), aDuration)) <= 0)");
		Logger.WARNING("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Logger.WARNING("Passed.");
		Logger.WARNING("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);

	}
	public static void debug5(final ItemStack aInput1, final ItemStack aInput2, final FluidStack aFluidInput, final FluidStack aFluidOutput, final ItemStack[] aOutput, final int aDuration, final int aEUt){

		String inputAname;
		String inputBname;
		String inputFluidname;
		String outputFluidName;

		if (aInput1 != null){
			inputAname = aInput1.getDisplayName();
		}
		else {
			inputAname = "null";
		}

		if (aInput2 != null){
			inputBname = aInput2.getDisplayName();
		}
		else {
			inputBname = "null";
		}

		if (aFluidInput != null){
			inputFluidname = aFluidInput.getFluid().getName();
		}
		else {
			inputFluidname = "null";
		}

		if (aFluidOutput != null){
			outputFluidName = aFluidOutput.getFluid().getName();
		}
		else {
			outputFluidName = "null";
		}

		Logger.INFO("Successfully added a Chemical Dehydrator recipe for: "+ItemUtils.getArrayStackNames(aOutput)+" & "+outputFluidName+", Using "+inputAname+" & "+inputBname+" & liquid "+inputFluidname+". This takes "+(aDuration/20)+" seconds for "+aEUt+"eu/t.");
		Logger.WARNING("==================================================================================");
		Logger.WARNING("==================================================================================");
		Logger.WARNING("==================================================================================");
	}

}
