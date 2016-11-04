package gtPlusPlus.core.item;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.common.compat.COMPAT_Baubles;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.*;
import gtPlusPlus.core.item.base.dusts.decimal.BaseItemCentidust;
import gtPlusPlus.core.item.base.dusts.decimal.BaseItemDecidust;
import gtPlusPlus.core.item.base.foods.BaseItemFood;
import gtPlusPlus.core.item.base.foods.BaseItemHotFood;
import gtPlusPlus.core.item.base.ingots.BaseItemIngot;
import gtPlusPlus.core.item.base.plates.BaseItemPlate;
import gtPlusPlus.core.item.effects.RarityUncommon;
import gtPlusPlus.core.item.general.*;
import gtPlusPlus.core.item.general.fuelrods.FuelRod_Base;
import gtPlusPlus.core.item.init.ItemsFoods;
import gtPlusPlus.core.item.tool.misc.SandstoneHammer;
import gtPlusPlus.core.item.tool.staballoy.*;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.CORE.configSwitches;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.*;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.debug.DEBUG_INIT;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.EnumHelper;

public final class ModItems {

	public static ToolMaterial		STABALLOY	= EnumHelper.addToolMaterial("Staballoy", 3, 2500, 7, 1.0F, 18);

	public static Item				AAA_Broken;

	public static Item				itemDebugShapeSpawner;

	public static Item				itemBaseSpawnEgg;

	// EnderIO
	public static Item				itemPlateSoularium;
	public static Item				itemPlateRedstoneAlloy;
	public static Item				itemPlateElectricalSteel;
	public static Item				itemPlatePulsatingIron;
	public static Item				itemPlateEnergeticAlloy;
	public static Item				itemPlateVibrantAlloy;
	public static Item				itemPlateConductiveIron;
	public static Item				itemPlateDarkSteel;
	// Big Reactors
	public static Item				itemPlateBlutonium;
	public static Item				itemPlateCyanite;
	public static Item				itemPlateLudicrite;
	// Thaumcraft
	public static Item				itemPlateVoidMetal;
	// Pneumaticraft
	public static Item				itemPlateCompressedIron;
	// SimplyJetpacks
	public static Item				itemPlateEnrichedSoularium;
	// rfTools
	public static Item				itemPlateDimensionShard;
	// Staballoy
	public static Item				itemStaballoyPickaxe;
	public static Item				itemStaballoyAxe;
	// Tools
	public static Item				itemSandstoneHammer;
	// Machine Related
	public static Item				itemBufferCore0;
	// Material related
	public static Item				itemStickyRubber;
	public static Item				itemIngotBatteryAlloy;
	public static Item				itemPlateBatteryAlloy;
	public static Item				itemHeliumBlob;
	public static Item				itemPLACEHOLDER_Circuit;

	public static Item				FuelRod_Empty;
	public static Item				FuelRod_Thorium;
	public static Item				FuelRod_Uranium;
	public static Item				FuelRod_Plutonium;

	public static Item				itemBedLocator_Base;
	public static Item				itemBaseItemWithCharge;

	public static Item				itemIngotRaisinBread;
	public static Item				itemHotIngotRaisinBread;

	public static ItemFood			itemFoodRaisinToast;
	public static BaseItemHotFood	itemHotFoodRaisinToast;
	public static BaseItemFood		itemFoodCurriedSausages;
	public static BaseItemHotFood	itemHotFoodCurriedSausages;

	public static Item				RfEuBattery;
	public static Item				itemPersonalCloakingDevice;
	public static Item				itemPersonalCloakingDeviceCharged;
	public static Item				itemPersonalHealingDevice;

	public static MultiPickaxeBase	MP_GTMATERIAL;
	public static MultiSpadeBase	MS_GTMATERIAL;

	public static BaseItemDecidust	itemBaseDecidust;
	public static BaseItemCentidust	itemBaseCentidust;

	public static ItemStack			FluidCell;

	public static BaseItemBackpack	backpack_Red;
	public static BaseItemBackpack	backpack_Green;
	public static BaseItemBackpack	backpack_Blue;
	public static BaseItemBackpack	backpack_Yellow;
	public static BaseItemBackpack	backpack_Purple;
	public static BaseItemBackpack	backpack_Cyan;
	public static BaseItemBackpack	backpack_Maroon;
	public static BaseItemBackpack	backpack_Olive;
	public static BaseItemBackpack	backpack_DarkGreen;
	public static BaseItemBackpack	backpack_DarkPurple;
	public static BaseItemBackpack	backpack_Teal;
	public static BaseItemBackpack	backpack_Navy;
	public static BaseItemBackpack	backpack_Silver;
	public static BaseItemBackpack	backpack_Gray;
	public static BaseItemBackpack	backpack_Black;
	public static BaseItemBackpack	backpack_White;

	public static ItemBlueprint		itemBlueprintBase;

	public static Item				dustLithiumCarbonate;
	public static Item				dustLithiumHydroxide;
	public static Item				dustLithiumPeroxide;
	public static Item				dustLithiumFluoride;

	public static Item				dustUraniumTetraFluoride;
	public static Item				dustUraniumHexaFluoride;

	public static Item				dustBerylliumFluoride;

	public static Item				dustQuicklime;
	public static Item				dustCalciumHydroxide;
	public static Item				dustCalciumCarbonate;
	public static Item				dust2LiOH_CaCO3;
	public static Item				dustLi2BeF4;

	public static BaseEuItem		metaItem2;

	// @SuppressWarnings("unused")
	@SuppressWarnings("unused")
	public static final void init() {

		ModItems.AAA_Broken = new BaseItemIngot("AAA_Broken", "Errors - Tell Alkalus",
				Utils.rgbtoHexValue(128, 128, 128), 0);

		// Debug Loading
		if (CORE.DEBUG) {
			DEBUG_INIT.registerItems();
		}

		// Some Simple forms of materials
		ModItems.itemStickyRubber = new Item().setUnlocalizedName("itemStickyRubber")
				.setCreativeTab(AddToCreativeTab.tabMachines).setTextureName(CORE.MODID + ":itemStickyRubber");
		GameRegistry.registerItem(ModItems.itemStickyRubber, "itemStickyRubber");
		GT_OreDictUnificator.registerOre("ingotRubber", ItemUtils.getItemStack(CORE.MODID + ":itemStickyRubber", 1));

		ModItems.itemHeliumBlob = new CoreItem("itemHeliumBlob", AddToCreativeTab.tabMisc)
				.setTextureName(CORE.MODID + ":itemHeliumBlob");
		GT_OreDictUnificator.registerOre("dustHydrogen", new ItemStack(ModItems.itemHeliumBlob));
		// GameRegistry.registerItem(itemHeliumBlob, "itemHeliumBlob");

		// Make some backpacks
		// Primary colours
		ModItems.backpack_Red = new BaseItemBackpack("backpackRed", Utils.rgbtoHexValue(200, 0, 0));
		ModItems.backpack_Green = new BaseItemBackpack("backpackGreen", Utils.rgbtoHexValue(0, 200, 0));
		ModItems.backpack_Blue = new BaseItemBackpack("backpackBlue", Utils.rgbtoHexValue(0, 0, 200));
		// Secondary Colours
		ModItems.backpack_Yellow = new BaseItemBackpack("backpackYellow", Utils.rgbtoHexValue(200, 200, 0));
		ModItems.backpack_Purple = new BaseItemBackpack("backpackPurple", Utils.rgbtoHexValue(200, 0, 200));
		ModItems.backpack_Cyan = new BaseItemBackpack("backpackCyan", Utils.rgbtoHexValue(0, 200, 200));
		// Tertiary Colours
		ModItems.backpack_Maroon = new BaseItemBackpack("backpackMaroon", Utils.rgbtoHexValue(128, 0, 0));
		ModItems.backpack_Olive = new BaseItemBackpack("backpackOlive", Utils.rgbtoHexValue(128, 128, 0));
		ModItems.backpack_DarkGreen = new BaseItemBackpack("backpackDarkGreen", Utils.rgbtoHexValue(0, 128, 0));
		ModItems.backpack_DarkPurple = new BaseItemBackpack("backpackDarkPurple", Utils.rgbtoHexValue(128, 0, 128));
		ModItems.backpack_Teal = new BaseItemBackpack("backpackTeal", Utils.rgbtoHexValue(0, 128, 128));
		ModItems.backpack_Navy = new BaseItemBackpack("backpackNavy", Utils.rgbtoHexValue(0, 0, 128));
		// Shades
		ModItems.backpack_Silver = new BaseItemBackpack("backpackSilver", Utils.rgbtoHexValue(192, 192, 192));
		ModItems.backpack_Gray = new BaseItemBackpack("backpackGray", Utils.rgbtoHexValue(128, 128, 128));
		ModItems.backpack_Black = new BaseItemBackpack("backpackBlack", Utils.rgbtoHexValue(20, 20, 20));
		ModItems.backpack_White = new BaseItemBackpack("backpackWhite", Utils.rgbtoHexValue(240, 240, 240));

		ModItems.itemBlueprintBase = new ItemBlueprint("itemBlueprint");

		// Start meta Item Generation
		ItemsFoods.load();

		try {
			// Elements generate first so they can be used in compounds.

			// Uranium-233 is a fissile isotope of uranium that is bred from
			// thorium-232 as part of the thorium fuel cycle.
			MaterialGenerator.generate(ELEMENT.getInstance().URANIUM233);
			MaterialGenerator.generate(ELEMENT.getInstance().ZIRCONIUM);

			// Carbides - Tungsten Carbide exists in .09 so don't generate it. -
			// Should still come before alloys though
			if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
				MaterialGenerator.generate(ALLOY.TUNGSTEN_CARBIDE);
			}
			MaterialGenerator.generate(ALLOY.SILICON_CARBIDE);
			MaterialGenerator.generate(ALLOY.ZIRCONIUM_CARBIDE);
			MaterialGenerator.generate(ALLOY.TANTALUM_CARBIDE);
			MaterialGenerator.generate(ALLOY.NIOBIUM_CARBIDE);

			// Generate some Alloys

			// Misc Alloys
			MaterialGenerator.generate(ALLOY.ENERGYCRYSTAL);
			MaterialGenerator.generate(ALLOY.BLOODSTEEL);
			MaterialGenerator.generate(ALLOY.BEDROCKIUM);
			MaterialGenerator.generate(ALLOY.ZERON_100);
			// Tumbaga was the name given by Spaniards to a non-specific alloy
			// of gold and copper
			MaterialGenerator.generate(ALLOY.TUMBAGA);
			// Potin is traditionally an alloy of bronze, tin and lead, with
			// varying quantities of each possible
			MaterialGenerator.generate(ALLOY.POTIN);

			// Staballoy & Tantalloy
			MaterialGenerator.generate(ALLOY.STABALLOY);
			MaterialGenerator.generate(ALLOY.TANTALLOY_60);
			MaterialGenerator.generate(ALLOY.TANTALLOY_61);

			// Inconel
			MaterialGenerator.generate(ALLOY.INCONEL_625);
			MaterialGenerator.generate(ALLOY.INCONEL_690);
			MaterialGenerator.generate(ALLOY.INCONEL_792);

			// Maraging Steel
			MaterialGenerator.generate(ALLOY.MARAGING250);
			MaterialGenerator.generate(ALLOY.MARAGING300);
			MaterialGenerator.generate(ALLOY.MARAGING350);

			// Composite Alloys
			MaterialGenerator.generate(ALLOY.STELLITE);
			MaterialGenerator.generate(ALLOY.TALONITE);

			// Hastelloy
			MaterialGenerator.generate(ALLOY.HASTELLOY_W);
			MaterialGenerator.generate(ALLOY.HASTELLOY_X);
			MaterialGenerator.generate(ALLOY.HASTELLOY_C276);
			MaterialGenerator.generate(ALLOY.HASTELLOY_N);

			// Incoloy
			MaterialGenerator.generate(ALLOY.INCOLOY_020);
			MaterialGenerator.generate(ALLOY.INCOLOY_DS);
			MaterialGenerator.generate(ALLOY.INCOLOY_MA956);

			// Leagrisium
			MaterialGenerator.generate(ALLOY.LEAGRISIUM);
			// Must be the final Alloy to Generate
			MaterialGenerator.generate(ALLOY.QUANTUM);

		}
		catch (final Throwable r) {
			Utils.LOG_INFO("Failed to Generated a Material. " + r.getMessage());
			// Utils.LOG_INFO("Failed to Generated a Material.
			// "+r.getCause().getMessage());
			Utils.LOG_INFO("Failed to Generated a Material. " + r.getStackTrace()[0].getMethodName());
			Utils.LOG_INFO("Failed to Generated a Material. " + r.getStackTrace()[1].getMethodName());
			r.printStackTrace();
			System.exit(1);
		}

		// Nuclear Fuel Dusts
		ModItems.dustUraniumTetraFluoride = ItemUtils.generateSpecialUseDusts("UraniumTetrafluoride",
				"Uranium Tetrafluoride", Utils.rgbtoHexValue(17, 179, 42))[0];
		ModItems.dustUraniumHexaFluoride = ItemUtils.generateSpecialUseDusts("UraniumHexafluoride",
				"Uranium Hexafluoride", Utils.rgbtoHexValue(9, 199, 32))[0];

		ModItems.dustBerylliumFluoride = ItemUtils.generateSpecialUseDusts("BerylliumFluoride", "Beryllium Fluoride",
				Utils.rgbtoHexValue(175, 175, 175))[0]; // https://en.wikipedia.org/wiki/Beryllium_fluoride

		ModItems.dustLithiumCarbonate = ItemUtils.generateSpecialUseDusts("LithiumCarbonate", "Lithium Carbonate",
				Utils.rgbtoHexValue(240, 240, 240))[0]; // https://en.wikipedia.org/wiki/Lithium_carbonate
		ModItems.dustLithiumFluoride = ItemUtils.generateSpecialUseDusts("LithiumFluoride", "Lithium Fluoride",
				Utils.rgbtoHexValue(245, 245, 245))[0]; // https://en.wikipedia.org/wiki/Lithium_fluoride
		ModItems.dustLithiumPeroxide = ItemUtils.generateSpecialUseDusts("LithiumPeroxide", "Lithium Peroxide",
				Utils.rgbtoHexValue(250, 250, 250))[0]; // https://en.wikipedia.org/wiki/Lithium_peroxide
		ModItems.dustLithiumHydroxide = ItemUtils.generateSpecialUseDusts("LithiumHydroxide", "Lithium Hydroxide",
				Utils.rgbtoHexValue(250, 250, 250))[0]; // https://en.wikipedia.org/wiki/Lithium_hydroxide

		if (ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 1).getItem() == ModItems.AAA_Broken
				|| !LoadedMods.IHL) {
			ModItems.dustQuicklime = ItemUtils.generateSpecialUseDusts("Quicklime", "Quicklime",
					Utils.rgbtoHexValue(255, 255, 255))[0]; // https://en.wikipedia.org/wiki/Calcium_oxide
		}
		ModItems.dustCalciumHydroxide = ItemUtils.generateSpecialUseDusts("CalciumHydroxide", "Hydrated Lime",
				Utils.rgbtoHexValue(255, 255, 255))[0]; // https://en.wikipedia.org/wiki/Calcium_hydroxide
		ModItems.dustCalciumCarbonate = ItemUtils.generateSpecialUseDusts("CalciumCarbonate", "Calcium Carbonate",
				Utils.rgbtoHexValue(255, 255, 255))[0]; // https://en.wikipedia.org/wiki/Calcium_carbonate
		ModItems.dust2LiOH_CaCO3 = ItemUtils.generateSpecialUseDusts("2LiOHCaCO3", "2LiOH & CaCO3 Compound",
				Utils.rgbtoHexValue(255, 255, 255))[0]; // https://en.wikipedia.org/wiki/Calcium_carbonate

		// FLiBe Fuel Compounds
		ModItems.dustLi2BeF4 = ItemUtils.generateSpecialUseDusts("Li2BeF4", "Li2BeF4 Fuel Compound",
				Utils.rgbtoHexValue(255, 255, 255))[0]; // https://en.wikipedia.org/wiki/FLiBe

		ModItems.metaItem2 = new BaseEuItem();
		ModItems.metaItem2.registerItem(0, EnumChatFormatting.BLACK + "Test Item 0", 0, 0, "I am 0.");
		ModItems.metaItem2.registerItem(1, EnumChatFormatting.GREEN + "Test Item 1", 1006346000, 1, "I Hold EU 1.",
				500);
		ModItems.metaItem2.registerItem(2, EnumChatFormatting.GOLD + "Test Item 2", 1004630000, 2, "I Hold EU 2.",
				8000);
		ModItems.metaItem2.registerItem(3, "Test Item 3", 1000765000, 4, "I Hold EU 3.", 32000);
		ModItems.metaItem2.registerItem(4, "Whirlygig", 1043644000, (short) 5, "Spin me right round.", EnumRarity.rare,
				EnumChatFormatting.DARK_GREEN, true);
		ModItems.metaItem2.registerItem(5, "Whirlygig 2", 2124867000, (short) 7, "Spin me right round.",
				EnumRarity.uncommon, EnumChatFormatting.RED, true);

		// ItemList.Battery_RE_HV_Cadmium.set(BaseEuItem.

		// GameRegistry.registerItem(this, unlocalName);

		final boolean gtStyleTools = LoadedMods.Gregtech;

		final Materials[] rm = Materials.values();
		for (final Materials m : rm) {
			ModItems.MP_GTMATERIAL = ItemUtils.generateMultiPick(gtStyleTools, m);
			ModItems.MS_GTMATERIAL = ItemUtils.generateMultiShovel(gtStyleTools, m);
			/*
			 * itemBaseDecidust = UtilsItems.generateDecidust(m);
			 * itemBaseCentidust = UtilsItems.generateCentidust(m);
			 */
		}

		// EnderIO Resources
		if (LoadedMods.EnderIO || CORE.LOAD_ALL_CONTENT) {
			Utils.LOG_INFO("EnderIO Found - Loading Resources.");
			// Item Init
			ModItems.itemPlateSoularium = new BaseItemPlate("itemPlate" + "Soularium", "Soularium", new short[] {
					95, 90, 54
			}, 2, 0);
			ModItems.itemPlateRedstoneAlloy = new BaseItemPlate("itemPlate" + "RedstoneAlloy", "Redstone Alloy",
					new short[] {
							178, 34, 34
					}, 2, 0);
			ModItems.itemPlateElectricalSteel = new BaseItemPlate("itemPlate" + "ElectricalSteel", "Electrical Steel",
					new short[] {
							194, 194, 194
					}, 2, 0);
			ModItems.itemPlatePulsatingIron = new BaseItemPlate("itemPlate" + "PhasedIron", "Phased Iron", new short[] {
					50, 91, 21
			}, 2, 0);
			ModItems.itemPlateEnergeticAlloy = new BaseItemPlate("itemPlate" + "EnergeticAlloy", "Energetic Alloy",
					new short[] {
							252, 152, 45
					}, 2, 0);
			ModItems.itemPlateVibrantAlloy = new BaseItemPlate("itemPlate" + "VibrantAlloy", "Vibrant Alloy",
					new short[] {
							204, 242, 142
					}, 2, 0);
			ModItems.itemPlateConductiveIron = new BaseItemPlate("itemPlate" + "ConductiveIron", "Conductive Iron",
					new short[] {
							164, 109, 100
					}, 2, 0);

		}
		else {
			Utils.LOG_WARNING("EnderIO not Found - Skipping Resources.");
		}
		// Big Reactors
		if (LoadedMods.Big_Reactors || CORE.LOAD_ALL_CONTENT) {
			Utils.LOG_INFO("BigReactors Found - Loading Resources.");
			// Item Init
			ModItems.itemPlateBlutonium = new BaseItemPlate("itemPlate" + "Blutonium", "Blutonium", new short[] {
					0, 0, 255
			}, 2, 0);
			ModItems.itemPlateCyanite = new BaseItemPlate("itemPlate" + "Cyanite", "Cyanite", new short[] {
					0, 191, 255
			}, 2, 0);
			ModItems.itemPlateLudicrite = new BaseItemPlate("itemPlate" + "Ludicrite", "Ludicrite", new short[] {
					167, 5, 179
			}, 2, 0);

		}
		else {
			Utils.LOG_WARNING("BigReactors not Found - Skipping Resources.");
		}
		// Thaumcraft
		if (LoadedMods.Thaumcraft || CORE.LOAD_ALL_CONTENT) {
			Utils.LOG_INFO("Thaumcraft Found - Loading Resources.");
			// Item Init
			try {

				ItemUtils.getItemForOreDict("Thaumcraft:ItemResource", "ingotVoidMetal", "Void Metal Ingot", 16);
				ModItems.itemPlateVoidMetal = new BaseItemPlate("itemPlate" + "Void", "Void", new short[] {
						82, 17, 82
				}, 2, 0);
				GT_OreDictUnificator.registerOre("plateVoidMetal", new ItemStack(ModItems.itemPlateVoidMetal));
			}
			catch (final NullPointerException e) {
				e.getClass();
			}

		}
		else {
			Utils.LOG_WARNING("Thaumcraft not Found - Skipping Resources.");
		}
		// ExtraUtils
		if (LoadedMods.Extra_Utils || CORE.LOAD_ALL_CONTENT) {
			Utils.LOG_INFO("ExtraUtilities Found - Loading Resources.");
			// Item Init
			try {
				// itemPlateBedrockium = new
				// Item().setUnlocalizedName("itemPlateBedrockium").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID
				// + ":itemPlateBedrockium");
			}
			catch (final NullPointerException e) {
				e.getClass();
			}
			// Registry
			// GameRegistry.registerItem(itemPlateBedrockium,
			// "itemPlateBedrockium");
		}
		else {
			Utils.LOG_WARNING("ExtraUtilities not Found - Skipping Resources.");
		}
		// Pneumaticraft
		if (LoadedMods.PneumaticCraft || CORE.LOAD_ALL_CONTENT) {
			Utils.LOG_INFO("PneumaticCraft Found - Loading Resources.");
			// Item Init
			ModItems.itemPlateCompressedIron = new BaseItemPlate("itemPlate" + "CompressedIron", "Compressed Iron",
					new short[] {
							128, 128, 128
					}, 2, 0);
		}
		else {
			Utils.LOG_WARNING("PneumaticCraft not Found - Skipping Resources.");
		}
		// Simply Jetpacks
		if (LoadedMods.Simply_Jetpacks || CORE.LOAD_ALL_CONTENT) {
			Utils.LOG_INFO("SimplyJetpacks Found - Loading Resources.");
			// Item Init
			ModItems.itemPlateEnrichedSoularium = new RarityUncommon().setUnlocalizedName("itemPlateEnrichedSoularium")
					.setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateSoularium");
			// Registry
			GameRegistry.registerItem(ModItems.itemPlateEnrichedSoularium, "itemPlateEnrichedSoularium");
		}
		else {
			Utils.LOG_WARNING("SimplyJetpacks not Found - Skipping Resources.");
		}
		// rfTools
		if (LoadedMods.RFTools || CORE.LOAD_ALL_CONTENT) {
			Utils.LOG_INFO("rfTools Found - Loading Resources.");
			// Item Init
			ModItems.itemPlateDimensionShard = new BaseItemPlate("itemPlate" + "DimensionShard", "Dimensional Shard",
					new short[] {
							170, 230, 230
					}, 2, 0);
		}
		else {
			Utils.LOG_WARNING("rfTools not Found - Skipping Resources.");
		}
		// IC2 Exp
		if (LoadedMods.IndustrialCraft2 || CORE.LOAD_ALL_CONTENT) {
			Utils.LOG_INFO("IndustrialCraft2 Found - Loading Resources.");
			// Item Init
			ModItems.FuelRod_Empty = new FuelRod_Base("itemFuelRod_Empty", "Empty", 0, 1000);
			ModItems.FuelRod_Thorium = new FuelRod_Base("itemFuelRod_Thorium", "Thorium", 1000, 1000);
			ModItems.FuelRod_Uranium = new FuelRod_Base("itemFuelRod_Uranium", "Uranium", 2500, 2500);
			ModItems.FuelRod_Plutonium = new FuelRod_Base("itemFuelRod_Plutonium", "Plutonium", 5000, 5000);
			ModItems.RfEuBattery = new RF2EU_Battery();

			try {
				final Class baublesTest = Class.forName("baubles.api.IBauble");
				if (baublesTest != null) {
					COMPAT_Baubles.run();
				}
				else {
					Utils.LOG_INFO("Baubles Not Found - Skipping Resources.");
				}
			}
			catch (final Throwable T) {
				Utils.LOG_INFO("Baubles Not Found - Skipping Resources.");
			}
			// Registry
			// GameRegistry.registerItem(FuelRod_Empty, "itemFuelRod_Empty");
			// GameRegistry.registerItem(FuelRod_Thorium,
			// "itemFuelRod_Thorium");
			// GameRegistry.registerItem(FuelRod_Uranium,
			// "itemFuelRod_Uranium");
			// GameRegistry.registerItem(FuelRod_Plutonium,
			// "itemFuelRod_Plutonium");

			// FluidCell = new ItemStack(new
			// IC2_ItemFluidCell("itemGT++FluidCell"));

		}
		else {
			Utils.LOG_WARNING("IndustrialCraft2 not Found - Skipping Resources.");
		}

		// Special Item Handling Case
		if (configSwitches.enableAlternativeBatteryAlloy) {
			// ModItems.itemIngotBatteryAlloy = new
			// BaseItemIngot("itemIngotBatteryAlloy", "Battery Alloy", new
			// short[]{35, 228, 141}, 0); TODO
			ModItems.itemPlateBatteryAlloy = new BaseItemPlate("itemPlateBatteryAlloy", "Battery Alloy", new short[] {
					35, 228, 141
			}, 2, 0);
		}

		// UtilsItems.generateSpawnEgg("ic2", "boatcarbon",
		// Utils.generateSingularRandomHexValue(),
		// Utils.generateSingularRandomHexValue());

		/*
		 * Misc Items
		 */

		// Staballoy Equipment
		ModItems.itemStaballoyPickaxe = new StaballoyPickaxe("itemStaballoyPickaxe", ModItems.STABALLOY)
				.setCreativeTab(AddToCreativeTab.tabTools);
		GameRegistry.registerItem(ModItems.itemStaballoyPickaxe, ModItems.itemStaballoyPickaxe.getUnlocalizedName());
		ModItems.itemStaballoyAxe = new StaballoyAxe("itemStaballoyAxe", ModItems.STABALLOY)
				.setCreativeTab(AddToCreativeTab.tabTools);
		GameRegistry.registerItem(ModItems.itemStaballoyAxe, ModItems.itemStaballoyAxe.getUnlocalizedName());

		// Sandstone Hammer
		ModItems.itemSandstoneHammer = new SandstoneHammer("itemSandstoneHammer")
				.setCreativeTab(AddToCreativeTab.tabTools);
		GameRegistry.registerItem(ModItems.itemSandstoneHammer, ModItems.itemSandstoneHammer.getUnlocalizedName());

		// Buffer Cores!
		Item itemBufferCore;
		for (int i = 1; i <= 10; i++) {
			// Utils.LOG_INFO(""+i);
			itemBufferCore = new BufferCore("itemBufferCore", i).setCreativeTab(AddToCreativeTab.tabMachines);
			GameRegistry.registerItem(itemBufferCore, itemBufferCore.getUnlocalizedName() + i);
			// System.out.println("Buffer Core registration count is: "+i);
		}

		ModItems.itemPLACEHOLDER_Circuit = new Item().setUnlocalizedName("itemPLACEHOLDER_Circuit")
				.setTextureName(CORE.MODID + ":itemPLACEHOLDER_Circuit");
		GameRegistry.registerItem(ModItems.itemPLACEHOLDER_Circuit, "itemPLACEHOLDER_Circuit");

		// ItemBlockGtFrameBox = new
		// ItemBlockGtFrameBox(ModBlocks.blockGtFrameSet1);
		// GameRegistry.registerItem(ItemBlockGtFrameBox, "itemGtFrameBoxSet1");
	}
}
