package gregtech.common.items.toolbox;

import java.util.List;
import java.util.Optional;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.NotImplementedException;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;

import gregtech.api.enums.ToolboxSlot;

/**
 * A delegate {@link IInventory} used to allow the soldering iron to consume soldering material from inside the toolbox.
 * Remember to save the passed handler to the toolbox stack's NBT data with
 * {@link ToolboxUtil#saveToolbox(ItemStack, ItemStackHandler)}
 * to persist any changes this delegate does.
 */
public class ToolboxDelegateInventory implements IInventory {

    private static final NotImplementedException NOT_IMPLEMENTED_EXCEPTION = new NotImplementedException(
        "ToolboxDelegateInventory is not designed for use in GUIs");

    private final ToolboxItemStackHandler handler;
    private final List<ToolboxSlot> slotWhitelist;

    public ToolboxDelegateInventory(final ItemStack toolbox) {
        this(new ToolboxItemStackHandler(toolbox), ToolboxSlot.GENERIC_SLOTS);
    }

    public ToolboxDelegateInventory(final ToolboxItemStackHandler handler, final List<ToolboxSlot> slotWhitelist) {
        this.handler = handler;
        this.slotWhitelist = slotWhitelist;
    }

    public ToolboxItemStackHandler getHandler() {
        return handler;
    }

    @Override
    public int getSizeInventory() {
        return slotWhitelist.size();
    }

    @Override
    public ItemStack getStackInSlot(final int slotIn) {
        return ifSlotValid(slotIn).map(slot -> handler.getStackInSlot(slot.getSlotID()))
            .orElse(null);
    }

    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        return ifSlotValid(index).map(slot -> {
            final ItemStack slotStack = handler.getStackInSlot(slot.getSlotID());
            if (slotStack != null) {
                final int remainder = Math.min(slotStack.stackSize, count);

                if (remainder > 0) {
                    final ItemStack newStack = slotStack.splitStack(remainder);
                    handler.setStackInSlot(slot.getSlotID(), slotStack);
                    return newStack;
                }
            }

            return null;
        })
            .orElse(null);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(final int index) {
        throw NOT_IMPLEMENTED_EXCEPTION;
    }

    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        ifSlotValid(index).ifPresent(slot -> handler.setStackInSlot(slot.getSlotID(), stack));
    }

    @Override
    public String getInventoryName() {
        throw NOT_IMPLEMENTED_EXCEPTION;
    }

    @Override
    public boolean hasCustomInventoryName() {
        throw NOT_IMPLEMENTED_EXCEPTION;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUseableByPlayer(final EntityPlayer player) {
        throw NOT_IMPLEMENTED_EXCEPTION;
    }

    @Override
    public void openInventory() {
        throw NOT_IMPLEMENTED_EXCEPTION;
    }

    @Override
    public void closeInventory() {
        throw NOT_IMPLEMENTED_EXCEPTION;
    }

    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return ifSlotValid(index).map(slot -> handler.isItemValid(slot.getSlotID(), stack))
            .orElse(false);
    }

    private Optional<ToolboxSlot> ifSlotValid(final int slotIndex) {
        if (slotIndex > -1 && slotIndex < slotWhitelist.size()) {
            return Optional.of(slotWhitelist.get(slotIndex));
        }

        return Optional.empty();
    }
}
