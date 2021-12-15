package gtPlusPlus.xmod.gregtech.recipes.machines;

import gtPlusPlus.api.objects.Logger;
import net.minecraftforge.fluids.FluidStack;

public class RECIPEHANDLER_MatterFabricator {

	public static void debug1(){
		Logger.WARNING("==================================================================================");
		Logger.WARNING("==================================================================================");
		Logger.WARNING("==================================================================================");
		Logger.WARNING("Walking Through Matter Fabrication Recipe Creation.");
		Logger.WARNING("My name is Ralph and I will be your humble host.");
	}
	public static void debug2(final FluidStack aFluidInput, final FluidStack aFluidOutput, final int aDuration, final int aEUt){
		Logger.WARNING("==================================================================================");
		Logger.WARNING("Taking a step forward.");
		Logger.WARNING("aInput1 == null && aFluidInput == null || aOutput == null && aFluidOutput == null");
		Logger.WARNING("aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Logger.WARNING("Passed.");
	}
	public static void debug3(final FluidStack aFluidInput, final FluidStack aFluidOutput, final int aDuration, final int aEUt){
		Logger.WARNING("==================================================================================");
		Logger.WARNING("Taking a step forward.");
		Logger.WARNING("(aOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get(cokeoven, aOutput, aDuration)) <= 0)");
		Logger.WARNING("aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Logger.WARNING("Passed.");
	}
	public static void debug4(final FluidStack aFluidInput, final FluidStack aFluidOutput, final int aDuration, final int aEUt){
		Logger.WARNING("==================================================================================");
		Logger.WARNING("Taking a step forward.");
		Logger.WARNING("(aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get(cokeoven, aFluidOutput.getFluid().getName(), aDuration)) <= 0)");
		Logger.WARNING("aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Logger.WARNING("Passed.");
		Logger.WARNING("aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);

	}
	public static void debug5(final FluidStack aFluidInput, final FluidStack aFluidOutput, final int aDuration, final int aEUt){
		String a = "nothing";
		String b = "";

		if (aFluidInput != null){
			a = aFluidInput.getFluid().getName();
		}
		if (aFluidOutput != null){
			b = aFluidOutput.getFluid().getName();
		}

		Logger.INFO("Successfully added a Matter Fabrication recipe for: "+b+", Using "+" liquid "+a+". This takes "+(aDuration/20)+" seconds for "+aEUt+"eu/t.");
		Logger.WARNING("==================================================================================");
		Logger.WARNING("==================================================================================");
		Logger.WARNING("==================================================================================");
	}

}
