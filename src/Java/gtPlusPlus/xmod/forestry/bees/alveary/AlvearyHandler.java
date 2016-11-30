package gtPlusPlus.xmod.forestry.bees.alveary;

import forestry.core.items.ItemBlockForestry;
import forestry.core.utils.StringUtil;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;

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

	protected static <T extends Block> T registerBlock(T block, Class<? extends ItemBlock> itemClass, String name, Object... itemCtorArgs) {
		block.setBlockName("for." + name);
		GameRegistry.registerBlock(block, itemClass, StringUtil.cleanBlockName(block), itemCtorArgs);
		return block;
	}

}
