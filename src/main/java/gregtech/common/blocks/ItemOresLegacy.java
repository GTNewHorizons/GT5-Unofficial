package gregtech.common.blocks;

import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

/**
 * The legacy ores. Must still be registered so that postea can transform them into the new ore blocks.
 */
public class ItemOresLegacy extends ItemBlock {

    public ItemOresLegacy(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        if (aStack.getItemDamage() == WILDCARD) return StatCollector.translateToLocal("gt.block.any_sub_block");
        return super.getItemStackDisplayName(aStack);
    }
}
