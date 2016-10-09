package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class GregtechDehydrator
{
	public static void run()
	{
		if (LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Chemical Dehydrators.");
			run1();
		}

	}

	private static void run1()
	{

		/*

		 public GT_MetaTileEntity_BasicMachine_GT_Recipe(
		  int aID, String aName, String aNameRegional, int aTier,
		  String aDescription, 
		  GT_Recipe_Map aRecipes,
		  int aInputSlots, int aOutputSlots,
		  int aTankCapacity,
		  int aGUIParameterA, int aGUIParameterB,
		  String aGUIName, String aSound,
		  boolean aSharedTank, boolean aRequiresFluidForFiltering,
		  int aSpecialEffect,
		  String aOverlays,
		  Object[] aRecipe) {	  

		 */

		GregtechItemList.GT_Dehydrator_EV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				813, "advancedmachine.dehydrator.tier.01", "Chemical Dehydrator I", 4,
				"Remind Alkalus to add something here."+CORE.GT_Tooltip,
				Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes,
				2, 9,
				10000,
				2, 5, 
				"Dehydrator.png", "",
				false, false,
				0,
				"UNBOXINATOR",
				null).getStackForm(1L));
		GregtechItemList.GT_Dehydrator_IV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				814, "advancedmachine.dehydrator.tier.02", "Chemical Dehydrator II", 5,
				"Remind Alkalus to add something here."+CORE.GT_Tooltip,
				Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes,
				2, 9,
				10000,
				2, 5, 
				"Dehydrator.png", "",
				false, false,
				0,
				"UNBOXINATOR",
				null).getStackForm(1L));
		GregtechItemList.GT_Dehydrator_LuV.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				815, "advancedmachine.dehydrator.tier.03", "Chemical Dehydrator III", 6,
				"Remind Alkalus to add something here."+CORE.GT_Tooltip,
				Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes,
				2, 9,
				10000,
				2, 5, 
				"Dehydrator.png", "",
				false, false,
				0,
				"UNBOXINATOR",
				null).getStackForm(1L));
		GregtechItemList.GT_Dehydrator_ZPM.set(new GT_MetaTileEntity_BasicMachine_GT_Recipe(
				816, "advancedmachine.dehydrator.tier.04", "Chemical Dehydrator IV", 7,
				"Remind Alkalus to add something here."+CORE.GT_Tooltip,
				Recipe_GT.Gregtech_Recipe_Map.sChemicalDehydratorRecipes,
				2, 9,
				10000,
				2, 5, 
				"Dehydrator.png", "",
				false, false,
				0,
				"UNBOXINATOR",
				null).getStackForm(1L));

		//GregtechItemList.GT_Dehydrator_EV.set(new GregtechMetaTileEntitySolarGenerator(813, "dehydrator.tier.01", "Extreme Voltage Chemical Dehydrator", 4).getStackForm(1L));
		//GregtechItemList..set(new GregtechMetaTileEntitySolarGenerator(814, "dehydrator.tier.02", "Insane Voltage Chemical Dehydrator", 5).getStackForm(1L));
		//GregtechItemList..set(new GregtechMetaTileEntitySolarGenerator(815, "dehydrator.tier.03", "Ludicrous Voltage Chemical Dehydrator", 6).getStackForm(1L));
		//GregtechItemList..set(new GregtechMetaTileEntitySolarGenerator(816, "dehydrator.tier.04", "ZPM Voltage Chemical Dehydrator", 7).getStackForm(1L));




	}
}
