package com.gtnewhorizons.gtnhintergalactic.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import gregtech.common.blocks.GT_Item_Casings_Abstract;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

/**
 * Items of the space elevator casings
 *
 * @author minecraft7771
 */
public class ItemCasingSpaceElevator extends GT_Item_Casings_Abstract {

    /**
     * Create a new item for the casing blocks
     *
     * @param block Block which this item represents
     */
    public ItemCasingSpaceElevator(Block block) {
        super(block);
    }

    /**
     * Add more information to the tooltip of this block
     *
     * @param stack   Item stack, which contains this item
     * @param player  Player which holds the item stack
     * @param tooltip Tooltip of the item stack
     * @param f3_h    Flag if advanced tooltips are enabled
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, @SuppressWarnings("rawtypes") List tooltip,
            boolean f3_h) {
        tooltip.add(GCCoreUtil.translate("gt.blockcasings.ig." + stack.getItemDamage() + ".desc0"));
        tooltip.add(
                EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GCCoreUtil.translate("gt.blockcasings.ig." + stack.getItemDamage() + ".desc1"));
    }
}
