package com.github.bartimaeusnek.bartworks.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class SimpleIconItem extends Item {

    String tex;

    public SimpleIconItem(String tex){
        super();
        this.tex=tex;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon=iconRegister.registerIcon("bartworks:"+tex);
    }
}
