package tectech.thing.casing;

import static net.minecraft.util.EnumChatFormatting.AQUA;
import static net.minecraft.util.EnumChatFormatting.BOLD;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import gregtech.api.util.GTLanguageManager;
import gregtech.common.blocks.ItemCasings;
import tectech.util.CommonValues;

public class ItemCasingsGodforge extends ItemCasings {

    public ItemCasingsGodforge(Block par1) {
        super(par1);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List tooltip, boolean aF3_H) {
        tooltip.add(CommonValues.GODFORGE_MARK);
        switch (aStack.getItemDamage()) {
            case 0:
                tooltip.add(
                    GTLanguageManager.addStringLocalization(
                        "godforge.casings.0.Tooltip.0",
                        "Shielded by the event horizon of a singularity"));
                tooltip.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager
                            .addStringLocalization("godforge.casings.0.Tooltip.1", "Don't get too close..."));
                break;
            case 1:
                tooltip.add(
                    GTLanguageManager.addStringLocalization(
                        "godforge.casings.1.Tooltip.0",
                        "Designed to route stellar matter using spacetime distortion"));
                tooltip.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager
                            .addStringLocalization("godforge.casings.1.Tooltip.1", "Reality Distortion"));
                break;
            case 2:
                tooltip.add(
                    GTLanguageManager
                        .addStringLocalization("godforge.casings.2.Tooltip.0", "Unaffected by gravitational forces"));
                tooltip.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager.addStringLocalization(
                            "godforge.casings.2.Tooltip.1",
                            "Not even a black hole could tear it apart."));
                break;
            case 3:
                tooltip.add(
                    GTLanguageManager.addStringLocalization(
                        "godforge.casings.3.Tooltip.0",
                        "Creates enormous magnetic fields capable of constraining entire stars"));
                tooltip.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager
                            .addStringLocalization("godforge.casings.3.Tooltip.1", "It's super effective!"));
                break;
            case 4:
                tooltip.add(
                    GTLanguageManager.addStringLocalization(
                        "godforge.casings.4.Tooltip.0",
                        "Taps into streams of stellar matter to harness their heat energy"));
                tooltip.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager.addStringLocalization("godforge.casings.4.Tooltip.1", "Turn up the heat!"));
                break;
            case 5:
                tooltip.add(
                    GTLanguageManager.addStringLocalization(
                        "godforge.casings.5.Tooltip.0",
                        "Controls the flow of gravitons to manipulate gravity"));
                tooltip.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager.addStringLocalization(
                            "godforge.casings.5.Tooltip.1",
                            "Exponential scaling past 800 Galaxies"));
                break;
            case 6:
                tooltip.add(
                    GTLanguageManager.addStringLocalization(
                        "godforge.casings.6.Tooltip.0",
                        "Controls the flow of gravitons to manipulate gravity"));
                tooltip.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager.addStringLocalization("godforge.casings.6.Tooltip.1", "Getting closer..."));
                break;
            case 7:
                tooltip.add(
                    GTLanguageManager.addStringLocalization(
                        "godforge.casings.7.Tooltip.0",
                        "Controls the flow of gravitons to manipulate gravity"));
                tooltip.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager.addStringLocalization("godforge.casings.7.Tooltip.1", "Gravity Central"));
                break;
            case 8:
                tooltip.add(
                    GTLanguageManager.addStringLocalization(
                        "godforge.casings.8.Tooltip.0",
                        "Transfers and stores extreme amounts of heat without any loss"));
                tooltip.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager.addStringLocalization("godforge.casings.8.Tooltip.1", "<<<Thermal<<<"));
                tooltip.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager.addStringLocalization("godforge.casings.8.Tooltip.2", ">>>Wave>>>"));
                break;
            default:
                tooltip.add(
                    EnumChatFormatting.RED.toString() + BOLD
                        + GTLanguageManager
                            .addStringLocalization("godforge.casings.Error.Tooltip", "Error, report to GTNH team"));
        }
    }
}
