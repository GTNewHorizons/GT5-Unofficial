package gregtech.api.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GT_Slot_Holo extends Slot {

    public final int mSlotIndex;
    public boolean mEnabled = true;
    public boolean mCanInsertItem, mCanStackItem;
    public int mMaxStacksize = 127;

    public GT_Slot_Holo(IInventory inventory, int slotIndex, int xPos, int yPos, boolean aCanInsertItem,
        boolean aCanStackItem, int aMaxStacksize) {
        super(inventory, slotIndex, xPos, yPos);
        mCanInsertItem = aCanInsertItem;
        mCanStackItem = aCanStackItem;
        mMaxStacksize = aMaxStacksize;
        mSlotIndex = slotIndex;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
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
    public ItemStack decrStackSize(int amount) {
        if (!mCanStackItem) return null;
        return super.decrStackSize(amount);
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return false;
    }

    /**
     * Sets if this slot should be ignored in event-processing. For example, highlight the slot on mouseOver.
     *
     * @param enabled if the slot should be enabled
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
    public boolean func_111238_b() {
        return isEnabled();
    }
}
