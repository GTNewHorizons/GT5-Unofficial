package gregtech.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

/**
 * The legacy ores. Must still be registered so that postea can transform them into the new ore blocks.
 */
public class ItemOresLegacy extends ItemBlock {

    public ItemOresLegacy(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }
}
