package com.minecraft7771.gtnhintergalactic.item;

import java.util.List;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import gregtech.common.blocks.GT_Item_Casings_Abstract;

public class ItemCasingSpaceElevator extends GT_Item_Casings_Abstract {

    public ItemCasingSpaceElevator(Block block) {
        super(block);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer aPlayer, List tooltip, boolean f3_h) {
        tooltip.add(GCCoreUtil.translate("gt.blockcasings.gs." + stack.getItemDamage() + ".desc0"));
        tooltip.add(
                EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GCCoreUtil.translate("gt.blockcasings.gs." + stack.getItemDamage() + ".desc1"));
    }
}
