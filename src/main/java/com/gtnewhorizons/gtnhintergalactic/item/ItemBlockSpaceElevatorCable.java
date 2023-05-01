package com.gtnewhorizons.gtnhintergalactic.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

/**
 * Items of the space elevator cable
 *
 * @author minecraft7771
 */
public class ItemBlockSpaceElevatorCable extends ItemBlock {

    /**
     * Create a new item for the cable block
     *
     * @param block Block which this item represents
     */
    public ItemBlockSpaceElevatorCable(Block block) {
        super(block);
    }

    /**
     * Get the unlocalized name of this block
     *
     * @param stack Item stack, which contains this item
     * @return Unlocalized name
     */
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "gt.blockcasings.ig.cable";
    }

    /**
     * Add more information to the tooltip of this block
     *
     * @param stack   Item stack, which contains this item
     * @param player  Player which holds the item stack
     * @param tooltip Tooltip of the item stack
     * @param f3_h    Flag if advanced tooltips are enabled
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean f3_h) {
        tooltip.add(GCCoreUtil.translate("gt.blockcasings.ig.cable.desc0"));
        tooltip.add(
                EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GCCoreUtil.translate("gt.blockcasings.ig.cable.desc1"));
    }
}
