package tectech.thing.block;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import tectech.util.CommonValues;

/**
 * Created by Tec on 11.04.2017.
 */
public class ItemQuantumGlass extends ItemBlock {

    public static ItemQuantumGlass INSTANCE;

    public ItemQuantumGlass(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        aList.add(CommonValues.TEC_MARK_EM);
        aList.add(translateToLocal("tile.quantumGlass.desc.0")); // Dense yet transparent
        aList.add(
            EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                + translateToLocal("tile.quantumGlass.desc.1")); // Glassy & Classy
    }
}
