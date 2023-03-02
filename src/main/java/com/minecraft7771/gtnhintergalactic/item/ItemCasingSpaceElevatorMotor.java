package com.minecraft7771.gtnhintergalactic.item;

import gregtech.common.blocks.GT_Item_Casings_Abstract;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class ItemCasingSpaceElevatorMotor extends GT_Item_Casings_Abstract {
    public ItemCasingSpaceElevatorMotor(Block block) {
        super(block);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer aPlayer, List tooltip, boolean f3_h) {
        tooltip.add(GCCoreUtil.translate("gt.blockcasings.gs.motor.desc0"));
        tooltip.add(
            EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                + GCCoreUtil.translate(
                "gt.blockcasings.gs.motor.t"
                    + (stack.getItemDamage() + 1)
                    + ".desc1"));
    }
}
