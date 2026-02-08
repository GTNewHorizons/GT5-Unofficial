package gtneioreplugin.plugin.block;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;

import cpw.mods.fml.common.registry.GameRegistry;
import gtneioreplugin.plugin.item.ItemDimensionDisplay;
import gtneioreplugin.util.DimensionHelper;

public class ModBlocks {

    public static final Map<String, Block> blocks = new HashMap<>();

    public static void init() {
        for (String dimension : DimensionHelper.getAllDisplayedNames()) {
            Block block = new BlockDimensionDisplay(dimension);
            GameRegistry.registerBlock(block, ItemDimensionDisplay.class, "blockDimensionDisplay_" + dimension);
            blocks.put(dimension, block);
        }
    }

    public static Block getBlock(String dimension) {
        return blocks.get(dimension);
    }
}
