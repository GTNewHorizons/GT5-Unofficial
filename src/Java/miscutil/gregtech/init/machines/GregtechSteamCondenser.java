package miscutil.gregtech.init.machines;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import miscutil.core.util.Utils;
import miscutil.gregtech.enums.GregtechItemList;
import miscutil.gregtech.metatileentity.implementations.GregtechMetaCondensor;

public class GregtechSteamCondenser
{

	

	public static void run()
	{
		if (miscutil.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("MiscUtils: Gregtech5u Content | Registering Steam Condensor.");
			run1();
		}

	}

	private static void run1()
	{
		//Steam Condensors
		GregtechItemList.Condensor_MAX.set(new GregtechMetaCondensor(769, "steamcondensor.01.tier.single", "Steam Condensor").getStackForm(1L));
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Condensor_MAX.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_HV, Character.valueOf('W'),OrePrefixes.wireGt04.get(Materials.ElectricalSteel),Character.valueOf('T'), ItemList.Electric_Pump_MV });
		/*Steam Condensors
		GregtechItemList.Condensor_MAX.set(new GregtechMetaCondensorII(780, "steamcondensor.01.tier.single", "Steam CondensorII").getStackForm(1L));
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Condensor_MAX.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_HV, Character.valueOf('W'),OrePrefixes.wireGt04.get(Materials.Nickel),Character.valueOf('T'), ItemList.Pump_MV });
		*/
	
	}
}
