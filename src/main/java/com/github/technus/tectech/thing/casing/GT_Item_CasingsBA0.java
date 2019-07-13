package com.github.technus.tectech.thing.casing;

import gregtech.common.blocks.GT_Item_Casings_Abstract;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

import static com.github.technus.tectech.CommonValues.*;

/**
 * Created by danie_000 on 03.10.2016.
 */
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
            case 0://"T0 Primary Tesla Windings"
                aList.add("Well suited for high power applications.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "The power levels are rising!");
                break;
            case 1://"T1 Primary Tesla Windings"
                aList.add("Nice and clean casing.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Dust can break it!?");
                break;
            case 2://"T2 Primary Tesla Windings"
                aList.add("Air vent with a filter.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Perfectly muffled sound!");
                break;
            case 3://"T3 Primary Tesla Windings"
                aList.add("Contains high bandwidth bus");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "couple thousand qubits wide.");
                break;
            case 4://"T4 Primary Tesla Windings"
                aList.add("Stops elemental things.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Radiation and emotions too...");
                break;
            case 5://"T5 Primary Tesla Windings"
                aList.add("Cooling and stabilization.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "A comfortable machine bed.");
                break;
            case 6://"Tesla Base Casing"
                aList.add("Creates a field that...");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "can stop even force carriers.");
                break;
            case 7://"Tesla Toroid Casing"
                aList.add("Well it does things too...");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "[Use this coil!]");
                break;
            case 8://"Tesla Secondary Windings"
                aList.add("Reinforced accelerator tunnel.");
                aList.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Most advanced pipe ever.");
                break;
            default://WTF?
                aList.add("Damn son where did you get that!?");
                aList.add(EnumChatFormatting.BLUE.toString() + "From outer space... I guess...");
        }
    }
}
