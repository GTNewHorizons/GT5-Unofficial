package gregtech.common.blocks.rubbertree;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockRubberLog extends ItemBlock {

    public ItemBlockRubberLog(Block block) {
        super(block);
        setHasSubtypes(false);
        setMaxDamage(0);
    }

    @Override
    public int getMetadata(int meta) {
        // Prevents the replacement of "natural" or oriented variants from the item
        return 0;
    }
}
