package gtPlusPlus.core.recipe;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
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
			final Item maceratorInput, final int maceratorInputAmount1,
			final Item maceratorOutput, final int maceratorOutputAmount1,
			final Item compressorInput, final int compressorInputAmount1,
			final Item compressorOutput, final int compressorOutputAmount1,
			final Item blastFurnaceInput, final int blastFurnaceInputAmount1,
			final Item blastFurnaceOutput, final int blastFurnaceOutputAmount1,
			final Item blastFurnaceInput2, final int blastFurnaceInputAmount2,
			final Item blastFurnaceOutput2, final int blastFurnaceOutputAmount2,
			final Item smeltingInput, final int smeltingInputAmount1,
			final Item smeltingOutput, final int smeltingOutputAmount1,

			final int euPerTick, final int timeInTicks,
			final boolean addMaceratorRecipe, final boolean addCompressorRecipe, final boolean addBlastFurnaceRecipe, final int blastFurnaceTemp, final boolean addSmeltingRecipe, final boolean addMixerRecipe){
		euT = euPerTick;
		ticks = timeInTicks;

		resetVars();
		if (addMaceratorRecipe){
			inputStack1 = ItemUtils.getSimpleStack(maceratorInput, maceratorInputAmount1);
			outputStack1 = ItemUtils.getSimpleStack(maceratorOutput, maceratorOutputAmount1);
			addMaceratorRecipe(inputStack1, outputStack1);
		}
		resetVars();
		if (addCompressorRecipe){
			inputStack1 = ItemUtils.getSimpleStack(compressorInput, compressorInputAmount1);
			outputStack1 = ItemUtils.getSimpleStack(compressorOutput, compressorOutputAmount1);
			addCompressorRecipe(inputStack1, outputStack1);
		}
		resetVars();
		if (addBlastFurnaceRecipe){
			inputStack1 = ItemUtils.getSimpleStack(blastFurnaceInput, blastFurnaceInputAmount1);
			inputStack2 = ItemUtils.getSimpleStack(blastFurnaceInput2, blastFurnaceInputAmount2);
			outputStack1 = ItemUtils.getSimpleStack(blastFurnaceOutput, blastFurnaceOutputAmount1);
			outputStack2 = ItemUtils.getSimpleStack(blastFurnaceOutput2, blastFurnaceOutputAmount2);
			addBlastFurnaceRecipe(inputStack1, inputStack2, outputStack1, outputStack2, blastFurnaceTemp);
		}
		resetVars();
		if (addSmeltingRecipe){
			inputStack1 = ItemUtils.getSimpleStack(smeltingInput, smeltingInputAmount1);
			outputStack1 = ItemUtils.getSimpleStack(smeltingOutput, smeltingOutputAmount1);
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

	private static void addMaceratorRecipe(final ItemStack input1, final ItemStack output1){
		GT_ModHandler.addPulverisationRecipe(input1, output1);
	}

	private static void addCompressorRecipe(final ItemStack input1, final ItemStack output1){
		GT_ModHandler.addCompressionRecipe(input1, output1);
	}

	private static void addBlastFurnaceRecipe(final ItemStack input1, final ItemStack input2, final ItemStack output1, final ItemStack output2, final int tempRequired){
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

	private static void addSmeltingRecipe(final ItemStack input1, final ItemStack output1){
		GT_ModHandler.addSmeltingRecipe(input1, output1);
	}

}
