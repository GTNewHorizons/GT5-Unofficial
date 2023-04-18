package com.github.technus.tectech.thing.casing;

import static com.google.common.math.LongMath.pow;
import static gregtech.api.enums.GT_Values.AuthorColen;
import static gregtech.api.util.GT_Utility.formatNumbers;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Item_Casings_Abstract;

public class GT_Item_Casings_Spacetime extends GT_Item_Casings_Abstract {

    public GT_Item_Casings_Spacetime(Block par1) {
        super(par1);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        switch (aStack.getItemDamage()) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                aList.add(
                        GT_LanguageManager.addStringLocalization(
                                "EOH.Spacetime.Standard.Tooltip.0",
                                "Supports an internal spacetime volume of up to ")
                                + formatNumbers(pow(10, 5 + aStack.getItemDamage()))
                                + "km³.");
                aList.add(
                        EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                                + GT_LanguageManager.addStringLocalization(
                                        "EOH.Spacetime.Standard.Tooltip.1",
                                        "Capable of running recipes up to tier ")
                                + (aStack.getItemDamage() + 1));
                break;
            default:
                aList.add(
                        EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD
                                + GT_LanguageManager.addStringLocalization(
                                        "EOH.TimeDilation.Error.Tooltip",
                                        "Error, report to GTNH team"));
        }
        aList.add(AuthorColen);
    }
}
