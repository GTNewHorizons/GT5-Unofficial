package gtPlusPlus.australia.dimension;

import net.minecraft.block.Block;
import gtPlusPlus.australia.block.BlockAustraliaPortal;
import gtPlusPlus.australia.item.ItemAustraliaPortalTrigger;
import gtPlusPlus.australia.world.AustraliaWorldProvider;
import gtPlusPlus.core.lib.CORE;
import net.minecraftforge.common.DimensionManager;

public class Dimension_Australia {

	public Object instance;
	public static int DIMID = CORE.AUSTRALIA_ID;
	public static BlockAustraliaPortal portalBlock;
	public static ItemAustraliaPortalTrigger portalItem;
	public static Block blockPortalFrame;

	public void load() {
		DimensionManager.registerProviderType(DIMID, AustraliaWorldProvider.class, false);
		DimensionManager.registerDimension(DIMID, DIMID);
	}

}
