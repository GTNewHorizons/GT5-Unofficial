package com.detrav.utils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class DetravCreativeTab extends CreativeTabs {

    public DetravCreativeTab() {
        super("Detrav Scanner");
    }

    @Override
    public Item getTabIconItem() {
        return Items.stick;
    }
}
