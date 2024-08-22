package gregtech.common.blocks;

import net.minecraft.block.Block;

/**
 * The glass types are split into separate files because they are registered as regular blocks, and a regular block can
 * have
 * 16 subtypes at most.
 */
public class ItemGlass1 extends ItemCasingsAbstract {

    public ItemGlass1(Block block) {
        super(block);
    }
}
