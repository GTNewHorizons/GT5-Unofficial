package com.minecraft7771.gtnhintergalactic.item;

import java.util.List;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

/**
 * Items of the space elevator cable
 *
 * @author minecraft7771
 */
public class ItemBlockSpaceElevatorCable extends ItemBlock {

    public ItemBlockSpaceElevatorCable(Block block) {
        super(block);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "gt.blockcasings.gs.cable";
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean f3_h) {
        tooltip.add(GCCoreUtil.translate("gt.blockcasings.gs.cable.desc0"));
        tooltip.add(
                EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GCCoreUtil.translate("gt.blockcasings.gs.cable.desc1"));
    }
}
