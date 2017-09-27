package com.github.technus.tectech.thing.casing;

import gregtech.common.blocks.GT_Item_Casings_Abstract;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

import static com.github.technus.tectech.CommonValues.TEC_MARK_GENERAL;

/**
 * Created by danie_000 on 03.10.2016.
 */
public class GT_Item_HintTT extends GT_Item_Casings_Abstract {
    public GT_Item_HintTT(Block par1) {
        super(par1);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        aList.add(TEC_MARK_GENERAL);
        aList.add("Helps while building");
        switch (aStack.getItemDamage()) {
            case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10: case 11:
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Placeholder for a certain group.");
                break;
            case 12:
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "General placeholder.");
                break;
            case 13:
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Make sure it contains Air material.");
                break;
            case 14:
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Make sure it does not contain Air material.");
                break;
            case 15:
                aList.add(EnumChatFormatting.BLUE.toString() + "ERROR, what did u expect?");
                break;
            default://WTF?
                aList.add("Damn son where did you get that!?");
                aList.add(EnumChatFormatting.BLUE.toString() + "From outer space... I guess...");
        }
    }
}
