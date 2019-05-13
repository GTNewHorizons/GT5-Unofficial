package gtPlusPlus.core.item;
import static gtPlusPlus.core.creative.AddToCreativeTab.tabMachines;
import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;
import static gtPlusPlus.core.lib.CORE.LOAD_ALL_CONTENT;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.common.compat.COMPAT_Baubles;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.BaseEuItem;
import gtPlusPlus.core.item.base.BaseItemBackpack;
import gtPlusPlus.core.item.base.BaseItemBurnable;
import gtPlusPlus.core.item.base.BaseItemComponent.ComponentTypes;
import gtPlusPlus.core.item.base.BaseItemDamageable;
import gtPlusPlus.core.item.base.BaseItemTCShard;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.item.base.foil.BaseItemFoil;
import gtPlusPlus.core.item.base.foods.BaseItemFood;
import gtPlusPlus.core.item.base.foods.BaseItemHotFood;
import gtPlusPlus.core.item.base.gears.BaseItemSmallGear;
import gtPlusPlus.core.item.base.ingots.BaseItemIngot;
import gtPlusPlus.core.item.base.ingots.BaseItemIngot_OLD;
import gtPlusPlus.core.item.base.misc.BaseItemMisc;
import gtPlusPlus.core.item.base.misc.BaseItemMisc.MiscTypes;
import gtPlusPlus.core.item.base.plates.BaseItemPlate;
import gtPlusPlus.core.item.base.plates.BaseItemPlateDouble;
import gtPlusPlus.core.item.bauble.BatteryPackBaseBauble;
import gtPlusPlus.core.item.bauble.HealthBoostBauble;
import gtPlusPlus.core.item.bauble.ModularBauble;
import gtPlusPlus.core.item.bauble.MonsterKillerBaseBauble;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.item.chemistry.CoalTar;
import gtPlusPlus.core.item.chemistry.GenericChem;
import gtPlusPlus.core.item.chemistry.IonParticles;
import gtPlusPlus.core.item.chemistry.NuclearChem;
import gtPlusPlus.core.item.chemistry.RocketFuels;
import gtPlusPlus.core.item.chemistry.StandardBaseParticles;
import gtPlusPlus.core.item.crafting.ItemDummyResearch;
import gtPlusPlus.core.item.effects.RarityUncommon;
import gtPlusPlus.core.item.general.BaseItemGrindle;
import gtPlusPlus.core.item.general.BufferCore;
import gtPlusPlus.core.item.general.ItemAirFilter;
import gtPlusPlus.core.item.general.ItemAreaClear;
import gtPlusPlus.core.item.general.ItemBasicFirestarter;
import gtPlusPlus.core.item.general.ItemBlueprint;
import gtPlusPlus.core.item.general.ItemControlCore;
import gtPlusPlus.core.item.general.ItemEmpty;
import gtPlusPlus.core.item.general.ItemGemShards;
import gtPlusPlus.core.item.general.ItemGenericToken;
import gtPlusPlus.core.item.general.ItemGiantEgg;
import gtPlusPlus.core.item.general.ItemHalfCompleteCasings;
import gtPlusPlus.core.item.general.ItemLavaFilter;
import gtPlusPlus.core.item.general.RF2EU_Battery;
import gtPlusPlus.core.item.general.books.ItemBaseBook;
import gtPlusPlus.core.item.general.capture.ItemEntityCatcher;
import gtPlusPlus.core.item.general.chassis.ItemBoilerChassis;
import gtPlusPlus.core.item.general.chassis.ItemDehydratorCoil;
import gtPlusPlus.core.item.general.chassis.ItemDehydratorCoilWire;
import gtPlusPlus.core.item.general.throwables.ItemHydrofluoricAcidPotion;
import gtPlusPlus.core.item.general.throwables.ItemSulfuricAcidPotion;
import gtPlusPlus.core.item.general.throwables.ItemThrowableBomb;
import gtPlusPlus.core.item.init.ItemsFoods;
import gtPlusPlus.core.item.materials.DustDecayable;
import gtPlusPlus.core.item.tool.misc.DebugScanner;
import gtPlusPlus.core.item.tool.misc.GregtechPump;
import gtPlusPlus.core.item.tool.misc.SandstoneHammer;
import gtPlusPlus.core.item.tool.misc.box.AutoLunchBox;
import gtPlusPlus.core.item.tool.misc.box.MagicToolBag;
import gtPlusPlus.core.item.tool.misc.box.UniversalToolBox;
import gtPlusPlus.core.item.tool.staballoy.MultiPickaxeBase;
import gtPlusPlus.core.item.tool.staballoy.MultiSpadeBase;
import gtPlusPlus.core.item.tool.staballoy.StaballoyAxe;
import gtPlusPlus.core.item.tool.staballoy.StaballoyPickaxe;
import gtPlusPlus.core.item.wearable.WearableLoader;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.CORE.ConfigSwitches;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.NONMATERIAL;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.debug.DEBUG_INIT;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.everglades.GTplusplus_Everglades;
import gtPlusPlus.xmod.eio.material.MaterialEIO;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechItems;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
public final class ModItems {


	public static ToolMaterial STABALLOY = EnumHelper.addToolMaterial("Staballoy", 3, 2500, 7, 1.0F, 18);
	
	public static Item ZZZ_Empty;
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
	
	public static Item dustTumbagaMix;

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
	public static Item shardDull;

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

	public static Item dustFertUN18;
	public static Item dustFertUN32;

	public static Fluid fluidFLiBeSalt;


	//Possibly missing base items that GT may be missing.

	public static Item itemSmallWroughtIronGear;
	public static Item itemPlateRawMeat;
	public static Item itemPlateClay;
	public static Item itemPlateLithium;
	public static Item itemPlateEuropium;
	public static Item itemPlateVanadium;
	public static Item itemDoublePlateClay;
	public static Item itemDoublePlateEuropium;
	public static Item itemFoilUranium235;
	public static BlockBaseModular blockRawMeat;
	
	public static Item itemBoilerChassis;
	public static Item itemDehydratorCoilWire;
	public static Item itemDehydratorCoil;

	public static Item itemLavaFilter;
	public static Item itemAirFilter;

	public static Item itemCoalCoke;

	public static Item itemCircuitLFTR;

	public static Item itemDebugAreaClear;

	public static Item itemGemShards;
	public static Item itemHalfCompleteCasings;

	public static Item itemSulfuricPotion;
	public static Item itemHydrofluoricPotion;

	public static Item itemModularBauble;
	public static Item itemCustomBook;

	public static Item itemGrindleTablet;
	public static Item itemRope;
	public static Item itemFiber;
	public static Item itemDragonJar;

	//Unstable Elements & Related Content
	public static Item dustNeptunium238;
	public static Item dustDecayedRadium226;
	public static Item dustRadium226;
	
	public static Item itemBigEgg;

	public static GregtechPump toolGregtechPump;

	public static ItemGenericToken itemGenericToken;

	public static Item itemControlCore;

	public static ItemStack itemHotTitaniumIngot;

	public static Fluid fluidZrF4;

	public static Item boxTools;
	public static Item boxFood;
	public static Item boxMagic;

	public static DustDecayable dustMolybdenum99;
	public static DustDecayable dustTechnetium99;
	public static DustDecayable dustTechnetium99M;

	public static IonParticles itemIonParticleBase;
	public static StandardBaseParticles itemStandarParticleBase;

	public static BatteryPackBaseBauble itemChargePack_Low_1;
	public static BatteryPackBaseBauble itemChargePack_Low_2;
	public static BatteryPackBaseBauble itemChargePack_Low_3;
	public static BatteryPackBaseBauble itemChargePack_Low_4;
	public static BatteryPackBaseBauble itemChargePack_Low_5;
	public static BatteryPackBaseBauble itemChargePack_High_1;
	public static BatteryPackBaseBauble itemChargePack_High_2;
	public static BatteryPackBaseBauble itemChargePack_High_3;
	public static BatteryPackBaseBauble itemChargePack_High_4;

	public static DebugScanner itemDebugScanner;

	public static ItemDummyResearch itemDummyResearch;

	public static CoreItem itemBombCasing;
	public static CoreItem itemBombUnf;
	public static CoreItem itemDetCable;
	public static ItemThrowableBomb itemBomb;

	public static MonsterKillerBaseBauble itemAmuletMonsterKiller_Zombie;
	public static MonsterKillerBaseBauble itemAmuletMonsterKiller_Skeleton;
	public static MonsterKillerBaseBauble itemAmuletMonsterKiller_Spider;
	public static MonsterKillerBaseBauble itemAmuletMonsterKiller_Creeper;
	public static MonsterKillerBaseBauble itemAmuletMonsterKiller_Enderman;
	
	public static MonsterKillerBaseBauble itemAmuletMonsterKiller_Nether;
	public static MonsterKillerBaseBauble itemAmuletMonsterKiller_Infernal;

	static {
		Logger.INFO("Items!");
		//Default item used when recipes fail, handy for debugging. Let's make sure they exist when this class is called upon.
		AAA_Broken = new BaseItemIngot_OLD("AAA_Broken", "Errors - Tell Alkalus", Utils.rgbtoHexValue(128, 128, 128), 0);
		ZZZ_Empty = new ItemEmpty();
	}

	public static final void init(){
		
		itemDebugScanner = new DebugScanner();
		
		itemAlkalusDisk = new BaseItemDamageable("itemAlkalusDisk", AddToCreativeTab.tabMisc, 1, 0, "Unknown Use", EnumRarity.rare, EnumChatFormatting.AQUA, false, null);
		itemBigEgg = new ItemGiantEgg("itemBigEgg", "Ginourmous Chicken Egg", tabMisc, 64, 0, "I had best try disassemble this.. for science!", "fuelLargeChickenEgg", 5000, 0).setTextureName(CORE.MODID + ":itemBigEgg");
		itemGenericToken = new ItemGenericToken();
		itemDummyResearch = new ItemDummyResearch();
		
		//Debug Loading
		if (CORE.DEBUG){
			DEBUG_INIT.registerItems();
		}

		itemDebugAreaClear = new ItemAreaClear();

		//Register meta item, because we need them for everything.
		MetaGeneratedGregtechItems.INSTANCE.generateMetaItems();

		//Some Simple forms of materials
		itemStickyRubber = new Item().setUnlocalizedName("itemStickyRubber").setCreativeTab(tabMachines).setTextureName(CORE.MODID + ":itemStickyRubber");
		GameRegistry.registerItem(itemStickyRubber, "itemStickyRubber");
		GT_OreDictUnificator.registerOre("ingotRubber", ItemUtils.getItemStackFromFQRN(CORE.MODID+":itemStickyRubber", 1));


		itemCoalCoke = new BaseItemBurnable("itemCoalCoke", "Coking Coal", tabMisc, 64, 0, "Used for metallurgy.", "fuelCoke", 3200, 0).setTextureName(CORE.MODID + ":itemCoalCoke");

		//Register Hydrogen Blobs first, so we can replace old helium blobs.
		itemHydrogenBlob = new CoreItem("itemHydrogenBlob", "Mysterious Hydrogen Blob", tabMisc).setTextureName(CORE.MODID + ":itemHeliumBlob");
		//Register Old Helium Blob, this will be replaced when held by a player.
		itemHeliumBlob = new CoreItem("itemHeliumBlob", tabMisc, ItemUtils.getSimpleStack(itemHydrogenBlob)).setTextureName(CORE.MODID + ":itemHydrogenBlob");

		//Register this neato device, for making some fires.
		itemBasicFireMaker = new ItemBasicFirestarter();

		//Register Rope
		itemFiber = new CoreItem("itemFiber", "Plant Fiber", tabMisc);
		itemRope = new CoreItem("itemRope", "Rope", tabMisc);
		
		//Load Wearable Items
		WearableLoader.run();

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
		
		
		//Load Custom Box/bags
		boxTools = new UniversalToolBox("Tool Box");
		boxFood = new AutoLunchBox("Eatotron-9000");
		boxMagic = new MagicToolBag("Mystic Bag");

		itemBlueprintBase = new ItemBlueprint("itemBlueprint");

		itemGemShards = new ItemGemShards("itemGemShards", "Gem Shards", AddToCreativeTab.tabMisc, 32, 0, "They glitter in the light", EnumRarity.rare, EnumChatFormatting.GRAY, false, Utils.rgbtoHexValue(182, 114, 18)).setTextureName(CORE.MODID + ":itemHeliumBlob");
		itemHalfCompleteCasings = new ItemHalfCompleteCasings("itemHalfCompleteCasings", AddToCreativeTab.tabMisc, 32, 0, "This isn't quite finished yet.", EnumRarity.common, EnumChatFormatting.GRAY, false, Utils.rgbtoHexValue(255, 255, 255)).setTextureName("gregtech" + ":" + "gt.metaitem.01/" + "761");
		itemSulfuricPotion = new ItemSulfuricAcidPotion("itemSulfuricPotion", "Throwable Vial of Sulfuric Acid", "Burn your foes alive!").setTextureName(CORE.MODID + ":itemSulfuricAcidPotion");
		itemHydrofluoricPotion = new ItemHydrofluoricAcidPotion("itemHydrofluoricPotion", "Throwable Vial of Hydrofluoric Acid", "They won't see this coming, nor anything after!").setTextureName(CORE.MODID + ":itemPotion");
		//Start meta Item Generation
		ItemsFoods.load();


		try{

			/**
			 * Try generate dusts for missing rare earth materials if they don't exist
			 */

			if (!ItemUtils.checkForInvalidItems(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustGadolinium", 1))){
				ItemUtils.generateSpecialUseDusts("Gadolinium", "Gadolinium", Materials.Gadolinium.mElement.name(), Utils.rgbtoHexValue(226, 172, 9));
			}
			if (!ItemUtils.checkForInvalidItems(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustYtterbium", 1))){
				ItemUtils.generateSpecialUseDusts("Ytterbium", "Ytterbium", Materials.Ytterbium.mElement.name(), Utils.rgbtoHexValue(Materials.Yttrium.mRGBa[0]-60, Materials.Yttrium.mRGBa[1]-60, Materials.Yttrium.mRGBa[2]-60));
			}
			if (!ItemUtils.checkForInvalidItems(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustSamarium", 1))){
				ItemUtils.generateSpecialUseDusts("Samarium", "Samarium", Materials.Samarium.mElement.name(), Utils.rgbtoHexValue(161, 168, 114));
			}
			if (!ItemUtils.checkForInvalidItems(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustLanthanum", 1))){
				ItemUtils.generateSpecialUseDusts("Lanthanum", "Lanthanum", Materials.Lanthanum.mElement.name(), Utils.rgbtoHexValue(106, 127, 163));
			}
			if (!ItemUtils.checkForInvalidItems(ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustGermanium", 1))){
		    	ItemUtils.generateSpecialUseDusts("Germanium", "Germanium", "Ge", ELEMENT.getInstance().GERMANIUM.getRgbAsHex());
			}


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
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().PROMETHIUM, false);
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
			MaterialGenerator.generate(ELEMENT.getInstance().LITHIUM7, false);
			//Thorium-232 is the most stable isotope of Thorium, purified for nuclear fuel use in this case.
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().THORIUM232);
			//Production of 233U (through the neutron irradiation of 232Th) invariably produces small amounts of 232U as an impurity
			//because of parasitic (n,2n) reactions on uranium-233 itself, or on protactinium-233, or on thorium-232:
			MaterialGenerator.generate(ELEMENT.getInstance().URANIUM232);
			//Uranium-233 is a fissile isotope of uranium that is bred from thorium-232 as part of the thorium fuel cycle.
			MaterialGenerator.generate(ELEMENT.getInstance().URANIUM233);
			//Plutonium-238 is a very powerful alpha emitter. This makes the plutonium-238 isotope suitable for usage in radioisotope thermoelectric generators (RTGs)
			//and radioisotope heater units - one gram of plutonium-238 generates approximately 0.5 W of thermal power.
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().PLUTONIUM238, false);
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustPlutonium239", 1) == null || Utils.getGregtechVersionAsInt() < 50931) {
				MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().PLUTONIUM239, false);
			}

			//RTG Fuel Materials
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().STRONTIUM90, false);
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().POLONIUM210, false);
			MaterialGenerator.generateNuclearMaterial(ELEMENT.getInstance().AMERICIUM241, false);
			
			
			//Custom Materials that will have standalone refinery processes
			MaterialGenerator.generate(ELEMENT.STANDALONE.ADVANCED_NITINOL, false);
			MaterialGenerator.generate(ELEMENT.STANDALONE.ASTRAL_TITANIUM);
			MaterialGenerator.generate(ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN);
			MaterialGenerator.generate(ELEMENT.STANDALONE.HYPOGEN);
			MaterialGenerator.generate(ELEMENT.STANDALONE.CHRONOMATIC_GLASS);
			
			//Custom Materials that are from Runescape
			MaterialGenerator.generate(ELEMENT.STANDALONE.BLACK_METAL);
			MaterialGenerator.generate(ELEMENT.STANDALONE.WHITE_METAL);
			MaterialGenerator.generate(ELEMENT.STANDALONE.GRANITE, false, false);
			MaterialGenerator.generate(ELEMENT.STANDALONE.RUNITE, false);
			MaterialGenerator.generate(ELEMENT.STANDALONE.DRAGON_METAL, false);
			
			MISC_MATERIALS.run();
			

			//Carbides - Tungsten Carbide exists in .09 so don't generate it. - Should still come before alloys though
			if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
				MaterialGenerator.generate(ALLOY.TUNGSTEN_CARBIDE);
			}
			MaterialGenerator.generate(ALLOY.SILICON_CARBIDE);
			MaterialGenerator.generate(ALLOY.ZIRCONIUM_CARBIDE);
			MaterialGenerator.generate(ALLOY.TANTALUM_CARBIDE);
			MaterialGenerator.generate(ALLOY.NIOBIUM_CARBIDE);
			MaterialGenerator.generate(ALLOY.TUNGSTEN_TITANIUM_CARBIDE);

			//LFTR Fuel components
			MaterialGenerator.generate(FLUORIDES.HYDROXIDE); //LFTR fuel component
			MaterialGenerator.generate(FLUORIDES.AMMONIA); //LFTR fuel component
			MaterialGenerator.generate(FLUORIDES.AMMONIUM); //LFTR fuel component
			MaterialGenerator.generate(FLUORIDES.AMMONIUM_BIFLUORIDE); //LFTR fuel component
			MaterialGenerator.generate(FLUORIDES.BERYLLIUM_HYDROXIDE); //LFTR fuel component
			MaterialGenerator.generate(FLUORIDES.AMMONIUM_TETRAFLUOROBERYLLATE); //LFTR fuel component

			//Generate Fluorides
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
			MaterialGenerator.generateNuclearMaterial(NUCLIDE.LiFBeF2ZrF4U235, false);
			MaterialGenerator.generateNuclearMaterial(NUCLIDE.LiFBeF2ZrF4UF4, false);
			MaterialGenerator.generateNuclearMaterial(NUCLIDE.LiFBeF2ThF4UF4, false);

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
			MaterialGenerator.generate(ALLOY.AQUATIC_STEEL);

			MaterialGenerator.generate(ALLOY.NITINOL_60);

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

			//Super Conductor
			MaterialGenerator.generate(ALLOY.HG1223, false, false);

			//Generate Fictional Materials
			if (!CORE.GTNH) {
				MaterialGenerator.generate(ELEMENT.getInstance().TRINIUM, false);
				MaterialGenerator.generate(ELEMENT.getInstance().TRINIUM_REFINED, false);
			}
			MaterialGenerator.generate(ALLOY.TRINIUM_TITANIUM);
			MaterialGenerator.generate(ALLOY.TRINIUM_NAQUADAH, false);
			MaterialGenerator.generate(ALLOY.TRINIUM_NAQUADAH_CARBON);
			MaterialGenerator.generate(ALLOY.TRINIUM_REINFORCED_STEEL);
			
			//Top Tier Alloys
			MaterialGenerator.generate(ALLOY.LAFIUM);
			MaterialGenerator.generate(ALLOY.CINOBITE);
			MaterialGenerator.generate(ALLOY.PIKYONIUM);
			MaterialGenerator.generate(ALLOY.ABYSSAL);
			

			MaterialGenerator.generate(ALLOY.TITANSTEEL);
			MaterialGenerator.generate(ALLOY.ARCANITE);
			MaterialGenerator.generate(ALLOY.OCTIRON);			

			MaterialGenerator.generate(ALLOY.BABBIT_ALLOY, false);
			MaterialGenerator.generate(ALLOY.BLACK_TITANIUM, false);

			// High Level Bioplastic
			MaterialGenerator.generate(ELEMENT.STANDALONE.RHUGNOR, false, false);
			
			
			
			
			
			
			
			
			//Must be the final Alloy to Generate
			MaterialGenerator.generate(ALLOY.QUANTUM);

			//Ores
			MaterialGenerator.generateOreMaterial(FLUORIDES.FLUORITE);
			MaterialGenerator.generateOreMaterial(ALLOY.KOBOLDITE);
			GTplusplus_Everglades.GenerateOreMaterials();


		} catch (final Throwable r){
			Logger.INFO("Failed to Generated a Material. "+r.getMessage());
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
		dustLithiumCarbonate = ItemUtils.generateSpecialUseDusts("LithiumCarbonate", "Lithium Carbonate", "Li2CO3", Utils.rgbtoHexValue(240, 240, 240))[0]; //https://en.wikipedia.org/wiki/Lithium_carbonate
		dustLithiumPeroxide = ItemUtils.generateSpecialUseDusts("LithiumPeroxide", "Lithium Peroxide", "Li2O2", Utils.rgbtoHexValue(250, 250, 250))[0]; //https://en.wikipedia.org/wiki/Lithium_peroxide
		dustLithiumHydroxide = ItemUtils.generateSpecialUseDusts("LithiumHydroxide", "Lithium Hydroxide", "LiOH", Utils.rgbtoHexValue(250, 250, 250))[0]; //https://en.wikipedia.org/wiki/Lithium_hydroxide

		if (!ItemUtils.checkForInvalidItems(ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 1)) && !LoadedMods.IHL){
			dustQuicklime = ItemUtils.generateSpecialUseDusts("Quicklime", "Quicklime", "CaO", Utils.rgbtoHexValue(255, 255, 175))[0]; //https://en.wikipedia.org/wiki/Calcium_oxide
		}
		dustCalciumHydroxide = ItemUtils.generateSpecialUseDusts("CalciumHydroxide", "Hydrated Lime", "Ca(OH)2", Utils.rgbtoHexValue(255, 255, 255))[0]; //https://en.wikipedia.org/wiki/Calcium_hydroxide
		dustCalciumCarbonate = ItemUtils.generateSpecialUseDusts("CalciumCarbonate", "Calcium Carbonate", "CaCO3", Utils.rgbtoHexValue(255, 255, 255))[0]; //https://en.wikipedia.org/wiki/Calcium_carbonate
		if ((ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustGypsum", 1) == null) || (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustCalciumSulfate", 1) == null)){
			dustCalciumSulfate = ItemUtils.generateSpecialUseDusts("Gypsum", "Calcium Sulfate (Gypsum)", "CaSO4", Utils.rgbtoHexValue(255, 255, 255))[0]; //https://en.wikipedia.org/wiki/Calcium_sulfate
			GT_OreDictUnificator.registerOre("dustCalciumSulfate", ItemUtils.getSimpleStack(dustCalciumSulfate));
		}
		else {
			GT_OreDictUnificator.registerOre("dustCalciumSulfate", ItemUtils.getItemStackOfAmountFromOreDictNoBroken("dustGypsum", 1));
		}
		dustLi2CO3CaOH2 = ItemUtils.generateSpecialUseDusts("Li2CO3CaOH2", "Li2CO3 + Ca(OH)2 Compound", "Li2CO3CaOH2", Utils.rgbtoHexValue(255, 255, 255))[0]; //https://en.wikipedia.org/wiki/Calcium_carbonate

		//FLiBe Fuel Compounds
		dustLi2BeF4 = ItemUtils.generateSpecialUseDusts("Li2BeF4", "Li2BeF4 Fuel Compound", "Li2BeF4", Utils.rgbtoHexValue(255, 255, 255))[0]; //https://en.wikipedia.org/wiki/FLiBe
		//fluidFLiBeSalt = ("Li2BeF4", "Li2BeF4", 7430, new short[]{255, 255, 255, 100}, 0);
		fluidFLiBeSalt = FluidUtils.addGTFluidNoPrefix("Li2BeF4", "Li2BeF4", new short[]{255, 255, 255, 100}, 0, 743, null, CI.emptyCells(1), 1000, true);

		//LFTR Control Circuit
		itemCircuitLFTR = new CoreItem("itemCircuitLFTR", ""+EnumChatFormatting.GREEN+"Control Circuit", AddToCreativeTab.tabMisc, 1, 0,  new String[] {"Keeps Multiblocks Stable"}, EnumRarity.epic, EnumChatFormatting.DARK_GREEN, false, null);


		//Zirconium
		//Cinter Pellet.
		itemZirconiumChlorideCinterPellet = new CoreItem("itemZirconiumPellet", "Zirconium Pellet ["+StringUtils.subscript("ZrCl4")+"]", tabMisc).setTextureName(CORE.MODID + ":itemShard");
		GT_OreDictUnificator.registerOre("pelletZirconium", new ItemStack(itemZirconiumChlorideCinterPellet));
		//Zirconium Chloride
		dustZrCl4 = ItemUtils.generateSpecialUseDusts("ZrCl4", "ZrCl4", "ZrCl4", Utils.rgbtoHexValue(180, 180, 180))[0]; //http://www.iaea.org/inis/collection/NCLCollectionStore/_Public/39/036/39036750.pdf
		dustCookedZrCl4 = ItemUtils.generateSpecialUseDusts("CookedZrCl4", "Cooked ZrCl4", "ZrCl4", Utils.rgbtoHexValue(180, 180, 180))[0]; //http://www.iaea.org/inis/collection/NCLCollectionStore/_Public/39/036/39036750.pdf		
		
		//Zirconium Tetrafluoride
		/*GT_OreDictUnificator.registerOre("cellZrF4", ItemUtils.getItemStackOfAmountFromOreDict("cellZirconiumTetrafluoride", 1));
		GT_OreDictUnificator.registerOre("dustZrF4", ItemUtils.getItemStackOfAmountFromOreDict("dustZirconiumTetrafluoride", 1));*/
		//GT_OreDictUnificator.registerOre("cellZrF4", ItemUtils.getItemStackOfAmountFromOreDict("cellZirconiumTetrafluoride", 1));
		//GT_OreDictUnificator.registerOre("dustZrF4", ItemUtils.getItemStackOfAmountFromOreDict("dustZirconiumTetrafluoride", 1));
		fluidZrF4 = FluidUtils.generateFluidNoPrefix("ZirconiumTetrafluoride", "Zirconium Tetrafluoride", 500, new short[]{170, 170, 140, 100}); //https://en.wikipedia.org/wiki/Zirconium_tetrafluoride

		//Coolant Salt
		//NaBF4 - NaF - 621C
		//dustNaBF4NaF = ItemUtils.generateSpecialUseDusts("NaBF4NaF", "NaBF4NaF", Utils.rgbtoHexValue(45, 45, 90))[0]; //https://en.wikipedia.org/wiki/Zirconium_tetrafluoride


		//Load Tree Farmer
		if (CORE.ConfigSwitches.enableMultiblock_TreeFarmer){ //https://en.wikipedia.org/wiki/UAN
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
				FluidUtils.generateFluidNonMolten("Fertiliser", "Fertiliser", 32, new short[]{45, 170, 45, 100}, temp1, temp2, true);
			}
			FluidUtils.generateFluidNonMolten("UN32Fertiliser", "UN-32 Fertiliser", 24, new short[]{55, 190, 55, 100}, null, null, true);
			FluidUtils.generateFluidNonMolten("UN18Fertiliser", "UN-18 Fertiliser", 22, new short[]{60, 155, 60, 100}, null, null, true);

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

		//Juice
		FluidUtils.generateFluidNonMolten("RaisinJuice", "Raisin Juice", 2, new short[]{51, 0, 51, 100}, ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foodRaisins", 1), ItemUtils.getItemStackOfAmountFromOreDictNoBroken("fruitRaisins", 1), 50, true);


		//Test items
		metaItem2 = new BaseEuItem();
		metaItem2.registerItem(0, EnumChatFormatting.BLACK+"Test Item 0", 0, 0, "I am 0.");
		metaItem2.registerItem(1, EnumChatFormatting.GREEN+"Test Item 1", 1006346000, 1, "I Hold EU 1.", 500);
		metaItem2.registerItem(2, EnumChatFormatting.GOLD+"Test Item 2", 1004630000, 2, "I Hold EU 2.", 8000);
		metaItem2.registerItem(3, "Test Item 3", 1000765000, 4, "I Hold EU 3.", 32000);
		metaItem2.registerItem(4, "Whirlygig", 1043644000, (short) 5, "Spin me right round.", EnumRarity.rare, EnumChatFormatting.DARK_GREEN, true);
		metaItem2.registerItem(5, "Whirlygig 2", 2124867000, (short) 7, "Spin me right round.", EnumRarity.uncommon, EnumChatFormatting.RED, true);
		
		toolGregtechPump = new GregtechPump();
		toolGregtechPump.registerPumpType(0, "Simple Hand Pump", 0, 0);
		toolGregtechPump.registerPumpType(1, "Advanced Hand Pump", 32000, 1);
		toolGregtechPump.registerPumpType(2, "Super Hand Pump", 128000, 2);
		toolGregtechPump.registerPumpType(3, "Ultimate Hand Pump", 512000, 3);
		
		//Create Multi-tools
		//ItemsMultiTools.load();

		//Xp Fluids - Dev
		if (!FluidRegistry.isFluidRegistered("mobessence")){
			FluidUtils.generateFluidNoPrefix("mobessence", "mobessence", 0, new short[]{125, 175, 125, 100});
		}
		if (!FluidRegistry.isFluidRegistered("xpjuice")){
			FluidUtils.generateFluidNoPrefix("xpjuice", "xpjuice", 0, new short[]{50, 150, 50, 100});
		}

		//Just an unusual plate needed for some black magic.		
		if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateClay", 1) == null){
			itemPlateClay = new BaseItemPlate(NONMATERIAL.CLAY);
		}
		if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateDoubleClay", 1) == null){
			itemDoublePlateClay = new BaseItemPlateDouble(NONMATERIAL.CLAY);
		}

		//Need this for Mutagenic Frames
		if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foilUranium235", 1) == null){
			itemFoilUranium235 = new BaseItemFoil(ELEMENT.getInstance().URANIUM235);
		}

		//A small gear needed for wizardry.
		if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("gearGtSmallWroughtIron", 1) == null){
			itemSmallWroughtIronGear = new BaseItemSmallGear(NONMATERIAL.WROUGHT_IRON);
		}
		//Krypton Processing
		if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingotHotTitanium", 1) == null){
			itemHotTitaniumIngot = ItemUtils.getSimpleStack(new BaseItemIngot(ELEMENT.getInstance().TITANIUM, ComponentTypes.HOTINGOT));
		}
		else {
			itemHotTitaniumIngot = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingotHotTitanium", 1);
		}
		
		//Special Sillyness
		if (true) {
			
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateSodium", 1) == null){
				new BaseItemPlate(ELEMENT.getInstance().SODIUM);
			}
			
			Material meatRaw = NONMATERIAL.MEAT;
			// A plate of Meat.
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateMeatRaw", 1) == null){
				itemPlateRawMeat = new BaseItemPlate(meatRaw);
				ItemUtils.registerFuel(ItemUtils.getSimpleStack(itemPlateRawMeat), 100);
			}
			// A Block of Meat.
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("blockMeatRaw", 1) == null){
				blockRawMeat  = new BlockBaseModular(meatRaw, BlockTypes.STANDARD);
				ItemUtils.registerFuel(ItemUtils.getSimpleStack(blockRawMeat), 900);
			}
		}
		

		// A plate of Vanadium.
		if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateVanadium", 1) == null){
			itemPlateVanadium = new BaseItemPlate(ELEMENT.getInstance().VANADIUM);
		}

		//A plate of Lithium.
		if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateLithium", 1) == null){
			itemPlateLithium = new BaseItemPlate(ELEMENT.getInstance().LITHIUM);
		}

		//A plate of Europium.
		if ((ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateEuropium", 1) == null) && CORE.ConfigSwitches.enableCustom_Pipes){
			itemPlateEuropium = new BaseItemPlate(ELEMENT.getInstance().EUROPIUM);
		}
		if ((ItemUtils.getItemStackOfAmountFromOreDictNoBroken("plateDoubleEuropium", 1) == null) && CORE.ConfigSwitches.enableCustom_Pipes){
			itemDoublePlateEuropium = new BaseItemPlateDouble(ELEMENT.getInstance().EUROPIUM);
		}
		
		//Tumbaga Mix (For Simple Crafting)
		dustTumbagaMix = ItemUtils.generateSpecialUseDusts("MixTumbaga", "Tumbaga Mix", "Au2Cu", Utils.rgbtoHexValue(255, 150, 80))[0];
		
		/*
		 * Decayable Materials
		 */
		
		dustNeptunium238 = new DustDecayable("dustNeptunium238", Utils.rgbtoHexValue(175, 240, 75), 50640, new String[] {""+StringUtils.superscript("238Np"), "Result: Plutonium 238 ("+StringUtils.superscript("238Pu")+")"}, ELEMENT.getInstance().PLUTONIUM238.getDust(1).getItem(), 5);
		dustDecayedRadium226 = ItemUtils.generateSpecialUseDusts("DecayedRadium226", "Decayed Radium-226", "Contains Radon ("+StringUtils.superscript("222Rn")+")", ELEMENT.getInstance().RADIUM.getRgbAsHex())[0];
		dustRadium226 = new DustDecayable("dustRadium226", ELEMENT.getInstance().RADIUM.getRgbAsHex(), 90000, new String[] {""+StringUtils.superscript("226Ra"), "Result: Radon ("+StringUtils.superscript("222Rn")+")"}, ItemUtils.getSimpleStack(dustDecayedRadium226).getItem(), 5);

		dustTechnetium99 = new DustDecayable("dustTechnetium99", ELEMENT.getInstance().TECHNETIUM.getRgbAsHex(), 164500, new String[] {""+StringUtils.superscript("99Mo"), "Result: Ruthenium 99("+StringUtils.superscript("99Ru")+")"}, ELEMENT.getInstance().RUTHENIUM.getDust(1).getItem(), 4);
		dustTechnetium99M = new DustDecayable("dustTechnetium99M", ELEMENT.getInstance().TECHNETIUM.getRgbAsHex(), 8570, new String[] {""+StringUtils.superscript("99ᵐTc"), "Result: Technicium 99 ("+StringUtils.superscript("99Tc")+")"}, dustTechnetium99, 4);
		dustMolybdenum99 = new DustDecayable("dustMolybdenum99", ELEMENT.getInstance().MOLYBDENUM.getRgbAsHex(), 16450, new String[] {""+StringUtils.superscript("99Mo"), "Result: Technicium 99ᵐ ("+StringUtils.superscript("99ᵐTc")+")"}, dustTechnetium99M, 4);
		
		itemIonParticleBase = new IonParticles();
		itemStandarParticleBase = new StandardBaseParticles();
		
		
		
		
		itemBoilerChassis = new ItemBoilerChassis();
		itemDehydratorCoilWire = new ItemDehydratorCoilWire();
		itemDehydratorCoil = new ItemDehydratorCoil();

		itemAirFilter = new ItemAirFilter();
		itemLavaFilter = new ItemLavaFilter();

		itemGrindleTablet = new BaseItemGrindle();
		itemDragonJar = new ItemEntityCatcher();
		
		itemControlCore = new ItemControlCore();

		//Chemistry
		new CoalTar();
		new RocketFuels();
		
		//Nuclear Processing
		new NuclearChem();
		
		//Farm Animal Fun
		new AgriculturalChem();
		
		//General Chemistry
		new GenericChem();
		
		
		//Bombs
		itemBombCasing = new CoreItem("itemBombCasing", "Bomb Casing", tabMisc);
		itemBombCasing.setTextureName(CORE.MODID + ":bomb_casing");		
		itemBombUnf = new CoreItem("itemBombUnf", "Bomb (unf)", tabMisc);
		itemBombUnf.setTextureName(CORE.MODID + ":bomb_casing");		
		itemDetCable = new CoreItem("itemDetCable", "Det. Cable", tabMisc);
		itemDetCable.setTextureName("string");		
		itemBomb = new ItemThrowableBomb();
		
		//Only used for debugging.
		/*if (CORE.DEVENV) {
			new ConnectedBlockFinder();
		}*/

		//Misc Items
		@SuppressWarnings("unused")
		Item tI;
		tI = new BaseItemMisc("Chilly", new short[]{0,64,196}, 32, MiscTypes.POTION, new String[]{"It's Blue"});
		tI = new BaseItemMisc("4000DC's", new short[]{180,100,30}, 1, MiscTypes.BIGKEY, new String[]{"It opens things."});
		tI = new BaseItemMisc("Dull", new short[]{64,64,64}, 64, MiscTypes.GEM, null);
		tI = new BaseItemMisc("Forest", new short[]{130,164,96}, 64, MiscTypes.MUSHROOM, new String[]{"You Found this on the ground.", "Definitely not sure if it's worth eating."});

		//Baubles
		if (LoadedMods.Baubles){
			tI = new HealthBoostBauble();
			itemModularBauble = new ModularBauble();
		}

		//EnderIO Resources
		if ((LoadedMods.EnderIO || LOAD_ALL_CONTENT)){
			Logger.INFO("EnderIO Found - Loading Resources.");
			//Enderio Dusts
			itemDustSoularium = ItemUtils.generateSpecialUseDusts("Soularium", "Soularium", MaterialEIO.SOULARIUM.vChemicalFormula, MaterialEIO.SOULARIUM.getRgbAsHex())[0];
			itemDustRedstoneAlloy = ItemUtils.generateSpecialUseDusts("RedstoneAlloy", "Redstone Alloy", MaterialEIO.REDSTONE_ALLOY.vChemicalFormula, MaterialEIO.REDSTONE_ALLOY.getRgbAsHex())[0];
			itemDustElectricalSteel = ItemUtils.generateSpecialUseDusts("ElectricalSteel", "Electrical Steel", MaterialEIO.ELECTRICAL_STEEL.vChemicalFormula, MaterialEIO.ELECTRICAL_STEEL.getRgbAsHex())[0];
			itemDustPulsatingIron = ItemUtils.generateSpecialUseDusts("PulsatingIron", "Pulsating Iron", MaterialEIO.PULSATING_IRON.vChemicalFormula, MaterialEIO.PULSATING_IRON.getRgbAsHex())[0];
			itemDustEnergeticAlloy = ItemUtils.generateSpecialUseDusts("EnergeticAlloy", "Energetic Alloy", MaterialEIO.ENERGETIC_ALLOY.vChemicalFormula, MaterialEIO.ENERGETIC_ALLOY.getRgbAsHex())[0];
			itemDustVibrantAlloy = ItemUtils.generateSpecialUseDusts("VibrantAlloy", "Vibrant Alloy", MaterialEIO.VIBRANT_ALLOY.vChemicalFormula, MaterialEIO.VIBRANT_ALLOY.getRgbAsHex())[0];
			itemDustConductiveIron = ItemUtils.generateSpecialUseDusts("ConductiveIron", "Conductive Iron", MaterialEIO.CONDUCTIVE_IRON.vChemicalFormula, MaterialEIO.CONDUCTIVE_IRON.getRgbAsHex())[0];

			//EnderIO Plates
			itemPlateSoularium = ItemUtils.generateSpecialUsePlate("Soularium", "Soularium", MaterialEIO.SOULARIUM.vChemicalFormula, MaterialEIO.SOULARIUM.getRgbAsHex(), 0);
			itemPlateRedstoneAlloy = ItemUtils.generateSpecialUsePlate("RedstoneAlloy", "Redstone Alloy", MaterialEIO.REDSTONE_ALLOY.vChemicalFormula, MaterialEIO.REDSTONE_ALLOY.getRgbAsHex(), 0);
			itemPlateElectricalSteel = ItemUtils.generateSpecialUsePlate("ElectricalSteel", "Electrical Steel", MaterialEIO.ELECTRICAL_STEEL.vChemicalFormula, MaterialEIO.ELECTRICAL_STEEL.getRgbAsHex(), 0);
			itemPlatePulsatingIron = ItemUtils.generateSpecialUsePlate("PhasedIron", "Phased Iron", MaterialEIO.PULSATING_IRON.vChemicalFormula, MaterialEIO.PULSATING_IRON.getRgbAsHex(), 0);
			itemPlateEnergeticAlloy = ItemUtils.generateSpecialUsePlate("EnergeticAlloy", "Energetic Alloy", MaterialEIO.ENERGETIC_ALLOY.vChemicalFormula, MaterialEIO.ENERGETIC_ALLOY.getRgbAsHex(), 0);
			itemPlateVibrantAlloy = ItemUtils.generateSpecialUsePlate("VibrantAlloy", "Vibrant Alloy", MaterialEIO.VIBRANT_ALLOY.vChemicalFormula, MaterialEIO.VIBRANT_ALLOY.getRgbAsHex(), 0);
			itemPlateConductiveIron = ItemUtils.generateSpecialUsePlate("ConductiveIron", "Conductive Iron", MaterialEIO.CONDUCTIVE_IRON.vChemicalFormula, MaterialEIO.CONDUCTIVE_IRON.getRgbAsHex(), 0);

			//Register dumb naming conventions - Who chose fucking phased Iron/Gold?
			GT_OreDictUnificator.registerOre("dustPhasedGold", ItemUtils.getSimpleStack(itemDustVibrantAlloy));
			GT_OreDictUnificator.registerOre("platePhasedGold", ItemUtils.getSimpleStack(itemPlateVibrantAlloy));
			GT_OreDictUnificator.registerOre("dustPhasedIron", ItemUtils.getSimpleStack(itemDustPulsatingIron));
			GT_OreDictUnificator.registerOre("platePhasedIron", ItemUtils.getSimpleStack(itemPlatePulsatingIron));
			GT_OreDictUnificator.registerOre("blockVibrantAlloy", ItemUtils.getItemStackOfAmountFromOreDict("blockPhasedGold", 1));			

			CORE.RA.addFluidExtractionRecipe(MaterialEIO.REDSTONE_ALLOY.getPlate(1), null, MaterialEIO.REDSTONE_ALLOY.getFluid(144), 10000, 16, 4*9);
			CORE.RA.addFluidExtractionRecipe(MaterialEIO.REDSTONE_ALLOY.getIngot(1), null, MaterialEIO.REDSTONE_ALLOY.getFluid(144), 10000, 16, 4*9);
			CORE.RA.addFluidExtractionRecipe(MaterialEIO.REDSTONE_ALLOY.getNugget(1), null, MaterialEIO.REDSTONE_ALLOY.getFluid(16), 10000, 16, 4);
			CORE.RA.addFluidExtractionRecipe(MaterialEIO.REDSTONE_ALLOY.getBlock(1), null, MaterialEIO.REDSTONE_ALLOY.getFluid(1294), 10000, 16, 4*9*9);
			
		}
		else {
			/*Logger.WARNING("EnderIO not Found - Generating our own Resources.");
			MaterialGenerator.generate(MaterialEIO.CONDUCTIVE_IRON);
			MaterialGenerator.generate(MaterialEIO.PULSATING_IRON);
			MaterialGenerator.generate(MaterialEIO.REDSTONE_ALLOY);
			MaterialGenerator.generate(MaterialEIO.SOULARIUM);
			MaterialGenerator.generate(MaterialEIO.ELECTRICAL_STEEL);
			MaterialGenerator.generate(MaterialEIO.ENERGETIC_ALLOY);
			MaterialGenerator.generate(MaterialEIO.VIBRANT_ALLOY);	*/	
		}

		//Big Reactors
		if (LoadedMods.Big_Reactors|| LOAD_ALL_CONTENT){
			Logger.INFO("BigReactors Found - Loading Resources.");
			//Item Init
			itemPlateBlutonium = ItemUtils.generateSpecialUsePlate("Blutonium", "Blutonium", new short[]{0, 0, 255}, 0);
			itemPlateCyanite = ItemUtils.generateSpecialUsePlate("Cyanite", "Cyanite", new short[]{0, 191, 255}, 0);
			itemPlateLudicrite = ItemUtils.generateSpecialUsePlate("Ludicrite", "Ludicrite", new short[]{167, 5, 179}, 0);
		}
		else {
			Logger.WARNING("BigReactors not Found - Skipping Resources.");
		}

		//Thaumcraft
		if ((LoadedMods.Thaumcraft|| LOAD_ALL_CONTENT) && !CORE.GTNH){
			Logger.INFO("Thaumcraft Found - Loading Resources.");
			//Item Init
			try {
				ItemUtils.getItemForOreDict("Thaumcraft:ItemResource", "ingotVoidMetal", "Void Metal Ingot", 16);
				itemPlateVoidMetal = ItemUtils.generateSpecialUsePlate("Void", "Void", new short[]{82, 17, 82}, 0);
				GT_OreDictUnificator.registerOre("plateVoidMetal", new ItemStack(ModItems.itemPlateVoidMetal));
			} catch (final NullPointerException e){}

		}
		else {
			Logger.WARNING("Thaumcraft not Found - Skipping Resources.");
		}

		//Pneumaticraft
		if (LoadedMods.PneumaticCraft|| LOAD_ALL_CONTENT){
			Logger.INFO("PneumaticCraft Found - Loading Resources.");
			//Item Init
			itemPlateCompressedIron = ItemUtils.generateSpecialUsePlate("CompressedIron", "Compressed Iron", new short[]{128, 128, 128}, 0);
		}
		else {
			Logger.WARNING("PneumaticCraft not Found - Skipping Resources.");
		}

		//Simply Jetpacks
		if (LoadedMods.Simply_Jetpacks|| LOAD_ALL_CONTENT){
			Logger.INFO("SimplyJetpacks Found - Loading Resources.");
			//Item Init
			itemPlateEnrichedSoularium = new RarityUncommon().setUnlocalizedName("itemPlateEnrichedSoularium").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateSoularium");
			//Registry
			GameRegistry.registerItem(itemPlateEnrichedSoularium, "itemPlateEnrichedSoularium");
		}
		else {
			Logger.WARNING("SimplyJetpacks not Found - Skipping Resources.");
		}

		//rfTools
		if (LoadedMods.RFTools|| LOAD_ALL_CONTENT){
			Logger.INFO("rfTools Found - Loading Resources.");
			//Item Init
			itemPlateDimensionShard = ItemUtils.generateSpecialUsePlate("DimensionShard", "Dimensional Shard", new short[]{170, 230, 230}, 0);
		}
		else {
			Logger.WARNING("rfTools not Found - Skipping Resources.");
		}

		//IC2 Exp
		if (LoadedMods.IndustrialCraft2|| LOAD_ALL_CONTENT){
			Logger.INFO("IndustrialCraft2 Found - Loading Resources.");
			
			if (LoadedMods.CoFHCore) {
				RfEuBattery = new RF2EU_Battery();				
			}

			//Baubles Mod Test
			try {
				final Class<?> baublesTest = ReflectionUtils.getClass("baubles.api.IBauble");
				if (baublesTest != null){
					COMPAT_Baubles.run();
				}
				else {
					Logger.INFO("Baubles Not Found - Skipping Resources.");
				}
			} catch(final Throwable T){
				Logger.INFO("Baubles Not Found - Skipping Resources.");
			}
		}
		else {
			Logger.WARNING("IndustrialCraft2 not Found - Skipping Resources.");
		}


		//Special Item Handling Case
		if (ConfigSwitches.enableAlternativeBatteryAlloy) {
			//ModItems.itemIngotBatteryAlloy = new BaseItemIngot("itemIngotBatteryAlloy", "Battery Alloy", new short[]{35, 228, 141}, 0); TODO
			ModItems.itemPlateBatteryAlloy = ItemUtils.generateSpecialUsePlate("BatteryAlloy", "Battery Alloy", new short[]{35, 228, 141}, 0);

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
			GameRegistry.registerItem(itemBufferCore, itemBufferCore.getUnlocalizedName());
			//System.out.println("Buffer Core registration count is: "+i);
		}

		itemPLACEHOLDER_Circuit = new Item().setUnlocalizedName("itemPLACEHOLDER_Circuit").setTextureName(CORE.MODID + ":itemPLACEHOLDER_Circuit");
		GameRegistry.registerItem(itemPLACEHOLDER_Circuit, "itemPLACEHOLDER_Circuit");

		//ItemBlockGtFrameBox = new ItemBlockGtFrameBox(ModBlocks.blockGtFrameSet1);
		//GameRegistry.registerItem(ItemBlockGtFrameBox, "itemGtFrameBoxSet1");

		itemCustomBook = new ItemBaseBook();
		registerCustomTokens();
	}
	
	public static void registerCustomTokens() {
		itemGenericToken.register(0, "BitCoin", 16, "Can be used on the dark web");
		itemGenericToken.register(1, "Hand Pump Trade Token I", 1, "Craft into a Tier I Hand pump");
		itemGenericToken.register(2, "Hand Pump Trade Token II", 1, "Craft into a Tier II Hand pump");
		itemGenericToken.register(3, "Hand Pump Trade Token III", 1, "Craft into a Tier III Hand pump");
		itemGenericToken.register(4, "Hand Pump Trade Token IV", 1, "Craft into a Tier IV Hand pump");
	}
}
