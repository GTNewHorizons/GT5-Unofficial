package gtPlusPlus.xmod.forestry.bees.blocks;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import forestry.core.utils.StringUtil;
import forestry.plugins.PluginManager;
import net.minecraftforge.oredict.OreDictionary;

public abstract class FR_BlockRegistry {
	protected static <T extends Block> T registerBlock(T block, Class<? extends ItemBlock> itemClass, String name) {
		if (PluginManager.getStage() != PluginManager.Stage.SETUP) {
			throw new RuntimeException("Tried to register Block outside of Setup");
		}
		block.setBlockName("for." + name);
		GameRegistry.registerBlock(block, itemClass, StringUtil.cleanBlockName(block));
		return block;
	}

	protected static void registerOreDictWildcard(String oreDictName, Block block) {
		OreDictionary.registerOre(oreDictName, new ItemStack(block, 1, 32767));
	}
}