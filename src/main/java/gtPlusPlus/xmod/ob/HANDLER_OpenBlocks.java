package gtPlusPlus.xmod.ob;

import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;

import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.BaseItemBurnable;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.railcraft.utils.RailcraftUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class HANDLER_OpenBlocks {

	public static void preInit() {
		if (LoadedMods.OpenBlocks) {
			
		}
	}

	public static void init() {
		if (LoadedMods.OpenBlocks) {
			GliderHandler.populateBlacklist();
		}
	}

	public static void postInit() {
		if (LoadedMods.OpenBlocks) {
			Utils.registerEvent(new GliderHandler());
		}
	}	

}
