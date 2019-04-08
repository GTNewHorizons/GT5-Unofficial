package gtPlusPlus.core.util.minecraft;

import static gregtech.api.GregTech_API.sBioHazmatList;
import static gregtech.api.GregTech_API.sElectroHazmatList;
import static gregtech.api.GregTech_API.sFrostHazmatList;
import static gregtech.api.GregTech_API.sGasHazmatList;
import static gregtech.api.GregTech_API.sHeatHazmatList;
import static gregtech.api.GregTech_API.sRadioHazmatList;

import gregtech.api.objects.GT_HashSet;
import gtPlusPlus.api.objects.data.AutoMap;
import net.minecraft.item.ItemStack;

public class HazmatUtils {

	/**
	 * Registers the {@link ItemStack} to all types of protection.
	 * Provides full hazmat protection. Frost, Fire, Bio, Gas, Radioaton & Electricity.
	 * @param aStack - The Armour to provide protection.
	 * @return - Did we register this ItemStack properly?
	 */
	public boolean addProtection(ItemStack aStack) {		
		AutoMap<Boolean> aAdded = new AutoMap<Boolean>();
		aAdded.put(addProtection_Frost(aStack));
		aAdded.put(addProtection_Fire(aStack));
		aAdded.put(addProtection_Biohazard(aStack));
		aAdded.put(addProtection_Gas(aStack));
		aAdded.put(addProtection_Radiation(aStack));
		aAdded.put(addProtection_Electricty(aStack));		
		for (boolean b : aAdded) {
			if (!b) {
				return false;
			}
		}
		return true;		
	}

	public static boolean addProtection_Frost(ItemStack aStack) {
		return addProtection_Generic(sFrostHazmatList, aStack);
	}
	public static boolean addProtection_Fire(ItemStack aStack) {
		return addProtection_Generic(sHeatHazmatList, aStack);
	}
	public static boolean addProtection_Biohazard(ItemStack aStack) {
		return addProtection_Generic(sBioHazmatList, aStack);
	}
	public static boolean addProtection_Gas(ItemStack aStack) {
		return addProtection_Generic(sGasHazmatList, aStack);
	}
	public static boolean addProtection_Radiation(ItemStack aStack) {
		return addProtection_Generic(sRadioHazmatList, aStack);
	}
	public static boolean addProtection_Electricty(ItemStack aStack) {
		return addProtection_Generic(sElectroHazmatList, aStack);
	}
	
	private static boolean addProtection_Generic(GT_HashSet aSet, ItemStack aStack) {
		int aMapSize = aSet.size();
		aSet.add(aStack);
		return aMapSize < aSet.size();
	}
	
}
