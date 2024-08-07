package gregtech.common.blocks;

import net.minecraft.block.Block;

/**
 * The glass types are split into separate files because they are registered as regular blocks, and a regular block can
 * have
 * 16 subtypes at most.
 */
public class GT_Item_Glass1 extends GT_Item_Casings_Abstract {

    public GT_Item_Glass1(Block block) {
        super(block);
    }
}
