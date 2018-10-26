package gtPlusPlus.xmod.gregtech.recipes;

import static gtPlusPlus.core.lib.CORE.GTNH;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.internal.IGT_RecipeAdder;
import gregtech.api.util.CustomRecipeMap;
import gregtech.api.util.Recipe_GT;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.data.ArrayUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.IGregtech_RecipeAdder;
import gtPlusPlus.xmod.gregtech.recipes.machines.RECIPEHANDLER_MatterFabricator;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class GregtechRecipeAdder implements IGregtech_RecipeAdder {

	@Override
	public boolean addCokeOvenRecipe(final ItemStack aInput1, final ItemStack aInput2, final FluidStack aFluidInput,
			final FluidStack aFluidOutput, final ItemStack aOutput, int aDuration, final int aEUt) {
		try {
			try {
				// RECIPEHANDLER_CokeOven.debug1();
				if (((aInput1 == null) /* && (aFluidInput == null) */)
						|| ((aOutput == null) || (aFluidOutput == null))) {
					// Utils.LOG_WARNING("aInput1:"+aInput1.toString()+"
					// aInput2:"+aInput2.toString()+"
					// aFluidInput:"+aFluidInput.toString()+"
					// aFluidOutput:"+aFluidOutput.toString()+"
					// aOutput:"+aOutput.toString()+" aDuration:"+aDuration+"
					// aEU/t:"+aEUt);
					Logger.WARNING("Something was null, returning false");
					return false;
				}

			}
			catch (final NullPointerException e) {
				e.getStackTrace();
			}
			try {
				// RECIPEHANDLER_CokeOven.debug2(aInput1, aInput2, aFluidInput,
				// aFluidOutput, aOutput, aDuration, aEUt);
				if ((aOutput != null)
						&& ((aDuration = GregTech_API.sRecipeFile.get("cokeoven", aOutput, aDuration)) <= 0)) {
					// Utils.LOG_WARNING("aInput1:"+aInput1.toString()+"
					// aInput2:"+aInput2.toString()+"
					// aFluidInput:"+aFluidInput.toString()+"
					// aFluidOutput:"+aFluidOutput.toString()+"
					// aOutput:"+aOutput.toString()+" aDuration:"+aDuration+"
					// aEU/t:"+aEUt);
					Logger.WARNING("Something was null, returning false");
					return false;
				}

			}
			catch (final NullPointerException e) {
				e.getStackTrace();
			}
			try {
				// RECIPEHANDLER_CokeOven.debug3(aInput1, aInput2, aFluidInput,
				// aFluidOutput, aOutput, aDuration, aEUt);
				if ((aFluidOutput == null) || ((aDuration = GregTech_API.sRecipeFile.get("cokeoven",
						aFluidOutput.getFluid().getName(), aDuration)) <= 0)) {
					// Utils.LOG_WARNING("aInput1:"+aInput1.toString()+"
					// aInput2:"+aInput2.toString()+"
					// aFluidInput:"+aFluidInput.toString()+"
					// aFluidOutput:"+aFluidOutput.toString()+"
					// aOutput:"+aOutput.toString()+" aDuration:"+aDuration+"
					// aEU/t:"+aEUt);
					Logger.WARNING("Something was null, returning false");
					return false;
				}

			}
			catch (final NullPointerException e) {
				e.getStackTrace();
			}
			try {
				// RECIPEHANDLER_CokeOven.debug4(aInput1, aInput2, aFluidInput,
				// aFluidOutput, aOutput, aDuration, aEUt);
				if (aFluidInput == null && aInput2 != null) {
					Recipe_GT.Gregtech_Recipe_Map.sCokeOvenRecipes.addRecipe(true, new ItemStack[] { aInput1, aInput2 },
							new ItemStack[] { aOutput }, null, null, null, new FluidStack[] { aFluidOutput }, aDuration,
							aEUt, 0);
				}
				else if (aFluidInput == null && aInput2 == null) {
					Recipe_GT.Gregtech_Recipe_Map.sCokeOvenRecipes.addRecipe(true, new ItemStack[] { aInput1 },
							new ItemStack[] { aOutput }, null, null, null, new FluidStack[] { aFluidOutput }, aDuration,
							aEUt, 0);
				}
				else {
					Recipe_GT.Gregtech_Recipe_Map.sCokeOvenRecipes.addRecipe(true, new ItemStack[] { aInput1, aInput2 },
							new ItemStack[] { aOutput }, null, null, new FluidStack[] { aFluidInput },
							new FluidStack[] { aFluidOutput }, aDuration, aEUt, 0);
				}
				// RECIPEHANDLER_CokeOven.debug5(aInput1, aInput2, aFluidInput,
				// aFluidOutput, aOutput, aDuration, aEUt);

				return true;

			}
			catch (final NullPointerException e) {
				Logger.WARNING("Something was null, returning false");
				return false;
			}
		}
		catch (final Throwable e) {
			// Logger.WARNING("aInput1:"+aInput1.toString()+"
			// aInput2:"+aInput2.toString()+"
			// aFluidInput:"+aFluidInput.toString()+"
			// aFluidOutput:"+aFluidOutput.toString()+"
			// aOutput:"+aOutput.toString()+" aDuration:"+aDuration+"
			// aEU/t:"+aEUt);
			Logger.WARNING("Failed.");
			e.getStackTrace();
			return false;
		}
	}

	@Override
	public boolean addMatterFabricatorRecipe(final FluidStack aFluidInput, final FluidStack aFluidOutput,
			final int aDuration, final int aEUt) {
		try {
			try {
				// RECIPEHANDLER_MatterFabricator.debug1();
				if (aFluidOutput == null) {
					// Utils.LOG_WARNING("aFluidInput:"+aFluidInput.toString()+"
					// aFluidOutput:"+aFluidOutput.toString()+"
					// aDuration:"+aDuration+" aEU/t:"+aEUt);
					Logger.WARNING("Something was null, returning false");
					return false;
				}

			}
			catch (final NullPointerException e) {
				e.getStackTrace();
			}
			try {

				// RECIPEHANDLER_MatterFabricator.debug4(aFluidInput,
				// aFluidOutput, aDuration, aEUt);
				if (aFluidInput == null) {
					// Recipe_GT.Gregtech_Recipe_Map.sMatterFabRecipes.addRecipe(true,
					// null, new FluidStack[]{aFluidOutput}, aDuration, aEUt,
					// 0);
					Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.addRecipe(true, null, null, null, null,
							new FluidStack[] { aFluidOutput }, aDuration, aEUt, 0);
				}
				else {
					// Recipe_GT.Gregtech_Recipe_Map.sMatterFabRecipes.addRecipe(true,
					// new FluidStack[]{aFluidInput}, new
					// FluidStack[]{aFluidOutput}, aDuration, aEUt, 0);
					Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.addRecipe(true, null, null, null,
							new FluidStack[] { aFluidInput }, new FluidStack[] { aFluidOutput }, aDuration, aEUt, 0);
				}
				RECIPEHANDLER_MatterFabricator.debug5(aFluidInput, aFluidOutput, aDuration, aEUt);

				return true;

			}
			catch (final NullPointerException e) {
				return false;
			}
		}
		catch (final Throwable e) {
			// Utils.LOG_WARNING("aFluidInput:"+aFluidInput.toString()+"
			// aFluidOutput:"+aFluidOutput.toString()+" aDuration:"+aDuration+"
			// aEU/t:"+aEUt);
			Logger.WARNING("Failed.");
			e.getStackTrace();
			return false;
		}
	}

	@Override
	public boolean addMatterFabricatorRecipe(final ItemStack aInputStack, final FluidStack aFluidInput,
			final FluidStack aFluidOutput, final int aDuration, final int aEUt) {
		try {
			try {
				if ((aFluidOutput == null) || (aInputStack == null)) {
					return false;
				}
			}
			catch (final NullPointerException e) {
			}
			try {
				if (aFluidInput == null) {
					Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.addRecipe(true, new ItemStack[] { aInputStack },
							null, null, null, new FluidStack[] { aFluidOutput }, aDuration, aEUt, 0);
				}
				else {
					Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.addRecipe(true, new ItemStack[] { aInputStack },
							null, null, new FluidStack[] { aFluidInput }, new FluidStack[] { aFluidOutput }, aDuration,
							aEUt, 0);
				}
				RECIPEHANDLER_MatterFabricator.debug5(aFluidInput, aFluidOutput, aDuration, aEUt);
				return true;
			}
			catch (final NullPointerException e) {
				return false;
			}
		}
		catch (final Throwable e) {
			return false;
		}
	}

	@Override
	public boolean addFuel(final ItemStack aInput1, final ItemStack aOutput1, final int aEU, final int aType) {
		if (aInput1 == null) {
			Logger.WARNING("Fuel Input is Invalid.");
			return false;
		}
		// new GregtechRecipe(aInput1, aOutput1,
		// GregTech_API.sRecipeFile.get("fuel_" + aType, aInput1, aEU), aType);
		return true;
	}

	/*
	 * @Override public boolean addDehydratorRecipe(ItemStack aItemA, ItemStack
	 * aItemB, FluidStack aFluid, ItemStack[] aOutputItems, FluidStack
	 * aOutputFluid, int aDuration, int aEUt) { if ((aItemA == null) || (aItemB
	 * == null) || (aOutputItems == null)) { return false; } for (ItemStack
	 * tStack : aOutputItems) { if (tStack != null) { if ((aDuration =
	 * GregTech_API.sRecipeFile.get("dehydrator", aItemA, aDuration)) <= 0) {
	 * return false; }
	 * Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true,
	 * new ItemStack[]{aItemA, aItemB}, aOutputItems, null, null, null,
	 * aDuration, aEUt, 0); RECIPEHANDLER_Dehydrator.debug5(aItemA, aItemB,
	 * aFluid, aOutputFluid, aOutputItems, aDuration, aEUt); return true; } }
	 * return false; }
	 * @Override public boolean addDehydratorRecipe(ItemStack aItemA, ItemStack
	 * aItemB, ItemStack[] aOutputItems, int aDuration, int aEUt) { if ((aItemA
	 * == null) || (aItemB == null) || (aOutputItems == null)) { return false; }
	 * if ((aDuration = GregTech_API.sRecipeFile.get("dehydrator", aItemA,
	 * aDuration)) <= 0) { return false; }
	 * Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true,
	 * new ItemStack[]{aItemA, aItemB}, aOutputItems, null, null, null,
	 * aDuration, aEUt, 0); RECIPEHANDLER_Dehydrator.debug5(aItemA, aItemB,
	 * null, null, aOutputItems, aDuration, aEUt); return true; }
	 * @Override public boolean addDehydratorRecipe(FluidStack aFluid,
	 * FluidStack aOutputFluid, ItemStack[] aOutputItems, int aDuration, int
	 * aEUt){ if ((aFluid == null) || (aOutputFluid == null || aOutputItems ==
	 * null)) { return false; } if ((aDuration =
	 * GregTech_API.sRecipeFile.get("dehydrator", aFluid.getUnlocalizedName(),
	 * aDuration)) <= 0) { return false; }
	 * Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true,
	 * null, aOutputItems, null, new FluidStack[]{aFluid}, new
	 * FluidStack[]{aOutputFluid}, aDuration, aEUt, 0);
	 * RECIPEHANDLER_Dehydrator.debug5(null, null, aFluid, aOutputFluid,
	 * aOutputItems, aDuration, aEUt); return true; }
	 */

	@Override
	public boolean addDehydratorRecipe(final ItemStack aInput, final FluidStack aFluid, final ItemStack[] aOutput,
			int aDuration, final int aEUt) {
		Logger.WARNING("Trying to add a Dehydrator recipe.");
		try {
			if ((aInput == null) || (aFluid == null) || (aOutput == null)) {
				return false;
			}
			if ((aDuration = GregTech_API.sRecipeFile.get("dehydrator", aInput, aDuration)) <= 0) {
				return false;
			}
			Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true, new ItemStack[] { aInput },
					aOutput, null, new FluidStack[] { aFluid }, null, aDuration, aEUt, 0);
			// RECIPEHANDLER_Dehydrator.debug5(aInput, null, aFluid, null,
			// aOutput, aDuration, aEUt);
			return true;
		}
		catch (final NullPointerException e) {
			Logger.WARNING("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
			return false;
		}
	}

	@Override
	public boolean addDehydratorRecipe(final ItemStack[] aInput, final FluidStack aFluidInput,
			final FluidStack aFluidOutput, final ItemStack[] aOutputItems, final int[] aChances, int aDuration,
			final int aEUt) throws IndexOutOfBoundsException {
		Logger.WARNING("Trying to add a Dehydrator recipe.");
		try {
			if (aInput[0] != null) {
				Logger.WARNING("Recipe requires input: " + aInput[0].getDisplayName() + " x" + aInput[0].stackSize);
			}
			if (aInput.length > 1) {
				if (aInput[1] != null) {
					Logger.WARNING("Recipe requires input: " + aInput[1].getDisplayName() + " x" + aInput[1].stackSize);
				}
			}
			if (aFluidInput != null) {
				Logger.WARNING("Recipe requires input: " + aFluidInput.getFluid().getName() + " " + aFluidInput.amount
						+ "mbst");
			}
			if (((aInput[0] == null) && (aFluidInput == null)) || ((aOutputItems == null) && (aFluidOutput == null))) {
				return false;
			}
			if ((aOutputItems != null)
					&& ((aDuration = GregTech_API.sRecipeFile.get("dehydrator", aOutputItems[0], aDuration)) <= 0)) {
				return false;
			}
			if (aOutputItems != null) {
				Logger.WARNING("Recipe will output: " + ItemUtils.getArrayStackNames(aOutputItems));
			}
			if ((aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("dehydrator",
					aFluidOutput.getFluid().getName(), aDuration)) <= 0)) {
				return false;
			}
			if (aFluidOutput != null) {
				Logger.WARNING("Recipe will output: " + aFluidOutput.getFluid().getName());
			}

			if (aInput.length == 1) {
				Logger.WARNING("Dehydrator recipe only has a single input item.");
				Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true, aInput, aOutputItems, null,
						aChances, new FluidStack[] { aFluidInput }, new FluidStack[] { aFluidOutput }, aDuration, aEUt,
						0);

			}
			else {
				Logger.WARNING("Dehydrator recipe has two input items.");
				Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(true, aInput, aOutputItems, null,
						aChances, new FluidStack[] { aFluidInput }, new FluidStack[] { aFluidOutput }, aDuration, aEUt,
						0);

			}

			return true;
		}
		catch (final NullPointerException e) {
			Logger.WARNING("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
			return false;
		}
	}

	@Override
	public boolean addBlastSmelterRecipe(final ItemStack[] aInput, FluidStack aOutput, final int aChance, int aDuration,
			final int aEUt) {
		return addBlastSmelterRecipe(aInput, null, aOutput, aChance, aDuration, aEUt, 3700);
	}

	@Override
	public boolean addBlastSmelterRecipe(final ItemStack[] aInput, FluidStack aInputFluid, FluidStack aOutput,
			final int aChance, int aDuration, final int aEUt) {
		return addBlastSmelterRecipe(aInput, aInputFluid, aOutput, aChance, aDuration, aEUt, 3700);
	}

	@Override
	public boolean addBlastSmelterRecipe(ItemStack[] aInput, FluidStack aInputFluid, FluidStack aOutput, int aChance,
			int aDuration, int aEUt, int aSpecialValue) {
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

		for (int das = 0; das < aInput.length; das++) {
			if (aInput[das] != null) {
				Logger.WARNING("tMaterial[" + das + "]: " + aInput[das].getDisplayName() + ", Amount: "
						+ aInput[das].stackSize);
			}
		}

		ArrayUtils.removeNulls(aInput);
		if (aInput.length <= 1) {
			return false;
		}		

		Recipe_GT.Gregtech_Recipe_Map.sAlloyBlastSmelterRecipes.addRecipe(true, aInput, new ItemStack[] { null }, null,
				new int[] { aChance*10 }, new FluidStack[] { aInputFluid }, new FluidStack[] { aOutput }, aDuration, aEUt,
				aSpecialValue);
		return true;
	}

	@Override
	public boolean addLFTRRecipe(final ItemStack aInput1, final FluidStack aInput2, final ItemStack aOutput1,
			final FluidStack aOutput2, final int aDuration, final int aEUt) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addLFTRRecipe(final ItemStack aInput1, final ItemStack aInput2, final ItemStack aOutput1,
			final int aDuration, final int aEUt) {
		return false;
	}

	@Override
	public boolean addLFTRRecipe(final FluidStack aInput1, final FluidStack aInput2, final FluidStack aOutput1,
			final int aDuration, final int aEUt) {
		if ((aInput1 == null) || (aInput2 == null) || (aOutput1 == null) || (aDuration < 1) || (aEUt < 1)) {
			return false;
		}
		Recipe_GT.Gregtech_Recipe_Map.sLiquidFluorineThoriumReactorRecipes.addRecipe(null,
				new FluidStack[] { aInput1, aInput2 }, new FluidStack[] { aOutput1 }, aDuration, aEUt, 16000);
		return true;
	}

	@Override
	public boolean addFissionFuel(final FluidStack aInput1, final FluidStack aInput2, final FluidStack aInput3,
			final FluidStack aInput4, final FluidStack aInput5, final FluidStack aInput6, final FluidStack aInput7,
			final FluidStack aInput8, final FluidStack aInput9, final FluidStack aOutput1, final FluidStack aOutput2,
			final int aDuration, final int aEUt) {

		if ((aInput1 == null) || (aInput2 == null) || (aOutput1 == null) || (aDuration < 1) || (aEUt < 1)) {
			return false;
		}
		final FluidStack inputs[] = { aInput1, aInput2, aInput3, aInput4, aInput5, aInput6, aInput7, aInput8, aInput9 };
		final FluidStack outputs[] = { aOutput1, aOutput2 };
		// Recipe_GT.Gregtech_Recipe_Map.sFissionFuelProcessing.addRecipe(null,
		// inputs, outputs, aDuration, aEUt, 0);
		CustomRecipeMap.sFissionFuelProcessing.addRecipe(null, inputs, outputs, aDuration, aEUt, 0);
		return true;
	}

	@Override
	public boolean addCyclotronRecipe(ItemStack aInputs, FluidStack aFluidInput, ItemStack[] aOutputs,
			FluidStack aFluidOutput, int[] aChances, int aDuration, int aEUt, int aSpecialValue) {
		return addCyclotronRecipe(new ItemStack[] {aInputs}, aFluidInput, aOutputs[0], aFluidOutput, aChances, aDuration, aEUt, aSpecialValue);
	}

	@Override
	public boolean addCyclotronRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput,
			FluidStack aFluidOutput, int[] aChances, int aDuration, int aEUt, int aSpecialValue) {
		if (aOutput == null) {
			return false;
		}
		if ((aDuration = GregTech_API.sRecipeFile.get("cyclotron", aOutput, aDuration)) <= 0) {
			return false;
		}
		if (Recipe_GT.Gregtech_Recipe_Map.sCyclotronRecipes.addRecipe(true, aInputs, new ItemStack[] { aOutput },
				null, aChances, new FluidStack[] { aFluidInput }, new FluidStack[] { aFluidOutput },
				Math.max(1, aDuration), Math.max(1, aEUt), aSpecialValue) != null) {
			return true;
		}

		return false;
	}

	@Override
	public boolean addMixerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aInput3, ItemStack aInput4,
			FluidStack aFluidInput, FluidStack aFluidOutput, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3,
			ItemStack aOutput4, int aDuration, int aEUt) {
		if (((aInput1 == null) && (aFluidInput == null)) || ((aOutput1 == null) && (aFluidOutput == null))) {
			return false;
		}
		if ((aOutput1 != null)
				&& ((aDuration = GregTech_API.sRecipeFile.get("advancedmixer", aOutput1, aDuration)) <= 0)) {
			return false;
		}
		if ((aFluidOutput != null) && ((aDuration = GregTech_API.sRecipeFile.get("advancedmixer",
				aFluidOutput.getFluid().getName(), aDuration)) <= 0)) {
			return false;
		}
		Recipe_GT.Gregtech_Recipe_Map.sAdvancedMixerRecipes.addRecipe(true,
				new ItemStack[] { aInput1, aInput2, aInput3, aInput4 },
				new ItemStack[] { aOutput1, aOutput2, aOutput3, aOutput4 }, null, null,
				new FluidStack[] { aFluidInput }, new FluidStack[] { aFluidOutput }, aDuration, aEUt, 0);
		return true;
	}

	// Machine Component Assembler
	@Override
	public boolean addComponentMakerRecipe(ItemStack[] aInputs, FluidStack aFluidInput, ItemStack aOutput1,
			int aDuration, int aEUt) {
		if (areItemsAndFluidsBothNull(aInputs, new FluidStack[] { aFluidInput })) {
			return false;
		}
		if (aOutput1 == null) {
			return false;
		}
		if ((aDuration = GregTech_API.sRecipeFile.get("machinecomponents", aOutput1, aDuration)) <= 0) {
			return false;
		}
		if (GTNH) {
			return false;
		}
		Recipe_GT.Gregtech_Recipe_Map.sComponentAssemblerRecipes.addRecipe(true, aInputs, new ItemStack[] { aOutput1 },
				null, new FluidStack[] { aFluidInput }, null, aDuration, aEUt, 0);
		return true;
	}


	public boolean addMultiblockCentrifugeRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUtick, int aSpecial){
		if (areItemsAndFluidsBothNull(aInputs, aFluidInputs) || areItemsAndFluidsBothNull(aOutputs, aFluidOutputs) || aEUtick <= 0) {
			return false;
		}

		if (!ItemUtils.checkForInvalidItems(aInputs, aOutputs)) {
			Logger.INFO("[Recipe] Error generating Large Centrifuge recipe.");
			Logger.INFO("Inputs: "+ItemUtils.getArrayStackNames(aInputs));
			Logger.INFO("Fluid Inputs: "+ItemUtils.getArrayStackNames(aFluidInputs));
			Logger.INFO("Outputs: "+ItemUtils.getArrayStackNames(aOutputs));
			Logger.INFO("Fluid Outputs: "+ItemUtils.getArrayStackNames(aFluidOutputs));
			return false;
		}
		Recipe_GT.Gregtech_Recipe_Map.sMultiblockCentrifugeRecipes.addRecipe(true, aInputs, aOutputs, null, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUtick, aSpecial);
		return true;
	}

	public boolean addMultiblockElectrolyzerRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUtick, int aSpecial){
		if (areItemsAndFluidsBothNull(aInputs, aFluidInputs) || areItemsAndFluidsBothNull(aOutputs, aFluidOutputs) || aEUtick <= 0) {
			return false;
		}
		if (!ItemUtils.checkForInvalidItems(aInputs, aOutputs)) {
			Logger.INFO("[Recipe] Error generating Large Electrolyzer recipe.");
			Logger.INFO("Inputs: "+ItemUtils.getArrayStackNames(aInputs));
			Logger.INFO("Fluid Inputs: "+ItemUtils.getArrayStackNames(aFluidInputs));
			Logger.INFO("Outputs: "+ItemUtils.getArrayStackNames(aOutputs));
			Logger.INFO("Fluid Outputs: "+ItemUtils.getArrayStackNames(aFluidOutputs));
			return false;
		}
		Recipe_GT.Gregtech_Recipe_Map.sMultiblockElectrolyzerRecipes.addRecipe(true, aInputs, aOutputs, null, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUtick, aSpecial);
		return true;
	}


	public boolean addAdvancedFreezerRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, ItemStack[] aOutputs, int[] aChances, int aDuration, int aEUtick, int aSpecial) {
		if (areItemsAndFluidsBothNull(aInputs, aFluidInputs) || areItemsAndFluidsBothNull(aOutputs, aFluidOutputs) || aEUtick <= 0) {
			return false;
		}

		if (!ItemUtils.checkForInvalidItems(aInputs, aOutputs)) {
			Logger.INFO("[Recipe] Error generating Adv. Vac Freezer recipe.");
			Logger.INFO("Inputs: "+ItemUtils.getArrayStackNames(aInputs));
			Logger.INFO("Fluid Inputs: "+ItemUtils.getArrayStackNames(aFluidInputs));
			Logger.INFO("Outputs: "+ItemUtils.getArrayStackNames(aOutputs));
			Logger.INFO("Fluid Outputs: "+ItemUtils.getArrayStackNames(aFluidOutputs));
			return false;
		}
		if (Recipe_GT.Gregtech_Recipe_Map.sAdvFreezerRecipes.addRecipe(true, aInputs, aOutputs, null, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUtick, aSpecial) != null) {
			return true;
		}
		return false;	

	}

	public boolean addAssemblerRecipeWithOreDict(Object aInput1, int aAmount1, Object aInput2, int aAmount2, ItemStack aOutput, int a1, int a2) {		
		if (aInput1 instanceof String || aInput2 instanceof String) {
			int mCompleted = 0;
			if (aInput1 instanceof String && aInput2 instanceof String) {
				List<ItemStack> x = OreDictionary.getOres((String) aInput1, false);
				List<ItemStack> x1 = OreDictionary.getOres((String) aInput2, false);
				if (x != null && x.size() > 0 && x1 != null && x1.size() > 0) {
					for (ItemStack r : x) {
						r.stackSize = aAmount1;
						for (ItemStack r1 : x1) {
							r1.stackSize = aAmount2;
							if (GT_Values.RA.addAssemblerRecipe(r, r1, aOutput, a1, a2)) {
								mCompleted++;
							}
						}
					}
				}
			}
			else if (aInput1 instanceof String) {
				List<ItemStack> x = OreDictionary.getOres((String) aInput1, false);				
				if (x != null && x.size() > 0) {
					for (ItemStack r : x) {
						r.stackSize = aAmount1;
						if (GT_Values.RA.addAssemblerRecipe(r, (ItemStack) aInput2, aOutput, a1, a2)) {
							mCompleted++;
						}
					}
				}

			}
			else {
				List<ItemStack> x = OreDictionary.getOres((String) aInput2, false);	
				if (x != null && x.size() > 0) {
					for (ItemStack r : x) {
						r.stackSize = aAmount1;
						if (GT_Values.RA.addAssemblerRecipe((ItemStack) aInput1, r, aOutput, a1, a2)) {
							mCompleted++;
						}
					}
				}
			}
			return mCompleted > 0;
		}
		else {
			return GT_Values.RA.addAssemblerRecipe((ItemStack) aInput1, (ItemStack) aInput2, aOutput, a1, a2);			
		}		
	}

	public boolean addAssemblerRecipeWithOreDict(Object aInput1, int aAmount1, Object aInput2, int aAmount2, FluidStack aInputFluid, ItemStack aOutput, int a1, int a2) {
		if (aInput1 instanceof String || aInput2 instanceof String) {
			int mCompleted = 0;
			if (aInput1 instanceof String && aInput2 instanceof String) {
				List<ItemStack> x = OreDictionary.getOres((String) aInput1, false);
				List<ItemStack> x1 = OreDictionary.getOres((String) aInput2, false);
				if (x != null && x.size() > 0 && x1 != null && x1.size() > 0) {
					for (ItemStack r : x) {
						r.stackSize = aAmount1;
						for (ItemStack r1 : x1) {
							r1.stackSize = aAmount2;
							if (GT_Values.RA.addAssemblerRecipe(r, r1, aInputFluid, aOutput, a1, a2)) {
								mCompleted++;
							}
						}
					}
				}
			}
			else if (aInput1 instanceof String) {
				List<ItemStack> x = OreDictionary.getOres((String) aInput1, false);				
				if (x != null && x.size() > 0) {
					for (ItemStack r : x) {
						r.stackSize = aAmount1;
						if (GT_Values.RA.addAssemblerRecipe(r, (ItemStack) aInput2, aInputFluid, aOutput, a1, a2)) {
							mCompleted++;
						}
					}
				}

			}
			else {
				List<ItemStack> x = OreDictionary.getOres((String) aInput2, false);	
				if (x != null && x.size() > 0) {
					for (ItemStack r : x) {
						r.stackSize = aAmount1;
						if (GT_Values.RA.addAssemblerRecipe((ItemStack) aInput1, r, aInputFluid, aOutput, a1, a2)) {
							mCompleted++;
						}
					}
				}
			}
			return mCompleted > 0;
		}
		else {
			return GT_Values.RA.addAssemblerRecipe((ItemStack) aInput1, (ItemStack) aInput2, aInputFluid, aOutput, a1, a2);			
		}
	}

	/*
	 * Reflection Based Recipe Additions with Fallbacks
	 */

	private static final  Method mSixSlotAssembly;
	private static final  Method mAssemblyLine;
	private static final  Method[] mChemicalRecipe = new Method[3];
	private static final  Method mLargeChemReactor;
	
	static {

		//Get GT's RA class;
		Class<? extends IGT_RecipeAdder> clazz = GT_Values.RA.getClass();		

		mChemicalRecipe[0] = ReflectionUtils.getMethod(clazz, "addChemicalRecipe", ItemStack.class, ItemStack.class, FluidStack.class, FluidStack.class, ItemStack.class, int.class);


		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK || CORE.GTNH) {	
			//6 Slot Assembler
			mSixSlotAssembly = ReflectionUtils.getMethod(clazz, "addAssemblerRecipe", ItemStack[].class, FluidStack.class, ItemStack.class, int.class, int.class);
			//Assembly Line
			mAssemblyLine = ReflectionUtils.getMethod(clazz, "addAssemblylineRecipe", ItemStack.class, int.class, ItemStack[].class, FluidStack[].class, ItemStack.class, int.class, int.class);

			mChemicalRecipe[1] = ReflectionUtils.getMethod(clazz, "addChemicalRecipe", ItemStack.class, ItemStack.class, FluidStack.class, FluidStack.class, ItemStack.class, int.class, int.class);
			mChemicalRecipe[2] = ReflectionUtils.getMethod(clazz, "addChemicalRecipe", ItemStack.class, ItemStack.class, FluidStack.class, FluidStack.class, ItemStack.class, ItemStack.class, int.class);

			mLargeChemReactor = ReflectionUtils.getMethod(clazz, "addMultiblockChemicalRecipe", ItemStack[].class, FluidStack[].class, FluidStack[].class, ItemStack[].class, int.class, int.class);



		}
		else {
			mSixSlotAssembly = null;
			mAssemblyLine = null;
			mLargeChemReactor = null;
		}


	}






	public boolean addSixSlotAssemblingRecipe(ItemStack[] aInputs, FluidStack aInputFluid, ItemStack aOutput1, int aDuration, int aEUt) {
		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK || CORE.GTNH) {			
			if (mSixSlotAssembly != null) {
				try {
					return (boolean) mSixSlotAssembly.invoke(GT_Values.RA, aInputs, aInputFluid, aOutput1, aDuration, aEUt);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					if (CORE.GTNH) {
						return false;
					}
				}
			}		
		}
		return CORE.RA.addComponentMakerRecipe(aInputs, aInputFluid, aOutput1, aDuration, aEUt);		
	}

	public boolean addAssemblylineRecipe(ItemStack aResearchItem, int aResearchTime, ItemStack[] aInputs, FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt) {
		if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
			if (aInputs.length < 6 && aFluidInputs.length < 2) {
				ItemStack[] aInputStack = new ItemStack[] {aResearchItem, aInputs[0], aInputs[1], aInputs[2], aInputs[3], aInputs[4]};
				return CORE.RA.addSixSlotAssemblingRecipe(aInputStack, aFluidInputs[0], aOutput, aDuration, aEUt);
			}        	
			return false;
		}
		else {
			if ((aResearchItem==null)||(aResearchTime<=0)||(aInputs == null) || (aOutput == null) || aInputs.length>15 || aInputs.length<4) {
				return false;
			}
			else {
				if (mAssemblyLine != null) {
					try {
						return (boolean) mAssemblyLine.invoke(GT_Values.RA, aResearchItem, aResearchTime, aInputs,
								aFluidInputs, aOutput, aDuration, aEUt);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						if (aInputs.length < 6 && aFluidInputs.length < 2) {
							ItemStack[] aInputStack = new ItemStack[] { aResearchItem, aInputs[0], aInputs[1],
									aInputs[2], aInputs[3], aInputs[4] };
							return CORE.RA.addSixSlotAssemblingRecipe(aInputStack, aFluidInputs[0], aOutput, aDuration,
									aEUt);
						}
						return false;
					}
				} else {
					if (aInputs.length < 6 && aFluidInputs.length < 2) {
						ItemStack[] aInputStack = new ItemStack[] { aResearchItem, aInputs[0], aInputs[1], aInputs[2],
								aInputs[3], aInputs[4] };
						return CORE.RA.addSixSlotAssemblingRecipe(aInputStack, aFluidInputs[0], aOutput, aDuration,
								aEUt);
					}
					return false;
				}
			}		
		}


	}


	public boolean addChemicalRecipe(ItemStack input1, ItemStack input2, FluidStack inputFluid, FluidStack outputFluid, ItemStack output, int time, int eu){
		return addChemicalRecipe(input1, input2, inputFluid, outputFluid, output, null, time, eu);
	}
	
	@Override
	public boolean addChemicalRecipe(ItemStack input1, ItemStack input2, FluidStack inputFluid, FluidStack outputFluid,	ItemStack output, Object object, int time, int eu) {
		try {
			if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
				return (boolean) mChemicalRecipe[0].invoke(GT_Values.RA, input1, input2, inputFluid, outputFluid, output, time);
			}
			else {
				return (boolean) mChemicalRecipe[1].invoke(GT_Values.RA, input1, input2, inputFluid, outputFluid, output, time, eu);
			}
		}
		catch (Throwable t) {
			return false;
		}
	}

	@Override
	public boolean addChemicalRecipe(ItemStack input1, ItemStack input2, FluidStack inputFluid, FluidStack outputFluid,	ItemStack output, ItemStack output2, int time) {
		try {
			if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
				return (boolean) mChemicalRecipe[0].invoke(GT_Values.RA, input1, input2, inputFluid, outputFluid, output, time);
			}
			else {
				return (boolean) mChemicalRecipe[2].invoke(GT_Values.RA, input1, input2, inputFluid, outputFluid, output, output2, time);
			}
		}
		catch (Throwable t) {
			return false;
		}
	}

	@Override
	public boolean addMultiblockChemicalRecipe(ItemStack[] itemStacks, FluidStack[] fluidStacks, FluidStack[] fluidStacks2, ItemStack[] outputs, int time, int eu) {
		if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
			return false;
		}		
		try {			
			return (boolean) mLargeChemReactor.invoke(GT_Values.RA, itemStacks, fluidStacks, fluidStacks2, outputs, time, eu);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return false;
		}		
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
