package gtPlusPlus.xmod.gregtech.common.helpers;

import java.util.HashMap;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.data.AES;
import gtPlusPlus.xmod.gregtech.api.enums.CustomOrePrefix;
import net.minecraft.item.ItemStack;

public class FlotationRecipeHandler {

	private static HashMap<String, Material> sMaterialMap = new HashMap<String, Material>();
	private static HashMap<String, ItemStack> sMilledMap = new HashMap<String, ItemStack>();
	private static final AES sEncodingHandler = new AES();
	
	public static boolean registerOreType(Material aMaterial) {	
		String aMaterialKey = sEncodingHandler.encode(aMaterial.getUnlocalizedName());
		if (sMaterialMap.containsKey(aMaterialKey)) {
			CORE.crash("Tried to register a Flotation material to an ID already in use. ID: "+aMaterialKey);
			return false;
		}
		else {
			sMaterialMap.put(aMaterialKey, aMaterial);
			sMilledMap.put(aMaterialKey, aMaterial.getMilled(1));
		}		
		return true;
	}
	
	public static int getHashForMaterial(Material aMaterial) {
		return getMaterialsID(aMaterial).hashCode();
	}
	
	public static String getMaterialsID(Material aMaterial) {
		for (String aKey : sMaterialMap.keySet()) {
			if (sMaterialMap.get(aKey).equals(aMaterial)) {
				return aKey;
			}
		}		
		return "BAD_MATERIAL_ID";
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
			if (CustomOrePrefix.milled.get().contains(aStack)) {
				return aStack;
			}
		}		
		return null;
	}
	
	public static AES getEncoder() {
		return sEncodingHandler;
	}
	
}
