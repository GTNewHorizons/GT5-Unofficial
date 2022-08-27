/*
 * Copyright (c) 2018-2020 bartimaeusnek
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

package com.github.bartimaeusnek.bartworks.common.items;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.WorldData;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class BW_SimpleWindMeter extends Item {

    public BW_SimpleWindMeter() {
        this.maxStackSize = 1;
        this.setMaxDamage(15);
        this.setCreativeTab(MainMod.BWT);
        this.hasSubtypes = false;
        this.setUnlocalizedName("BW_SimpleWindMeter");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_) {
        this.itemIcon = p_94581_1_.registerIcon(MainMod.MOD_ID + ":BW_SimpleWindMeter");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean p_77624_4_) {
        super.addInformation(itemStack, entityPlayer, list, p_77624_4_);
        list.add(StatCollector.translateToLocal("tooltip.windmeter.0.name"));
        list.add(StatCollector.translateToLocal("tooltip.windmeter.1.name") + " "
                + (this.getMaxDamage() - this.getDamage(itemStack)) + "/" + this.getMaxDamage());
        list.add(BW_Tooltip_Reference.ADDED_BY_BARTWORKS.get());
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if (entityPlayer.worldObj.isRemote
                || world == null
                || WorldData.get(world) == null
                || WorldData.get(world).windSim == null) return itemStack;

        float windStrength = (float) WorldData.get(world).windSim.getWindAt(entityPlayer.posY);
        String windS = windStrength < 1f
                ? StatCollector.translateToLocal("tooltip.windmeter.2.name")
                : windStrength < 10f
                        ? StatCollector.translateToLocal("tooltip.windmeter.3.name")
                        : windStrength < 20f
                                ? StatCollector.translateToLocal("tooltip.windmeter.4.name")
                                : windStrength < 30f
                                        ? StatCollector.translateToLocal("tooltip.windmeter.5.name")
                                        : windStrength < 50f
                                                ? StatCollector.translateToLocal("tooltip.windmeter.6.name")
                                                : StatCollector.translateToLocal("tooltip.windmeter.7.name");
        entityPlayer.addChatMessage(
                new ChatComponentText(StatCollector.translateToLocal("tooltip.windmeter.8.name") + " " + windS + "."));
        itemStack.damageItem(1, entityPlayer);
        return itemStack;
    }
}
