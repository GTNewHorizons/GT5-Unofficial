/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2024  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.loaders.item;

import static kubatech.kubatech.KT;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kubatech.loaders.ItemLoader;

public class KubaItems extends Item {

    private static final HashMap<Integer, ItemProxy> items = new HashMap<>();
    private static int idCounter = 0;

    public KubaItems() {
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setCreativeTab(KT);
        this.setUnlocalizedName("kubaitems");
    }

    public ItemStack registerProxyItem(ItemProxy item) {
        items.put(idCounter, item);
        item.ItemInit(idCounter);
        return new ItemStack(this, 1, idCounter++);
    }

    private ItemProxy getItem(ItemStack stack) {
        return items.get(stack.getItemDamage());
    }

    public static ItemProxy getItemProxy(ItemStack stack) {
        if (!(stack.getItem() instanceof KubaItems)) return null;
        return ItemLoader.kubaitems.getItem(stack);
    }

    private ItemProxy getItem(int damage) {
        return items.get(damage);
    }

    @Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_,
        int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        return false;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_) {
        return getItem(p_77661_1_).getItemUseAction(p_77661_1_);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        return getItem(p_77659_1_).onItemRightClick(p_77659_1_, p_77659_2_, p_77659_3_);
    }

    @Override
    public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_) {
        return getItem(p_77654_1_).onEaten(p_77654_1_, p_77654_2_, p_77654_3_);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return getItem(p_77626_1_).getMaxItemUseDuration();
    }

    @Override
    public int getMetadata(int p_77647_1_) {
        return p_77647_1_;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        getItem(p_77624_1_).addInformation(p_77624_1_, p_77624_2_, (List<String>) p_77624_3_, p_77624_4_);
    }

    @Override
    public String getUnlocalizedName(ItemStack p_77667_1_) {
        return getItem(p_77667_1_).getUnlocalizedName();
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return getItem(p_77653_1_).getDisplayName(p_77653_1_);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister p_94581_1_) {
        items.values()
            .forEach(t -> t.registerIcon(p_94581_1_));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int damage) {
        return getItem(damage).getIcon();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
        for (int i = 0; i < items.size(); i++) p_150895_3_.add(new ItemStack(p_150895_1_, 1, i));
    }

    @Override
    public void onUpdate(ItemStack p_77663_1_, World p_77663_2_, Entity p_77663_3_, int p_77663_4_,
        boolean p_77663_5_) {
        getItem(p_77663_1_).onUpdate(p_77663_1_, p_77663_2_, p_77663_3_, p_77663_4_, p_77663_5_);
    }
}
