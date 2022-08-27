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

import static com.github.bartimaeusnek.bartworks.common.loaders.BioItemList.*;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.util.BW_ColorUtil;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.BioCulture;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class LabParts extends SimpleSubItemClass {

    public LabParts(String[] tex) {
        super(tex);
        this.setCreativeTab(MainMod.BIO_TAB);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {

        if (itemStack == null || itemStack.getTagCompound() == null) return EnumRarity.common;

        switch (itemStack.getItemDamage()) {
            case 0:
                return BW_Util.getRarityFromByte(
                        itemStack.getTagCompound().getCompoundTag("DNA").getByte("Rarity"));
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
        if (stack.getItemDamage() == 0
                && stack.getTagCompound() != null
                && stack.getTagCompound().getIntArray("Color") != null
                && stack.getTagCompound().getIntArray("Color").length > 0) {
            int[] rgb = stack.getTagCompound().getIntArray("Color");
            return BW_ColorUtil.getColorFromRGBArray(rgb);
        }
        return super.getColorFromItemStack(stack, p_82790_2_);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean b) {
        if (itemStack == null) return;

        if (itemStack.getTagCompound() == null) {
            switch (itemStack.getItemDamage()) {
                case 0:
                    list.add(StatCollector.translateToLocal("tooltip.labparts.0.name"));
                    break;
                case 1:
                    list.add(StatCollector.translateToLocal("tooltip.labparts.1.name"));
                    break;
                case 2:
                    list.add(StatCollector.translateToLocal("tooltip.labparts.2.name"));
                    break;
                case 3:
                    list.add(StatCollector.translateToLocal("tooltip.labparts.3.name"));
                    break;
                case 4:
                    list.add(StatCollector.translateToLocal("tooltip.labparts.4.name"));
                    break;
                default:
                    break;
            }
            super.addInformation(itemStack, entityPlayer, list, b);
            return;
        }

        BioCulture culture = BioCulture.getBioCulture(itemStack.getTagCompound().getString("Name"));

        switch (itemStack.getItemDamage()) {
            case 0:
                list.add(StatCollector.translateToLocal("tooltip.labparts.5.name") + " "
                        + itemStack.getTagCompound().getString("Name")
                        + (culture != null ? " (" + culture.getLocalisedName() + ")" : ""));
                if (!itemStack.getTagCompound().getBoolean("Breedable")) {
                    list.add(StatCollector.translateToLocal("tooltip.labparts.6.name"));
                }
                break;
            case 1:
                list.add(StatCollector.translateToLocal("tooltip.labparts.7.name") + " "
                        + itemStack.getTagCompound().getString("Name")
                        + (culture != null ? " (" + culture.getLocalisedName() + ")" : ""));
                break;
            case 2:
                list.add(StatCollector.translateToLocal("tooltip.labparts.8.name") + " "
                        + itemStack.getTagCompound().getString("Name")
                        + (culture != null ? " (" + culture.getLocalisedName() + ")" : ""));
                break;
            default:
                break;
        }
        super.addInformation(itemStack, entityPlayer, list, b);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        list.addAll(getAllPetriDishes());
        list.addAll(getAllDNASampleFlasks());
        list.addAll(getAllPlasmidCells());
        super.getSubItems(item, creativeTabs, list);
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        if (itemStack.getItemDamage() == 0 && itemStack.getTagCompound() != null)
            return "filled.item." + this.tex[itemStack.getItemDamage()].replaceAll("/", ".");
        return super.getUnlocalizedName(itemStack);
    }
}
