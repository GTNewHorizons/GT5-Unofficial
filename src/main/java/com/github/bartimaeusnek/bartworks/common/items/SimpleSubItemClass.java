package com.github.bartimaeusnek.bartworks.common.items;

import com.github.bartimaeusnek.bartworks.MainMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class SimpleSubItemClass extends Item {
    String[] tex;
    @SideOnly(Side.CLIENT)
    protected IIcon[] itemIcon;

    public SimpleSubItemClass(String[] tex){
        this.tex=tex;
        this.hasSubtypes=true;
        this.setCreativeTab(MainMod.BWT);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = new IIcon[tex.length];
        for (int i = 0; i < tex.length; i++) {
            itemIcon[i]=iconRegister.registerIcon(MainMod.modID+":"+tex[i]);
        }

    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
        for (int i = 0; i < tex.length; i++) {
            p_150895_3_.add(new ItemStack(p_150895_1_, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_)
    {
        if (p_77617_1_<tex.length)
            return this.itemIcon[p_77617_1_];
        else
            return this.itemIcon[0];
    }

    public String getUnlocalizedName(ItemStack p_77667_1_)
    {
        if (p_77667_1_.getItemDamage()<tex.length)
            return "item."+this.tex[p_77667_1_.getItemDamage()].replaceAll("/",".");
        else
            return "WrongDamageItemDestroyIt";
    }

}
