package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class ItemCasings12 extends ItemCasingsAbstract {

    public ItemCasings12(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        int meta = getDamage(aStack);
        if (meta <= 2) {
            aList.add(
                StatCollector.translateToLocalFormatted(
                    "gt.casing.tiertooltip",
                    (EnumChatFormatting.YELLOW + Integer.toString(meta + 1))));
        }
    }
}
