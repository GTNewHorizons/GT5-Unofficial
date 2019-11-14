package com.github.technus.tectech.thing.casing;

import gregtech.common.blocks.GT_Item_Casings_Abstract;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

import static com.github.technus.tectech.CommonValues.*;
import static net.minecraft.util.StatCollector.translateToLocal;

public class GT_Item_CasingsBA0 extends GT_Item_Casings_Abstract {
    public GT_Item_CasingsBA0(Block par1) {
        super(par1);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        if (aStack.getItemDamage() < 15) {
            aList.add(BASS_MARK);
        } else {
            aList.add(COSMIC_MARK);
        }
        switch (aStack.getItemDamage()) {
            case 0://"Redstone Alloy Primary Tesla Windings"
            case 1://"MV Superconductor Primary Tesla Windings"
            case 2://"HV Superconductor Primary Tesla Windings"
            case 3://"EV Superconductor Primary Tesla Windings"
            case 4://"IV Superconductor Primary Tesla Windings"
            case 5://"LuV Superconductor Primary Tesla Windings"
                aList.add(translateToLocal("gt.blockcasingsBA0.0.desc.0") + " " + V[aStack.getItemDamage() + 1] + " EU/t");//Handles up to
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockcasingsBA0.0.desc.1"));//What one man calls God, another calls the laws of physics.
                break;
            case 6://"Tesla Base Casing"
                aList.add(translateToLocal("gt.blockcasingsBA0.6.desc.0"));//The base of a wondrous contraption
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockcasingsBA0.6.desc.1"));//it's alive, IT'S ALIVE!
                break;
            case 7://"Tesla Toroid Casing"
                aList.add(translateToLocal("gt.blockcasingsBA0.7.desc.0"));//Made out of the finest tin foil!
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockcasingsBA0.7.desc.1"));//Faraday suits might come later
                break;
            case 8://"Tesla Secondary Windings"
                aList.add(translateToLocal("gt.blockcasingsBA0.8.desc.0"));//Picks up power from a primary coil
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockcasingsBA0.8.desc.1"));//Who wouldn't want a 32k epoxy multi?
                break;
            default://WTF?
                aList.add("Damn son where did you get that!?");
                aList.add(EnumChatFormatting.BLUE.toString() + "From outer space... I guess...");
        }
    }
}
