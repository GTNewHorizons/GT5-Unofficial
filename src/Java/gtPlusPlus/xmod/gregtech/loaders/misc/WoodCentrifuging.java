package gtPlusPlus.xmod.gregtech.loaders.misc;

import java.util.ArrayList;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class WoodCentrifuging {

	private static final ArrayList<ItemStack> aLogData;
	private static final ArrayList<ItemStack> aRubberLogs;
	private static final ArrayList<ItemStack> aRubberLogs2;
	
	static {
		aLogData = OreDictionary.getOres("logWood");
		aRubberLogs = OreDictionary.getOres("logRubber");
		aRubberLogs2 = OreDictionary.getOres("woodRubber");
	}
	
	private static boolean isNormalLog(ItemStack aStack) {
		if (aLogData.contains(aStack) & !isRubberLog(aStack)) {
			return true;
		}
		return false;
	}
	
	private static boolean isRubberLog(ItemStack aStack) {
		if (aRubberLogs.contains(aStack)) {
			return true;
		}
		else if (aRubberLogs2.contains(aStack)) {
			return true;
		}		
		return false;
	}
	

	private static boolean addCentrifugeRecipe(ItemStack aStack) {
		if (isNormalLog(aStack)) {
			return addNormalLogCentrifugeRecipe(aStack);
		}
		else if (isRubberLog(aStack)) {
			return addRubberLogCentrifugeRecipe(aStack);			
		}
		return false;
	}

	private static boolean addNormalLogCentrifugeRecipe(ItemStack aStack) {
		GT_Recipe aFoundRecipe = GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.findRecipe(null, false, gregtech.api.enums.GT_Values.V[1], null, new ItemStack[]{aStack});
		if (aFoundRecipe == null && GT_Values.RA.addCentrifugeRecipe(GT_Utility.copyAmount(1L, aStack), null, null, Materials.Methane.getGas(60L), GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, GT_Values.NI, null, 200, 20)) {
			Logger.INFO("Added methane extraction for "+ItemUtils.getItemName(aStack));	
			return true;
		}
		return false;
	}
	private static boolean addRubberLogCentrifugeRecipe(ItemStack aStack) {
		GT_Recipe aFoundRecipe = GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.findRecipe(null, false, gregtech.api.enums.GT_Values.V[1], null, new ItemStack[]{aStack});
		if (aFoundRecipe == null && GT_Values.RA.addCentrifugeRecipe(GT_Utility.copyAmount(1L, aStack), null, null, Materials.Methane.getGas(60L), ItemList.IC2_Resin.get(1L, new Object[0]), GT_ModHandler.getIC2Item("plantBall", 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wood, 1L), null, null, new int[] { 5000, 3750, 2500, 2500 }, 200, 20)) {
			Logger.INFO("Added rubber plant based methane extraction for "+ItemUtils.getItemName(aStack));	
			return true;
		}
		return false;
	}
	
	public static void processLogsForMethane() {
		//Try use all woods found, fix/add methane extraction.
		if (!aLogData.isEmpty()) {
			Logger.INFO("Fixing Methane output of centrifuged logs.");
			for (ItemStack stack : aLogData) {				
				addCentrifugeRecipe(stack);
			}			
		}		
	}

}
