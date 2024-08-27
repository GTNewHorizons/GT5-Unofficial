package com.github.bartimaeusnek.bartworks.client.ClientEventHandler;

import static gregtech.api.util.GT_Utility.getColoredTierNameFromTier;

import net.minecraft.block.Block;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import com.github.bartimaeusnek.bartworks.API.GlassTier;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TooltipEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void getTooltip(ItemTooltipEvent event) {

        if (event == null || event.itemStack == null || event.itemStack.getItem() == null) return;

        final Block block = Block.getBlockFromItem(event.itemStack.getItem());
        final int meta = event.itemStack.getItemDamage();

        int tier = GlassTier.getGlassTier(block, meta);

        if (tier == 0) return;

        event.toolTip.add(
            StatCollector.translateToLocal("tooltip.glass_tier.0.name") + " "
                + getColoredTierNameFromTier((byte) tier));

    }
}
