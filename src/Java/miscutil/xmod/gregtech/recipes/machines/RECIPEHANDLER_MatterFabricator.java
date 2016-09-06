package miscutil.xmod.gregtech.recipes.machines;

import miscutil.core.util.Utils;
import net.minecraftforge.fluids.FluidStack;

public class RECIPEHANDLER_MatterFabricator {

	public static void debug1(){
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("Walking Through Matter Fabrication Recipe Creation.");
		Utils.LOG_WARNING("My name is Ralph and I will be your humble host.");
	}
	public static void debug2(FluidStack aFluidInput, FluidStack aFluidOutput, int aDuration, int aEUt){
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("Taking a step forward.");
		Utils.LOG_WARNING("aInput1 == null && aFluidInput == null || aOutput == null && aFluidOutput == null");
		Utils.LOG_WARNING("aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Utils.LOG_WARNING("Passed.");
	}
	public static void debug3(FluidStack aFluidInput, FluidStack aFluidOutput, int aDuration, int aEUt){
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("Taking a step forward.");
		Utils.LOG_WARNING("(aOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get(cokeoven, aOutput, aDuration)) <= 0)");
		Utils.LOG_WARNING("aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Utils.LOG_WARNING("Passed.");
	}
	public static void debug4(FluidStack aFluidInput, FluidStack aFluidOutput, int aDuration, int aEUt){
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("Taking a step forward.");
		Utils.LOG_WARNING("(aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get(cokeoven, aFluidOutput.getFluid().getName(), aDuration)) <= 0)");
		Utils.LOG_WARNING("aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
		Utils.LOG_WARNING("Passed.");
		Utils.LOG_WARNING("aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);

	}
	public static void debug5(FluidStack aFluidInput, FluidStack aFluidOutput, int aDuration, int aEUt){
		String a = "nothing";
		String b = "";
		
		if (aFluidInput != null){
			a = aFluidInput.getFluid().getName();
		}
		if (aFluidOutput != null){
			b = aFluidOutput.getFluid().getName();
		}
		
		Utils.LOG_INFO("Successfully added a Matter Fabrication recipe for: "+b+", Using "+" liquid "+a+". This takes "+(aDuration/20)+" seconds for "+aEUt+"eu/t.");
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("==================================================================================");
		Utils.LOG_WARNING("==================================================================================");
	}
	
}
