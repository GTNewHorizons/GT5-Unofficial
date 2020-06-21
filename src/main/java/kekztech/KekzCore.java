package kekztech;

import client.renderer.TESR_SECapacitor;
import client.renderer.TESR_SETether;
import common.Blocks;
import common.CommonProxy;
import common.Recipes;
import common.Researches;
import common.tileentities.*;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import common.items.ErrorItem;
import common.items.MetaItem_CraftingComponent;
import common.items.MetaItem_ReactorComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * My GT-Meta-IDs are: 13101 - 13500
 * 
 * @author kekzdealer
 *
 */
@Mod(modid = KekzCore.MODID, name = KekzCore.NAME, version = KekzCore.VERSION, 
		dependencies =
			  "required-after:IC2;"
			+ "required-after:gregtech;"
			+ "required-after:tectech;"
			+ "required-after:Thaumcraft;"
			+ "required-after:ThaumicTinkerer;"
			+ "after:bartworks;"
			+ "after:dreamcraft"
		)
public class KekzCore {
	
	public static final String NAME = "KekzTech";
	public static final String MODID = "kekztech";
	public static final String VERSION = "0.5";

	public static final Logger LOGGER = LogManager.getLogger(NAME);

	@Mod.Instance("kekztech")
	public static KekzCore instance;

	@SidedProxy(clientSide = "client.ClientProxy", serverSide = "kekztech.ServerProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}
