package com.github.technus.tectech.thing.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

import static com.github.technus.tectech.CommonValues.tecMark;

/**
 * Created by Tec on 11.04.2017.
 */
public class QuantumGlassItem extends ItemBlock {
    public static QuantumGlassItem INSTANCE;

    public QuantumGlassItem(Block b) {
        super(b);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        aList.add(tecMark);
        aList.add("Dense yet transparent");
        aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Glassy & Classy");
    }
}
