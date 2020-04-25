package kekztech;

import common.Blocks;
import common.Recipes;
import common.blocks.Block_ControlRod;
import common.blocks.Block_GDCUnit;
import common.blocks.Block_IchorJar;
import common.blocks.Block_ReactorChamber_OFF;
import common.blocks.Block_ReactorChamber_ON;
import common.blocks.Block_TFFTCasing;
import common.blocks.Block_TFFTMultiHatch;
import common.blocks.Block_TFFTStorageFieldBlockT1;
import common.blocks.Block_TFFTStorageFieldBlockT2;
import common.blocks.Block_TFFTStorageFieldBlockT3;
import common.blocks.Block_TFFTStorageFieldBlockT4;
import common.blocks.Block_TFFTStorageFieldBlockT5;
import common.blocks.Block_ThaumiumReinforcedJar;
import common.blocks.Block_YSZUnit;
import common.tileentities.GTMTE_FluidMultiStorage;
import common.tileentities.GTMTE_ItemServer;
import common.tileentities.GTMTE_ModularNuclearReactor;
import common.tileentities.GTMTE_SOFuelCellMK1;
import common.tileentities.GTMTE_SOFuelCellMK2;
import common.tileentities.TE_IchorJar;
import common.tileentities.TE_TFFTMultiHatch;
import common.tileentities.TE_ThaumiumReinforcedJar;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import items.ErrorItem;
import items.Item_ThaumiumReinforcedJarFilled;
import items.MetaItem_CraftingComponent;
import items.MetaItem_ReactorComponent;

/**
 * My GT-Meta-IDs are: 13101 - 13500
 * 
 * @author kekzdealer
 *
 */
@Mod(modid = KekzCore.MODID, name = KekzCore.NAME, version = KekzCore.VERSION, 
		dependencies =
			  "required-after:IC2; "
			+ "required-after:gregtech;"
			+ "after:bartworks"
		)
public class KekzCore {
	
	public static final String NAME = "KekzTech";
	public static final String MODID = "kekztech";
	public static final String VERSION = "0.2.3";
	
	@Mod.Instance("kekztech")
	public static KekzCore instance;

	public static GTMTE_SOFuelCellMK1 sofc1;
	public static GTMTE_SOFuelCellMK2 sofc2;
	@SuppressWarnings("unused")
	public static GTMTE_ModularNuclearReactor mdr;
	public static GTMTE_FluidMultiStorage fms;
	public static GTMTE_ItemServer is;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Items
		ErrorItem.getInstance().registerItem();
		MetaItem_ReactorComponent.getInstance().registerItem();
		MetaItem_CraftingComponent.getInstance().registerItem();
		//Item_Configurator.getInstance().registerItem();
		Items.registerOreDictNames();
		
		Item_ThaumiumReinforcedJarFilled.getInstance().registerItem();
		
		Blocks.init();
		
		// Register TileEntities
		GameRegistry.registerTileEntity(TE_TFFTMultiHatch.class, "kekztech_tfftmultihatch_tile");
		//GameRegistry.registerTileEntity(TE_ItemServerIOPort.class, "kekztech_itemserverioport_tile");
		//GameRegistry.registerTileEntity(TE_ItemProxyCable.class, "kekztech_itemproxycable_tile");
		//GameRegistry.registerTileEntity(TE_ItemProxySource.class, "kekztech_itemproxysource_tile");
		//GameRegistry.registerTileEntity(TE_ItemProxyEndpoint.class, "kekztech_itemproxyendpoint_tile");
		
		GameRegistry.registerTileEntity(TE_ThaumiumReinforcedJar.class, "kekztech_thaumiumreinforcedjar");
		GameRegistry.registerTileEntity(TE_IchorJar.class, "kekztech_ichorjar");
		
		// Register guis
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		// Multiblock controllers
		sofc1 = new GTMTE_SOFuelCellMK1(13101, "multimachine.fuelcellmk1", "Solid-Oxide Fuel Cell Mk I");
		sofc2 = new GTMTE_SOFuelCellMK2(13102, "multimachine.fuelcellmk2", "Solid-Oxide Fuel Cell Mk II");
		mdr = new GTMTE_ModularNuclearReactor(13103, "multimachine.nuclearreactor", "Nuclear Reactor");
		fms = new GTMTE_FluidMultiStorage(13104, "multimachine.tf_fluidtank", "T.F.F.T");
		//is = new GTMTE_ItemServer(13105, "multimachine.itemserver", "Item Server");	
		// Register renderer
		//ConduitRenderer.getInstance().registerRenderer();
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Recipes.init();
	}
	
}
