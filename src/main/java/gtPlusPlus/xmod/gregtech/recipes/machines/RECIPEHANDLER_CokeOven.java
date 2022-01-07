package gtPlusPlus.xmod.gregtech.recipes.machines;

import net.minecraft.item.ItemStack;

import gtPlusPlus.api.objects.Logger;
import net.minecraftforge.fluids.FluidStack;

public class RECIPEHANDLER_CokeOven {

	public static void debug1(){
		Logger.WARNING("==================================================================================");
		Logger.WARNING("==================================================================================");
		Logger.WARNING("==================================================================================");
		Logger.WARNING("Walking Through CokeOven Recipe Creation.");
		Logger.WARNING("My name is Ralph and I will be your humble host.");
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
	public static void debug5(final ItemStack aInput1, final ItemStack aInput2, final FluidStack aFluidInput, final FluidStack aFluidOutput, final ItemStack aOutput, final int aDuration, final int aEUt){
		Logger.INFO("Successfully added a Coke Oven recipe for: "+aOutput.getDisplayName()+" & "+aFluidOutput.getFluid().getName()+", Using "+aInput1.getDisplayName()+" & "+aInput2.getDisplayName()+" & liquid "+aFluidInput.getFluid().getName()+". This takes "+(aDuration/20)+" seconds for "+aEUt+"eu/t.");
		Logger.WARNING("==================================================================================");
		Logger.WARNING("==================================================================================");
		Logger.WARNING("==================================================================================");
	}

}
