package gtPlusPlus.core.recipe;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Gregtech_Recipe_Adder {

	private static int			euT;
	private static int			ticks;
	private static ItemStack	inputStack1;
	private static ItemStack	inputStack2;
	private static ItemStack	outputStack1;
	private static ItemStack	outputStack2;

	private static void addBlastFurnaceRecipe(final ItemStack input1, final ItemStack input2, final ItemStack output1,
			final ItemStack output2, final int tempRequired) {
		Utils.LOG_INFO("Registering Blast Furnace Recipes.");
		GT_Values.RA.addBlastRecipe(input1, input2, GT_Values.NF, GT_Values.NF, output1, output2,
				Gregtech_Recipe_Adder.ticks, Gregtech_Recipe_Adder.euT, tempRequired);
	}

	private static void addCompressorRecipe(final ItemStack input1, final ItemStack output1) {
		GT_ModHandler.addCompressionRecipe(input1, output1);
	}

	private static void addMaceratorRecipe(final ItemStack input1, final ItemStack output1) {
		GT_ModHandler.addPulverisationRecipe(input1, output1);
	}

	public static void addRecipe(final Item maceratorInput, final int maceratorInputAmount1, final Item maceratorOutput,
			final int maceratorOutputAmount1, final Item compressorInput, final int compressorInputAmount1,
			final Item compressorOutput, final int compressorOutputAmount1, final Item blastFurnaceInput,
			final int blastFurnaceInputAmount1, final Item blastFurnaceOutput, final int blastFurnaceOutputAmount1,
			final Item blastFurnaceInput2, final int blastFurnaceInputAmount2, final Item blastFurnaceOutput2,
			final int blastFurnaceOutputAmount2, final Item smeltingInput, final int smeltingInputAmount1,
			final Item smeltingOutput, final int smeltingOutputAmount1,

			final int euPerTick, final int timeInTicks, final boolean addMaceratorRecipe,
			final boolean addCompressorRecipe, final boolean addBlastFurnaceRecipe, final int blastFurnaceTemp,
			final boolean addSmeltingRecipe, final boolean addMixerRecipe) {
		Gregtech_Recipe_Adder.euT = euPerTick;
		Gregtech_Recipe_Adder.ticks = timeInTicks;

		Gregtech_Recipe_Adder.resetVars();
		if (addMaceratorRecipe) {
			Gregtech_Recipe_Adder.inputStack1 = ItemUtils.getSimpleStack(maceratorInput, maceratorInputAmount1);
			Gregtech_Recipe_Adder.outputStack1 = ItemUtils.getSimpleStack(maceratorOutput, maceratorOutputAmount1);
			Gregtech_Recipe_Adder.addMaceratorRecipe(Gregtech_Recipe_Adder.inputStack1,
					Gregtech_Recipe_Adder.outputStack1);
		}
		Gregtech_Recipe_Adder.resetVars();
		if (addCompressorRecipe) {
			Gregtech_Recipe_Adder.inputStack1 = ItemUtils.getSimpleStack(compressorInput, compressorInputAmount1);
			Gregtech_Recipe_Adder.outputStack1 = ItemUtils.getSimpleStack(compressorOutput, compressorOutputAmount1);
			Gregtech_Recipe_Adder.addCompressorRecipe(Gregtech_Recipe_Adder.inputStack1,
					Gregtech_Recipe_Adder.outputStack1);
		}
		Gregtech_Recipe_Adder.resetVars();
		if (addBlastFurnaceRecipe) {
			Gregtech_Recipe_Adder.inputStack1 = ItemUtils.getSimpleStack(blastFurnaceInput, blastFurnaceInputAmount1);
			Gregtech_Recipe_Adder.inputStack2 = ItemUtils.getSimpleStack(blastFurnaceInput2, blastFurnaceInputAmount2);
			Gregtech_Recipe_Adder.outputStack1 = ItemUtils.getSimpleStack(blastFurnaceOutput,
					blastFurnaceOutputAmount1);
			Gregtech_Recipe_Adder.outputStack2 = ItemUtils.getSimpleStack(blastFurnaceOutput2,
					blastFurnaceOutputAmount2);
			Gregtech_Recipe_Adder.addBlastFurnaceRecipe(Gregtech_Recipe_Adder.inputStack1,
					Gregtech_Recipe_Adder.inputStack2, Gregtech_Recipe_Adder.outputStack1,
					Gregtech_Recipe_Adder.outputStack2, blastFurnaceTemp);
		}
		Gregtech_Recipe_Adder.resetVars();
		if (addSmeltingRecipe) {
			Gregtech_Recipe_Adder.inputStack1 = ItemUtils.getSimpleStack(smeltingInput, smeltingInputAmount1);
			Gregtech_Recipe_Adder.outputStack1 = ItemUtils.getSimpleStack(smeltingOutput, smeltingOutputAmount1);
			Gregtech_Recipe_Adder.addSmeltingRecipe(Gregtech_Recipe_Adder.inputStack1,
					Gregtech_Recipe_Adder.outputStack1);
		}
		Gregtech_Recipe_Adder.resetVars();

	}

	private static void addSmeltingRecipe(final ItemStack input1, final ItemStack output1) {
		GT_ModHandler.addSmeltingRecipe(input1, output1);
	}

	private static void resetVars() {
		Gregtech_Recipe_Adder.inputStack1 = null;
		Gregtech_Recipe_Adder.inputStack2 = null;
		Gregtech_Recipe_Adder.outputStack1 = null;
		Gregtech_Recipe_Adder.outputStack2 = null;
	}

}
