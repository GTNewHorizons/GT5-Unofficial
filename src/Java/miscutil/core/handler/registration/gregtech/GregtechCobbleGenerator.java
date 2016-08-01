package miscutil.core.handler.registration.gregtech;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import miscutil.core.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.core.xmod.gregtech.api.metatileentity.implementations.base.GregtechSteelBoiler;
import cpw.mods.fml.common.FMLLog;

public class GregtechCobbleGenerator
{
	public static void run()
	{
		if (miscutil.core.lib.LoadedMods.Gregtech){
			FMLLog.info("Registering Cobblestone Powered Engines.");
			run1();
		}
		
	}

	private static void run1()
	{
		//Cobble Generators
		// ItemList.Machine_Steel_Boiler.set(new GT_MetaTileEntity_Boiler_Steel(101, "boiler.steel", "High Pressure Coal Boiler").getStackForm(1L));
		// GT_ModHandler.addCraftingRecipe(ItemList.Machine_Steel_Boiler.get(1L, new Object[0]), GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "PPP", "P P", "BFB", Character.valueOf('F'), OreDictNames.craftingFurnace, Character.valueOf('P'), OrePrefixes.plate.get(Materials.Steel), Character.valueOf('B'), new ItemStack(Blocks.brick_block, 1) });

		GregtechItemList.Cobble_Generator_ULV.set(new GregtechSteelBoiler(760, "CobGen.01.tier.00", "Ultra Low Voltage Cobblestone Generator", 0, "You're shit.").getStackForm(1L));
		GregtechItemList.Cobble_Generator_LV.set(new GregtechSteelBoiler(761, "CobGen.01.tier.01", "Low Voltage Cobblestone Generator", 1, "Still Pretty garbage, bro.").getStackForm(1L));
		GregtechItemList.Cobble_Generator_MV.set(new GregtechSteelBoiler(762, "CobGen.01.tier.02", "Medium Voltage Cobblestone Generator", 2, "Testy Test.").getStackForm(1L));
		GregtechItemList.Cobble_Generator_HV.set(new GregtechSteelBoiler(763, "CobGen.01.tier.03", "High Voltage Cobblestone Generator", 3, "").getStackForm(1L));
		GregtechItemList.Cobble_Generator_EV.set(new GregtechSteelBoiler(764, "CobGen.01.tier.04", "Extreme Voltage Cobblestone Generator", 4, "").getStackForm(1L));
		GregtechItemList.Cobble_Generator_IV.set(new GregtechSteelBoiler(765, "CobGen.01.tier.05", "Insane Voltage Cobblestone Generator", 5, "").getStackForm(1L));
		GregtechItemList.Cobble_Generator_LuV.set(new GregtechSteelBoiler(766, "CobGen.01.tier.06", "Ludicrous Voltage Cobblestone Generator", 6, "").getStackForm(1L));
		GregtechItemList.Cobble_Generator_ZPM.set(new GregtechSteelBoiler(767, "CobGen.01.tier.07", "ZPM Voltage Cobblestone Generator", 7, "").getStackForm(1L));
		GregtechItemList.Cobble_Generator_UV.set(new GregtechSteelBoiler(768, "CobGen.01.tier.08", "Ultimate Voltage Cobblestone Generator", 8, "").getStackForm(1L));
		GregtechItemList.Cobble_Generator_MAX.set(new GregtechSteelBoiler(769, "CobGen.01.tier.09", "MAX Voltage Cobblestone Generator", 9, "").getStackForm(1L));

		GT_ModHandler.addCraftingRecipe(GregtechItemList.Cobble_Generator_ULV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_ULV, Character.valueOf('W'), OrePrefixes.wireGt04.get(Materials.Lead), Character.valueOf('T'), OreDictNames.craftingChest });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Cobble_Generator_LV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_LV, Character.valueOf('W'), OrePrefixes.wireGt04.get(Materials.Tin), Character.valueOf('T'), OreDictNames.craftingChest });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Cobble_Generator_MV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_MV, Character.valueOf('W'), OrePrefixes.wireGt04.get(Materials.AnyCopper), Character.valueOf('T'), OreDictNames.craftingChest });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Cobble_Generator_HV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_HV, Character.valueOf('W'), OrePrefixes.wireGt04.get(Materials.Gold), Character.valueOf('T'), OreDictNames.craftingChest });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Cobble_Generator_EV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_EV, Character.valueOf('W'), OrePrefixes.wireGt04.get(Materials.Aluminium), Character.valueOf('T'), OreDictNames.craftingChest });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Cobble_Generator_IV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_IV, Character.valueOf('W'), OrePrefixes.wireGt04.get(Materials.Tungsten), Character.valueOf('T'), OreDictNames.craftingChest });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Cobble_Generator_LuV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_LuV, Character.valueOf('W'), OrePrefixes.wireGt04.get(Materials.Osmium), Character.valueOf('T'), OreDictNames.craftingChest });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Cobble_Generator_ZPM.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_ZPM, Character.valueOf('W'), OrePrefixes.wireGt04.get(Materials.Osmium), Character.valueOf('T'), OreDictNames.craftingChest });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Cobble_Generator_UV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_UV, Character.valueOf('W'), OrePrefixes.wireGt04.get(Materials.Osmium), Character.valueOf('T'), OreDictNames.craftingChest });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Cobble_Generator_MAX.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_MAX, Character.valueOf('W'), OrePrefixes.wireGt04.get(Materials.Superconductor), Character.valueOf('T'), OreDictNames.craftingChest });
	}
}
