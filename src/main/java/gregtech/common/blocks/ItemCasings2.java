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
public class ItemCasings2 extends ItemCasingsAbstract {

    public ItemCasings2(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        super.addInformation(aStack, aPlayer, aList, aF3_H);
        switch (getDamage(aStack)) {
            case 8 -> aList.add(this.mBlastProofTooltip);
            case 12 -> aList
                .add(StatCollector.translateToLocalFormatted("gt.casing.tiertooltip", EnumChatFormatting.YELLOW + "1"));
            case 13 -> aList
                .add(StatCollector.translateToLocalFormatted("gt.casing.tiertooltip", EnumChatFormatting.YELLOW + "2"));
            case 14 -> aList
                .add(StatCollector.translateToLocalFormatted("gt.casing.tiertooltip", EnumChatFormatting.YELLOW + "3"));
            case 15 -> aList
                .add(StatCollector.translateToLocalFormatted("gt.casing.tiertooltip", EnumChatFormatting.YELLOW + "4"));
        }
    }
}
