/*
 * Copyright (c) 2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.client.ClientEventHandler;

import com.github.bartimaeusnek.bartworks.API.BioVatLogicAdder;
import com.github.bartimaeusnek.bartworks.common.blocks.BW_Blocks;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void getTooltip(ItemTooltipEvent event) {
        if (event.itemStack == null || event.itemStack.getItem() == null || Block.getBlockFromItem(event.itemStack.getItem()) == null || event.itemStack.getItemDamage() > 127)
            return;

        final Block BLOCK = Block.getBlockFromItem(event.itemStack.getItem());
        if (BLOCK instanceof BW_Blocks) {
            return;
        }
        final BioVatLogicAdder.BlockMetaPair PAIR = new BioVatLogicAdder.BlockMetaPair(BLOCK, (byte) event.itemStack.getItemDamage());
        final HashMap<BioVatLogicAdder.BlockMetaPair, Byte> GLASSMAP = BioVatLogicAdder.BioVatGlass.getGlassMap();
        if (GLASSMAP.containsKey(PAIR)) {
            int tier = GLASSMAP.get(PAIR);
            event.toolTip.add("Glass-Tier: " + BW_Util.getColorForTier(tier) + GT_Values.VN[tier] + ChatColorHelper.RESET);
        } else if (BLOCK.getMaterial().equals(Material.glass)) {
            event.toolTip.add("Glass-Tier: " + BW_Util.getColorForTier(3) + GT_Values.VN[3] + ChatColorHelper.RESET);
        }
    }
}