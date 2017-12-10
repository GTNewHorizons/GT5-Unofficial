package gtPlusPlus.core.recipe;

import gregtech.api.enums.*;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.item.ItemStack;

public class RECIPES_LaserEngraver implements IOreRecipeRegistrator {
	public RECIPES_LaserEngraver() {
		OrePrefixes.crafting.add(this);
	}

	@Override
	public void registerOre(final OrePrefixes aPrefix, final Materials aMaterial, final String aOreDictName, final String aModName, final ItemStack aStack) {
		if (aOreDictName.equals(OreDictNames.craftingLensBlue.toString())) {

			if (CORE.ConfigSwitches.enableCustomCircuits && !CORE.GTNH){
				if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foilYttriumBariumCuprate", 1) != null){
					GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.YttriumBariumCuprate, 2L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Circuit_Parts_Wiring_IV.get(1L, new Object[0]), 64, 480);
				}
				else {
					Utils.LOG_INFO("foilYttriumBariumCuprate does not exist within Gregtech, please report this issue to Blood-asp on github.");
					Utils.LOG_INFO("This material item can be re-enabled within the gregtech configuration files, If you wish to fix this yourself.");
				}
				if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foilVanadiumGallium", 1) != null){
					GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.VanadiumGallium, 2L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Circuit_Parts_Wiring_IV.get(1L, new Object[0]), 64, 480);
				}
				else {
					Utils.LOG_INFO("foilVanadiumGallium does not exist within Gregtech, please report this issue to Blood-asp on github.");
					Utils.LOG_INFO("This material item can be re-enabled within the gregtech configuration files, If you wish to fix this yourself.");
				}
				if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foilNiobiumTitanium", 1) != null){
					GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.NiobiumTitanium, 2L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Circuit_Parts_Wiring_IV.get(1L, new Object[0]), 64, 480);
				}
				else {
					Utils.LOG_INFO("foilNiobiumTitanium does not exist within Gregtech, please report this issue to Blood-asp on github.");
					Utils.LOG_INFO("This material item can be re-enabled within the gregtech configuration files, If you wish to fix this yourself.");
				}
			}


		} else if (aOreDictName.equals(OreDictNames.craftingLensYellow.toString())) {
			if (CORE.ConfigSwitches.enableCustomCircuits && !CORE.GTNH){
				if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foilOsmium", 1) != null){
					GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmium, 2L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Circuit_Parts_Wiring_LuV.get(1L, new Object[0]), 64, 1024);
				}
				else {
					Utils.LOG_INFO("foilOsmium does not exist within Gregtech, please report this issue to Blood-asp on github.");
					Utils.LOG_INFO("This material item can be re-enabled within the gregtech configuration files, If you wish to fix this yourself.");
				}
			}

		} else if (aOreDictName.equals(OreDictNames.craftingLensCyan.toString())) {
		} else if (aOreDictName.equals(OreDictNames.craftingLensRed.toString())) {
		} else if (aOreDictName.equals(OreDictNames.craftingLensGreen.toString())) {
			if (CORE.ConfigSwitches.enableCustomCircuits && !CORE.GTNH){
				if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foilNaquadah", 1) != null){
					GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadah, 2L), GT_Utility.copyAmount(0L, new Object[]{aStack}), GregtechItemList.Circuit_Parts_Wiring_ZPM.get(1L, new Object[0]), 64, 2000);
				}
				else {
					Utils.LOG_INFO("foilNaquadah does not exist within Gregtech, please report this issue to Blood-asp on github.");
					Utils.LOG_INFO("This material item can be re-enabled within the gregtech configuration files, If you wish to fix this yourself.");
				}
			}
		} else if (aOreDictName.equals(OreDictNames.craftingLensWhite.toString())) {
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateLithium", 1) != null){
				GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Lithium, 2L), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemUtils.getItemStackOfAmountFromOreDict("plateDoubleLithium7", 1), 4*60*20, 2000);
			}
			else {
				Utils.LOG_INFO("plateLithium does not exist within Gregtech, please report this issue to Blood-asp on github.");
				Utils.LOG_INFO("This material item can be re-enabled within the gregtech configuration files, If you wish to fix this yourself.");
			}
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustLithium", 1) != null){
				GT_Values.RA.addLaserEngraverRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 3L), GT_Utility.copyAmount(0L, new Object[]{aStack}), ItemUtils.getItemStackOfAmountFromOreDict("dustLithium7", 1), 2*60*20, 2000);
			}
			else {
				Utils.LOG_INFO("dustLithium does not exist within Gregtech, please report this issue to Blood-asp on github.");
				Utils.LOG_INFO("This material item can be re-enabled within the gregtech configuration files, If you wish to fix this yourself.");
			}

		}

		else if (aOreDictName.equals(OreDictNames.craftingLensLime.toString())) {
			//Coil Wires
			ItemStack coilWire1 = ItemUtils.getItemStackWithMeta(true, "miscutils:itemDehydratorCoilWire", "coilWire1", 0, 1);
			ItemStack coilWire2 = ItemUtils.getItemStackWithMeta(true, "miscutils:itemDehydratorCoilWire:1", "coilWire2", 1, 1);
			ItemStack coilWire3 = ItemUtils.getItemStackWithMeta(true, "miscutils:itemDehydratorCoilWire:2", "coilWire3", 2, 1);
			ItemStack coilWire4 = ItemUtils.getItemStackWithMeta(true, "miscutils:itemDehydratorCoilWire:3", "coilWire4", 3, 1);

			//Simple Life
			String wire = "wireGt02";

			//Wires to Laser
			ItemStack wireT1a = ItemUtils.getItemStackOfAmountFromOreDict(wire+"Aluminium", 1);
			ItemStack wireT1b = ItemUtils.getItemStackOfAmountFromOreDict(wire+"Nichrome", 1);
			ItemStack wireT2a = ItemUtils.getItemStackOfAmountFromOreDict(wire+"Osmium", 1);
			ItemStack wireT2b = ItemUtils.getItemStackOfAmountFromOreDict(wire+"Platinum", 1);
			ItemStack wireT3a = ItemUtils.getItemStackOfAmountFromOreDict(wire+"VanadiumGallium", 1);
			ItemStack wireT3b = ItemUtils.getItemStackOfAmountFromOreDict(wire+"YttriumBariumCuprate", 1);
			ItemStack wireT3c = ItemUtils.getItemStackOfAmountFromOreDict(wire+"NiobiumTitanium", 1);
			ItemStack wireT4a = ItemUtils.getItemStackOfAmountFromOreDict(wire+"Naquadah", 1);

			//T1
			GT_Values.RA.addLaserEngraverRecipe(wireT1a, GT_Utility.copyAmount(0L, new Object[]{aStack}), coilWire1, 10*20, 500);
			GT_Values.RA.addLaserEngraverRecipe(wireT1b, GT_Utility.copyAmount(0L, new Object[]{aStack}), coilWire1, 10*20, 500);
			//T2
			GT_Values.RA.addLaserEngraverRecipe(wireT2a, GT_Utility.copyAmount(0L, new Object[]{aStack}), coilWire2, 20*20, 2000);
			GT_Values.RA.addLaserEngraverRecipe(wireT2b, GT_Utility.copyAmount(0L, new Object[]{aStack}), coilWire2, 20*20, 2000);
			//T3
			GT_Values.RA.addLaserEngraverRecipe(wireT3a, GT_Utility.copyAmount(0L, new Object[]{aStack}), coilWire3, 30*20, 8000);
			GT_Values.RA.addLaserEngraverRecipe(wireT3b, GT_Utility.copyAmount(0L, new Object[]{aStack}), coilWire3, 30*20, 8000);
			GT_Values.RA.addLaserEngraverRecipe(wireT3c, GT_Utility.copyAmount(0L, new Object[]{aStack}), coilWire3, 30*20, 8000);
			//T4
			GT_Values.RA.addLaserEngraverRecipe(wireT4a, GT_Utility.copyAmount(0L, new Object[]{aStack}), coilWire4, 40*20, 32000);


		}


	}
}
