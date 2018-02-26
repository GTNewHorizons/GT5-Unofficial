package gtPlusPlus.core.world.darkworld;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.world.darkworld.block.BlockDarkWorldPortal;
import gtPlusPlus.core.world.darkworld.item.ItemDarkWorldPortalTrigger;
import gtPlusPlus.core.world.darkworld.world.WorldProviderMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.DimensionManager;

public class Dimension_DarkWorld {

	public Object instance;
	public static int DIMID = CORE.DARKWORLD_ID;
	public static BlockDarkWorldPortal portalBlock;
	public static ItemDarkWorldPortalTrigger portalItem;
	public static Block blockTopLayer;
	public static Block blockSecondLayer;
	public static Block blockMainFiller = Blocks.stone;
	public static Block blockSecondaryFiller;
	public static Block blockFluidLakes;
	public static Block blockPortalFrame;

	public void load() {
		DimensionManager.registerProviderType(DIMID, WorldProviderMod.class, false);
		DimensionManager.registerDimension(DIMID, DIMID);
	}

}
