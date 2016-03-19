package com.detrav.items;

import gregtech.api.interfaces.IItemContainer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by wital_000 on 19.03.2016.
 */
public enum ItemList implements IItemContainer {
    ;

    @Override
    public Item getItem() {
        return null;
    }

    @Override
    public Block getBlock() {
        return null;
    }

    @Override
    public boolean isStackEqual(Object o) {
        return false;
    }

    @Override
    public boolean isStackEqual(Object o, boolean b, boolean b1) {
        return false;
    }

    @Override
    public ItemStack get(long l, Object... objects) {
        return null;
    }

    @Override
    public ItemStack getWildcard(long l, Object... objects) {
        return null;
    }

    @Override
    public ItemStack getUndamaged(long l, Object... objects) {
        return null;
    }

    @Override
    public ItemStack getAlmostBroken(long l, Object... objects) {
        return null;
    }

    @Override
    public ItemStack getWithDamage(long l, long l1, Object... objects) {
        return null;
    }

    @Override
    public IItemContainer set(Item item) {
        return null;
    }

    @Override
    public IItemContainer set(ItemStack itemStack) {
        return null;
    }

    @Override
    public IItemContainer registerOre(Object... objects) {
        return null;
    }

    @Override
    public IItemContainer registerWildcardAsOre(Object... objects) {
        return null;
    }

    @Override
    public ItemStack getWithCharge(long l, int i, Object... objects) {
        return null;
    }

    @Override
    public ItemStack getWithName(long l, String s, Object... objects) {
        return null;
    }

    @Override
    public boolean hasBeenSet() {
        return false;
    }
}
