package com.detrav.utils;

import gregtech.api.interfaces.IDamagableItem;
import gregtech.api.items.GT_MetaBase_Item;
import net.minecraft.item.ItemStack;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class DetravMetaGeneratedTool extends GT_MetaBase_Item implements IDamagableItem {

    public DetravMetaGeneratedTool(String aUnlocalized) {
        super(aUnlocalized);
    }

    @Override
    public Long[] getElectricStats(ItemStack itemStack) {
        return new Long[0];
    }

    @Override
    public Long[] getFluidContainerStats(ItemStack itemStack) {
        return new Long[0];
    }

    @Override
    public boolean doDamageToItem(ItemStack itemStack, int i) {
        return false;
    }
}
