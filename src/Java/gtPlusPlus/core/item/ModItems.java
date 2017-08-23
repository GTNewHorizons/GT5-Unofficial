package gtPlusPlus.core.item;
import static gtPlusPlus.core.creative.AddToCreativeTab.tabMachines;
import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;
import static gtPlusPlus.core.lib.CORE.LOAD_ALL_CONTENT;

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
import gtPlusPlus.core.item.base.gears.BaseItemSmallGear;
import gtPlusPlus.core.item.base.ingots.BaseItemIngot_OLD;
import gtPlusPlus.core.item.base.misc.BaseItemMisc;
import gtPlusPlus.core.item.base.misc.BaseItemMisc.MiscTypes;
import gtPlusPlus.core.item.base.plates.BaseItemPlate;
import gtPlusPlus.core.item.base.plates.BaseItemPlateDouble;
import gtPlusPlus.core.item.chemistry.CoalTar;
import gtPlusPlus.core.item.effects.RarityUncommon;
import gtPlusPlus.core.item.general.*;
import gtPlusPlus.core.item.general.chassis.*;
import gtPlusPlus.core.item.init.ItemsFoods;
import gtPlusPlus.core.item.init.ItemsMultiTools;
import gtPlusPlus.core.item.tool.misc.SandstoneHammer;
import gtPlusPlus.core.item.tool.staballoy.*;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.CORE.configSwitches;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.*;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.util.StringUtils;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.debug.DEBUG_INIT;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.materials.MaterialUtils;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
public final class ModItems {

	public static ToolMaterial STABALLOY = EnumHelper.addToolMaterial("Staballoy", 3, 2500, 7, 1.0F, 18);

	public static Item AAA_Broken;
	public static Item itemAlkalusDisk;

	public static Item itemDebugShapeSpawner;

	public static Item itemBaseSpawnEgg;

	//EnderIO
	public static Item itemPlateSoularium;
	public static Item itemPlateRedstoneAlloy;
	public static Item itemPlateElectricalSteel;
	public static Item itemPlatePulsatingIron;
	public static Item itemPlateEnergeticAlloy;
	public static Item itemPlateVibrantAlloy;
	public static Item itemPlateConductiveIron;
	public static Item itemPlateDarkSteel;
	public static Item itemDustSoularium;
	public static Item itemDustRedstoneAlloy;
	public static Item itemDustElectricalSteel;
	public static Item itemDustPulsatingIron;
	public static Item itemDustEnergeticAlloy;
	public static Item itemDustVibrantAlloy;
	public static Item itemDustConductiveIron;
	//Big Reactors
	public static Item itemPlateBlutonium;
	public static Item itemPlateCyanite;
	public static Item itemPlateLudicrite;
	//Thaumcraft
	public static Item itemPlateVoidMetal;
	//Pneumaticraft
	public static Item itemPlateCompressedIron;
	//SimplyJetpacks
	public static Item itemPlateEnrichedSoularium;
	//rfTools
	public static Item itemPlateDimensionShard;
	//Staballoy
	public static Item itemStaballoyPickaxe;
	public static Item itemStaballoyAxe;
	//Tools
	public static Item itemSandstoneHammer;
	//Machine Related
	public static Item itemBufferCore0;
	//Material related
	public static Item itemStickyRubber;
	public static Item itemIngotBatteryAlloy;
	public static Item itemPlateBatteryAlloy;
	public static Item itemHeliumBlob;
	public static Item itemHydrogenBlob;
	public static Item itemPLACEHOLDER_Circuit;

	public static Item FuelRod_Empty;
	public static Item FuelRod_Thorium;
	public static Item FuelRod_Uranium;
	public static Item FuelRod_Plutonium;

	public static Item itemBedLocator_Base;
	public static Item itemBaseItemWithCharge;

	public static Item itemIngotRaisinBread;
	public static Item itemHotIngotRaisinBread;

	public static ItemFood itemFoodRaisinToast;
	public static BaseItemHotFood itemHotFoodRaisinToast;
	public static BaseItemFood itemFoodCurriedSausages;
	public static BaseItemHotFood itemHotFoodCurriedSausages;

	public static Item RfEuBattery;
	public static Item itemPersonalCloakingDevice;
	public static Item itemPersonalCloakingDeviceCharged;
	public static Item itemPersonalHealingDevice;
	public static Item itemSlowBuildingRing;

	public static MultiPickaxeBase MP_GTMATERIAL;
	public static MultiSpadeBase MS_GTMATERIAL;

	public static BaseItemDecidust itemBaseDecidust;
	public static BaseItemCentidust itemBaseCentidust;

	public static ItemStack FluidCell;

	public static BaseItemBackpack backpack_Red;
	public static BaseItemBackpack backpack_Green;
	public static BaseItemBackpack backpack_Blue;
	public static BaseItemBackpack backpack_Yellow;
	public static BaseItemBackpack backpack_Purple;
	public static BaseItemBackpack backpack_Cyan;
	public static BaseItemBackpack backpack_Maroon;
	public static BaseItemBackpack backpack_Olive;
	public static BaseItemBackpack backpack_DarkGreen;
	public static BaseItemBackpack backpack_DarkPurple;
	public static BaseItemBackpack backpack_Teal;
	public static BaseItemBackpack backpack_Navy;
	public static BaseItemBackpack backpack_Silver;
	public static BaseItemBackpack backpack_Gray;
	public static BaseItemBackpack backpack_Black;
	public static BaseItemBackpack backpack_White;

	public static ItemBlueprint itemBlueprintBase;

	public static Item dustLithiumCarbonate;
	public static Item dustLithiumHydroxide;
	public static Item dustLithiumPeroxide;
	public static Item dustLithiumFluoride;

	public static Item dustUranium232;
	public static Item dustUraniumTetraFluoride;
	public static Item dustUraniumHexaFluoride;

	public static Item dustBerylliumFluoride;

	public static Item dustQuicklime;
	public static Item dustCalciumHydroxide;
	public static Item dustCalciumCarbonate;
	public static Item dustLi2CO3CaOH2;
	public static Item dustLi2BeF4;

	public static Item dustAer;
	public static Item dustIgnis;
	public static Item dustTerra;
	public static Item dustAqua;

	public static BaseEuItem metaItem2;

	public static Item shardAer;
	public static Item shardIgnis;
	public static Item shardTerra;
	public static Item shardAqua;

	//Tc Compat for energy crystal recipes
	public static BaseItemTCShard shardDull;

	//Lighter
	public static Item itemBasicFireMaker;

	//Zirconium
	public static Item itemZirconiumChlorideCinterPellet;
	public static Item dustZrCl4;
	public static Item dustCookedZrCl4;
	public static Item dustZrF4;

	public static Item dustNaBF4NaF;
	public static Item dustLiFBeF2ZrF4UF4;
	public static Item dustLiFBeF2ZrF4U235;
	public static Item dustLiFBeF2ThF4UF4;

	public static Item dustCalciumSulfate;

	public static BaseItemPlate itemPlateClay;
	public static BaseItemPlateDouble itemDoublePlateClay;

	public static Item dustFertUN18;
	public static Item dustFertUN32;

	public static Fluid fluidFLiBeSalt;

	public static Item itemSmallWroughtIronGear;
	public static Item itemPlateLithium;
	public static BaseItemPlate itemPlateEuropium;
	public static BaseItemPlateDouble itemDoublePlateEuropium;

	public static itemBoilerChassis itemBoilerChassis;
	public static itemDehydratorCoilWire itemDehydratorCoilWire;
	public static itemDehydratorCoil itemDehydratorCoil;

	public static Item itemLavaFilter;
	public static Item itemAirFilter;

	public static Item itemCoalCoke;

	public static CoreItem itemCircuitLFTR;





	public static final void init(){

		//Default item used when recipes fail, handy for debugging.
		AAA_Broken = new BaseItemIngot_OLD("AAA_Broken", "Errors - Tell Alkalus", Utils.rgbtoHexValue(128, 128, 128), 0);
		itemAlkalusDisk = new CoreItem("itemAlkalusDisk", AddToCreativeTab.tabMisc, 1, 0, "Unknown Use", EnumRarity.rare, EnumChatFormatting.AQUA, false, null);

		//Debug Loading
		if (CORE.DEBUG){
			DEBUG_INIT.registerItems();
		}


		//Some Simple forms of materials
		itemStickyRubber = new Item().setUnlocalizedName("itemStickyRubber").setCreativeTab(tabMachines).setTextureName(CORE.MODID + ":itemStickyRubber");
		GameRegistry.registerItem(itemStickyRubber, "itemStickyRubber");
		GT_OreDictUnificator.registerOre("ingotRubber", ItemUtils.getItemStack(CORE.MODID+":itemStickyRubber", 1));




		//Register Hydrogen Blobs first, so we can replace old helium blobs.
		itemCoalCoke = new BaseItemBurnable("itemCoalCoke", "Coking Coal", tabMisc, 64, 0, "Used for metallurgy.", "fuelCoke", 3200, 0).setTextureName(CORE.MODID + ":itemCoalCoke");

		//Register Hydrogen Blobs first, so we can replace old helium blobs.
		itemHydrogenBlob = new CoreItem("itemHydrogenBlob", "Mysterious Hydrogen Blob", tabMisc).setTextureName(CORE.MODID + ":itemHeliumBlob");
		//GT_OreDictUnificator.registerOre("dustHydrogen", new ItemStack(ModItems.itemHydrogenBlob));
		//Register Old Helium Blob, this will be replaced when held by a player.
		itemHeliumBlob = new CoreItem("itemHeliumBlob", tabMisc, ItemUtils.getSimpleStack(itemHydrogenBlob)).setTextureName(CORE.MODID + ":itemHydrogenBlob");

		//Register this neato device, for making some fires.
		itemBasicFireMaker = new ItemBasicFirestarter();

		//Make some backpacks
		//Primary colours
		backpack_Red = new BaseItemBackpack("backpackRed", Utils.rgbtoHexValue(200, 0, 0));
		backpack_Green = new BaseItemBackpack("backpackGreen", Utils.rgbtoHexValue(0, 200, 0));
		backpack_Blue = new BaseItemBackpack("backpackBlue", Utils.rgbtoHexValue(0, 0, 200));
		//Secondary Colours
		backpack_Yellow = new BaseItemBackpack("backpackYellow", Utils.rgbtoHexValue(200, 200, 0));
		backpack_Purple = new BaseItemBackpack("backpackPurple", Utils.rgbtoHexValue(200, 0, 200));
		backpack_Cyan = new BaseItemBackpack("backpackCyan", Utils.rgbtoHexValue(0, 200, 200));
		//Tertiary Colours
		backpack_Maroon = new BaseItemBackpack("backpackMaroon", Utils.rgbtoHexValue(128, 0, 0));
		backpack_Olive = new BaseItemBackpack("backpackOlive", Utils.rgbtoHexValue(128, 128, 0));
		backpack_DarkGreen = new BaseItemBackpack("backpackDarkGreen", Utils.rgbtoHexValue(0, 128, 0));
		backpack_DarkPurple = new BaseItemBackpack("backpackDarkPurple", Utils.rgbtoHexValue(128, 0, 128));
		backpack_Teal = new BaseItemBackpack("backpackTeal", Utils.rgbtoHexValue(0, 128, 128));
		backpack_Navy = new BaseItemBackpack("backpackNavy", Utils.rgbtoHexValue(0, 0, 128));
		//Shades
		backpack_Silver = new BaseItemBackpack("backpackSilver", Utils.rgbtoHexValue(192, 192, 192));
		backpack_Gray = new BaseItemBackpack("backpackGray", Utils.rgbtoHexValue(128, 128, 128));
		backpack_Black = new BaseItemBackpack("backpackBlack", Utils.rgbtoHexValue(20, 20, 20));
		backpack_White = new BaseItemBackpack("backpackWhite", Utils.rgbtoHexValue(240, 240, 240));

		itemBlueprintBase = new ItemBlueprint("itemBlueprint");

		//Start meta Item Generation
		ItemsFoods.load();

		try{

			//Elements generate first so they can be used in compounds.
			//Missing Elements
			MaterialGenerator.generate(ELEMENT.getInstance().SELENIUM); //LFTR byproduct
			MaterialGenerator.generate(ELEMENT.getInstance().BROMINE);
			MaterialGenerator.generate(ELEMENT.getInstance().KRYPTON); //LFTR byproduct
			MaterialGenerator.generate(ELEMENT.getInstance().STRONTIUM);
			MaterialGenerator.generate(ELEMENT.getInstance().ZIRCONIUM);
			MaterialGenerator.generate(ELEMENT.getInstance().RUTHENIUM);
			MaterialGenerator.generate(ELEMENT.getInstance().IODINE); //LFTR byproduct
			MaterialGenerator.generate(ELEMENT.getInstance().HAFNIUM);
			MaterialGenerator.generate(ELEMENT.getInstance().DYSPROSIUM);
			MaterialGenerator.generate(ELEMENT.getInstance().TELLURIUM); //LFTR byproduct
			MaterialGenerator.generate(ELEMENT.getInstance().RHODIUM);
			MaterialGenerator.generate(ELEMENT.getInstance().RHENIUM);
			MaterialGenerator.generate(ELEMENT.getInstance().THALLIUM);

			//RADIOACTIVE ELEMENTS
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().TECHNETIUM, false); //LFTR byproduct
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().POLONIUM, false);
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().ASTATINE, false);
			//MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().RADON, false);
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().FRANCIUM, false);
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().RADIUM, false);
			//MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().PROMETHIUM, false);
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().ACTINIUM, false);
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().PROTACTINIUM, false);
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().NEPTUNIUM, false); //LFTR byproduct
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().CURIUM, false);
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().BERKELIUM, false);
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().CALIFORNIUM, false);
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().EINSTEINIUM, false);
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().FERMIUM, false);
			
			



			//Nuclear Isotopes

			//Lithium-7 is used as a part of the molten lithium fluoride in molten salt reactors: liquid-fluoride nuclear reactors.
			//The large neutron-absorption cross-section of lithium-6 (about 940 barns[5]) as compared with the very small
			//neutron cross-section of lithium-7 (about 45 millibarns) makes high separation of lithium-7 from natural lithium a
			//strong requirement for the possible use in lithium fluoride reactors.
			MaterialGenerator.generate(NUCLIDE.getInstance().LITHIUM7, false);
			//Thorium-232 is the most stable isotope of Thorium, purified for nuclear fuel use in this case.
			MaterialGenerator.generateNuclearMaterial(NUCLIDE.getInstance().THORIUM232);
			//Production of 233U (through the neutron irradiation of 232Th) invariably produces small amounts of 232U as an impurity
			//because of parasitic (n,2n) reactions on uranium-233 itself, or on protactinium-233, or on thorium-232:
			MaterialGenerator.generate(NUCLIDE.getInstance().URANIUM232);
			//Uranium-233 is a fissile isotope of uranium that is bred from thorium-232 as part of the thorium fuel cycle.
			MaterialGenerator.generate(NUCLIDE.getInstance().URANIUM233);
			//Plutonium-238 is a very powerful alpha emitter. This makes the plutonium-238 isotope suitable for usage in radioisotope thermoelectric generators (RTGs)
			//and radioisotope heater units - one gram of plutonium-238 generates approximately 0.5 W of thermal power.
			MaterialGenerator.generateNuclearMaterial(NUCLIDE.getInstance().PLUTONIUM238, false);

			//RTG Fuel Materials
			MaterialGenerator.generateNuclearMaterial(NUCLIDE.getInstance().STRONTIUM90, false);
			MaterialGenerator.generateNuclearMaterial(NUCLIDE.getInstance().POLONIUM210, false);
			MaterialGenerator.generateNuclearMaterial(NUCLIDE.getInstance().AMERICIUM241, false);


			//Carbides - Tungsten Carbide exists in .09 so don't generate it. - Should still come before alloys though
			if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
				MaterialGenerator.generate(ALLOY.TUNGSTEN_CARBIDE);
			}
			MaterialGenerator.generate(ALLOY.SILICON_CARBIDE);
			MaterialGenerator.generate(ALLOY.ZIRCONIUM_CARBIDE);
			MaterialGenerator.generate(ALLOY.TANTALUM_CARBIDE);
			MaterialGenerator.generate(ALLOY.NIOBIUM_CARBIDE);


			//Generate Fluorides
			MaterialGenerator.generateNuclearMaterial(FLUORIDES.FLUORITE);
			MaterialGenerator.generateNuclearMaterial(FLUORIDES.BERYLLIUM_FLUORIDE);
			MaterialGenerator.generateNuclearMaterial(FLUORIDES.LITHIUM_FLUORIDE);
			MaterialGenerator.generateNuclearMaterial(FLUORIDES.THORIUM_TETRAFLUORIDE);
			MaterialGenerator.generateNuclearMaterial(FLUORIDES.THORIUM_HEXAFLUORIDE);
			MaterialGenerator.generateNuclearMaterial(FLUORIDES.URANIUM_TETRAFLUORIDE);
			MaterialGenerator.generateNuclearMaterial(FLUORIDES.URANIUM_HEXAFLUORIDE);
			MaterialGenerator.generateNuclearMaterial(FLUORIDES.ZIRCONIUM_TETRAFLUORIDE);
			//LFTR Fluoride outputs
			MaterialGenerator.generateNuclearMaterial(FLUORIDES.NEPTUNIUM_HEXAFLUORIDE);
			MaterialGenerator.generateNuclearMaterial(FLUORIDES.TECHNETIUM_HEXAFLUORIDE);
			MaterialGenerator.generateNuclearMaterial(FLUORIDES.SELENIUM_HEXAFLUORIDE);

			//Generate Reactor Fuel Salts
			MaterialGenerator.generateNuclearMaterial(NUCLIDE.LiFBeF2ZrF4U235);
			MaterialGenerator.generateNuclearMaterial(NUCLIDE.LiFBeF2ZrF4UF4);
			MaterialGenerator.generateNuclearMaterial(NUCLIDE.LiFBeF2ThF4UF4);


			//Generate some Alloys

			//Misc Alloys
			MaterialGenerator.generate(ALLOY.ENERGYCRYSTAL);
			MaterialGenerator.generate(ALLOY.BLOODSTEEL);

			MaterialGenerator.generate(ALLOY.ZERON_100);
			//Tumbaga was the name given by Spaniards to a non-specific alloy of gold and copper
			MaterialGenerator.generate(ALLOY.TUMBAGA);
			//Potin is traditionally an alloy of bronze, tin and lead, with varying quantities of each possible
			MaterialGenerator.generate(ALLOY.POTIN);

			//Staballoy & Tantalloy
			MaterialGenerator.generate(ALLOY.STABALLOY);
			MaterialGenerator.generate(ALLOY.TANTALLOY_60);
			MaterialGenerator.generate(ALLOY.TANTALLOY_61);

			//Inconel
			MaterialGenerator.generate(ALLOY.INCONEL_625);
			MaterialGenerator.generate(ALLOY.INCONEL_690);
			MaterialGenerator.generate(ALLOY.INCONEL_792);


			//Steels
			MaterialGenerator.generateDusts(ALLOY.EGLIN_STEEL_BASE);
			MaterialGenerator.generate(ALLOY.EGLIN_STEEL);
			MaterialGenerator.generate(ALLOY.MARAGING250);
			MaterialGenerator.generate(ALLOY.MARAGING300);
			MaterialGenerator.generate(ALLOY.MARAGING350);

			//Composite Alloys
			MaterialGenerator.generate(ALLOY.STELLITE);
			MaterialGenerator.generate(ALLOY.TALONITE);

			//Hastelloy
			MaterialGenerator.generate(ALLOY.HASTELLOY_W);
			MaterialGenerator.generate(ALLOY.HASTELLOY_X);
			MaterialGenerator.generate(ALLOY.HASTELLOY_C276);
			MaterialGenerator.generate(ALLOY.HASTELLOY_N);

			//Incoloy
			MaterialGenerator.generate(ALLOY.INCOLOY_020);
			MaterialGenerator.generate(ALLOY.INCOLOY_DS);
			MaterialGenerator.generate(ALLOY.INCOLOY_MA956);

			//Leagrisium
			MaterialGenerator.generate(ALLOY.LEAGRISIUM);

			//Must be the final Alloy to Generate
			MaterialGenerator.generate(ALLOY.QUANTUM);


		} catch (final Throwable r){
			Utils.LOG_INFO("Failed to Generated a Material. "+r.getMessage());
			//Utils.LOG_INFO("Failed to Generated a Material. "+r.getCause().getMessage());
			//Utils.LOG_INFO("Failed to Generated a Material. "+r.getStackTrace()[0].getMethodName());
			//Utils.LOG_INFO("Failed to Generated a Material. "+r.getStackTrace()[1].getMethodName());
			r.printStackTrace();
			//System.exit(1);
		}

		//TC Style Shards, for use in making energy crystal mix.
		//A dull shard, able to be infused with an element.
		shardDull = new BaseItemTCShard("Drained", Utils.rgbtoHexValue(75, 75, 75), "Can be infused to create a magical shard.", "Obtained from Mining Stone/SandStone, Chopping Logs or Shovelling Dirt.");

		//Generates four elemental shards when TC is not installed.
		if (!LoadedMods.Thaumcraft){
			shardAer = new BaseItemTCShard("Aer", Utils.rgbtoHexValue(225, 225, 5));
			shardIgnis = new BaseItemTCShard("Ignis", Utils.rgbtoHexValue(255, 5, 5));
			shardTerra = new BaseItemTCShard("Terra", Utils.rgbtoHexValue(5, 255, 5));
			shardAqua = new BaseItemTCShard("Aqua", Utils.rgbtoHexValue(5, 5, 255));
		}
		else {
			shardAer = ItemUtils.getItemStackWithMeta(LoadedMods.Thaumcraft, "Thaumcraft:ItemShard", "Air Shard", 0, 1).getItem();
			shardIgnis = ItemUtils.getItemStackWithMeta(LoadedMods.Thaumcraft, "Thaumcraft:ItemShard", "Fire Shard", 1, 1).getItem();
			shardAqua = ItemUtils.getItemStackWithMeta(LoadedMods.Thaumcraft, "Thaumcraft:ItemShard", "Warer Shard", 2, 1).getItem();
			shardTerra = ItemUtils.getItemStackWithMeta(LoadedMods.Thaumcraft, "Thaumcraft:ItemShard", "Earth Shard", 3, 1).getItem();
		}
		//Generates a set of four special dusts to be used in my recipes.
		dustAer = ItemUtils.generateSpecialUseDusts(ELEMENT.getInstance().AER, true)[0];
		dustIgnis = ItemUtils.generateSpecialUseDusts(ELEMENT.getInstance().IGNIS, true)[0];
		dustTerra = ItemUtils.generateSpecialUseDusts(ELEMENT.getInstance().TERRA, true)[0];
		dustAqua = ItemUtils.generateSpecialUseDusts(ELEMENT.getInstance().AQUA, true)[0];

		//Nuclear Fuel Dusts
		dustLithiumCarbonate = ItemUtils.generateSpecialUseDusts("LithiumCarbonate", "Lithium Carbonate", Utils.rgbtoHexValue(240, 240, 240))[0]; //https://en.wikipedia.org/wiki/Lithium_carbonate
		dustLithiumPeroxide = ItemUtils.generateSpecialUseDusts("LithiumPeroxide", "Lithium Peroxide", Utils.rgbtoHexValue(250, 250, 250))[0]; //https://en.wikipedia.org/wiki/Lithium_peroxide
		dustLithiumHydroxide = ItemUtils.generateSpecialUseDusts("LithiumHydroxide", "Lithium Hydroxide", Utils.rgbtoHexValue(250, 250, 250))[0]; //https://en.wikipedia.org/wiki/Lithium_hydroxide

		if ((ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 1).getItem() == ModItems.AAA_Broken) || !LoadedMods.IHL){
			dustQuicklime = ItemUtils.generateSpecialUseDusts("Quicklime", "Quicklime", Utils.rgbtoHexValue(255, 255, 255))[0]; //https://en.wikipedia.org/wiki/Calcium_oxide
		}
		dustCalciumHydroxide = ItemUtils.generateSpecialUseDusts("CalciumHydroxide", "Hydrated Lime", Utils.rgbtoHexValue(255, 255, 255))[0]; //https://en.wikipedia.org/wiki/Calcium_hydroxide
		dustCalciumCarbonate = ItemUtils.generateSpecialUseDusts("CalciumCarbonate", "Calcium Carbonate", Utils.rgbtoHexValue(255, 255, 255))[0]; //https://en.wikipedia.org/wiki/Calcium_carbonate
		if ((ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustGypsum", 1) == null) || (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustCalciumSulfate", 1) == null)){
			dustCalciumSulfate = ItemUtils.generateSpecialUseDusts("Gypsum", "Calcium Sulfate (Gypsum)", Utils.rgbtoHexValue(255, 255, 255))[0]; //https://en.wikipedia.org/wiki/Calcium_sulfate
			GT_OreDictUnificator.registerOre("dustCalciumSulfate", ItemUtils.getSimpleStack(dustCalciumSulfate));
		}
		else {
			GT_OreDictUnificator.registerOre("dustCalciumSulfate", ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustGypsum", 1));
		}
		dustLi2CO3CaOH2 = ItemUtils.generateSpecialUseDusts("Li2CO3CaOH2", "Li2CO3 + Ca(OH)2 Compound", Utils.rgbtoHexValue(255, 255, 255))[0]; //https://en.wikipedia.org/wiki/Calcium_carbonate

		//FLiBe Fuel Compounds
		dustLi2BeF4 = ItemUtils.generateSpecialUseDusts("Li2BeF4", "Li2BeF4 Fuel Compound", Utils.rgbtoHexValue(255, 255, 255))[0]; //https://en.wikipedia.org/wiki/FLiBe
		fluidFLiBeSalt = FluidUtils.generateFluid("Li2BeF4", "Li2BeF4", 7430, new short[]{255, 255, 255, 100});

		//LFTR Control Circuit
		itemCircuitLFTR = new CoreItem("itemCircuitLFTR", ""+EnumChatFormatting.GREEN+"Thorium Reactor Control Circuit", AddToCreativeTab.tabMisc, 1, 0, "Helps your LFTR not explode", EnumRarity.epic, EnumChatFormatting.GREEN, false, null);

		
		//Zirconium
		//Cinter Pellet.
		itemZirconiumChlorideCinterPellet = new CoreItem("itemZirconiumPellet", "Zirconium Pellet ["+StringUtils.subscript("ZrCl4")+"]", tabMisc).setTextureName(CORE.MODID + ":itemShard");
		GT_OreDictUnificator.registerOre("pelletZirconium", new ItemStack(itemZirconiumChlorideCinterPellet));
		//Zirconium Chloride
		dustZrCl4 = ItemUtils.generateSpecialUseDusts("ZrCl4", "ZrCl4", Utils.rgbtoHexValue(180, 180, 180))[0]; //http://www.iaea.org/inis/collection/NCLCollectionStore/_Public/39/036/39036750.pdf
		dustCookedZrCl4 = ItemUtils.generateSpecialUseDusts("CookedZrCl4", "Cooked ZrCl4", Utils.rgbtoHexValue(180, 180, 180))[0]; //http://www.iaea.org/inis/collection/NCLCollectionStore/_Public/39/036/39036750.pdf
		//Zirconium Tetrafluoride
		GT_OreDictUnificator.registerOre("cellZrF4", ItemUtils.getItemStackOfAmountFromOreDict("cellZirconiumTetrafluoride", 1));
		GT_OreDictUnificator.registerOre("dustZrF4", ItemUtils.getItemStackOfAmountFromOreDict("dustZirconiumTetrafluoride", 1));
		FluidUtils.generateFluid("ZirconiumTetrafluoride", "Zirconium Tetrafluoride [ZrF4]", 500, new short[]{170, 170, 140, 100}); //https://en.wikipedia.org/wiki/Zirconium_tetrafluoride

		//Coolant Salt
		//NaBF4 - NaF - 621C
		//dustNaBF4NaF = ItemUtils.generateSpecialUseDusts("NaBF4NaF", "NaBF4NaF", Utils.rgbtoHexValue(45, 45, 90))[0]; //https://en.wikipedia.org/wiki/Zirconium_tetrafluoride


		//Load Tree Farmer
		if (CORE.configSwitches.enableMultiblock_TreeFarmer){ //https://en.wikipedia.org/wiki/UAN
			dustFertUN18 = ItemUtils.generateSpecialUseDusts("UN18Fertiliser", "UN-18 Fertiliser", Utils.rgbtoHexValue(60, 155, 60))[0];
			dustFertUN32 = ItemUtils.generateSpecialUseDusts("UN32Fertiliser", "UN-32 Fertiliser", Utils.rgbtoHexValue(55, 190, 55))[0];

			ItemStack temp1 = null;
			ItemStack temp2 = null;

			if (LoadedMods.IndustrialCraft2){
				temp1 = ItemUtils.getCorrectStacktype("IC2:itemFertilizer", 1);
			}
			if (LoadedMods.Forestry){
				temp2 = ItemUtils.getCorrectStacktype("Forestry:fertilizerCompound", 1);
			}
			if (temp1 != null){
				FluidUtils.generateFluidNonMolten("Fertiliser", "Fertiliser", 32, new short[]{45, 170, 45, 100}, temp1, temp2);
			}
			FluidUtils.generateFluidNonMolten("UN32Fertiliser", "UN-32 Fertiliser", 24, new short[]{55, 190, 55, 100}, null, null);
			FluidUtils.generateFluidNonMolten("UN18Fertiliser", "UN-18 Fertiliser", 22, new short[]{60, 155, 60, 100}, null, null);

			/*GT_Values.RA.addMixerRecipe(
			arg0, //Item In
			arg1,
			arg2,
			arg3,
			arg4, //Fluid in
			arg5, //Fluid Out
			arg6, //Item out
			arg7, //Eu
			arg8); //Time
			 */

		}


		//Test items
		metaItem2 = new BaseEuItem();
		metaItem2.registerItem(0, EnumChatFormatting.BLACK+"Test Item 0", 0, 0, "I am 0.");
		metaItem2.registerItem(1, EnumChatFormatting.GREEN+"Test Item 1", 1006346000, 1, "I Hold EU 1.", 500);
		metaItem2.registerItem(2, EnumChatFormatting.GOLD+"Test Item 2", 1004630000, 2, "I Hold EU 2.", 8000);
		metaItem2.registerItem(3, "Test Item 3", 1000765000, 4, "I Hold EU 3.", 32000);
		metaItem2.registerItem(4, "Whirlygig", 1043644000, (short) 5, "Spin me right round.", EnumRarity.rare, EnumChatFormatting.DARK_GREEN, true);
		metaItem2.registerItem(5, "Whirlygig 2", 2124867000, (short) 7, "Spin me right round.", EnumRarity.uncommon, EnumChatFormatting.RED, true);

		//Create Multi-tools
		ItemsMultiTools.load();

		//Just an unusual plate needed for some black magic.
		itemPlateClay = new BaseItemPlate(MaterialUtils.generateMaterialFromGtENUM(Materials.Clay));
		itemDoublePlateClay = new BaseItemPlateDouble(MaterialUtils.generateMaterialFromGtENUM(Materials.Clay));

		//A small gear needed for wizardry.
		if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("gearGtSmallWroughtIron", 1) == null){
			itemSmallWroughtIronGear = new BaseItemSmallGear(MaterialUtils.generateMaterialFromGtENUM(Materials.WroughtIron));
		}

		//A plate of Lithium.
		if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateLithium", 1) == null){
			itemPlateLithium = new BaseItemPlate(MaterialUtils.generateMaterialFromGtENUM(Materials.Lithium));
		}

		//A plate of Europium.
		if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateEuropium", 1) == null && CORE.configSwitches.enableCustom_Pipes){
			itemPlateEuropium = new BaseItemPlate(MaterialUtils.generateMaterialFromGtENUM(Materials.Europium));
		}
		if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateDoubleEuropium", 1) == null && CORE.configSwitches.enableCustom_Pipes){
			itemDoublePlateEuropium = new BaseItemPlateDouble(MaterialUtils.generateMaterialFromGtENUM(Materials.Europium));
		}

		itemBoilerChassis = new itemBoilerChassis();
		itemDehydratorCoilWire = new itemDehydratorCoilWire();
		itemDehydratorCoil = new itemDehydratorCoil();

		itemAirFilter = new ItemAirFilter();
		itemLavaFilter = new ItemLavaFilter();

		//Chemistry
		CoalTar.run();

		//Misc Items
		Item tI;
		tI = new BaseItemMisc("Chilly", new short[]{0,64,196}, 32, MiscTypes.POTION, new String[]{"It's Blue"});
		tI = new BaseItemMisc("4000DC's", new short[]{180,100,30}, 1, MiscTypes.BIGKEY, new String[]{"It opens things."});
		tI = new BaseItemMisc("Dull", new short[]{64,64,64}, 64, MiscTypes.GEM, null);
		tI = new BaseItemMisc("Forest", new short[]{130,164,96}, 64, MiscTypes.MUSHROOM, new String[]{"You Found this on the ground.", "Definitely not sure if it's worth eating."});

		//EnderIO Resources
		if ((LoadedMods.EnderIO || LOAD_ALL_CONTENT) && !CORE.GTNH){
			Utils.LOG_INFO("EnderIO Found - Loading Resources.");
			//Enderio Dusts
			itemDustSoularium = ItemUtils.generateSpecialUseDusts("Soularium", "Soularium", Utils.rgbtoHexValue(95,90,54))[0];
			itemDustRedstoneAlloy = ItemUtils.generateSpecialUseDusts("RedstoneAlloy", "Redstone Alloy", Utils.rgbtoHexValue(178,34,34))[0];
			itemDustElectricalSteel = ItemUtils.generateSpecialUseDusts("ElectricalSteel", "Electrical Steel", Utils.rgbtoHexValue(194,194,194))[0];
			itemDustPulsatingIron = ItemUtils.generateSpecialUseDusts("PulsatingIron", "Pulsating Iron", Utils.rgbtoHexValue(50,91,21))[0];
			itemDustEnergeticAlloy = ItemUtils.generateSpecialUseDusts("EnergeticAlloy", "Energetic Alloy", Utils.rgbtoHexValue(252,151,45))[0];
			itemDustVibrantAlloy = ItemUtils.generateSpecialUseDusts("VibrantAlloy", "Vibrant Alloy", Utils.rgbtoHexValue(204,242,142))[0];
			itemDustConductiveIron = ItemUtils.generateSpecialUseDusts("ConductiveIron", "Conductive Iron", Utils.rgbtoHexValue(164,109,100))[0];

			//EnderIO Plates
			itemPlateSoularium = ItemUtils.generateSpecialUsePlate("itemPlate"+"Soularium", "Soularium", new short[]{95, 90, 54}, 0);
			itemPlateRedstoneAlloy = ItemUtils.generateSpecialUsePlate("itemPlate"+"RedstoneAlloy", "Redstone Alloy", new short[]{178,34,34}, 0);
			itemPlateElectricalSteel = ItemUtils.generateSpecialUsePlate("itemPlate"+"ElectricalSteel", "Electrical Steel", new short[]{194, 194, 194}, 0);
			itemPlatePulsatingIron = ItemUtils.generateSpecialUsePlate("itemPlate"+"PhasedIron", "Phased Iron", new short[]{50, 91, 21}, 0);
			itemPlateEnergeticAlloy = ItemUtils.generateSpecialUsePlate("itemPlate"+"EnergeticAlloy", "Energetic Alloy", new short[]{252, 152, 45}, 0);
			itemPlateVibrantAlloy = ItemUtils.generateSpecialUsePlate("itemPlate"+"VibrantAlloy", "Vibrant Alloy", new short[]{204, 242, 142}, 0);
			itemPlateConductiveIron = ItemUtils.generateSpecialUsePlate("itemPlate"+"ConductiveIron", "Conductive Iron", new short[]{164, 109, 100}, 0);		

			//Register dumb naming conventions - Who chose fucking phased Iron/Gold?
			GT_OreDictUnificator.registerOre("dustPhasedGold", ItemUtils.getSimpleStack(itemDustVibrantAlloy));
			GT_OreDictUnificator.registerOre("platePhasedGold", ItemUtils.getSimpleStack(itemPlateVibrantAlloy));
			GT_OreDictUnificator.registerOre("dustPhasedIron", ItemUtils.getSimpleStack(itemDustPulsatingIron));
			GT_OreDictUnificator.registerOre("platePhasedIron", ItemUtils.getSimpleStack(itemPlatePulsatingIron));
		}
		else {
			Utils.LOG_WARNING("EnderIO not Found - Skipping Resources.");
		}

		//Big Reactors
		if (LoadedMods.Big_Reactors|| LOAD_ALL_CONTENT){
			Utils.LOG_INFO("BigReactors Found - Loading Resources.");
			//Item Init			
			itemPlateBlutonium = ItemUtils.generateSpecialUsePlate("itemPlate"+"Blutonium", "Blutonium", new short[]{0, 0, 255}, 0);
			itemPlateBlutonium = ItemUtils.generateSpecialUsePlate("itemPlate"+"Cyanite", "Cyanite", new short[]{0, 191, 255}, 0);
			itemPlateLudicrite = ItemUtils.generateSpecialUsePlate("itemPlate"+"Ludicrite", "Ludicrite", new short[]{167, 5, 179}, 0);
		}
		else {
			Utils.LOG_WARNING("BigReactors not Found - Skipping Resources.");
		}

		//Thaumcraft
		if ((LoadedMods.Thaumcraft|| LOAD_ALL_CONTENT) && !CORE.GTNH){
			Utils.LOG_INFO("Thaumcraft Found - Loading Resources.");
			//Item Init
			try {
				ItemUtils.getItemForOreDict("Thaumcraft:ItemResource", "ingotVoidMetal", "Void Metal Ingot", 16);
				itemPlateVoidMetal = ItemUtils.generateSpecialUsePlate("itemPlate"+"Void", "Void", new short[]{82, 17, 82}, 0);			
				GT_OreDictUnificator.registerOre("plateVoidMetal", new ItemStack(ModItems.itemPlateVoidMetal));
			} catch (final NullPointerException e){
				e.getClass();
			}

		}
		else {
			Utils.LOG_WARNING("Thaumcraft not Found - Skipping Resources.");
		}

		//ExtraUtils
		if (LoadedMods.Extra_Utils|| LOAD_ALL_CONTENT){
			Utils.LOG_INFO("ExtraUtilities Found - Loading Resources.");
			try {
				//MaterialGenerator.generate(ALLOY.BEDROCKIUM);
			} catch (final NullPointerException e){
				e.getClass();
			}
		}
		else {
			Utils.LOG_WARNING("ExtraUtilities not Found - Skipping Resources.");
		}

		//Pneumaticraft
		if (LoadedMods.PneumaticCraft|| LOAD_ALL_CONTENT){
			Utils.LOG_INFO("PneumaticCraft Found - Loading Resources.");
			//Item Init
			itemPlateCompressedIron = ItemUtils.generateSpecialUsePlate("itemPlate"+"CompressedIron", "Compressed Iron", new short[]{128, 128, 128}, 0);
		}
		else {
			Utils.LOG_WARNING("PneumaticCraft not Found - Skipping Resources.");
		}

		//Simply Jetpacks
		if (LoadedMods.Simply_Jetpacks|| LOAD_ALL_CONTENT){
			Utils.LOG_INFO("SimplyJetpacks Found - Loading Resources.");
			//Item Init
			itemPlateEnrichedSoularium = new RarityUncommon().setUnlocalizedName("itemPlateEnrichedSoularium").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateSoularium");
			//Registry
			GameRegistry.registerItem(itemPlateEnrichedSoularium, "itemPlateEnrichedSoularium");
		}
		else {
			Utils.LOG_WARNING("SimplyJetpacks not Found - Skipping Resources.");
		}

		//rfTools
		if (LoadedMods.RFTools|| LOAD_ALL_CONTENT){
			Utils.LOG_INFO("rfTools Found - Loading Resources.");
			//Item Init
			itemPlateDimensionShard = ItemUtils.generateSpecialUsePlate("itemPlate"+"DimensionShard", "Dimensional Shard", new short[]{170, 230, 230}, 0);			
		}
		else {
			Utils.LOG_WARNING("rfTools not Found - Skipping Resources.");
		}

		//IC2 Exp
		if (LoadedMods.IndustrialCraft2|| LOAD_ALL_CONTENT){
			Utils.LOG_INFO("IndustrialCraft2 Found - Loading Resources.");
			RfEuBattery = new RF2EU_Battery();

			//Baubles Mod Test
			try {final Class baublesTest = Class.forName("baubles.api.IBauble");
			if (baublesTest != null){
				COMPAT_Baubles.run();
			}
			else {
				Utils.LOG_INFO("Baubles Not Found - Skipping Resources.");
			}
			} catch(final Throwable T){
				Utils.LOG_INFO("Baubles Not Found - Skipping Resources.");
			}
		}
		else {
			Utils.LOG_WARNING("IndustrialCraft2 not Found - Skipping Resources.");
		}


		//Special Item Handling Case
		if (configSwitches.enableAlternativeBatteryAlloy) {
			//ModItems.itemIngotBatteryAlloy = new BaseItemIngot("itemIngotBatteryAlloy", "Battery Alloy", new short[]{35, 228, 141}, 0); TODO
			ModItems.itemPlateBatteryAlloy = ItemUtils.generateSpecialUsePlate("itemPlateBatteryAlloy", "Battery Alloy", new short[]{35, 228, 141}, 0);

		}


		//UtilsItems.generateSpawnEgg("ic2", "boatcarbon", Utils.generateSingularRandomHexValue(), Utils.generateSingularRandomHexValue());



		/*
		 * Misc Items
		 */

		//Staballoy Equipment
		itemStaballoyPickaxe = new StaballoyPickaxe("itemStaballoyPickaxe", STABALLOY).setCreativeTab(AddToCreativeTab.tabTools);
		GameRegistry.registerItem(itemStaballoyPickaxe, itemStaballoyPickaxe.getUnlocalizedName());
		itemStaballoyAxe = new StaballoyAxe("itemStaballoyAxe", STABALLOY).setCreativeTab(AddToCreativeTab.tabTools);
		GameRegistry.registerItem(itemStaballoyAxe, itemStaballoyAxe.getUnlocalizedName());

		//Sandstone Hammer
		itemSandstoneHammer = new SandstoneHammer("itemSandstoneHammer").setCreativeTab(AddToCreativeTab.tabTools);
		GameRegistry.registerItem(itemSandstoneHammer, itemSandstoneHammer.getUnlocalizedName());

		//Buffer Cores!
		Item itemBufferCore;
		for(int i=1; i<=10; i++){
			//Utils.LOG_INFO(""+i);
			itemBufferCore = new BufferCore("itemBufferCore", i).setCreativeTab(AddToCreativeTab.tabMachines);
			GameRegistry.registerItem(itemBufferCore, itemBufferCore.getUnlocalizedName()+i);
			//System.out.println("Buffer Core registration count is: "+i);
		}

		itemPLACEHOLDER_Circuit = new Item().setUnlocalizedName("itemPLACEHOLDER_Circuit").setTextureName(CORE.MODID + ":itemPLACEHOLDER_Circuit");
		GameRegistry.registerItem(itemPLACEHOLDER_Circuit, "itemPLACEHOLDER_Circuit");

		//ItemBlockGtFrameBox = new ItemBlockGtFrameBox(ModBlocks.blockGtFrameSet1);
		//GameRegistry.registerItem(ItemBlockGtFrameBox, "itemGtFrameBoxSet1");
	}
}
