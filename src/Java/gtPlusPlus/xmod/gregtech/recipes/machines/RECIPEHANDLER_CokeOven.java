package gtPlusPlus.xmod.gregtech.recipes.machines;

import gtPlusPlus.core.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RECIPEHANDLER_CokeOven {

	public static void debug1(){
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("Walking Through CokeOven Recipe Creation.");
		Utils.LOG_WARNING("My name is Ralph and I will be your humble host.");
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
	public static void debug5(ItemStack aInput1, ItemStack aInput2, FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput, int aDuration, int aEUt){
		Utils.LOG_INFO("Successfully added a Coke Oven recipe for: "+aOutput.getDisplayName()+" & "+aFluidOutput.getFluid().getName()+", Using "+aInput1.getDisplayName()+" & "+aInput2.getDisplayName()+" & liquid "+aFluidInput.getFluid().getName()+". This takes "+(aDuration/20)+" seconds for "+aEUt+"eu/t.");
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("==================================================================================");
	}
	
}
