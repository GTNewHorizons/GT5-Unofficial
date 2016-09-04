package miscutil.xmod.gregtech.registration.gregtech;

import miscutil.core.lib.CORE;
import miscutil.core.lib.LoadedMods;
import miscutil.core.util.Utils;
import miscutil.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.xmod.gregtech.api.metatileentity.implementations.base.GT_MTE_BasicMachine_Custom_Recipe;
import miscutil.xmod.gregtech.api.util.GregtechRecipe;

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

		GregtechItemList.GT_Dehydrator_EV.set(new GT_MTE_BasicMachine_Custom_Recipe(
				813, "advancedmachine.dehydrator.tier.01", "Chemical Dehydrator I", 4,
				"Remind Alkalus to add something here."+System.getProperty("line.separator")+CORE.GT_Tooltip,
				GregtechRecipe.Gregtech_Recipe_Map.sChemicalDehydratorRecipes,
				2, 9,
				10000,
				2, 5, 
				"Dehydrator.png", "",
				false, false,
				0,
				"UNBOXINATOR",
				null).getStackForm(1L));
		GregtechItemList.GT_Dehydrator_IV.set(new GT_MTE_BasicMachine_Custom_Recipe(
				814, "advancedmachine.dehydrator.tier.02", "Chemical Dehydrator II", 5,
				"Remind Alkalus to add something here."+System.getProperty("line.separator")+CORE.GT_Tooltip,
				GregtechRecipe.Gregtech_Recipe_Map.sChemicalDehydratorRecipes,
				2, 9,
				10000,
				2, 5, 
				"Dehydrator.png", "",
				false, false,
				0,
				"UNBOXINATOR",
				null).getStackForm(1L));
		GregtechItemList.GT_Dehydrator_LuV.set(new GT_MTE_BasicMachine_Custom_Recipe(
				815, "advancedmachine.dehydrator.tier.03", "Chemical Dehydrator III", 6,
				"Remind Alkalus to add something here."+System.getProperty("line.separator")+CORE.GT_Tooltip,
				GregtechRecipe.Gregtech_Recipe_Map.sChemicalDehydratorRecipes,
				2, 9,
				10000,
				2, 5, 
				"Dehydrator.png", "",
				false, false,
				0,
				"UNBOXINATOR",
				null).getStackForm(1L));
		GregtechItemList.GT_Dehydrator_ZPM.set(new GT_MTE_BasicMachine_Custom_Recipe(
				816, "advancedmachine.dehydrator.tier.04", "Chemical Dehydrator IV", 7,
				"Remind Alkalus to add something here."+System.getProperty("line.separator")+CORE.GT_Tooltip,
				GregtechRecipe.Gregtech_Recipe_Map.sChemicalDehydratorRecipes,
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
