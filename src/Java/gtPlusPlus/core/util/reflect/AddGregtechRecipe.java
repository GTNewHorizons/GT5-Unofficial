package gtPlusPlus.core.util.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.internal.IGT_RecipeAdder;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public final class AddGregtechRecipe {

	public static boolean addCokeAndPyrolyseRecipes(
			ItemStack input1, int circuitNumber,
			FluidStack inputFluid1, 
			ItemStack output1,
			FluidStack outputFluid1,
			int timeInSeconds,
			int euTick
			){
		//Seconds Conversion
		int TIME = timeInSeconds*20;
		int TIMEPYRO = TIME+(TIME/5);
		CORE.RA.addCokeOvenRecipe(
				input1,
				ItemUtils.getGregtechCircuit(circuitNumber),
				inputFluid1,
				outputFluid1,
				output1,
				TIME,
				euTick);
		PyrolyseOven(
				input1,
				inputFluid1,
				circuitNumber,
				output1,
				outputFluid1,
				TIMEPYRO,
				euTick);


		return false;
	}




	public static boolean PyrolyseOven(final ItemStack p0, final FluidStack p1, final int p2, final ItemStack p3,
			final FluidStack p4, final int p5, final int p6){

		try {
			IGT_RecipeAdder IGT_RecipeAdder = GT_Values.RA;
			if (IGT_RecipeAdder != null){
				Class<? extends IGT_RecipeAdder> classRA = IGT_RecipeAdder.getClass();
				Method addRecipe = classRA.getMethod("addPyrolyseRecipe", ItemStack.class, FluidStack.class, int.class, ItemStack.class, FluidStack.class, int.class, int.class);
				if (addRecipe != null){
					return (boolean) addRecipe.invoke(IGT_RecipeAdder, p0, p1, p2, p3, p4, p5, p6);
				}
			}
		}
		catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return false;
		}
		return false;
	}
	
	
	
	public static boolean addAssemblylineRecipe(
			ItemStack aResearchItem,
			int aResearchTime,
			ItemStack[] aInputs,
			FluidStack[] aFluidInputs,
			ItemStack aOutput, 
			int aDuration, int aEUt){

		try {
			IGT_RecipeAdder IGT_RecipeAdder = GT_Values.RA;
			if (IGT_RecipeAdder != null){
				Class<? extends IGT_RecipeAdder> classRA = IGT_RecipeAdder.getClass();
				Method addRecipe = classRA.getMethod(
						"addAssemblylineRecipe",
						ItemStack.class,
						int.class,
						ItemStack.class,
						FluidStack.class,
						ItemStack.class,
						int.class, 
						int.class);
				if (addRecipe != null){
					return (boolean) addRecipe.invoke(aResearchItem, aResearchTime, aInputs, aFluidInputs, aOutput, aDuration, aEUt);
				}
			}
		}
		catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Utils.LOG_INFO("[Assembly Line] - Failed to add recipe, due to GT being .09 branch. Research: "+aResearchItem.getDisplayName()+" | Result: "+aOutput.getDisplayName());
			return false;
		}	
		Utils.LOG_INFO("[Assembly Line] - Failed to add recipe. Research: "+aResearchItem.getDisplayName()+" | Result: "+aOutput.getDisplayName());
		return false;
	}

	public static boolean addCircuitAssemblerRecipe(
			ItemStack[] aInputs,
			FluidStack aFluidInput,
			ItemStack aOutput,
			int aDuration,
			int aEUt) {
		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			try {
				IGT_RecipeAdder IGT_RecipeAdder = GT_Values.RA;
				if (IGT_RecipeAdder != null){
					Class<? extends IGT_RecipeAdder> classRA = IGT_RecipeAdder.getClass();
					Method addRecipe = classRA.getMethod(
							"addCircuitAssemblerRecipe",
							ItemStack.class,
							FluidStack.class,
							ItemStack.class,
							int.class,
							int.class);
					if (addRecipe != null){
						if (aFluidInput.isFluidEqual(FluidUtils.getFluidStack("molten.tin", 1))){
							boolean[] didAdd = new boolean[3];
							FluidStack moltenMetal = FluidUtils.getFluidStack("molten.tin", 144);
							//Tin
							didAdd[0] = (boolean) addRecipe.invoke(
									IGT_RecipeAdder,
									aInputs,
									moltenMetal,
									aOutput,
									aDuration,
									aEUt);
							moltenMetal = FluidUtils.getFluidStack("molten.lead", 144);
							//Lead
							didAdd[1] = (boolean) addRecipe.invoke(
									IGT_RecipeAdder,
									aInputs,
									moltenMetal,
									aOutput,
									aDuration,
									aEUt);
							moltenMetal = FluidUtils.getFluidStack("molten.solderingalloy", 144/2);
							//Soldering Alloy
							didAdd[2] = (boolean) addRecipe.invoke(
									IGT_RecipeAdder,
									aInputs,
									moltenMetal,
									aOutput,
									aDuration,
									aEUt);

							if (didAdd[0] && didAdd[1] && didAdd[2]){
								return true;
							}
							else {
								return false;
							}						
						}
						else {
							return (boolean) addRecipe.invoke(
									IGT_RecipeAdder,
									aInputs,
									aFluidInput,
									aOutput,
									aDuration,
									aEUt);	
						}					
					}
				}
			}
			catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				return false;
			}
		}
		return false;
	}

}