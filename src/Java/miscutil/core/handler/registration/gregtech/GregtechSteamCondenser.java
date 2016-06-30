package miscutil.core.handler.registration.gregtech;

import miscutil.core.util.Utils;
import miscutil.core.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.core.xmod.gregtech.api.metatileentity.implementations.GregtechMetaCondensor;
import miscutil.core.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityElectricBlastFurnace;
import miscutil.core.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityIronBlastFurnace;

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
		//GT_ModHandler.addCraftingRecipe(GregtechItemList.Condensor_MAX.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_HV, Character.valueOf('W'),OrePrefixes.wireGt04.get(Materials.ElectricalSteel),Character.valueOf('T'), ItemList.Electric_Pump_MV });


		GregtechItemList.Machine_Iron_BlastFurnace.set(new GregtechMetaTileEntityIronBlastFurnace(768, "ironmachine.blastfurnace", "Iron Plated Blast Furnace").getStackForm(1L));
		GregtechItemList.Machine_Electric_BlastFurnace.set(new GregtechMetaTileEntityElectricBlastFurnace(796, "electric.blastfurnace", "Electric Blast Furnace").getStackForm(1L));
		
		//ItemUtils.recipeBuilder(slot_1, slot_2, slot_3, slot_4, slot_5, slot_6, slot_7, slot_8, slot_9, resultItem);
		
		//GT_ModHandler.addCraftingRecipe(ItemList.Machine_Bronze_BlastFurnace.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"PFP", "FwF", "PFP", Character.valueOf('P'), OrePrefixes.plate.get(Materials.Bronze), Character.valueOf('F'), OreDictNames.craftingFurnace});

	}
}
