package ggfab;

import static gregtech.api.enums.GTValues.W;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.IItemContainer;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public enum GGItemList implements IItemContainer {

    LinkedInputBus,
    AdvAssLine,
    // region single use tool
    ToolCast_MV,
    ToolCast_HV,
    ToolCast_EV,
    // order matters, do not insert randomly like a n00b
    One_Use_craftingToolFile,
    One_Use_craftingToolWrench,
    One_Use_craftingToolCrowbar,
    One_Use_craftingToolWireCutter,
    One_Use_craftingToolHardHammer,
    One_Use_craftingToolSoftHammer,
    One_Use_craftingToolScrewdriver,
    One_Use_craftingToolSaw,
    Shape_One_Use_craftingToolFile,
    Shape_One_Use_craftingToolWrench,
    Shape_One_Use_craftingToolCrowbar,
    Shape_One_Use_craftingToolWireCutter,
    Shape_One_Use_craftingToolHardHammer,
    Shape_One_Use_craftingToolSoftHammer,
    Shape_One_Use_craftingToolScrewdriver,
    Shape_One_Use_craftingToolSaw,
    // ordered section ends
    // endregion
    //
    ;

    private ItemStack mStack;
    private boolean mHasNotBeenSet = true;

    @Override
    public IItemContainer set(Item aItem) {
        mHasNotBeenSet = false;
        if (aItem == null) {
            return this;
        }
        ItemStack aStack = new ItemStack(aItem, 1, 0);
        mStack = GTUtility.copyAmount(1, aStack);
        return this;
    }

    @Override
    public IItemContainer hidden() {
        codechicken.nei.api.API.hideItem(get(1L));
        return this;
    }

    @Override
    public IItemContainer set(ItemStack aStack) {
        mHasNotBeenSet = false;
        mStack = GTUtility.copyAmount(1, aStack);
        return this;
    }

    @Override
    public Item getItem() {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        if (GTUtility.isStackInvalid(mStack)) {
            return null;
        }
        return mStack.getItem();
    }

    @Override
    public Block getBlock() {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        return GTUtility.getBlockFromStack(new ItemStack(getItem()));
    }

    @Override
    public final boolean hasBeenSet() {
        return !mHasNotBeenSet;
    }

    @Override
    public boolean isStackEqual(Object aStack) {
        return isStackEqual(aStack, false, false);
    }

    @Override
    public boolean isStackEqual(Object aStack, boolean aWildcard, boolean aIgnoreNBT) {
        if (GTUtility.isStackInvalid(aStack)) {
            return false;
        }
        return GTUtility.areUnificationsEqual((ItemStack) aStack, aWildcard ? getWildcard(1) : get(1), aIgnoreNBT);
    }

    @Override
    public ItemStack get(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        if (GTUtility.isStackInvalid(mStack)) {
            return GTUtility.copyAmount(aAmount, aReplacements);
        }
        return GTUtility.copyAmount(aAmount, GTOreDictUnificator.get(mStack));
    }

    @Override
    public ItemStack getWildcard(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        if (GTUtility.isStackInvalid(mStack)) {
            return GTUtility.copyAmount(aAmount, aReplacements);
        }
        return GTUtility.copyAmountAndMetaData(aAmount, W, GTOreDictUnificator.get(mStack));
    }

    @Override
    public ItemStack getUndamaged(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        if (GTUtility.isStackInvalid(mStack)) {
            return GTUtility.copyAmount(aAmount, aReplacements);
        }
        return GTUtility.copyAmountAndMetaData(aAmount, 0, GTOreDictUnificator.get(mStack));
    }

    @Override
    public ItemStack getAlmostBroken(long aAmount, Object... aReplacements) {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        if (GTUtility.isStackInvalid(mStack)) {
            return GTUtility.copyAmount(aAmount, aReplacements);
        }
        return GTUtility.copyAmountAndMetaData(aAmount, mStack.getMaxDamage() - 1, GTOreDictUnificator.get(mStack));
    }

    @Override
    public ItemStack getWithName(long aAmount, String aDisplayName, Object... aReplacements) {
        ItemStack rStack = get(1, aReplacements);
        if (GTUtility.isStackInvalid(rStack)) {
            return null;
        }
        rStack.setStackDisplayName(aDisplayName);
        return GTUtility.copyAmount(aAmount, rStack);
    }

    @Override
    public ItemStack getWithCharge(long aAmount, int aEnergy, Object... aReplacements) {
        ItemStack rStack = get(1, aReplacements);
        if (GTUtility.isStackInvalid(rStack)) {
            return null;
        }
        GTModHandler.chargeElectricItem(rStack, aEnergy, Integer.MAX_VALUE, true, false);
        return GTUtility.copyAmount(aAmount, rStack);
    }

    @Override
    public ItemStack getWithDamage(long aAmount, long aMetaValue, Object... aReplacements) {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        if (GTUtility.isStackInvalid(mStack)) {
            return GTUtility.copyAmount(aAmount, aReplacements);
        }
        return GTUtility.copyAmountAndMetaData(aAmount, aMetaValue, GTOreDictUnificator.get(mStack));
    }

    @Override
    public IItemContainer registerOre(Object... aOreNames) {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        for (Object tOreName : aOreNames) {
            GTOreDictUnificator.registerOre(tOreName, get(1));
        }
        return this;
    }

    @Override
    public IItemContainer registerWildcardAsOre(Object... aOreNames) {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        for (Object tOreName : aOreNames) {
            GTOreDictUnificator.registerOre(tOreName, getWildcard(1));
        }
        return this;
    }

}
