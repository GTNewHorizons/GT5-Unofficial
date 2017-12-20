package gtPlusPlus.xmod.gregtech.recipes;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.util.CustomRecipeMap;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.IGregtech_RecipeAdder;
import gtPlusPlus.xmod.gregtech.recipes.machines.RECIPEHANDLER_CokeOven;
import gtPlusPlus.xmod.gregtech.recipes.machines.RECIPEHANDLER_MatterFabricator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GregtechRecipeAdder implements IGregtech_RecipeAdder {



	@Override
	public boolean addCokeOvenRecipe(final ItemStack aInput1, final ItemStack aInput2, final FluidStack aFluidInput, final FluidStack aFluidOutput, final ItemStack aOutput, int aDuration, final int aEUt) {
		try {
			try {
				//RECIPEHANDLER_CokeOven.debug1();
				if (((aInput1 == null) /*&& (aFluidInput == null)*/) || ((aOutput == null) || (aFluidOutput == null))) {
					//Utils.LOG_WARNING("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
					Logger.WARNING("Something was null, returning false");
					return false;
				}

			} catch (final NullPointerException e){e.getStackTrace();}
			try {
				//RECIPEHANDLER_CokeOven.debug2(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt);
				if ((aOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("cokeoven", aOutput, aDuration)) <= 0)) {
					//Utils.LOG_WARNING("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
					Logger.WARNING("Something was null, returning false");
					return false;
				}

			} catch (final NullPointerException e){e.getStackTrace();}
			try {
				//RECIPEHANDLER_CokeOven.debug3(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt);
				if ((aFluidOutput == null) || ((aDuration = GregTech_API.sRecipeFile.get("cokeoven", aFluidOutput.getFluid().getName(), aDuration)) <= 0)) {
					//Utils.LOG_WARNING("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
					Logger.WARNING("Something was null, returning false");
					return false;
				}

			} catch (final NullPointerException e){e.getStackTrace();}
			try {
				//RECIPEHANDLER_CokeOven.debug4(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt);
				if (aFluidInput == null && aInput2 != null){
					Recipe_GT.Gregtech_Recipe_Map.sCokeOvenRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2}, new ItemStack[]{aOutput}, null, null, null, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
				}
				else if (aFluidInput == null && aInput2 == null){
					Recipe_GT.Gregtech_Recipe_Map.sCokeOvenRecipes.addRecipe(true, new ItemStack[]{aInput1}, new ItemStack[]{aOutput}, null, null, null, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
				}
				else {
					Recipe_GT.Gregtech_Recipe_Map.sCokeOvenRecipes.addRecipe(true, new ItemStack[]{aInput1, aInput2}, new ItemStack[]{aOutput}, null, null, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
				}
				RECIPEHANDLER_CokeOven.debug5(aInput1, aInput2, aFluidInput, aFluidOutput, aOutput, aDuration, aEUt);

				return true;

			} catch (final NullPointerException e){
				Logger.WARNING("Something was null, returning false");
				return false;
			}
		} catch (final Throwable e){
			Logger.WARNING("aInput1:"+aInput1.toString()+" aInput2:"+aInput2.toString()+" aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aOutput:"+aOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
			Logger.WARNING("Failed.");
			e.getStackTrace();
			return false;
		}
	}

	@Override
	public boolean addMatterFabricatorRecipe(final FluidStack aFluidInput, final FluidStack aFluidOutput, final int aDuration, final int aEUt) {
		try {
			try {
				//RECIPEHANDLER_MatterFabricator.debug1();
				if (aFluidOutput == null) {
					//Utils.LOG_WARNING("aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
					Logger.WARNING("Something was null, returning false");
					return false;
				}

			} catch (final NullPointerException e){e.getStackTrace();}
			try{

				//RECIPEHANDLER_MatterFabricator.debug4(aFluidInput, aFluidOutput, aDuration, aEUt);
				if (aFluidInput == null){
					//Recipe_GT.Gregtech_Recipe_Map.sMatterFabRecipes.addRecipe(true, null, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
					Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.addRecipe(true, null, null, null, null, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
				}
				else {
					//Recipe_GT.Gregtech_Recipe_Map.sMatterFabRecipes.addRecipe(true, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
					Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.addRecipe(true, null, null, null, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
				}
				RECIPEHANDLER_MatterFabricator.debug5(aFluidInput, aFluidOutput, aDuration, aEUt);

				return true;

			} catch (final NullPointerException e){
				return false;
			}
		} catch (final Throwable e){
			//Utils.LOG_WARNING("aFluidInput:"+aFluidInput.toString()+" aFluidOutput:"+aFluidOutput.toString()+" aDuration:"+aDuration+" aEU/t:"+aEUt);
			Logger.WARNING("Failed.");
			e.getStackTrace();
			return false;
		}
	}

	@Override
	public boolean addMatterFabricatorRecipe(final ItemStack aInputStack, final FluidStack aFluidInput, final FluidStack aFluidOutput, final int aDuration, final int aEUt) {
		try {
			try {if ((aFluidOutput == null) || (aInputStack == null)) {return false;}} catch (final NullPointerException e){}
			try{
				if (aFluidInput == null){
					Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.addRecipe(true, new ItemStack[]{aInputStack}, null, null, null, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
				}
				else {
					Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.addRecipe(true, new ItemStack[]{aInputStack}, null, null, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
				}
				RECIPEHANDLER_MatterFabricator.debug5(aFluidInput, aFluidOutput, aDuration, aEUt);
				return true;
			} catch (final NullPointerException e){return false;}
		} catch (final Throwable e){return false;}
	}


	@Override
	public boolean addFuel(final ItemStack aInput1, final ItemStack aOutput1, final int aEU, final int aType) {
		if (aInput1 == null) {
			Logger.WARNING("Fuel Input is Invalid.");
			return false;
		}
		//new GregtechRecipe(aInput1, aOutput1, GregTech_API.sRecipeFile.get("fuel_" + aType, aInput1, aEU), aType);
		return true;
	}

	/*@Override
	public boolean addDehydratorRecipe(ItemStack aItemA, ItemStack aItemB, FluidStack aFluid, ItemStack[] aOutputItems, FluidStack aOutputFluid, int aDuration, int aEUt) {
		if ((aItemA == null) || (aItemB == null) || (aOutputItems == null)) {
            return false;
        }
        for (ItemStack tStack : aOutputItems) {
            if (tStack != null) {
                if ((aDuration = GregTech_API.sRecipeFile.get("dehydrator", aItemA, aDuration)) <= 0) {
                    return false;
                }
                Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true, new ItemStack[]{aItemA, aItemB}, aOutputItems, null, null, null, aDuration, aEUt, 0);
                RECIPEHANDLER_Dehydrator.debug5(aItemA, aItemB, aFluid, aOutputFluid, aOutputItems, aDuration, aEUt);
                return true;
            }
        }
        return false;
	}

	@Override
	public boolean addDehydratorRecipe(ItemStack aItemA, ItemStack aItemB, ItemStack[] aOutputItems, int aDuration, int aEUt) {
        if ((aItemA == null) || (aItemB == null) || (aOutputItems == null)) {
            return false;
        }
        if ((aDuration = GregTech_API.sRecipeFile.get("dehydrator", aItemA, aDuration)) <= 0) {
            return false;
        }
        Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true, new ItemStack[]{aItemA, aItemB}, aOutputItems, null, null, null, aDuration, aEUt, 0);
        RECIPEHANDLER_Dehydrator.debug5(aItemA, aItemB, null, null, aOutputItems, aDuration, aEUt);
        return true;
    }



	@Override
	public boolean addDehydratorRecipe(FluidStack aFluid, FluidStack aOutputFluid, ItemStack[] aOutputItems, int aDuration, int aEUt){
    if ((aFluid == null) || (aOutputFluid == null || aOutputItems == null)) {
        return false;
    }
    if ((aDuration = GregTech_API.sRecipeFile.get("dehydrator", aFluid.getUnlocalizedName(), aDuration)) <= 0) {
        return false;
    }
    Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true, null, aOutputItems, null, new FluidStack[]{aFluid}, new FluidStack[]{aOutputFluid}, aDuration, aEUt, 0);
    RECIPEHANDLER_Dehydrator.debug5(null, null, aFluid, aOutputFluid, aOutputItems, aDuration, aEUt);
    return true;
}*/


	@Override
	public boolean addDehydratorRecipe(final ItemStack aInput, final FluidStack aFluid, final ItemStack[] aOutput, int aDuration, final int aEUt) {
		Logger.WARNING("Trying to add a Dehydrator recipe.");
		try{
			if ((aInput == null) || (aFluid == null) || (aOutput == null)) {
				return false;
			}
			if ((aDuration = GregTech_API.sRecipeFile.get("dehydrator", aInput, aDuration)) <= 0) {
				return false;
			}
			Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true, new ItemStack[]{aInput}, aOutput, null, new FluidStack[]{aFluid}, null, aDuration, aEUt, 0);
			//RECIPEHANDLER_Dehydrator.debug5(aInput, null, aFluid, null, aOutput, aDuration, aEUt);
			return true;
		}catch (final NullPointerException e){Logger.WARNING("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");return false;}
	}




	@Override
	public boolean addDehydratorRecipe(final ItemStack[] aInput, final FluidStack aFluidInput, final FluidStack aFluidOutput, final ItemStack[] aOutputItems, final int[] aChances, int aDuration, final int aEUt) throws IndexOutOfBoundsException{
		Logger.WARNING("Trying to add a Dehydrator recipe.");
		try{
			if (aInput[0] != null){
				Logger.WARNING("Recipe requires input: "+aInput[0].getDisplayName()+" x"+aInput[0].stackSize);
			}
			if (aInput.length > 1){
				if (aInput[1] != null){
					Logger.WARNING("Recipe requires input: "+aInput[1].getDisplayName()+" x"+aInput[1].stackSize);
				}
			}
			if (aFluidInput != null){
				Logger.WARNING("Recipe requires input: "+aFluidInput.getFluid().getName()+" "+aFluidInput.amount+"mbst");
			}
			if (((aInput[0] == null) && (aFluidInput == null)) || ((aOutputItems == null) && (aFluidOutput == null))) {
				return false;
			}
			if ((aOutputItems != null) && ((aDuration = GregTech_API.sRecipeFile.get("dehydrator", aOutputItems[0], aDuration)) <= 0)) {
				return false;
			}
			if (aOutputItems != null){
				Logger.WARNING("Recipe will output: "+ItemUtils.getArrayStackNames(aOutputItems));
			}
			if ((aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("dehydrator", aFluidOutput.getFluid().getName(), aDuration)) <= 0)) {
				return false;
			}
			if (aFluidOutput != null){
				Logger.WARNING("Recipe will output: "+aFluidOutput.getFluid().getName());
			}



			if (aInput.length == 1){
				Logger.WARNING("Dehydrator recipe only has a single input item.");
				Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true, aInput, aOutputItems, null, aChances, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);

			}
			else {
				Logger.WARNING("Dehydrator recipe has two input items.");
				Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true, aInput, aOutputItems, null, aChances, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);

			}

			return true;
		}catch (final NullPointerException e){Logger.WARNING("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");return false;}
	}

	@Override
	public boolean addBlastSmelterRecipe(final ItemStack[] aInput, FluidStack aOutput, final int aChance, int aDuration, final int aEUt) {
		if ((aInput == null) || (aOutput == null)) {
			Logger.WARNING("Fail - Input or Output was null.");
			return false;
		}


		if (aOutput.isFluidEqual(Materials.PhasedGold.getMolten(1))) {
			aOutput = Materials.VibrantAlloy.getMolten(aOutput.amount);
		}
		if (aOutput.isFluidEqual(Materials.PhasedIron.getMolten(1))) {
			aOutput = Materials.PulsatingIron.getMolten(aOutput.amount);
		}
		if ((aDuration = GregTech_API.sRecipeFile.get("blastsmelter", aOutput.getFluid().getName(), aDuration)) <= 0) {
			Logger.WARNING("Recipe did not register.");
			return false;
		}

		for (int das=0;das<aInput.length;das++){
			if (aInput[das] != null) {
				Logger.WARNING("tMaterial["+das+"]: "+aInput[das].getDisplayName()+", Amount: "+aInput[das].stackSize);
			}
		}

		Recipe_GT.Gregtech_Recipe_Map.sAlloyBlastSmelterRecipes.addRecipe(true, aInput, new ItemStack[]{null}, null, new int[]{aChance}, null, new FluidStack[]{aOutput}, aDuration, aEUt, 0);
		return true;
	}

	@Override
	public boolean addBlastSmelterRecipe(final ItemStack[] aInput, FluidStack aInputFluid, FluidStack aOutput, final int aChance, int aDuration, final int aEUt) {
		if ((aInput == null) || (aOutput == null)) {
			Logger.WARNING("Fail - Input or Output was null.");
			return false;
		}


		if (aOutput.isFluidEqual(Materials.PhasedGold.getMolten(1))) {
			aOutput = Materials.VibrantAlloy.getMolten(aOutput.amount);
		}
		if (aOutput.isFluidEqual(Materials.PhasedIron.getMolten(1))) {
			aOutput = Materials.PulsatingIron.getMolten(aOutput.amount);
		}
		if ((aDuration = GregTech_API.sRecipeFile.get("blastsmelter", aOutput.getFluid().getName(), aDuration)) <= 0) {
			Logger.WARNING("Recipe did not register.");
			return false;
		}

		for (int das=0;das<aInput.length;das++){
			if (aInput[das] != null) {
				Logger.WARNING("tMaterial["+das+"]: "+aInput[das].getDisplayName()+", Amount: "+aInput[das].stackSize);
			}
		}

		Recipe_GT.Gregtech_Recipe_Map.sAlloyBlastSmelterRecipes.addRecipe(true, aInput, new ItemStack[]{null}, null, new int[]{aChance}, new FluidStack[]{aInputFluid}, new FluidStack[]{aOutput}, aDuration, aEUt, 0);
		return true;
	}



	@Override
	public boolean addLFTRRecipe(final ItemStack aInput1, final FluidStack aInput2,
			final ItemStack aOutput1, final FluidStack aOutput2, final int aDuration, final int aEUt) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addLFTRRecipe(final ItemStack aInput1, final ItemStack aInput2, final ItemStack aOutput1, final int aDuration, final int aEUt) {
		return false;
	}

	@Override
	public boolean addLFTRRecipe(final FluidStack aInput1, final FluidStack aInput2, final FluidStack aOutput1, final int aDuration, final int aEUt) {
		if ((aInput1 == null) || (aInput2 == null) || (aOutput1 == null) || (aDuration < 1) || (aEUt < 1)) {
			return false;
		}
		Recipe_GT.Gregtech_Recipe_Map.sLiquidFluorineThoriumReactorRecipes.addRecipe(null, new FluidStack[]{aInput1, aInput2}, new FluidStack[]{aOutput1}, aDuration, aEUt, 16000);
		return true;
	}

	@Override
	public boolean addFissionFuel(
			final FluidStack aInput1, final FluidStack aInput2,	final FluidStack aInput3,
			final FluidStack aInput4, final FluidStack aInput5, final FluidStack aInput6,
			final FluidStack aInput7, final FluidStack aInput8, final FluidStack aInput9,
			final FluidStack aOutput1, final FluidStack aOutput2,
			final int aDuration, final int aEUt) {

		if ((aInput1 == null) || (aInput2 == null) || (aOutput1 == null) || (aDuration < 1) || (aEUt < 1)) {
			return false;
		}
		final FluidStack inputs[] = {aInput1, aInput2, aInput3, aInput4, aInput5, aInput6, aInput7, aInput8, aInput9};
		final FluidStack outputs[] = {aOutput1, aOutput2};
		//Recipe_GT.Gregtech_Recipe_Map.sFissionFuelProcessing.addRecipe(null, inputs, outputs, aDuration, aEUt, 0);
		CustomRecipeMap.sFissionFuelProcessing.addRecipe(null, inputs, outputs, aDuration, aEUt, 0);
		return true;
	}

	@Override
	public boolean addCyclotronRecipe(ItemStack aInput, FluidStack aFluidInput, ItemStack[] aOutputs,
			FluidStack aFluidOutput, int[] aChances, int aDuration, int aEUt, int aSpecialValue) {
		if (aOutputs == null) {
			return false;
		}
		for (ItemStack tStack : aOutputs) {
			if (tStack != null) {
				if ((aDuration = GregTech_API.sRecipeFile.get("cyclotron", aInput, aDuration)) <= 0) {
					return false;
				}
				Recipe_GT.Gregtech_Recipe_Map.sCyclotronRecipes.addRecipe(true, new ItemStack[] { aInput }, aOutputs,
						null, aChances, new FluidStack[] { aFluidInput }, new FluidStack[] { aFluidOutput },
						Math.max(1, aDuration), Math.max(1, aEUt), aSpecialValue);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4,
			FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt) {
		if (((aInput1 == null) && (aFluidInput == null)) || ((aOutput1 == null) && (aFluidOutput == null))) {
			return false;
		}
		if ((aOutput1 != null) && ((aDuration = GregTech_API.sRecipeFile.get("advancedmixer", aOutput1, aDuration)) <= 0)) {
			return false;
		}
		if ((aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("advancedmixer",
				aFluidOutput.getFluid().getName(), aDuration)) <= 0)) {
			return false;
		}
		Recipe_GT.Gregtech_Recipe_Map.sAdvancedMixerRecipes.addRecipe(true, new ItemStack[] { aInput1, aInput2, aInput3, aInput4 },
				new ItemStack[] { aOutput1, aOutput2, aOutput3, aOutput4 }, null, null, new FluidStack[] { aFluidInput },
				new FluidStack[] { aFluidOutput }, aDuration, aEUt, 0);
		return true;
	}

	//Machine Component Assembler
	@Override
	public boolean addComponentMakerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1, int aDuration, int aEUt) {
		if (areItemsAndFluidsBothNull(aInputs, new FluidStack[]{aFluidInput})) {
			return false;
		}
		if (aOutput1 == null) {
			return false;
		}
		if ((aDuration = GregTech_API.sRecipeFile.get("machinecomponents", aOutput1, aDuration)) <= 0) {
			return false;
		}
		Recipe_GT.Gregtech_Recipe_Map.sComponentAssemblerRecipes.addRecipe(true, aInputs, new ItemStack[]{aOutput1}, null, new FluidStack[]{aFluidInput}, null, aDuration, aEUt, 0);
		return true;
	}













	private boolean areItemsAndFluidsBothNull(final ItemStack[] items, final FluidStack[] fluids) {
		boolean itemsNull = true;
		if (items != null) {
			for (final ItemStack itemStack : items) {
				if (itemStack != null) {
					itemsNull = false;
					break;
				}
			}
		}
		boolean fluidsNull = true;
		if (fluids != null) {
			for (final FluidStack fluidStack : fluids) {
				if (fluidStack != null) {
					fluidsNull = false;
					break;
				}
			}
		}
		return itemsNull && fluidsNull;
	}


}
