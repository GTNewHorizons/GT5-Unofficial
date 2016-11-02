package gtPlusPlus.xmod.gregtech.recipes.machines;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RECIPEHANDLER_Dehydrator {

	public static void debug1(){
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("Walking Through Chemical Dehydrator Recipe Creation.");
		Utils.LOG_WARNING("My name is Willus and I will be your humble host.");
	}
	public static void debug2(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt){
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("Taking a step forward.");
		Utils.LOG_WARNING("aInput1 == null && aFluidInput == null || aOutput == null && aFluidOutput == null");
		Utils.LOG_WARNING("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Utils.LOG_WARNING("Passed.");
	}
	public static void debug3(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt){
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("Taking a step forward.");
		Utils.LOG_WARNING("(aOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get(cokeoven, aOutput, aDuration)) <= 0)");
		Utils.LOG_WARNING("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Utils.LOG_WARNING("Passed.");
	}
	public static void debug4(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt){
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("Taking a step forward.");
		Utils.LOG_WARNING("(aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get(cokeoven, aFluidOutput.getFluid().getName(), aDuration)) <= 0)");
		Utils.LOG_WARNING("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Utils.LOG_WARNING("Passed.");
		Utils.LOG_WARNING("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);

	}
	public static void debug5(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack[] aOutput, int aDuration, int aEUt){
		
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
		
		Utils.LOG_INFO("Successfully added a Chemical Dehydrator recipe for: "+ItemUtils.getArrayStackNames(aOutput)+" & "+outputFluidName+", Using "+inputAname+" & "+inputBname+" & liquid "+inputFluidname+". This takes "+(aDuration/20)+" seconds for "+aEUt+"eu/t.");
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("==================================================================================");
	}
	
}
