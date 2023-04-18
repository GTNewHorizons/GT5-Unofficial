package com.github.technus.tectech.compatibility.openmodularturrets.blocks.turretheads;

import static com.github.technus.tectech.util.CommonValues.TEC_MARK_EM;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.text.DecimalFormat;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import openmodularturrets.handler.ConfigHandler;

/**
 * Created by Tec on 28/07/2017.
 */
public class TurretHeadItemEM extends ItemBlock {

    private static final DecimalFormat df = new DecimalFormat("0.0");

    public TurretHeadItemEM(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List<String> list, boolean p_77624_4_) {
        list.add(TEC_MARK_EM);
        list.add("");
        list.add(EnumChatFormatting.GOLD + "--" + translateToLocal("tooltip.info") + "--");
        list.add(translateToLocal("tooltip.tier") + ": " + EnumChatFormatting.WHITE + '5');
        list.add(
                translateToLocal("tooltip.range") + ": "
                        + EnumChatFormatting.WHITE
                        + ConfigHandler.getLaserTurretSettings().getRange());
        list.add(
                translateToLocal("tooltip.accuracy") + ": "
                        + EnumChatFormatting.WHITE
                        + translateToLocal("turret.accuracy.high"));
        list.add(
                translateToLocal("tooltip.ammo") + ": " + EnumChatFormatting.WHITE + translateToLocal("turret.ammo.4"));
        list.add(
                translateToLocal("tooltip.tierRequired") + ": "
                        + EnumChatFormatting.WHITE
                        + translateToLocal("base.tier.5"));
        list.add("");
        list.add(EnumChatFormatting.DARK_PURPLE + "--" + translateToLocal("tooltip.damage.label") + "--");
        list.add(
                translateToLocal("tooltip.damage.stat") + ": "
                        + EnumChatFormatting.WHITE
                        + (float) ConfigHandler.getLaserTurretSettings().getDamage() / 2.0F
                        + ' '
                        + translateToLocal("tooltip.health"));
        list.add(translateToLocal("tooltip.aoe") + ": " + EnumChatFormatting.WHITE + '0');
        list.add(
                translateToLocal("tooltip.firerate") + ": "
                        + EnumChatFormatting.WHITE
                        + df.format(20.0F / (float) ConfigHandler.getLaserTurretSettings().getFireRate()));
        list.add(
                translateToLocal("tooltip.energy.stat") + ": "
                        + EnumChatFormatting.WHITE
                        + ConfigHandler.getLaserTurretSettings().getPowerUsage()
                        + " RF");
        list.add("");
        list.add(EnumChatFormatting.DARK_GRAY + translateToLocal("flavour.turret.4"));
    }
}
