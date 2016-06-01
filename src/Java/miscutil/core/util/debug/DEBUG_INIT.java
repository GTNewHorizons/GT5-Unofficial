package miscutil.core.util.debug;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.item.ModItems;
import miscutil.core.lib.CORE;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;

public class DEBUG_INIT {

	public static void registerBlocks(){
		//Debug Loading
		if (CORE.DEBUG){

		}
	}

	public static void registerItems(){
		ModItems.itemDebugShapeSpawner = new DEBUG_ITEM_ShapeSpawner("itemDebugShapeSpawner", AddToCreativeTab.tabMisc, 1, 500);
		GameRegistry.registerItem(ModItems.itemDebugShapeSpawner, "itemDebugShapeSpawner");
	}

	public static void registerTEs(){

	}

	public static void registerMisc(){



	}

	public static void registerHandlers(){
		MinecraftForge.EVENT_BUS.register(new DEBUG_ScreenOverlay());	
	}

}
