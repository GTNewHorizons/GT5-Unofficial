package com.github.technus.tectech.util;

import net.minecraft.item.ItemStack;

public class ItemStackLong {

    public final ItemStack itemStack;
    public long stackSize;

    public ItemStackLong(ItemStack itemStack, long stackSize) {
        this.itemStack = itemStack;
        this.stackSize = stackSize;
    }
}
