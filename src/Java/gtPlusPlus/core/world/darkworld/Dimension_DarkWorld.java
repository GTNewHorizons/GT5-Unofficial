package gtPlusPlus.core.world.darkworld;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.world.darkworld.block.blockDarkWorldPortal;
import gtPlusPlus.core.world.darkworld.item.itemDarkWorldPortalTrigger;
import gtPlusPlus.core.world.darkworld.world.WorldProviderMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.DimensionManager;

public class Dimension_DarkWorld {

	public Object instance;
	public static int DIMID = CORE.DARKWORLD_ID;

	public static blockDarkWorldPortal portalBlock;
	public static itemDarkWorldPortalTrigger portalItem;
	public static Block blockTopLayer;
	public static Block blockSecondLayer;
	public static Block blockMainFiller = Blocks.stone;
	public static Block blockSecondaryFiller;
	public static Block blockFluidLakes;

	public static Block blockPortalFrame;

	public void load() {
		GameRegistry.registerBlock(portalBlock, "dimensionDarkWorld_portal");
		DimensionManager.registerProviderType(DIMID, WorldProviderMod.class, false);
		DimensionManager.registerDimension(DIMID, DIMID);
		// GameRegistry.addSmelting(Items.record_11, new ItemStack(block),
		// 1.0f);
	}

}
