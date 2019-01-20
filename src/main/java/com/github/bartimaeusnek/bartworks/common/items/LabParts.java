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
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

import static com.github.bartimaeusnek.bartworks.common.loaders.BioItemList.*;

public class LabParts extends SimpleSubItemClass {

    public LabParts(String[] tex) {
        super(tex);
        this.setCreativeTab(MainMod.BioTab);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {

        if (itemStack == null || itemStack.getTagCompound() == null)
            return EnumRarity.common;

        switch (itemStack.getItemDamage()) {
            case 0:
                return BW_Util.getRarityFromByte(itemStack.getTagCompound().getCompoundTag("DNA").getByte("Rarity"));
            case 1:
            case 2:
                return BW_Util.getRarityFromByte(itemStack.getTagCompound().getByte("Rarity"));
            default:
                return EnumRarity.common;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int p_82790_2_) {
        if (stack.getItemDamage() == 0 && stack.getTagCompound() != null && stack.getTagCompound().getIntArray("Color") != null && stack.getTagCompound().getIntArray("Color").length > 0) {
            int[] rgb = stack.getTagCompound().getIntArray("Color");
            return BW_Util.getColorFromArray(rgb);
        }
        return super.getColorFromItemStack(stack, p_82790_2_);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean b) {
        if (itemStack == null)
            return;

        if (itemStack.getTagCompound() == null) {
            switch (itemStack.getItemDamage()) {
                case 0:
                    list.add("An empty Sterilized Petri Dish.");
                    break;
                case 1:
                    list.add("An empty DNA Flask.");
                    break;
                case 2:
                    list.add("An empty Plasmid Cell.");
                    break;
                case 3:
                    list.add("A special washing power for Bio Engineering.");
                    break;
                case 4:
                    list.add("A powder for the separation of DNA by electrophoresis.");
                    break;
                default:
                    break;
            }
            super.addInformation(itemStack, entityPlayer, list, b);
            return;
        }


        switch (itemStack.getItemDamage()) {
            case 0:
                list.add("A Petri Dish containing: " + itemStack.getTagCompound().getString("Name"));
                if (!itemStack.getTagCompound().getBoolean("Breedable")) {
                    list.add("This is a weak culture, it can not be bred in the Bacterial Vat");
                }
                break;
            case 1:
                list.add("A DNA Flask containing: " + itemStack.getTagCompound().getString("Name"));
                break;
            case 2:
                list.add("A Plasmid Cell containing: " + itemStack.getTagCompound().getString("Name"));
                break;
            default:
                break;
        }
        super.addInformation(itemStack, entityPlayer, list, b);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for (ItemStack stack : getAllPetriDishes())
            list.add(stack);
        for (ItemStack stack : getAllDNASampleFlasks())
            list.add(stack);
        for (ItemStack stack : getAllPlasmidCells())
            list.add(stack);
        super.getSubItems(item, creativeTabs, list);
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        if (itemStack.getItemDamage() == 0 && itemStack.getTagCompound() != null)
            return "filled.item." + this.tex[itemStack.getItemDamage()].replaceAll("/", ".");
        return super.getUnlocalizedName(itemStack);
    }
}
