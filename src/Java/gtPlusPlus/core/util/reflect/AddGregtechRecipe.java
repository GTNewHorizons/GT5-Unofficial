package gtPlusPlus.core.util.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.internal.IGT_RecipeAdder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraftforge.fluids.FluidStack;

public final class AddGregtechRecipe {
    
    
    public static boolean importPyroRecipe(GT_Recipe aRecipe) {
        
        int aModifiedTime = (int) (aRecipe.mDuration * 0.8);
        
        if (aRecipe.mInputs.length > 2 || aRecipe.mFluidInputs.length > 1 || aRecipe.mFluidOutputs.length > 1 || aRecipe.mOutputs.length > 1) {
            return false;
        }
        
        int aCircuitNumber = -1;
        int aItemSlot = -1;
        
        int aSlot = 0;
        for (ItemStack a : aRecipe.mInputs) {
            if (a != null && a.getItem() != CI.getNumberedCircuit(1).getItem()) {
                aItemSlot = aSlot;
            }
            else {
                aSlot++;
            }
        }
        
        for (int i=0;i<25;i++) {
            ItemStack aTest = CI.getNumberedCircuit(i);
            for (ItemStack a : aRecipe.mInputs) {
                if (a != null && GT_Utility.areStacksEqual(a, aTest)) {
                    aCircuitNumber = i;
                    break;
                }
            }
        }
        
        if (aCircuitNumber < 0) {
            return false;
        }
        
        
        return CORE.RA.addCokeOvenRecipe(
                aRecipe.mInputs[aItemSlot],
                ItemUtils.getGregtechCircuit(aCircuitNumber),
                aRecipe.mFluidInputs[0],
                aRecipe.mFluidOutputs[0],
                aRecipe.mOutputs[0],
                aModifiedTime,
                aRecipe.mEUt);
        
        
    }
    

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
		/*
		try {
			IGT_RecipeAdder IGT_RecipeAdder = GT_Values.RA;
			if (IGT_RecipeAdder != null){
				Class<? extends IGT_RecipeAdder> classRA = IGT_RecipeAdder.getClass();

				for(Method current : classRA.getDeclaredMethods()){
					//Utils.LOG_INFO("-----------------------------------------------");
					////Utils.LOG_INFO("Found method: "+current.getName());
					//Utils.LOG_INFO("With Parameters: ");
					//Utils.LOG_INFO("===============================================");
					for (Class<?> P : current.getParameterTypes()){
						//Utils.LOG_INFO(""+P.getName());
						//Utils.LOG_INFO(""+P.getClass().getName());
					}
					//Utils.LOG_INFO("===============================================");
				}

				try {
					Method testRA = GT_Values.RA.getClass().getMethod("addAssemblylineRecipe", GT_Values.RA.getClass(), aResearchItem.getClass(), int.class, aInputs.getClass(), aFluidInputs.getClass(), aOutput.getClass(), int.class, int.class);
				testRA.invoke(aResearchItem, aResearchTime, aInputs, aFluidInputs, aOutput, aDuration, aEUt);
				}
				catch (Throwable masndj){
					masndj.printStackTrace();
				}


				Method addRecipe = classRA.getDeclaredMethod(
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
			Utils.LOG_INFO("[Assembly Line] - Failed to add recipe, due to GT not being .09 branch. Research: "+aResearchItem.getDisplayName()+" | Result: "+aOutput.getDisplayName());
			e.printStackTrace();
			return false;
		}	
		Utils.LOG_INFO("[Assembly Line] - Failed to add recipe. Research: "+aResearchItem.getDisplayName()+" | Result: "+aOutput.getDisplayName());
		 */return false;
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


	public static boolean addChemicalRecipeForBasicMachineOnly(final ItemStack p0, final ItemStack p1, final FluidStack p2, final FluidStack p3, final ItemStack p4, final ItemStack p5, final int p6, final int p7){

		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
			try {
				IGT_RecipeAdder IGT_RecipeAdder = GT_Values.RA;
				if (IGT_RecipeAdder != null){
					Class<? extends IGT_RecipeAdder> classRA = IGT_RecipeAdder.getClass();
					//final ItemStack p0, final ItemStack p1, final FluidStack p2, final FluidStack p3, final ItemStack p4, final ItemStack p5, final int p6, final int p7
					Method addRecipe = classRA.getMethod("addChemicalRecipeForBasicMachineOnly", ItemStack.class, ItemStack.class, FluidStack.class, FluidStack.class, ItemStack.class, ItemStack.class, int.class, int.class);
					if (addRecipe != null){
						return (boolean) addRecipe.invoke(IGT_RecipeAdder, p0, p1, p2, p3, p4, p5, p6, p7);
					}
				}
			}
			catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

			}
		}

		return GT_Values.RA.addChemicalRecipe(
				p0,
				p1,
				p2,
				p3,
				p4,
				p6);		

	}




}