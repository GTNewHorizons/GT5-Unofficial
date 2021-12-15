package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOreDictNames;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.GT_MetaTileEntity_RfConvertor;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.creative.GregtechMetaCreativeEnergyBuffer;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GregtechMetaEnergyBuffer;

public class GregtechEnergyBuffer
{

	//Misc Items
	//public static Item itemBufferCore;

	public static void run()
	{
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech){
			Logger.INFO("Gregtech5u Content | Registering Energy Buffer Blocks.");
			run1();
		}
	}

	private static void run1()
	{

		//itemBufferCore = new Item().setUnlocalizedName("itemBufferCore").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemBufferCore");

		//Registry
		//GameRegistry.registerItem(itemBufferCore, "itemBufferCore");
		//LanguageRegistry.addName(itemBufferCore, "Buffer Core");
		//OreDictionary.registerOre("itemBufferCore", itemBufferCore);


		//Energy Buffers
		GregtechItemList.Energy_Buffer_1by1_ULV.set(new GregtechMetaEnergyBuffer(770, "energybuffer.tier.00", "Ultra Low Voltage Energy Buffer", 0, "", 1).getStackForm(1L));
		GregtechItemList.Energy_Buffer_1by1_LV.set(new GregtechMetaEnergyBuffer(771, "energybuffer.tier.01", "Low Voltage Energy Buffer", 1, "", 1).getStackForm(1L));
		GregtechItemList.Energy_Buffer_1by1_MV.set(new GregtechMetaEnergyBuffer(772, "energybuffer.tier.02", "Medium Voltage Energy Buffer", 2, "", 1).getStackForm(1L));
		GregtechItemList.Energy_Buffer_1by1_HV.set(new GregtechMetaEnergyBuffer(773, "energybuffer.tier.03", "High Voltage Energy Buffer", 3, "", 1).getStackForm(1L));
		GregtechItemList.Energy_Buffer_1by1_EV.set(new GregtechMetaEnergyBuffer(774, "energybuffer.tier.04", "Extreme Voltage Energy Buffer", 4, "", 1).getStackForm(1L));
		GregtechItemList.Energy_Buffer_1by1_IV.set(new GregtechMetaEnergyBuffer(775, "energybuffer.tier.05", "Insane Voltage Energy Buffer", 5, "", 1).getStackForm(1L));
		GregtechItemList.Energy_Buffer_1by1_LuV.set(new GregtechMetaEnergyBuffer(776, "energybuffer.tier.06", "Ludicrous Voltage Energy Buffer", 6, "", 1).getStackForm(1L));
		GregtechItemList.Energy_Buffer_1by1_ZPM.set(new GregtechMetaEnergyBuffer(777, "energybuffer.tier.07", "ZPM Voltage Energy Buffer", 7, "", 1).getStackForm(1L));
		GregtechItemList.Energy_Buffer_1by1_UV.set(new GregtechMetaEnergyBuffer(778, "energybuffer.tier.08", "Ultimate Voltage Energy Buffer", 8, "", 1).getStackForm(1L));
		GregtechItemList.Energy_Buffer_1by1_MAX.set(new GregtechMetaEnergyBuffer(779, "energybuffer.tier.09", "MAX Voltage Energy Buffer", 9, "", 1).getStackForm(1L));
		// Creative Buffer Has Special ID
		GregtechItemList.Energy_Buffer_CREATIVE
		.set(new GregtechMetaCreativeEnergyBuffer(750,
				"energybuffer.tier.xx",
				"512V Creative Energy Buffer", 3, "", 0)
				.getStackForm(1L));

		if (LoadedMods.CoFHCore && CORE.ConfigSwitches.enableMachine_RF_Convetor) {
			// RF Convertor Buffer Has Special ID
			GregtechItemList.Energy_Buffer_RF_Convertor
			.set(new GT_MetaTileEntity_RfConvertor(31022,
					"energybuffer.rf.tier.01",
					"RF Energy Convertor", 3, "", 0)
					.getStackForm(1L));
		}

		GT_ModHandler.addCraftingRecipe(GregtechItemList.Energy_Buffer_1by1_ULV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_ULV, Character.valueOf('W'), OrePrefixes.wireGt08.get(Materials.Lead), Character.valueOf('T'), GregtechOreDictNames.buffer_core });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Energy_Buffer_1by1_LV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_LV, Character.valueOf('W'), OrePrefixes.wireGt08.get(Materials.Tin), Character.valueOf('T'), GregtechOreDictNames.buffer_core });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Energy_Buffer_1by1_MV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_MV, Character.valueOf('W'), OrePrefixes.wireGt08.get(Materials.AnyCopper), Character.valueOf('T'), GregtechOreDictNames.buffer_core });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Energy_Buffer_1by1_HV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_HV, Character.valueOf('W'), OrePrefixes.wireGt08.get(Materials.Gold), Character.valueOf('T'), GregtechOreDictNames.buffer_core });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Energy_Buffer_1by1_EV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_EV, Character.valueOf('W'), OrePrefixes.wireGt08.get(Materials.Aluminium), Character.valueOf('T'), GregtechOreDictNames.buffer_core });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Energy_Buffer_1by1_IV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_IV, Character.valueOf('W'), OrePrefixes.wireGt08.get(Materials.Tungsten), Character.valueOf('T'), GregtechOreDictNames.buffer_core });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Energy_Buffer_1by1_LuV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_LuV, Character.valueOf('W'), OrePrefixes.wireGt08.get(Materials.Osmium), Character.valueOf('T'), GregtechOreDictNames.buffer_core });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Energy_Buffer_1by1_ZPM.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_ZPM, Character.valueOf('W'), OrePrefixes.wireGt08.get(Materials.Osmium), Character.valueOf('T'), GregtechOreDictNames.buffer_core });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Energy_Buffer_1by1_UV.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_UV, Character.valueOf('W'), OrePrefixes.wireGt08.get(Materials.Osmium), Character.valueOf('T'), GregtechOreDictNames.buffer_core });
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Energy_Buffer_1by1_MAX.get(1L, new Object[0]), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, new Object[] { "WTW", "WMW", Character.valueOf('M'), ItemList.Hull_MAX, Character.valueOf('W'), OrePrefixes.wireGt08.get(Materials.Superconductor), Character.valueOf('T'), GregtechOreDictNames.buffer_core });
		/*GT_ModHandler.addCraftingRecipe(
				GregtechItemList.Energy_Buffer_1by1_MAX.get(1L, new Object[0]),
				GT_ModHandler.RecipeBits.DISMANTLEABLE
						| GT_ModHandler.RecipeBits.NOT_REMOVABLE
						| GT_ModHandler.RecipeBits.REVERSIBLE
						| GT_ModHandler.RecipeBits.BUFFERED, new Object[] {
						"WTW", "WMW", Character.valueOf('M'),
						ItemList.Hull_MAX, Character.valueOf('W'),
						OrePrefixes.wireGt08.get(Materials.Superconductor),
						Character.valueOf('T'), GregtechOreDictNames.buffer_core });*/




	}
}
