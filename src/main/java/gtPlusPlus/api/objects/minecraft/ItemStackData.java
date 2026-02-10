package gtPlusPlus.api.objects.minecraft;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemStackData {

    protected final @Nullable Item mItem;
    protected final int mDamage;
    protected final int mStackSize;
    protected final @Nullable NBTTagCompound mNBT;
    protected final @NotNull String mUniqueDataTag;

    public ItemStackData(@NotNull ItemStack aStack) {
        mItem = aStack.getItem();
        mDamage = aStack.getItemDamage();
        mStackSize = aStack.stackSize;
        mNBT = aStack.getTagCompound();
        mUniqueDataTag = String.valueOf(Item.getIdFromItem(mItem)) + mDamage + mStackSize + 10;
    }

    public String getUniqueDataIdentifier() {
        return this.mUniqueDataTag;
    }

    public @NotNull ItemStack getStack() {
        ItemStack aTemp = new ItemStack(mItem, mStackSize, mDamage);
        if (mNBT != null) aTemp.setTagCompound(mNBT);
        return aTemp;
    }
}
