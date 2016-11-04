package gtPlusPlus.core.util.debug;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.BaseItemWithCharge;
import gtPlusPlus.core.item.general.BedLocator_Base;
import gtPlusPlus.core.lib.CORE;
import net.minecraftforge.common.MinecraftForge;

public class DEBUG_INIT {

	public static void registerBlocks() {
		// Debug Loading
		if (CORE.DEBUG) {

		}
	}

	public static void registerHandlers() {
		MinecraftForge.EVENT_BUS.register(new DEBUG_ScreenOverlay());
	}

	public static void registerItems() {
		ModItems.itemDebugShapeSpawner = new DEBUG_ITEM_ShapeSpawner("itemDebugShapeSpawner", AddToCreativeTab.tabMisc,
				1, 500);
		GameRegistry.registerItem(ModItems.itemDebugShapeSpawner, "itemDebugShapeSpawner");
		ModItems.itemBedLocator_Base = new BedLocator_Base("itemBedLocator_Base");
		GameRegistry.registerItem(ModItems.itemBedLocator_Base, "itemBedLocator_Base");
		ModItems.itemBaseItemWithCharge = new BaseItemWithCharge("itemBaseItemWithCharge", 0, 1000);
		GameRegistry.registerItem(ModItems.itemBaseItemWithCharge, "itemBaseItemWithCharge");
	}

	public static void registerMisc() {

	}

	public static void registerTEs() {

	}

}
