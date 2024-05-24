package com.github.technus.tectech.thing.casing;

import static com.github.technus.tectech.util.CommonValues.GODFORGE_MARK;
import static net.minecraft.util.EnumChatFormatting.AQUA;
import static net.minecraft.util.EnumChatFormatting.BOLD;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Item_Casings_Abstract;

public class GT_Item_Casings_Godforge extends GT_Item_Casings_Abstract {

    public GT_Item_Casings_Godforge(Block par1) {
        super(par1);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        aList.add(GODFORGE_MARK);
        switch (aStack.getItemDamage()) {
            case 0:
                aList.add(
                    GT_LanguageManager.addStringLocalization(
                        "godforge.casings.0.Tooltip.0",
                        "Shielded by the event horizon of a singularity"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GT_LanguageManager
                            .addStringLocalization("godforge.casings.0.Tooltip.1", "Don't get too close..."));
                break;
            case 1:
                aList.add(
                    GT_LanguageManager.addStringLocalization(
                        "godforge.casings.1.Tooltip.0",
                        "Designed to route stellar matter using spacetime distortion"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GT_LanguageManager
                            .addStringLocalization("godforge.casings.1.Tooltip.1", "Reality Distortion"));
                break;
            case 2:
                aList.add(
                    GT_LanguageManager
                        .addStringLocalization("godforge.casings.2.Tooltip.0", "Unaffected by gravitational forces"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GT_LanguageManager.addStringLocalization(
                            "godforge.casings.2.Tooltip.1",
                            "Not even a black hole could tear it apart."));
                break;
            case 3:
                aList.add(
                    GT_LanguageManager.addStringLocalization(
                        "godforge.casings.3.Tooltip.0",
                        "Creates enormous magnetic fields capable of constraining entire stars"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GT_LanguageManager
                            .addStringLocalization("godforge.casings.3.Tooltip.1", "It's super effective!"));
                break;
            case 4:
                aList.add(
                    GT_LanguageManager.addStringLocalization(
                        "godforge.casings.4.Tooltip.0",
                        "Taps into streams of stellar matter to harness their heat energy"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GT_LanguageManager
                            .addStringLocalization("godforge.casings.4.Tooltip.1", "Turn up the heat!"));
                break;
            case 5:
                aList.add(
                    GT_LanguageManager.addStringLocalization(
                        "godforge.casings.5.Tooltip.0",
                        "Controls the flow of gravitons to manipulate gravity"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GT_LanguageManager.addStringLocalization(
                            "godforge.casings.5.Tooltip.1",
                            "Exponential scaling past 800 Galaxies"));
                break;
            case 6:
                aList.add(
                    GT_LanguageManager.addStringLocalization(
                        "godforge.casings.6.Tooltip.0",
                        "Controls the flow of gravitons to manipulate gravity"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GT_LanguageManager
                            .addStringLocalization("godforge.casings.6.Tooltip.1", "Getting closer..."));
                break;
            case 7:
                aList.add(
                    GT_LanguageManager.addStringLocalization(
                        "godforge.casings.7.Tooltip.0",
                        "Controls the flow of gravitons to manipulate gravity"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GT_LanguageManager.addStringLocalization("godforge.casings.7.Tooltip.1", "Gravity Central"));
                break;
            default:
                aList.add(
                    EnumChatFormatting.RED.toString() + BOLD
                        + GT_LanguageManager
                            .addStringLocalization("godforge.casings.Error.Tooltip", "Error, report to GTNH team"));
        }
    }
}
