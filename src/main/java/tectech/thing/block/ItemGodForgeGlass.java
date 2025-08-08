package tectech.thing.block;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import tectech.util.CommonValues;

public class ItemGodForgeGlass extends ItemBlock {

    public static ItemGodForgeGlass INSTANCE;

    public ItemGodForgeGlass(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        aList.add(CommonValues.GODFORGE_MARK);
        aList.add(translateToLocal("tile.godforgeGlass.desc.0"));
        aList.add(
            EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                + translateToLocal("tile.godforgeGlass.desc.1"));
    }
}
