package tectech.thing.casing;

import static net.minecraft.util.EnumChatFormatting.AQUA;
import static net.minecraft.util.EnumChatFormatting.BOLD;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import gregtech.api.util.GTLanguageManager;
import gregtech.common.blocks.ItemCasingsAbstract;
import tectech.util.CommonValues;

public class ItemCasingsGodforge extends ItemCasingsAbstract {

    public ItemCasingsGodforge(Block par1) {
        super(par1);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        aList.add(CommonValues.GODFORGE_MARK);
        switch (aStack.getItemDamage()) {
            case 0:
                aList.add(
                    GTLanguageManager.addStringLocalization(
                        "godforge.casings.0.Tooltip.0",
                        "Shielded by the event horizon of a singularity"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager
                            .addStringLocalization("godforge.casings.0.Tooltip.1", "Don't get too close..."));
                break;
            case 1:
                aList.add(
                    GTLanguageManager.addStringLocalization(
                        "godforge.casings.1.Tooltip.0",
                        "Designed to route stellar matter using spacetime distortion"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager
                            .addStringLocalization("godforge.casings.1.Tooltip.1", "Reality Distortion"));
                break;
            case 2:
                aList.add(
                    GTLanguageManager
                        .addStringLocalization("godforge.casings.2.Tooltip.0", "Unaffected by gravitational forces"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager.addStringLocalization(
                            "godforge.casings.2.Tooltip.1",
                            "Not even a black hole could tear it apart."));
                break;
            case 3:
                aList.add(
                    GTLanguageManager.addStringLocalization(
                        "godforge.casings.3.Tooltip.0",
                        "Creates enormous magnetic fields capable of constraining entire stars"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager
                            .addStringLocalization("godforge.casings.3.Tooltip.1", "It's super effective!"));
                break;
            case 4:
                aList.add(
                    GTLanguageManager.addStringLocalization(
                        "godforge.casings.4.Tooltip.0",
                        "Taps into streams of stellar matter to harness their heat energy"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager.addStringLocalization("godforge.casings.4.Tooltip.1", "Turn up the heat!"));
                break;
            case 5:
                aList.add(
                    GTLanguageManager.addStringLocalization(
                        "godforge.casings.5.Tooltip.0",
                        "Controls the flow of gravitons to manipulate gravity"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager.addStringLocalization(
                            "godforge.casings.5.Tooltip.1",
                            "Exponential scaling past 800 Galaxies"));
                break;
            case 6:
                aList.add(
                    GTLanguageManager.addStringLocalization(
                        "godforge.casings.6.Tooltip.0",
                        "Controls the flow of gravitons to manipulate gravity"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager.addStringLocalization("godforge.casings.6.Tooltip.1", "Getting closer..."));
                break;
            case 7:
                aList.add(
                    GTLanguageManager.addStringLocalization(
                        "godforge.casings.7.Tooltip.0",
                        "Controls the flow of gravitons to manipulate gravity"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager.addStringLocalization("godforge.casings.7.Tooltip.1", "Gravity Central"));
                break;
            case 8:
                aList.add(
                    GTLanguageManager.addStringLocalization(
                        "godforge.casings.8.Tooltip.0",
                        "Transfers and stores extreme amounts of heat without any loss"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager.addStringLocalization("godforge.casings.8.Tooltip.1", "<<<Thermal<<<"));
                aList.add(
                    AQUA.toString() + BOLD
                        + GTLanguageManager.addStringLocalization("godforge.casings.8.Tooltip.2", ">>>Wave>>>"));
                break;
            default:
                aList.add(
                    EnumChatFormatting.RED.toString() + BOLD
                        + GTLanguageManager
                            .addStringLocalization("godforge.casings.Error.Tooltip", "Error, report to GTNH team"));
        }
    }
}
