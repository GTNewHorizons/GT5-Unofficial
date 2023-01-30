package com.github.technus.tectech.loader.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.github.technus.tectech.thing.item.ElementalDefinitionContainer_EM;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTabEM extends CreativeTabs {

    public CreativeTabEM(String name) {
        super(name);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Item getTabIconItem() {
        return ElementalDefinitionContainer_EM.INSTANCE;
    }
}
