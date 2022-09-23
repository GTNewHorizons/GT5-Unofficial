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

import static ic2.api.item.IKineticRotor.GearboxType.WATER;
import static ic2.api.item.IKineticRotor.GearboxType.WIND;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IKineticRotor;
import ic2.core.block.kineticgenerator.gui.GuiWaterKineticGenerator;
import ic2.core.block.kineticgenerator.gui.GuiWindKineticGenerator;
import ic2.core.util.StackUtil;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class BW_Stonage_Rotors extends Item implements IKineticRotor {

    private final int[] DiaMinMax = new int[3];
    private final float eff;
    private final IKineticRotor.GearboxType type;
    private final ResourceLocation tex;
    private final String itemTex;
    private final int speed;
    private final float mRotor;
    private final int maxDamageEx;
    private int dura;

    public BW_Stonage_Rotors(
            int diameter,
            float eff,
            int speed,
            float mRotor,
            int min,
            int max,
            int durability,
            IKineticRotor.GearboxType type,
            ResourceLocation tex,
            String Name,
            String itemTex) {
        this.DiaMinMax[0] = diameter;
        this.DiaMinMax[1] = min;
        this.DiaMinMax[2] = max;
        this.eff = eff;
        this.mRotor = mRotor;
        this.speed = speed;
        this.type = type;
        this.tex = tex;
        this.setMaxDamage(30000);
        this.maxDamageEx = durability;
        this.setUnlocalizedName(Name);
        this.setCreativeTab(MainMod.BWT);
        this.itemTex = itemTex;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(MainMod.MOD_ID + ":" + this.itemTex);
    }

    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b) {
        info.add(StatCollector.translateToLocalFormatted(
                "ic2.itemrotor.wind.info", this.DiaMinMax[1], this.DiaMinMax[2]));
        IKineticRotor.GearboxType type = null;
        if (Minecraft.getMinecraft().currentScreen instanceof GuiWaterKineticGenerator) {
            type = WATER;
        } else if (Minecraft.getMinecraft().currentScreen instanceof GuiWindKineticGenerator) {
            type = WIND;
        }
        info.add(StatCollector.translateToLocal("tooltip.rotor.0.name") + " " + this.DiaMinMax[0]);
        info.add(StatCollector.translateToLocal("tooltip.rotor.1.name") + " "
                + ((this.getMaxDamageEx() - this.getDamageOfStack(itemStack)) / 100) + "/"
                + (this.getMaxDamageEx() / 100));
        info.add(StatCollector.translateToLocal("tooltip.rotor.2.name") + " " + this.eff);
        info.add(StatCollector.translateToLocal("tooltip.rotor.3.name") + " " + this.speed);
        info.add(StatCollector.translateToLocal("tooltip.rotor.4.name") + " " + this.mRotor);
        if (type != null) {
            info.add(StatCollector.translateToLocal(("ic2.itemrotor.fitsin." + this.isAcceptedType(itemStack, type))));
        }
        info.add(BW_Tooltip_Reference.ADDED_BY_BARTWORKS.get());
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
    public boolean isAcceptedType(ItemStack itemStack, IKineticRotor.GearboxType gearboxType) {
        return gearboxType.equals(this.type);
    }

    public int getSpeed() {
        return speed;
    }

    public float getmRotor() {
        return mRotor;
    }

    public void setDamageForStack(ItemStack stack, int advDmg) {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        nbtData.setInteger("DmgEx", advDmg);
        if (this.maxDamageEx > 0) {
            double p = (double) advDmg / (double) this.maxDamageEx;
            int newDmg = (int) (stack.getMaxDamage() * p);
            if (newDmg >= stack.getMaxDamage()) {
                newDmg = stack.getMaxDamage() - 1;
            }
            stack.setItemDamage(newDmg);
            this.dura = newDmg;
        }
    }

    public int getDamageOfStack(ItemStack stack) {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        this.dura = nbtData.getInteger("DmgEx");
        return this.dura;
    }

    public int getMaxDamageEx() {
        return this.maxDamageEx;
    }

    public void damageItemStack(ItemStack stack, int Dmg) {
        setDamageForStack(stack, getDamageOfStack(stack) + Dmg);
    }
}
