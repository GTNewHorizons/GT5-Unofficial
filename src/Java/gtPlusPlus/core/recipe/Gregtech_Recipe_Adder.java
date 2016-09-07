package gtPlusPlus.core.recipe;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Gregtech_Recipe_Adder {

	private static int euT;
	private static int ticks;
	private static ItemStack inputStack1;
	private static ItemStack inputStack2;
	private static ItemStack outputStack1;
	private static ItemStack outputStack2;

	public static void addRecipe(
			Item maceratorInput, int maceratorInputAmount1,
			Item maceratorOutput, int maceratorOutputAmount1,
			Item compressorInput, int compressorInputAmount1,
			Item compressorOutput, int compressorOutputAmount1,
			Item blastFurnaceInput, int blastFurnaceInputAmount1,
			Item blastFurnaceOutput, int blastFurnaceOutputAmount1,
			Item blastFurnaceInput2, int blastFurnaceInputAmount2,
			Item blastFurnaceOutput2, int blastFurnaceOutputAmount2,
			Item smeltingInput, int smeltingInputAmount1,
			Item smeltingOutput, int smeltingOutputAmount1,			
			
			int euPerTick, int timeInTicks,
			boolean addMaceratorRecipe, boolean addCompressorRecipe, boolean addBlastFurnaceRecipe, int blastFurnaceTemp, boolean addSmeltingRecipe, boolean addMixerRecipe){
		euT = euPerTick;
		ticks = timeInTicks;
		
		resetVars();
		if (addMaceratorRecipe){
			inputStack1 = UtilsItems.getSimpleStack(maceratorInput, maceratorInputAmount1);
			outputStack1 = UtilsItems.getSimpleStack(maceratorOutput, maceratorOutputAmount1);
			addMaceratorRecipe(inputStack1, outputStack1);
		}
		resetVars();
		if (addCompressorRecipe){
			inputStack1 = UtilsItems.getSimpleStack(compressorInput, compressorInputAmount1);
			outputStack1 = UtilsItems.getSimpleStack(compressorOutput, compressorOutputAmount1);
			addCompressorRecipe(inputStack1, outputStack1);
		}
		resetVars();
		if (addBlastFurnaceRecipe){
			inputStack1 = UtilsItems.getSimpleStack(blastFurnaceInput, blastFurnaceInputAmount1);
			inputStack2 = UtilsItems.getSimpleStack(blastFurnaceInput2, blastFurnaceInputAmount2);
			outputStack1 = UtilsItems.getSimpleStack(blastFurnaceOutput, blastFurnaceOutputAmount1);
			outputStack2 = UtilsItems.getSimpleStack(blastFurnaceOutput2, blastFurnaceOutputAmount2);
			addBlastFurnaceRecipe(inputStack1, inputStack2, outputStack1, outputStack2, blastFurnaceTemp);
		}
		resetVars();
		if (addSmeltingRecipe){
			inputStack1 = UtilsItems.getSimpleStack(smeltingInput, smeltingInputAmount1);
			outputStack1 = UtilsItems.getSimpleStack(smeltingOutput, smeltingOutputAmount1);
			addSmeltingRecipe(inputStack1, outputStack1);
		}
		resetVars();

	}
	
	private static void resetVars(){
		inputStack1 = null;
		inputStack2 = null;
		outputStack1 = null;
		outputStack2 = null;
	}

	private static void addMaceratorRecipe(ItemStack input1, ItemStack output1){
		GT_ModHandler.addPulverisationRecipe(input1, output1);
	}

	private static void addCompressorRecipe(ItemStack input1, ItemStack output1){
		GT_ModHandler.addCompressionRecipe(input1, output1);
	}

	private static void addBlastFurnaceRecipe(ItemStack input1, ItemStack input2, ItemStack output1, ItemStack output2, int tempRequired){
		Utils.LOG_INFO("Registering Blast Furnace Recipes.");
		GT_Values.RA.addBlastRecipe(
				input1,
				input2,
				GT_Values.NF, GT_Values.NF,
				output1,
				output2,
				ticks,
				euT, 
				tempRequired);
	}

	private static void addSmeltingRecipe(ItemStack input1, ItemStack output1){
		GT_ModHandler.addSmeltingRecipe(input1, output1);
	}

}
