package com.gtnewhorizons.gtnhintergalactic.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevator.ElevatorUtil;

import gregtech.common.blocks.GT_Item_Casings_Abstract;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

/**
 * Items of the space elevator motors
 *
 * @author minecraft7771
 */
public class ItemCasingSpaceElevatorMotor extends GT_Item_Casings_Abstract {

    /**
     * Create a new item for the motor blocks
     *
     * @param block Block which this item represents
     */
    public ItemCasingSpaceElevatorMotor(Block block) {
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
        tooltip.add(GCCoreUtil.translate("gt.blockcasings.ig.motor.desc0"));
        tooltip.add(
                EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD
                        + GCCoreUtil.translate("gt.blockcasings.ig.motor.t" + (stack.getItemDamage() + 1) + ".desc1"));
        tooltip.add(
                String.format(
                        GCCoreUtil.translate("gt.blockcasings.ig.motor.desc2"),
                        ElevatorUtil.getModuleSlotsUnlocked(stack.getItemDamage() + 1)));
    }
}
