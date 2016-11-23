package gtPlusPlus.core.recipe;

import gregtech.api.enums.*;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.item.ItemStack;

public class RECIPES_LaserEngraver implements IOreRecipeRegistrator {
	public RECIPES_LaserEngraver() {
		OrePrefixes.crafting.add(this);
	}

	@Override
	public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
		if (aOreDictName.equals(OreDictNames.craftingLensBlue.toString())) {			
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foilYttriumBariumCuprate", 1) != null){
				GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.YttriumBariumCuprate, 2L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Circuit_Parts_Wiring_IV.get(1L, new Object[0]), 64, 480);
			}
			else {
				Utils.LOG_INFO("foilYttriumBariumCuprate does not exist within Gregtech, please report this issue to Blood-asp on github.");
			}
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foilVanadiumGallium", 1) != null){
				GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.VanadiumGallium, 2L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Circuit_Parts_Wiring_IV.get(1L, new Object[0]), 64, 480);
			}
			else {
				Utils.LOG_INFO("foilVanadiumGallium does not exist within Gregtech, please report this issue to Blood-asp on github.");
			}
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foilNiobiumTitanium", 1) != null){
				GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.NiobiumTitanium, 2L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Circuit_Parts_Wiring_IV.get(1L, new Object[0]), 64, 480);
			}
			else {
				Utils.LOG_INFO("foilNiobiumTitanium does not exist within Gregtech, please report this issue to Blood-asp on github.");
			}

			
		} else if (aOreDictName.equals(OreDictNames.craftingLensYellow.toString())) {			
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foilOsmium", 1) != null){
				GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmium, 2L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Circuit_Parts_Wiring_LuV.get(1L, new Object[0]), 64, 1024);
			}
			else {
				Utils.LOG_INFO("foilOsmium does not exist within Gregtech, please report this issue to Blood-asp on github.");
			}
			
		} else if (aOreDictName.equals(OreDictNames.craftingLensCyan.toString())) {
		} else if (aOreDictName.equals(OreDictNames.craftingLensRed.toString())) {
		} else if (aOreDictName.equals(OreDictNames.craftingLensGreen.toString())) {
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foilNaquadah", 1) != null){
				GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadah, 2L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Circuit_Parts_Wiring_ZPM.get(1L, new Object[0]), 64, 2000);
			}
			else {
				Utils.LOG_INFO("foilNaquadah does not exist within Gregtech, please report this issue to Blood-asp on github.");
			}

		} else if (aOreDictName.equals(OreDictNames.craftingLensWhite.toString())) {
		}
	}
}
