package gregtech.api.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Slot_Holo extends Slot {
    public final int mSlotIndex;
    public boolean mEnabled = true;
    public boolean
            mCanInsertItem,
            mCanStackItem;
    public int mMaxStacksize = 127;

    public GT_Slot_Holo(IInventory par1iInventory, int par2, int par3, int par4, boolean aCanInsertItem, boolean aCanStackItem, int aMaxStacksize) {
        super(par1iInventory, par2, par3, par4);
        mCanInsertItem = aCanInsertItem;
        mCanStackItem = aCanStackItem;
        mMaxStacksize = aMaxStacksize;
        mSlotIndex = par2;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack) {
        return mCanInsertItem;
    }

    @Override
    public int getSlotStackLimit() {
        return mMaxStacksize;
    }

    @Override
    public boolean getHasStack() {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int par1) {
        if (!mCanStackItem)
            return null;
        return super.decrStackSize(par1);
    }

    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
        return false;
    }

    /**
     * Whether this slot should be ignored in event processing,
     * for example highlight the slot on mouseOver
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    /**
     * Use this value to determine whether to ignore this slot in event processing
     */
    public boolean isEnabled() {
        return mEnabled;
    }

    /**
     * This function controls whether to highlight the slot on mouseOver.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean func_111238_b()
    {
        return isEnabled();
    }
}
