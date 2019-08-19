package com.github.technus.tectech.thing.casing;

import gregtech.common.blocks.GT_Item_Casings_Abstract;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

import static com.github.technus.tectech.CommonValues.*;

public class GT_Item_CasingsBA0 extends GT_Item_Casings_Abstract {
    public GT_Item_CasingsBA0(Block par1) {
        super(par1);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        if(aStack.getItemDamage() < 15) {
            aList.add(BASS_MARK);
        } else {
            aList.add(COSMIC_MARK);
        }
        switch (aStack.getItemDamage()) {
            case 0://"Redstone Alloy Primary Tesla Windings"
                aList.add("Handles up to 32 EU/t");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "What one man calls God, another calls the laws of physics.");
                break;
            case 1://"MV Superconductor Primary Tesla Windings"
                aList.add("Handles up to 128 EU/t");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "What one man calls God, another calls the laws of physics.");
                break;
            case 2://"HV Superconductor Primary Tesla Windings"
                aList.add("Handles up to 512 EU/t");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "What one man calls God, another calls the laws of physics.");
                break;
            case 3://"EV Superconductor Primary Tesla Windings"
                aList.add("Handles up to 2048 EU/t");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "What one man calls God, another calls the laws of physics.");
                break;
            case 4://"IV Superconductor Primary Tesla Windings"
                aList.add("Handles up to 8192 EU/t");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "What one man calls God, another calls the laws of physics.");
                break;
            case 5://"LuV Superconductor Primary Tesla Windings"
                aList.add("Handles up to 32768 EU/t");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "What one man calls God, another calls the laws of physics.");
                break;
            case 6://"Tesla Base Casing"
                aList.add("The base of a wondrous contraption");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "it's alive, IT'S ALIVE!");
                break;
            case 7://"Tesla Toroid Casing"
                aList.add("A shell for your coils");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Faraday suits might come later");
                break;
            case 8://"Tesla Secondary Windings"
                aList.add("Picks up power from a primary coil");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Who wouldn't want a 32k epoxy multi?");
                break;
            default://WTF?
                aList.add("Damn son where did you get that!?");
                aList.add(EnumChatFormatting.BLUE.toString() + "From outer space... I guess...");
        }
    }
}
