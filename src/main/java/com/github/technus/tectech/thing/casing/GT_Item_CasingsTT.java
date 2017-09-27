package com.github.technus.tectech.thing.casing;

import gregtech.common.blocks.GT_Item_Casings_Abstract;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

import static com.github.technus.tectech.CommonValues.TEC_MARK_EM;
import static com.github.technus.tectech.CommonValues.TEC_MARK_GENERAL;

/**
 * Created by danie_000 on 03.10.2016.
 */
public class GT_Item_CasingsTT extends GT_Item_Casings_Abstract {
    public GT_Item_CasingsTT(Block par1) {
        super(par1);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        if(aStack.getItemDamage()>0 && aStack.getItemDamage()<15) aList.add(TEC_MARK_EM);
        else aList.add(TEC_MARK_GENERAL);
        switch (aStack.getItemDamage()) {
            case 0://"High Power Casing"
                aList.add("Well suited for high power applications.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "The power levels are rising!");
                break;
            case 1://"Computer Casing"
                aList.add("Nice and clean casing.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Dust can break it!?");
                break;
            case 2://"Computer Heat Vent"
                aList.add("Air vent with a filter.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Perfectly muffled sound!");
                break;
            case 3://"Advanced Computer Casing"
                aList.add("Contains high bandwidth bus");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "couple thousand qubits wide.");
                break;
            case 4://"Molecular Casing"
                aList.add("Stops elemental things.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Radiation and emotions too...");
                break;
            case 5://"Advanced Molecular Casing"
                aList.add("Cooling and stabilization.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "A comfortable machine bed.");
                break;
            case 6://"Containment Field Generator"
                aList.add("Creates a field that...");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "can stop even force carriers.");
                break;
            case 7://"Molecular Coil"
                aList.add("Well it does things too...");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "[Use this coil!]");
                break;
            case 8://"Collider Hollow Casing"
                aList.add("Reinforced accelerator tunnel.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Most advanced pipe ever.");
                break;
            case 9://"Spacetime Altering Casing"
                aList.add("C is no longer the limit.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Wibbly wobbly timey wimey stuff.");
                break;
            case 10://"Teleportation Casing"
                aList.add("Remote connection.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Better touch with a stick.");
                break;
            case 11://"Dimensional Bridge Generator"
                aList.add("Interdimensional Operations.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Around the universe and other places too.");
                break;
            case 12://"Ultimate Molecular Casing"
                aList.add("Ultimate in every way.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "I don't know what it can't do.");
                break;
            case 13://"Ultimate Advanced Molecular Casing"
                aList.add("More Ultimate in every way.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "I don't know what I am doing!");
                break;
            case 14://"Ultimate Containment Field Generator"
                aList.add("Black Hole...");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Meh...");
                break;
            case 15://"Debug Sides"
                aList.add("Lazy man way of determining sides.");
                aList.add(EnumChatFormatting.BLUE.toString() + "0, 1, 2, 3, 4, 5, 6?!");
                break;
            default://WTF?
                aList.add("Damn son where did you get that!?");
                aList.add(EnumChatFormatting.BLUE.toString() + "From outer space... I guess...");
        }
    }
}
