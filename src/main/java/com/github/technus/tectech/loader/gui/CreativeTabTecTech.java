package com.github.technus.tectech.loader.gui;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.item.DebugElementalInstanceContainer_EM;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;

public class CreativeTabTecTech extends CreativeTabs {

    public CreativeTabTecTech(String name) {
        super(name);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Item getTabIconItem() {
        return DebugElementalInstanceContainer_EM.INSTANCE;
    }

    @Override
    public void displayAllReleventItems(List<ItemStack> stuffToShow) {
        for (CustomItemList item : CustomItemList.values()) {
            if (item.hasBeenSet() && item.getBlock() == GregTech_API.sBlockMachines) {
                stuffToShow.add(item.get(1));
            }
        }
        super.displayAllReleventItems(stuffToShow);
    }
}
