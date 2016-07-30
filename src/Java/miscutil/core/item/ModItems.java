package miscutil.core.item;
import static miscutil.core.creative.AddToCreativeTab.tabMachines;
import static miscutil.core.creative.AddToCreativeTab.tabMisc;
import static miscutil.core.lib.CORE.LOAD_ALL_CONTENT;
import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.item.base.CoreItem;
import miscutil.core.item.base.bolts.BaseItemBolt;
import miscutil.core.item.base.foods.BaseItemFood;
import miscutil.core.item.base.foods.BaseItemHotFood;
import miscutil.core.item.base.gears.BaseItemGear;
import miscutil.core.item.base.ingots.BaseItemIngot;
import miscutil.core.item.base.ingots.BaseItemIngotHot;
import miscutil.core.item.base.plates.BaseItemPlate;
import miscutil.core.item.base.rings.BaseItemRing;
import miscutil.core.item.base.rods.BaseItemRod;
import miscutil.core.item.base.rotors.BaseItemRotor;
import miscutil.core.item.base.screws.BaseItemScrew;
import miscutil.core.item.effects.RarityUncommon;
import miscutil.core.item.general.BufferCore;
import miscutil.core.item.general.fuelrods.FuelRod_Base;
import miscutil.core.item.init.ItemsFoods;
import miscutil.core.item.tool.misc.SandstoneHammer;
import miscutil.core.item.tool.staballoy.StaballoyAxe;
import miscutil.core.item.tool.staballoy.StaballoyPickaxe;
import miscutil.core.lib.CORE;
import miscutil.core.lib.CORE.configSwitches;
import miscutil.core.lib.LoadedMods;
import miscutil.core.lib.MaterialInfo;
import miscutil.core.util.Utils;
import miscutil.core.util.debug.DEBUG_INIT;
import miscutil.core.util.item.UtilsItems;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemFood;
import net.minecraftforge.common.util.EnumHelper;
import cpw.mods.fml.common.registry.GameRegistry;
public final class ModItems {

	public static ToolMaterial STABALLOY = EnumHelper.addToolMaterial("Staballoy", 3, 2500, 7, 1.0F, 18);

	public static Item AAA_Broken;

	public static Item itemDebugShapeSpawner;

	public static Item itemBaseSpawnEgg;
	
	
	//Tantaloy60(789, TextureSet.SET_DULL, 8.0F, 5120, 3, 1 | 2 | 16 | 32 | 64 | 128, 213, 231, 237, 0, "Tantaloy 60", 0, 0, 3035, 2200, true, false, 1, 2, 1, Dyes.dyeLightGray, 2, Arrays.asList(new MaterialStack(Tungsten, 1), new MaterialStack(Tantalum, 9)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 8), new TC_AspectStack(TC_Aspects.STRONTIO, 3))),
	//Tantaloy61(790, TextureSet.SET_DULL, 7.0F, 5120, 2, 1 | 2 | 16 | 32 | 64 | 128, 193, 211, 217, 0, "Tantaloy 61", 0, 0, 3015, 2150, true, false, 1, 2, 1, Dyes.dyeLightGray, 2, Arrays.asList(new MaterialStack(Tungsten, 1), new MaterialStack(Tantalum, 9), new MaterialStack(Titanium, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 8), new TC_AspectStack(TC_Aspects.STRONTIO, 3))),


	//EnderIO
	public static Item itemPlateSoularium;
	public static Item itemPlateRedstoneAlloy;
	public static Item itemPlateElectricalSteel;
	public static Item itemPlatePulsatingIron;
	public static Item itemPlateEnergeticAlloy;
	public static Item itemPlateVibrantAlloy;
	public static Item itemPlateConductiveIron;
	public static Item itemPlateDarkSteel;
	//Big Reactors
	public static Item itemPlateBlutonium;
	public static Item itemPlateCyanite;
	public static Item itemPlateLudicrite;
	//Thaumcraft
	public static Item itemPlateVoidMetal;
	//ExtraUtils
	public static Item itemPlateBedrockium;
	//Pneumaticraft
	public static Item itemPlateCompressedIron;
	//SimplyJetpacks
	public static Item itemPlateEnrichedSoularium;
	//rfTools
	public static Item itemPlateDimensionShard;
	//Blood Steel Items
	public static Item itemIngotBloodSteel;
	public static Item itemPlateBloodSteel;
	//Staballoy
	public static Item itemStaballoyPickaxe;
	public static Item itemStaballoyAxe;
	public static Item itemPlateStaballoy;
	public static Item itemIngotStaballoy;
	public static Item itemDustStaballoy;
	public static Item itemDustTinyStaballoy;
	public static Item itemDustSmallStaballoy;
	public static Item itemDustBloodSteel;
	public static Item itemDustTinyBloodSteel;
	public static Item itemDustSmallBloodSteel;
	//Tools
	public static Item itemSandstoneHammer;
	//Machine Related
	public static Item itemBufferCore0;
	//Material related
	public static Item itemStickyRubber;
	public static Item itemIngotBatteryAlloy;
	public static Item itemPlateBatteryAlloy;
	public static Item itemHeliumBlob;
	public static Item itemPLACEHOLDER_Circuit;

	public static Item FuelRod_Empty;
	public static Item FuelRod_Thorium;
	public static Item FuelRod_Uranium;
	public static Item FuelRod_Plutonium;

	public static Item itemBedLocator_Base;
	public static Item itemBaseItemWithCharge;

	public static Item itemHotIngotStaballoy;

	public static Item itemIngotRaisinBread;
	public static Item itemHotIngotRaisinBread;

	public static BaseItemIngot itemIngotTantalloy60;
	public static BaseItemIngotHot itemHotIngotTantalloy60;
	public static BaseItemPlate itemPlateTantalloy60;
	public static BaseItemIngot itemIngotTantalloy61;
	public static BaseItemIngotHot itemHotIngotTantalloy61;
	public static BaseItemPlate itemPlateTantalloy61;

	public static ItemFood itemFoodRaisinToast;
	public static BaseItemHotFood itemHotFoodRaisinToast;
	public static BaseItemFood itemFoodCurriedSausages;
	public static BaseItemHotFood itemHotFoodCurriedSausages;
	
	public static Item itemDustTantalloy60;
	public static Item itemDustSmallTantalloy60;
	public static Item itemDustTinyTantalloy60;
	
	public static Item itemDustTantalloy61;
	public static Item itemDustSmallTantalloy61;
	public static Item itemDustTinyTantalloy61;
	
	public static BaseItemGear itemGearStaballoy;
	public static BaseItemGear itemGearBloodSteel;
	public static BaseItemGear itemGearTantalloy60;
	public static BaseItemGear itemGearTantalloy61;
	
	public static BaseItemRotor itemRotorStaballoy;
	public static BaseItemRotor itemRotorBloodSteel;
	public static BaseItemRotor itemRotorTantalloy60;
	public static BaseItemRotor itemRotorTantalloy61;
	
	public static BaseItemRod itemRodStaballoy;
	public static BaseItemRod itemRodBloodSteel;
	public static BaseItemRod itemRodTantalloy60;
	public static BaseItemRod itemRodTantalloy61;

	public static BaseItemScrew itemScrewStaballoy;
	public static BaseItemScrew itemScrewBloodSteel;
	public static BaseItemScrew itemScrewTantalloy60;
	public static BaseItemScrew itemScrewTantalloy61;

	public static BaseItemRing itemRingStaballoy;
	public static BaseItemRing itemRingBloodSteel;
	public static BaseItemRing itemRingTantalloy60;
	public static BaseItemRing itemRingTantalloy61;

	public static BaseItemBolt itemBoltStaballoy;
	public static BaseItemBolt itemBoltBloodSteel;
	public static BaseItemBolt itemBoltTantalloy60;
	public static BaseItemBolt itemBoltTantalloy61;


	//@SuppressWarnings("unused")
	public static final void init(){

		AAA_Broken = new BaseItemIngot("AAA_Broken", "Errors - Tell Alkalus", Utils.rgbtoHexValue(128, 128, 128));

		//Debug Loading
		if (CORE.DEBUG){
			DEBUG_INIT.registerItems();
		}		

		
		/*ItemsIngots.load();
		ItemsPlates.load();
		ItemsDusts.load();
		ItemsRods.load();
		ItemsGears.load();
		ItemsRotors.load();
		ItemsRings.load();
		ItemsBolts.load();
		ItemsScrews.load();*/
	
		
		//Start meta Item Generation
		ItemsFoods.load();

		UtilsItems.generateItemsFromMaterial("EnergyCrystal", "Energy Crystal", 8, MaterialInfo.ENERGYCRYSTAL, Utils.rgbtoHexValue(228, 225, 0), true);
		UtilsItems.generateItemsFromMaterial("BloodSteel", "Blood Steel", 3, MaterialInfo.BLOODSTEEL, Utils.rgbtoHexValue(142, 28, 0), false);
		UtilsItems.generateItemsFromMaterial("Staballoy", "Staballoy", 6, MaterialInfo.STABALLOY, Utils.rgbtoHexValue(68, 75, 66), true);
		UtilsItems.generateItemsFromMaterial("Tantalloy60", "Tantalloy-60", 5, MaterialInfo.TANTALLOY60, Utils.rgbtoHexValue(68, 75, 166), true);
		UtilsItems.generateItemsFromMaterial("Tantalloy61", "Tantalloy-61", 6, MaterialInfo.TANTALLOY61, Utils.rgbtoHexValue(122, 135, 196), true);
		UtilsItems.generateItemsFromMaterial("Bedrockium", "Bedrockium", 9, MaterialInfo.BEDROCKIUM, Utils.rgbtoHexValue(32, 32, 32), false);
		UtilsItems.generateItemsFromMaterial("Quantum", "Quantum", 10, MaterialInfo.QUANTUM, Utils.rgbtoHexValue(128, 128, 128), true);
		

		UtilsItems.generateItemsFromMaterial("Inconel625", "Inconel-625", 4, MaterialInfo.INCONEL625, Utils.rgbtoHexValue(128, 200, 128), true); //Inconel 625: Acid resistant, good weldability. The LCF version is typically used in bellows.
		UtilsItems.generateItemsFromMaterial("Inconel690", "Inconel-690", 6, MaterialInfo.INCONEL690, Utils.rgbtoHexValue(118, 220, 138), true); //Inconel 690: Low cobalt content for nuclear applications, and low resistivity.
		UtilsItems.generateItemsFromMaterial("Inconel792", "Inconel-792", 5, MaterialInfo.INCONEL792, Utils.rgbtoHexValue(108, 240, 118), true); //Inconel 792: Increased aluminium content for improved high temperature corrosion properties, used especially in gas turbines.
		

		UtilsItems.generateItemsFromMaterial("TungstenCarbide", "Tungsten Carbide", 5, MaterialInfo.TUNGSTENCARBIDE, Utils.rgbtoHexValue(44, 44, 44), true);
		UtilsItems.generateItemsFromMaterial("SiliconCarbide", "Silicon Carbide", 4, MaterialInfo.SILICONCARBIDE, Utils.rgbtoHexValue(32, 32, 32), false);
		UtilsItems.generateItemsFromMaterial("Zeron100", "Zeron-100", 8, MaterialInfo.ZERON100, Utils.rgbtoHexValue(180, 180, 20), true);
		UtilsItems.generateItemsFromMaterial("MaragingSteel250", "Maraging Steel 250", 4, MaterialInfo.MARAGING250, Utils.rgbtoHexValue(140, 140, 140), true);
		UtilsItems.generateItemsFromMaterial("MaragingSteel300", "Maraging Steel 300", 5, MaterialInfo.MARAGING300, Utils.rgbtoHexValue(150, 150, 150), true);
		UtilsItems.generateItemsFromMaterial("MaragingSteel350", "Maraging Steel 350", 6, MaterialInfo.MARAGING350, Utils.rgbtoHexValue(160, 160, 160), true);
		UtilsItems.generateItemsFromMaterial("Stellite", "Stellite", 7, MaterialInfo.STELLITE, Utils.rgbtoHexValue(129, 75, 120), true);
		UtilsItems.generateItemsFromMaterial("Talonite", "Talonite", 8, MaterialInfo.TALONITE, Utils.rgbtoHexValue(228, 75, 120), false);
		
		UtilsItems.generateItemsFromMaterial("Tumbaga", "Tumbaga", 2, MaterialInfo.TUMBAGA, Utils.rgbtoHexValue(255,178,15), false); //Tumbaga was the name given by Spaniards to a non-specific alloy of gold and copper 
		UtilsItems.generateItemsFromMaterial("Potin", "Potin", 4, MaterialInfo.POTIN, Utils.rgbtoHexValue(201,151,129), false); //Potin is traditionally an alloy of bronze, tin and lead, with varying quantities of each possible
		
		//EnderIO Resources
		if (LoadedMods.EnderIO || LOAD_ALL_CONTENT){
			Utils.LOG_INFO("EnderIO Found - Loading Resources.");
			//Item Init
			itemPlateSoularium = new Item().setUnlocalizedName("itemPlateSoularium").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateSoularium");
			itemPlateRedstoneAlloy = new Item().setUnlocalizedName("itemPlateRedstoneAlloy").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateRedstoneAlloy");
			itemPlateElectricalSteel = new Item().setUnlocalizedName("itemPlateElectricalSteel").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateElectricalSteel");
			itemPlatePulsatingIron = new Item().setUnlocalizedName("itemPlatePulsatingIron").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlatePulsatingIron");
			itemPlateEnergeticAlloy = new Item().setUnlocalizedName("itemPlateEnergeticAlloy").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateEnergeticAlloy");
			itemPlateVibrantAlloy = new Item().setUnlocalizedName("itemPlateVibrantAlloy").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateVibrantAlloy");
			itemPlateConductiveIron = new Item().setUnlocalizedName("itemPlateConductiveIron").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateConductiveIron");
			itemPlateDarkSteel = new Item().setUnlocalizedName("itemPlateDarkSteel").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateDarkSteel");
			//Registry
			GameRegistry.registerItem(itemPlateSoularium, "itemPlateSoularium");
			GameRegistry.registerItem(itemPlateRedstoneAlloy, "itemPlateRedstoneAlloy");
			GameRegistry.registerItem(itemPlateElectricalSteel, "itemPlateElectricalSteel");
			GameRegistry.registerItem(itemPlatePulsatingIron, "itemPlatePulsatingIron");
			GameRegistry.registerItem(itemPlateEnergeticAlloy, "itemPlateEnergeticAlloy");
			GameRegistry.registerItem(itemPlateVibrantAlloy, "itemPlateVibrantAlloy");
			GameRegistry.registerItem(itemPlateConductiveIron, "itemPlateConductiveIron");
			GameRegistry.registerItem(itemPlateDarkSteel, "itemPlateDarkSteel");
		}
		else {
			Utils.LOG_WARNING("EnderIO not Found - Skipping Resources.");
		}
		//Big Reactors
		if (LoadedMods.Big_Reactors|| LOAD_ALL_CONTENT){
			Utils.LOG_INFO("BigReactors Found - Loading Resources.");
			//Item Init
			itemPlateBlutonium = new Item().setUnlocalizedName("itemPlateBlutonium").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateBlutonium");
			itemPlateCyanite = new Item().setUnlocalizedName("itemPlateCyanite").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateCyanite");
			itemPlateLudicrite = new Item().setUnlocalizedName("itemPlateLudicrite").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateLudicrite");
			//Registry
			GameRegistry.registerItem(itemPlateBlutonium, "itemPlateBlutonium");
			GameRegistry.registerItem(itemPlateCyanite, "itemPlateCyanite");
			GameRegistry.registerItem(itemPlateLudicrite, "itemPlateLudicrite");
		}
		else {
			Utils.LOG_WARNING("BigReactors not Found - Skipping Resources.");
		}
		//Thaumcraft
		if (LoadedMods.Thaumcraft|| LOAD_ALL_CONTENT){
			Utils.LOG_INFO("Thaumcraft Found - Loading Resources.");
			//Item Init
			try {
				itemPlateVoidMetal = new Item().setUnlocalizedName("itemPlateVoidMetal").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateVoidMetal");
			} catch (NullPointerException e){
				e.getClass();
			}
			//Registry
			GameRegistry.registerItem(itemPlateVoidMetal, "itemPlateVoidMetal");
		}
		else {
			Utils.LOG_WARNING("Thaumcraft not Found - Skipping Resources.");
		}
		//ExtraUtils
		if (LoadedMods.Extra_Utils|| LOAD_ALL_CONTENT){
			Utils.LOG_INFO("ExtraUtilities Found - Loading Resources.");
			//Item Init
			try {
				//itemPlateBedrockium = new Item().setUnlocalizedName("itemPlateBedrockium").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateBedrockium");
			} catch (NullPointerException e){
				e.getClass();
			}
			//Registry
			//GameRegistry.registerItem(itemPlateBedrockium, "itemPlateBedrockium");
		}
		else {
			Utils.LOG_WARNING("ExtraUtilities not Found - Skipping Resources.");
		}
		//Pneumaticraft
		if (LoadedMods.PneumaticCraft|| LOAD_ALL_CONTENT){
			Utils.LOG_INFO("PneumaticCraft Found - Loading Resources.");
			//Item Init
			itemPlateCompressedIron = new Item().setUnlocalizedName("itemPlateCompressedIron").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateCompressedIron");
			//Registry
			GameRegistry.registerItem(itemPlateCompressedIron, "itemPlateCompressedIron");
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
			itemPlateDimensionShard = new Item().setUnlocalizedName("itemPlateDimensionShard").setCreativeTab(AddToCreativeTab.tabMisc).setTextureName(CORE.MODID + ":itemPlateDimensionShard");
			//Registry
			GameRegistry.registerItem(itemPlateDimensionShard, "itemPlateDimensionShard");
		}
		else {
			Utils.LOG_WARNING("rfTools not Found - Skipping Resources.");
		}
		//IC2 Exp
		if (LoadedMods.IndustrialCraft2|| LOAD_ALL_CONTENT){
			Utils.LOG_INFO("IndustrialCraft2 Found - Loading Resources.");
			//Item Init
			FuelRod_Empty = new FuelRod_Base("itemFuelRod_Empty", "Empty", 0, 1000);
			FuelRod_Thorium = new FuelRod_Base("itemFuelRod_Thorium", "Thorium", 1000, 1000);
			FuelRod_Uranium = new FuelRod_Base("itemFuelRod_Uranium", "Uranium", 2500, 2500);
			FuelRod_Plutonium = new FuelRod_Base("itemFuelRod_Plutonium", "Plutonium", 5000, 5000);
			//Registry
			//GameRegistry.registerItem(FuelRod_Empty, "itemFuelRod_Empty");
			//GameRegistry.registerItem(FuelRod_Thorium, "itemFuelRod_Thorium");
			//GameRegistry.registerItem(FuelRod_Uranium, "itemFuelRod_Uranium");
			//GameRegistry.registerItem(FuelRod_Plutonium, "itemFuelRod_Plutonium");
		}
		else {
			Utils.LOG_WARNING("IndustrialCraft2 not Found - Skipping Resources.");
		}


		//Special Item Handling Case
		if (configSwitches.enableAlternativeBatteryAlloy) {
			ModItems.itemIngotBatteryAlloy = new BaseItemIngot("itemIngotBatteryAlloy", "Battery Alloy", Utils.rgbtoHexValue(35, 228, 141));
			ModItems.itemPlateBatteryAlloy = new BaseItemPlate("itemPlateBatteryAlloy", "Battery Alloy", Utils.rgbtoHexValue(35, 228, 141), 2);
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

		itemStickyRubber = new Item().setUnlocalizedName("itemStickyRubber").setCreativeTab(tabMachines).setTextureName(CORE.MODID + ":itemStickyRubber");
		GameRegistry.registerItem(itemStickyRubber, "itemStickyRubber");
		GT_OreDictUnificator.registerOre("ingotRubber", UtilsItems.getItemStack(CORE.MODID+":itemStickyRubber", 1));

		itemHeliumBlob = new CoreItem("itemHeliumBlob", tabMisc).setTextureName(CORE.MODID + ":itemHeliumBlob");
		//GameRegistry.registerItem(itemHeliumBlob, "itemHeliumBlob");

		itemPLACEHOLDER_Circuit = new Item().setUnlocalizedName("itemPLACEHOLDER_Circuit").setTextureName(CORE.MODID + ":itemPLACEHOLDER_Circuit");
		GameRegistry.registerItem(itemPLACEHOLDER_Circuit, "itemPLACEHOLDER_Circuit");

		//ItemBlockGtFrameBox = new ItemBlockGtFrameBox(ModBlocks.blockGtFrameSet1);
		//GameRegistry.registerItem(ItemBlockGtFrameBox, "itemGtFrameBoxSet1");		
	}
}