package com.github.technus.tectech.thing.block;

import static com.github.technus.tectech.util.CommonValues.TEC_MARK_EM;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by Tec on 11.04.2017.
 */
public class QuantumGlassItem extends ItemBlock {

    public static QuantumGlassItem INSTANCE;

    public QuantumGlassItem(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        aList.add(TEC_MARK_EM);
        aList.add(translateToLocal("tile.quantumGlass.desc.0")); // Dense yet transparent
        aList.add(
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                        + translateToLocal("tile.quantumGlass.desc.1")); // Glassy & Classy
    }
}
