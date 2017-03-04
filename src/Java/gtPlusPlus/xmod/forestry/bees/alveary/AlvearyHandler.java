package gtPlusPlus.xmod.forestry.bees.alveary;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.core.items.ItemBlockForestry;
import forestry.core.utils.StringUtil;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class AlvearyHandler {

	@Optional.Method(modid = "Forestry")
	public static void run(){
		if (!LoadedMods.ExtraBees){
			if (CORE.configSwitches.enableCustomAlvearyBlocks){//Alveary Stuff
				FR_BlockAlveary alveary;
				alveary = registerBlock(new FR_BlockAlveary(), ItemBlockForestry.class, "alveary");
				GameRegistry.registerTileEntity(TileAlvearyFrameHousing.class, "FrameHousing");
			}
		}
	}

	protected static <T extends Block> T registerBlock(final T block, final Class<? extends ItemBlock> itemClass, final String name, final Object... itemCtorArgs) {
		block.setBlockName("for." + name);
		GameRegistry.registerBlock(block, itemClass, StringUtil.cleanBlockName(block), itemCtorArgs);
		return block;
	}

}
