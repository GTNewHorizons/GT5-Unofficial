package com.github.technus.tectech.thing.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

import static com.github.technus.tectech.CommonValues.TEC_MARK_GENERAL;

/**
 * Created by danie_000 on 30.09.2017.
 */
public class ReactorSimItem extends ItemBlock {
    public static QuantumGlassItem INSTANCE;

    public ReactorSimItem(Block b) {
        super(b);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        aList.add(TEC_MARK_GENERAL);
        aList.add("ReactorSimulator 9001");
        aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Explodes, but not as much...");
    }
}
