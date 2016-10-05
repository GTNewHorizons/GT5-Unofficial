package gtPlusPlus.core.item;
import static gtPlusPlus.core.creative.AddToCreativeTab.tabMachines;
import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;
import static gtPlusPlus.core.lib.CORE.LOAD_ALL_CONTENT;
import static gtPlusPlus.core.util.item.UtilsItems.generateItemsFromMaterial;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.BaseItemBackpack;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.item.base.foods.BaseItemFood;
import gtPlusPlus.core.item.base.foods.BaseItemHotFood;
import gtPlusPlus.core.item.base.ingots.BaseItemIngot;
import gtPlusPlus.core.item.base.plates.BaseItemPlate;
import gtPlusPlus.core.item.effects.RarityUncommon;
import gtPlusPlus.core.item.general.BufferCore;
import gtPlusPlus.core.item.general.ItemCloakingDevice;
import gtPlusPlus.core.item.general.ItemHealingDevice;
import gtPlusPlus.core.item.general.RF2EU_Battery;
import gtPlusPlus.core.item.general.fuelrods.FuelRod_Base;
import gtPlusPlus.core.item.init.ItemsFoods;
import gtPlusPlus.core.item.tool.misc.SandstoneHammer;
import gtPlusPlus.core.item.tool.staballoy.MultiPickaxeBase;
import gtPlusPlus.core.item.tool.staballoy.MultiSpadeBase;
import gtPlusPlus.core.item.tool.staballoy.StaballoyAxe;
import gtPlusPlus.core.item.tool.staballoy.StaballoyPickaxe;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.CORE.configSwitches;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.debug.DEBUG_INIT;
import gtPlusPlus.core.util.item.UtilsItems;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
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
	
	public static Item dustLithiumCarbonate;


	//@SuppressWarnings("unused")
	public static final void init(){

		AAA_Broken = new BaseItemIngot("AAA_Broken", "Errors - Tell Alkalus", Utils.rgbtoHexValue(128, 128, 128), 0);

		//Debug Loading
		if (CORE.DEBUG){
			DEBUG_INIT.registerItems();
		}		
		
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
		
		//Start meta Item Generation
		ItemsFoods.load();

		try{
		generateItemsFromMaterial(ALLOY.ENERGYCRYSTAL);
		generateItemsFromMaterial(ALLOY.BLOODSTEEL);
		generateItemsFromMaterial(ALLOY.STABALLOY);
		generateItemsFromMaterial(ALLOY.TANTALLOY_60);
		generateItemsFromMaterial(ALLOY.TANTALLOY_61);
		generateItemsFromMaterial(ALLOY.BEDROCKIUM);
		generateItemsFromMaterial(ALLOY.QUANTUM);
		

		generateItemsFromMaterial(ALLOY.INCONEL_625);
		generateItemsFromMaterial(ALLOY.INCONEL_690);
		generateItemsFromMaterial(ALLOY.INCONEL_792);

		if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			generateItemsFromMaterial(ALLOY.TUNGSTEN_CARBIDE);
		}
		generateItemsFromMaterial(ALLOY.SILICON_CARBIDE);
		generateItemsFromMaterial(ALLOY.ZERON_100);
		generateItemsFromMaterial(ALLOY.MARAGING250);
		generateItemsFromMaterial(ALLOY.MARAGING300);
		generateItemsFromMaterial(ALLOY.MARAGING350);
		generateItemsFromMaterial(ALLOY.STELLITE);
		generateItemsFromMaterial(ALLOY.TALONITE);
		
		//Tumbaga was the name given by Spaniards to a non-specific alloy of gold and copper 
		generateItemsFromMaterial(ALLOY.TUMBAGA);
		//Potin is traditionally an alloy of bronze, tin and lead, with varying quantities of each possible
		generateItemsFromMaterial(ALLOY.POTIN);
		

		generateItemsFromMaterial(ALLOY.HASTELLOY_W);
		generateItemsFromMaterial(ALLOY.HASTELLOY_X);
		generateItemsFromMaterial(ALLOY.HASTELLOY_C276);
		generateItemsFromMaterial(ALLOY.HASTELLOY_N);
		
		generateItemsFromMaterial(ALLOY.INCOLOY_020);
		generateItemsFromMaterial(ALLOY.INCOLOY_DS);
		generateItemsFromMaterial(ALLOY.INCOLOY_MA956);
		
		
		generateItemsFromMaterial(ELEMENT.ZIRCONIUM);
		generateItemsFromMaterial(ALLOY.ZIRCONIUM_CARBIDE);
		generateItemsFromMaterial(ALLOY.TANTALUM_CARBIDE);
		generateItemsFromMaterial(ALLOY.NIOBIUM_CARBIDE);		
		
		//Uranium-233 is a fissile isotope of uranium that is bred from thorium-232 as part of the thorium fuel cycle.
		UtilsItems.generateItemsFromMaterial(ELEMENT.URANIUM233);
		
		} catch (Throwable r){
			Utils.LOG_INFO("Failed to Generated a Material. "+r.getMessage());
			//Utils.LOG_INFO("Failed to Generated a Material. "+r.getCause().getMessage());
			Utils.LOG_INFO("Failed to Generated a Material. "+r.getStackTrace()[0].getMethodName());
			Utils.LOG_INFO("Failed to Generated a Material. "+r.getStackTrace()[1].getMethodName());
			r.printStackTrace();
			System.exit(1);
		}
		
		
		dustLithiumCarbonate = UtilsItems.generateSpecialUseDusts("LithiumCarbonate", "Lithium Carbonate", Utils.rgbtoHexValue(137, 139, 142))[0];

		
		
		boolean gtStyleTools = LoadedMods.Gregtech;
		
		Materials[] rm = Materials.values();
		for (Materials m : rm){
			MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, m);
			MS_GTMATERIAL = UtilsItems.generateMultiShovel(gtStyleTools, m);
		}
		

		/*MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Wood);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Cobblestone);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Iron);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.WroughtIron);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.DarkIron);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Gold);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Silver);
		
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Diamond);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Emerald);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Ruby);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Sapphire);

		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Cupronickel);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Brass);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Bronze);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Steel);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Lead);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Titanium);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Tungsten);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Platinum);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Chrome);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.TungstenSteel);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Aluminium);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Thaumium);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Cobalt);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Iridium);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Ultimet);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Osmiridium);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.NetherStar);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Naquadah);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.NaquadahAlloy);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.NaquadahEnriched);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Neutronium);
		
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Thorium);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.DamascusSteel);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Magnalium);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.BlackSteel);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Invar);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.MeteoricSteel);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.StainlessSteel);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.BlueSteel);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Neodymium);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.Desh);
		MP_GTMATERIAL = UtilsItems.generateMultiPick(gtStyleTools, Materials.ElectrumFlux);*/
		
		//EnderIO Resources
		if (LoadedMods.EnderIO || LOAD_ALL_CONTENT){
			Utils.LOG_INFO("EnderIO Found - Loading Resources.");
			//Item Init
			itemPlateSoularium = new BaseItemPlate("itemPlate"+"Soularium", "Soularium", Utils.rgbtoHexValue(95, 90, 54), 2, 0);
			itemPlateRedstoneAlloy = new BaseItemPlate("itemPlate"+"RedstoneAlloy", "Redstone Alloy", Utils.rgbtoHexValue(178,34,34), 2, 0);
			itemPlateElectricalSteel =new BaseItemPlate("itemPlate"+"ElectricalSteel", "Electrical Steel", Utils.rgbtoHexValue(194, 194, 194), 2, 0);
			itemPlatePulsatingIron = new BaseItemPlate("itemPlate"+"PhasedIron", "Pulsating Iron", Utils.rgbtoHexValue(50, 91, 21), 2, 0);
			itemPlateEnergeticAlloy = new BaseItemPlate("itemPlate"+"EnergeticAlloy", "Energetic Alloy", Utils.rgbtoHexValue(252, 152, 45), 2, 0);
			itemPlateVibrantAlloy = new BaseItemPlate("itemPlate"+"VibrantAlloy", "Vibrant Alloy", Utils.rgbtoHexValue(204, 242, 142), 2, 0);
			itemPlateConductiveIron = new BaseItemPlate("itemPlate"+"ConductiveIron", "Conductive Iron", Utils.rgbtoHexValue(164, 109, 100), 2, 0);
					
		}
		else {
			Utils.LOG_WARNING("EnderIO not Found - Skipping Resources.");
		}
		//Big Reactors
		if (LoadedMods.Big_Reactors|| LOAD_ALL_CONTENT){
			Utils.LOG_INFO("BigReactors Found - Loading Resources.");
			//Item Init
			itemPlateBlutonium = new BaseItemPlate("itemPlate"+"Blutonium", "Blutonium", Utils.rgbtoHexValue(0, 0, 255), 2, 0);
			itemPlateCyanite = new BaseItemPlate("itemPlate"+"Cyanite", "Cyanite", Utils.rgbtoHexValue(0, 191, 255), 2, 0);
			itemPlateLudicrite = new BaseItemPlate("itemPlate"+"Ludicrite", "Ludicrite", Utils.rgbtoHexValue(167, 5, 179), 2, 0);
			
		}
		else {
			Utils.LOG_WARNING("BigReactors not Found - Skipping Resources.");
		}
		//Thaumcraft
		if (LoadedMods.Thaumcraft|| LOAD_ALL_CONTENT){
			Utils.LOG_INFO("Thaumcraft Found - Loading Resources.");
			//Item Init
			try {
				
				UtilsItems.getItemForOreDict("Thaumcraft:ItemResource", "ingotVoidMetal", "Void Metal Ingot", 16);
				GT_OreDictUnificator.registerOre("plateVoidMetal", new ItemStack(ModItems.itemPlateVoidMetal));
				
				itemPlateVoidMetal = new BaseItemPlate("itemPlate"+"Void", "Void Metal", Utils.rgbtoHexValue(82, 17, 82), 2, 0);
			} catch (NullPointerException e){
				e.getClass();
			}
			
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
			itemPlateCompressedIron = new BaseItemPlate("itemPlate"+"CompressedIron", "Compressed Iron", Utils.rgbtoHexValue(128, 128, 128), 2, 0);
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
			itemPlateDimensionShard = new BaseItemPlate("itemPlate"+"DimensionShard", "Dimensional Shard", Utils.rgbtoHexValue(170, 230, 230), 2, 0);
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
			RfEuBattery = new RF2EU_Battery();
			
			if (LoadedMods.Baubles){
				Utils.LOG_INFO("Baubles Found - Loading Wearables.");
			itemPersonalCloakingDevice = new ItemCloakingDevice(0);
			//itemPersonalCloakingDeviceCharged = new ItemCloakingDevice(0).set;
			itemPersonalHealingDevice = new ItemHealingDevice();
			}
			else {
				Utils.LOG_INFO("Baubles Not Found - Skipping Resources.");
			}
			//Registry
			//GameRegistry.registerItem(FuelRod_Empty, "itemFuelRod_Empty");
			//GameRegistry.registerItem(FuelRod_Thorium, "itemFuelRod_Thorium");
			//GameRegistry.registerItem(FuelRod_Uranium, "itemFuelRod_Uranium");
			//GameRegistry.registerItem(FuelRod_Plutonium, "itemFuelRod_Plutonium");
			
			//FluidCell = new ItemStack(new IC2_ItemFluidCell("itemGT++FluidCell"));
			
			
		}
		else {
			Utils.LOG_WARNING("IndustrialCraft2 not Found - Skipping Resources.");
		}


		//Special Item Handling Case
		if (configSwitches.enableAlternativeBatteryAlloy) {
			ModItems.itemIngotBatteryAlloy = new BaseItemIngot("itemIngotBatteryAlloy", "Battery Alloy", Utils.rgbtoHexValue(35, 228, 141), 0);
			ModItems.itemPlateBatteryAlloy = new BaseItemPlate("itemPlateBatteryAlloy", "Battery Alloy", Utils.rgbtoHexValue(35, 228, 141), 2, 0);
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