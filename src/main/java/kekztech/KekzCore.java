package kekztech;

import common.Blocks;
import common.Recipes;
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
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

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
	public static final String VERSION = "0.3";
	
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
		
		// Thaumcraft research
		final ResearchItem jar_thaumiumreinforced = new ResearchItem("THAUMIUMREINFORCEDJAR", "ALCHEMY", new AspectList(), 3, -4, 2, new ItemStack(Blocks.jarThaumiumReinforced, 1));
		jar_thaumiumreinforced.setPages(new ResearchPage[] {
			new ResearchPage("kekztech.research_page.THAUMIUMREINFORCEDJAR"),
			new ResearchPage(Recipes.arcaneRecipes.get("THAUMIUMREINFORCEDJAR"))
		});
		jar_thaumiumreinforced.setParents(new String[] {"JARLABEL"});
		jar_thaumiumreinforced.registerResearchItem();
		
		final ResearchItem jar_ichor = new ResearchItem("ICHORJAR", "ALCHEMY", new AspectList(), 2, -5, 3, new ItemStack(Blocks.jarIchor, 1));
		jar_ichor.setPages(new ResearchPage[] {
			new ResearchPage("kekztech.research_page.ICHORJAR"),
			new ResearchPage(Recipes.infusionRecipes.get("ICHORJAR"))
		});
		jar_ichor.setParents(new String[] {"THAUMIUMREINFORCEDJAR"});
		jar_ichor.registerResearchItem();
	}
}
