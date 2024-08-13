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

package com.github.bartimaeusnek.bartworks.system.material;

import static gregtech.api.enums.GT_Values.W;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.IItemContainer;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public enum BW_NonMeta_MaterialItems implements IItemContainer {

    Depleted_Tiberium_1,
    Depleted_Tiberium_2,
    Depleted_Tiberium_4,
    TiberiumCell_1,
    TiberiumCell_2,
    TiberiumCell_4,
    TheCoreCell,
    Depleted_TheCoreCell;

    private ItemStack mStack;
    private boolean mHasNotBeenSet = true;

    @Override
    public IItemContainer set(Item aItem) {
        this.mHasNotBeenSet = false;
        if (aItem == null) return this;
        ItemStack aStack = new ItemStack(aItem, 1, 0);
        this.mStack = GT_Utility.copyAmount(1, aStack);
        return this;
    }

    @Override
    public IItemContainer set(ItemStack aStack) {
        this.mHasNotBeenSet = false;
        this.mStack = GT_Utility.copyAmount(1, aStack);
        return this;
    }

    @Override
    public IItemContainer hidden() {
        codechicken.nei.api.API.hideItem(get(1L));
        return this;
    }

    @Override
    public Item getItem() {
        if (this.mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        if (GT_Utility.isStackInvalid(this.mStack)) return null;
        return this.mStack.getItem();
    }

    @Override
    public Block getBlock() {
        if (this.mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        return GT_Utility.getBlockFromItem(this.getItem());
    }

    @Override
    public final boolean hasBeenSet() {
        return !this.mHasNotBeenSet;
    }

    @Override
    public boolean isStackEqual(Object aStack) {
        return this.isStackEqual(aStack, false, false);
    }

    @Override
    public boolean isStackEqual(Object aStack, boolean aWildcard, boolean aIgnoreNBT) {
        if (GT_Utility.isStackInvalid(aStack)) return false;
        return GT_Utility
            .areUnificationsEqual((ItemStack) aStack, aWildcard ? this.getWildcard(1) : this.get(1), aIgnoreNBT);
    }

    @Override
    public ItemStack get(long aAmount, Object... aReplacements) {
        if (this.mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        if (GT_Utility.isStackInvalid(this.mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
        return GT_Utility.copyAmount(aAmount, GT_OreDictUnificator.get(this.mStack));
    }

    @Override
    public ItemStack getWildcard(long aAmount, Object... aReplacements) {
        if (this.mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        if (GT_Utility.isStackInvalid(this.mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
        return GT_Utility.copyAmountAndMetaData(aAmount, W, GT_OreDictUnificator.get(this.mStack));
    }

    @Override
    public ItemStack getUndamaged(long aAmount, Object... aReplacements) {
        if (this.mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        if (GT_Utility.isStackInvalid(this.mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
        return GT_Utility.copyAmountAndMetaData(aAmount, 0, GT_OreDictUnificator.get(this.mStack));
    }

    @Override
    public ItemStack getAlmostBroken(long aAmount, Object... aReplacements) {
        if (this.mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        if (GT_Utility.isStackInvalid(this.mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
        return GT_Utility
            .copyAmountAndMetaData(aAmount, this.mStack.getMaxDamage() - 1, GT_OreDictUnificator.get(this.mStack));
    }

    @Override
    public ItemStack getWithName(long aAmount, String aDisplayName, Object... aReplacements) {
        ItemStack rStack = this.get(1, aReplacements);
        if (GT_Utility.isStackInvalid(rStack)) return null;
        rStack.setStackDisplayName(aDisplayName);
        return GT_Utility.copyAmount(aAmount, rStack);
    }

    @Override
    public ItemStack getWithCharge(long aAmount, int aEnergy, Object... aReplacements) {
        ItemStack rStack = this.get(1, aReplacements);
        if (GT_Utility.isStackInvalid(rStack)) return null;
        GT_ModHandler.chargeElectricItem(rStack, aEnergy, Integer.MAX_VALUE, true, false);
        return GT_Utility.copyAmount(aAmount, rStack);
    }

    @Override
    public ItemStack getWithDamage(long aAmount, long aMetaValue, Object... aReplacements) {
        if (this.mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        if (GT_Utility.isStackInvalid(this.mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
        return GT_Utility.copyAmountAndMetaData(aAmount, aMetaValue, GT_OreDictUnificator.get(this.mStack));
    }

    @Override
    public IItemContainer registerOre(Object... aOreNames) {
        if (this.mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        for (Object tOreName : aOreNames) GT_OreDictUnificator.registerOre(tOreName, this.get(1));
        return this;
    }

    @Override
    public IItemContainer registerWildcardAsOre(Object... aOreNames) {
        if (this.mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + this.name() + "' has not been set to an Item at this time!");
        for (Object tOreName : aOreNames) GT_OreDictUnificator.registerOre(tOreName, this.getWildcard(1));
        return this;
    }
}
