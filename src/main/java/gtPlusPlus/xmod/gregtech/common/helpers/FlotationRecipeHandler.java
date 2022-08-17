package gtPlusPlus.xmod.gregtech.common.helpers;

import java.util.HashMap;

import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.sys.Log;
import gtPlusPlus.xmod.gregtech.api.enums.CustomOrePrefix;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class FlotationRecipeHandler {

	private static final HashMap<String, Material> sMaterialMap = new HashMap<String, Material>();
	private static final HashMap<String, ItemStack> sMilledMap = new HashMap<String, ItemStack>();
	
	public static boolean registerOreType(Material aMaterial) {	
		String aMaterialKey = aMaterial.getUnlocalizedName();
		if (sMaterialMap.containsKey(aMaterialKey)) {
			Log.warn("Tried to register a Flotation material already in use. Material: "+aMaterialKey);
			return false;
		}
		else {
			sMaterialMap.put(aMaterialKey, aMaterial);
			sMilledMap.put(aMaterialKey, aMaterial.getMilled(1));
		}		
		return true;
	}
	
	public static Material getMaterialOfMilledProduct(ItemStack aMilled) {
		for (String aKey : sMilledMap.keySet()) {
			ItemStack aTempMilledStack = sMilledMap.get(aKey);
			if (GT_Utility.areStacksEqual(aTempMilledStack, aMilled, true)) {
				return sMaterialMap.get(aKey);
			}
		}		
		return null;
	}
	
	public static ItemStack findMilledStack(GT_Recipe aRecipe) {
		if (aRecipe == null || aRecipe.mInputs == null || aRecipe.mInputs.length <= 0) {
			return null;
		}
		return findMilledStack(aRecipe.mInputs);
	}
	
	public static ItemStack findMilledStack(ItemStack[] aInputs) {
		if (aInputs == null || aInputs.length <= 0) {
			return null;
		}
		for (ItemStack aStack : aInputs) {
			for (int oredictID : OreDictionary.getOreIDs(aStack)) {
				String oredict = OreDictionary.getOreName(oredictID);
				if (oredict.startsWith(CustomOrePrefix.milled.toString())) {
					return aStack;
				}
			}
		}		
		return null;
	}
	
}
