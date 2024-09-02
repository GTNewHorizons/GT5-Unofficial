package gregtech.common.blocks;

import net.minecraft.block.Block;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class ItemCasings1 extends ItemCasingsAbstract {

    public ItemCasings1(Block block) {
        super(block);
    }
}
