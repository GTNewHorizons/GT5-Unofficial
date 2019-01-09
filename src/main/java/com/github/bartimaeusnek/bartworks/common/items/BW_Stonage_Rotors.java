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

package com.github.bartimaeusnek.bartworks.common.items;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IKineticRotor;
import ic2.core.block.kineticgenerator.gui.GuiWaterKineticGenerator;
import ic2.core.block.kineticgenerator.gui.GuiWindKineticGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import java.util.List;

import static ic2.api.item.IKineticRotor.GearboxType.WATER;
import static ic2.api.item.IKineticRotor.GearboxType.WIND;

public class BW_Stonage_Rotors extends Item implements IKineticRotor {

    private int[] DiaMinMax = new int[3];
    private float eff;
    private GearboxType type;
    private ResourceLocation tex;
    private String itemTex;

    public BW_Stonage_Rotors(int diameter, float eff, int min, int max, int durability, GearboxType type, ResourceLocation tex, String Name, String itemTex) {
        this.DiaMinMax[0] = diameter;
        this.DiaMinMax[1] = min;
        this.DiaMinMax[2] = max;
        this.eff = eff;
        this.type = type;
        this.tex = tex;
        this.setMaxDamage(durability);
        this.setUnlocalizedName(Name);
        this.setCreativeTab(MainMod.BWT);
        this.itemTex = itemTex;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(MainMod.modID + ":" + itemTex);
    }

    public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b) {
        info.add(StatCollector.translateToLocalFormatted("ic2.itemrotor.wind.info", this.DiaMinMax[1], this.DiaMinMax[2]));
        GearboxType type = null;
        if (Minecraft.getMinecraft().currentScreen instanceof GuiWaterKineticGenerator) {
            type = WATER;
        } else if (Minecraft.getMinecraft().currentScreen instanceof GuiWindKineticGenerator) {
            type = WIND;
        }
        if (type != null) {
            info.add(StatCollector.translateToLocal(("ic2.itemrotor.fitsin." + this.isAcceptedType(itemStack, type))));
        }
        info.add("Added by" + ChatColorHelper.DARKGREEN + " BartWorks");
    }

    @Override
    public int getDiameter(ItemStack itemStack) {
        return this.DiaMinMax[0];
    }

    @Override
    public ResourceLocation getRotorRenderTexture(ItemStack itemStack) {
        return this.tex;
    }

    @Override
    public float getEfficiency(ItemStack itemStack) {
        return this.eff;
    }

    @Override
    public int getMinWindStrength(ItemStack itemStack) {
        return this.DiaMinMax[1];
    }

    @Override
    public int getMaxWindStrength(ItemStack itemStack) {
        return this.DiaMinMax[2];
    }

    @Override
    public boolean isAcceptedType(ItemStack itemStack, GearboxType gearboxType) {
        return gearboxType.equals(this.type);
    }
}
