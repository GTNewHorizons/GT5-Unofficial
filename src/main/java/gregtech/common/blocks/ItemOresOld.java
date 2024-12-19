package gregtech.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemOresOld extends ItemBlock {

    public ItemOresOld(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }
}
