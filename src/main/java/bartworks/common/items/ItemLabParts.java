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

package bartworks.common.items;

import java.awt.Color;
import java.util.List;

import bartworks.API.enums.BioCultureEnum;
import bartworks.API.enums.BioDataEnum;
import bartworks.util.BioData;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import bartworks.MainMod;
import bartworks.common.loaders.BioItemList;
import bartworks.util.BWColorUtil;
import bartworks.util.BWUtil;
import bartworks.util.BioCulture;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLabParts extends SimpleSubItemClass {

    public ItemLabParts(String[] tex) {
        super(tex);
        this.setCreativeTab(MainMod.BIO_TAB);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {

        if (itemStack == null || itemStack.getTagCompound() == null) return EnumRarity.common;

        return switch (itemStack.getItemDamage()) {
            case 0 ->
                BioCultureEnum.LOOKUPS_BY_NAME.getOrDefault(itemStack.getTagCompound()
                    .getString("Name"), BioCultureEnum.NullBioCulture).rarity;
            case 1, 2 ->
                BioDataEnum.LOOKUPS_BY_NAME.getOrDefault(itemStack.getTagCompound()
                    .getString("Name"), BioDataEnum.NullBioData).rarity;
            default -> EnumRarity.common;
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int p_82790_2_) {
        if (stack.getItemDamage() == 0 && stack.getTagCompound() != null){
            Color color = BioCultureEnum.LOOKUPS_BY_NAME.getOrDefault(stack.getTagCompound().getString("Name"), BioCultureEnum.NullBioCulture).color;
            return color.getRGB();
        }
        return super.getColorFromItemStack(stack, p_82790_2_);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean b) {
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

        String name = itemStack.getTagCompound().getString("Name");
        switch (itemStack.getItemDamage()) {
            case 0:
                BioCulture culture = BioCultureEnum.LOOKUPS_BY_NAME.getOrDefault(name, BioCultureEnum.NullBioCulture).bioCulture;
                list.add(StatCollector.translateToLocalFormatted("tooltip.labparts.5.name", culture.getLocalisedName()));
                if (!culture.isBreedable()) {
                    list.add(StatCollector.translateToLocal("tooltip.labparts.6.name"));
                }
                break;
            case 1:
                BioData DNAFlask = BioDataEnum.LOOKUPS_BY_NAME.getOrDefault(name, BioDataEnum.NullBioData).getBioData();
                list.add(StatCollector.translateToLocalFormatted("tooltip.labparts.7.name", DNAFlask.getLocalisedName()));
                break;
            case 2:
                BioData plasmidCell = BioDataEnum.LOOKUPS_BY_NAME.getOrDefault(name, BioDataEnum.NullBioData).getBioData();
                list.add(StatCollector.translateToLocalFormatted("tooltip.labparts.8.name", plasmidCell.getLocalisedName()));
                break;
            default:
                break;
        }
        super.addInformation(itemStack, entityPlayer, list, b);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        list.addAll(BioCultureEnum.getAllPetriDishes());
        list.addAll(BioDataEnum.getAllDNASampleFlasks());
        list.addAll(BioDataEnum.getAllPlasmidCells());
        super.getSubItems(item, creativeTabs, list);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        if (itemStack.getItemDamage() == 0 && itemStack.getTagCompound() != null)
            return "filled.item." + this.tex[itemStack.getItemDamage()].replace('/', '.');
        return super.getUnlocalizedName(itemStack);
    }
}
