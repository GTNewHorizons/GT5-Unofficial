package com.github.bartimaeusnek.bartworks.client.ClientEventHandler;

import net.minecraft.block.Block;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import com.github.bartimaeusnek.bartworks.API.BioVatLogicAdder;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;

import static gregtech.api.util.GT_Utility.getColoredTierNameFromTier;

@SideOnly(Side.CLIENT)
public class TooltipEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void getTooltip(ItemTooltipEvent event) {

        if (event == null || event.itemStack == null || event.itemStack.getItem() == null) return;

        final Block block = Block.getBlockFromItem(event.itemStack.getItem());

        Byte tier = BioVatLogicAdder.BioVatGlass.getGlassMap().getOrDefault(new BioVatLogicAdder.BlockMetaPair(
            block,
            (byte) event.itemStack.getItemDamage()), null);

        if (tier == null) return;

        event.toolTip.add(StatCollector.translateToLocal("tooltip.glass_tier.0.name") + " " + getColoredTierNameFromTier(tier));

    }
}
