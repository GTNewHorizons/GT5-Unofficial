/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.github.bartimaeusnek.bartworks.MainMod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SimpleSubItemClass extends Item {

    @SideOnly(Side.CLIENT)
    protected IIcon[] itemIcon;

    String[] tex;

    public SimpleSubItemClass(String... tex) {
        this.tex = tex;
        this.hasSubtypes = true;
        this.setCreativeTab(MainMod.BWT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = new IIcon[this.tex.length];
        for (int i = 0; i < this.tex.length; i++) {
            this.itemIcon[i] = iconRegister.registerIcon(MainMod.MOD_ID + ":" + this.tex[i]);
        }
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> p_150895_3_) {
        for (int i = 0; i < this.tex.length; i++) {
            p_150895_3_.add(new ItemStack(p_150895_1_, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_) {
        if (p_77617_1_ < this.tex.length) return this.itemIcon[p_77617_1_];
        return this.itemIcon[0];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (stack.getItemDamage() < this.tex.length) return "item." + this.tex[stack.getItemDamage()].replace('/', '.');
        return "WrongDamageItemDestroyIt";
    }
}
