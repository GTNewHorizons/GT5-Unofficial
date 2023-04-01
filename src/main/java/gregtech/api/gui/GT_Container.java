package gregtech.api.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import gregtech.api.interfaces.IFluidAccess;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * Main Container-Class, used for all my GUIs
 */
public class GT_Container extends Container {

    public IGregTechTileEntity mTileEntity;
    public InventoryPlayer mPlayerInventory;

    public GT_Container(InventoryPlayer aPlayerInventory, IGregTechTileEntity aTileEntityInventory) {

        mTileEntity = aTileEntityInventory;
        mPlayerInventory = aPlayerInventory;
        mTileEntity.openInventory();
    }

    /**
     * To add the Slots to your GUI
     */
    public void addSlots(InventoryPlayer aPlayerInventory) {
        //
    }

    /**
     * Amount of regular Slots in the GUI (so, non-HoloSlots)
     */
    public int getSlotCount() {
        return 0;
    }

    /**
     * Amount of ALL Slots in the GUI including HoloSlots and ArmorSlots, but excluding regular Player Slots
     */
    protected final int getAllSlotCount() {
        if (inventorySlots != null) {
            if (doesBindPlayerInventory()) return inventorySlots.size() - 36;
            return inventorySlots.size();
        }
        return getSlotCount();
    }

    /**
     * Start-Index of the usable Slots (the first non-HoloSlot)
     */
    public int getSlotStartIndex() {
        return 0;
    }

    public int getShiftClickStartIndex() {
        return getSlotStartIndex();
    }

    /**
     * Amount of Slots in the GUI the player can Shift-Click into. Uses also getSlotStartIndex
     */
    public int getShiftClickSlotCount() {
        return 0;
    }

    /**
     * Is Player-Inventory visible?
     */
    public boolean doesBindPlayerInventory() {
        return true;
    }

    /**
     * Override this Function with something like "return mTileEntity.isUseableByPlayer(aPlayer);"
     */
    @Override
    public boolean canInteractWith(EntityPlayer aPlayer) {
        return false;
    }

    protected void bindPlayerInventory(InventoryPlayer aInventoryPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(aInventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(aInventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        mTileEntity.markDirty();

        if (aSlotIndex >= 0) {
            if (inventorySlots.get(aSlotIndex) == null || inventorySlots.get(aSlotIndex) instanceof GT_Slot_Holo)
                return null;
            if (!(inventorySlots.get(aSlotIndex) instanceof GT_Slot_Armor)) if (aSlotIndex < getAllSlotCount())
                if (aSlotIndex < getSlotStartIndex() || aSlotIndex >= getSlotStartIndex() + getSlotCount()) return null;
        }

        try {
            return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }

        // It looks like the rest of this code should ideally never be
        // called, and might in fact never be called.

        ItemStack rStack = null;
        InventoryPlayer aPlayerInventory = aPlayer.inventory;
        Slot aSlot;
        ItemStack tTempStack;
        int tTempStackSize;
        ItemStack aHoldStack;

        if ((aShifthold == 0 || aShifthold == 1) && (aMouseclick == 0 || aMouseclick == 1)) {
            if (aSlotIndex == -999) {
                if (aPlayerInventory.getItemStack() != null) {
                    if (aMouseclick == 0) {
                        aPlayer.dropPlayerItemWithRandomChoice(aPlayerInventory.getItemStack(), true);
                        aPlayerInventory.setItemStack(null);
                    }
                    if (aMouseclick == 1) {
                        aPlayer.dropPlayerItemWithRandomChoice(
                                aPlayerInventory.getItemStack()
                                                .splitStack(1),
                                true);
                        if (aPlayerInventory.getItemStack().stackSize == 0) {
                            aPlayerInventory.setItemStack(null);
                        }
                    }
                }
            } else if (aShifthold == 1) {
                aSlot = (Slot) this.inventorySlots.get(aSlotIndex);
                if (aSlot != null && aSlot.canTakeStack(aPlayer)) {
                    tTempStack = this.transferStackInSlot(aPlayer, aSlotIndex);
                    if (tTempStack != null) {
                        rStack = GT_Utility.copyOrNull(tTempStack);
                        if (aSlot.getStack() != null && aSlot.getStack()
                                                             .getItem()
                                == tTempStack.getItem()) {
                            slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
                        }
                    }
                }
            } else {
                if (aSlotIndex < 0) {
                    return null;
                }
                aSlot = (Slot) this.inventorySlots.get(aSlotIndex);
                if (aSlot != null) {
                    tTempStack = aSlot.getStack();
                    ItemStack mouseStack = aPlayerInventory.getItemStack();
                    if (tTempStack != null) {
                        rStack = GT_Utility.copyOrNull(tTempStack);
                    }
                    if (tTempStack == null) {
                        if (mouseStack != null && aSlot.isItemValid(mouseStack)) {
                            tTempStackSize = aMouseclick == 0 ? mouseStack.stackSize : 1;
                            if (tTempStackSize > aSlot.getSlotStackLimit()) {
                                tTempStackSize = aSlot.getSlotStackLimit();
                            }
                            aSlot.putStack(mouseStack.splitStack(tTempStackSize));

                            if (mouseStack.stackSize == 0) {
                                aPlayerInventory.setItemStack(null);
                            }
                        }
                    } else if (aSlot.canTakeStack(aPlayer)) {
                        if (mouseStack == null) {
                            tTempStackSize = aMouseclick == 0 ? tTempStack.stackSize : (tTempStack.stackSize + 1) / 2;
                            aHoldStack = aSlot.decrStackSize(tTempStackSize);
                            aPlayerInventory.setItemStack(aHoldStack);
                            if (tTempStack.stackSize == 0) {
                                aSlot.putStack(null);
                            }
                            aSlot.onPickupFromSlot(aPlayer, aPlayerInventory.getItemStack());
                        } else if (aSlot.isItemValid(mouseStack)) {
                            if (tTempStack.getItem() == mouseStack.getItem()
                                    && tTempStack.getItemDamage() == mouseStack.getItemDamage()
                                    && ItemStack.areItemStackTagsEqual(tTempStack, mouseStack)) {
                                tTempStackSize = aMouseclick == 0 ? mouseStack.stackSize : 1;
                                if (tTempStackSize > aSlot.getSlotStackLimit() - tTempStack.stackSize) {
                                    tTempStackSize = aSlot.getSlotStackLimit() - tTempStack.stackSize;
                                }
                                if (tTempStackSize > mouseStack.getMaxStackSize() - tTempStack.stackSize) {
                                    tTempStackSize = mouseStack.getMaxStackSize() - tTempStack.stackSize;
                                }
                                mouseStack.splitStack(tTempStackSize);
                                if (mouseStack.stackSize == 0) {
                                    aPlayerInventory.setItemStack(null);
                                }
                                tTempStack.stackSize += tTempStackSize;
                            } else if (mouseStack.stackSize <= aSlot.getSlotStackLimit()) {
                                aSlot.putStack(mouseStack);
                                aPlayerInventory.setItemStack(tTempStack);
                            }
                        } else if (tTempStack.getItem() == mouseStack.getItem() && mouseStack.getMaxStackSize() > 1
                                && (!tTempStack.getHasSubtypes()
                                        || tTempStack.getItemDamage() == mouseStack.getItemDamage())
                                && ItemStack.areItemStackTagsEqual(tTempStack, mouseStack)) {
                                    tTempStackSize = tTempStack.stackSize;

                                    if (tTempStackSize > 0
                                            && tTempStackSize + mouseStack.stackSize <= mouseStack.getMaxStackSize()) {
                                        mouseStack.stackSize += tTempStackSize;
                                        tTempStack = aSlot.decrStackSize(tTempStackSize);

                                        if (tTempStack.stackSize == 0) {
                                            aSlot.putStack(null);
                                        }

                                        aSlot.onPickupFromSlot(aPlayer, aPlayerInventory.getItemStack());
                                    }
                                }
                    }
                    aSlot.onSlotChanged();
                }
            }
            // Did the player try to swap a slot with his hotbar using a
            // number key from 1 to 9
            // aMouseclick == 0 means number 1, aMouseclick == 8 means number 9
        } else if (aShifthold == 2 && aMouseclick >= 0 && aMouseclick < 9) {
            aSlot = (Slot) this.inventorySlots.get(aSlotIndex);

            if (aSlot.canTakeStack(aPlayer)) {
                // get the stack at the specified hotbar slot.
                tTempStack = aPlayerInventory.getStackInSlot(aMouseclick);
                boolean canSwap = tTempStack == null
                        || aSlot.inventory == aPlayerInventory && aSlot.isItemValid(tTempStack);
                tTempStackSize = -1;

                if (!canSwap) {
                    tTempStackSize = aPlayerInventory.getFirstEmptyStack();
                    canSwap = tTempStackSize > -1;
                }

                if (canSwap && aSlot.getHasStack()) {
                    aHoldStack = aSlot.getStack();
                    aPlayerInventory.setInventorySlotContents(aMouseclick, aHoldStack);

                    if (tTempStack != null && (aSlot.inventory != aPlayerInventory || !aSlot.isItemValid(tTempStack))) {
                        if (tTempStackSize > -1) {
                            aPlayerInventory.addItemStackToInventory(tTempStack);
                            aSlot.decrStackSize(aHoldStack.stackSize);
                            aSlot.putStack(null);
                            aSlot.onPickupFromSlot(aPlayer, aHoldStack);
                        }
                    } else {
                        aSlot.decrStackSize(aHoldStack.stackSize);
                        aSlot.putStack(tTempStack);
                        aSlot.onPickupFromSlot(aPlayer, aHoldStack);
                    }
                } else if (tTempStack != null && !aSlot.getHasStack() && aSlot.isItemValid(tTempStack)) {
                    aPlayerInventory.setInventorySlotContents(aMouseclick, null);
                    aSlot.putStack(tTempStack);
                }
            }
        } else if (aShifthold == 3 && aPlayer.capabilities.isCreativeMode
                && aPlayerInventory.getItemStack() == null
                && aSlotIndex >= 0) {
                    aSlot = (Slot) this.inventorySlots.get(aSlotIndex);
                    if (aSlot != null && aSlot.getHasStack()) {
                        tTempStack = GT_Utility.copyOrNull(aSlot.getStack());
                        tTempStack.stackSize = tTempStack.getMaxStackSize();
                        aPlayerInventory.setItemStack(tTempStack);
                    }
                }
        return rStack;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer aPlayer, int aSlotIndex) {
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(aSlotIndex);

        mTileEntity.markDirty();

        // null checks and checks if the item can be stacked (maxStackSize > 1)
        if (getSlotCount() > 0 && slotObject != null
                && slotObject.getHasStack()
                && !(slotObject instanceof GT_Slot_Holo)) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = GT_Utility.copyOrNull(stackInSlot);

            // TileEntity -> Player
            if (aSlotIndex < getAllSlotCount()) {
                if (doesBindPlayerInventory())
                    if (!mergeItemStack(stackInSlot, getAllSlotCount(), getAllSlotCount() + 36, true)) {
                        return null;
                    }
                // Player -> TileEntity
            } else if (!mergeItemStack(
                    stackInSlot,
                    getShiftClickStartIndex(),
                    getShiftClickStartIndex() + getShiftClickSlotCount(),
                    false)) {
                        return null;
                    }

            if (stackInSlot.stackSize == 0) {
                slotObject.putStack(null);
            } else {
                slotObject.onSlotChanged();
            }
        }
        return stack;
    }

    /**
     * merges provided ItemStack with the first avaliable one in the container/player inventory
     */
    @Override
    protected boolean mergeItemStack(ItemStack aStack, int aStartIndex, int aSlotCount, boolean reverseOrder) {
        boolean transferredStack = false;
        int slotIndex = aStartIndex;

        mTileEntity.markDirty();

        if (reverseOrder) {
            slotIndex = aSlotCount - 1;
        }

        Slot slot;
        ItemStack itemStack;

        if (aStack.isStackable()) {
            while (aStack.stackSize > 0
                    && (!reverseOrder && slotIndex < aSlotCount || reverseOrder && slotIndex >= aStartIndex)) {
                slot = (Slot) this.inventorySlots.get(slotIndex);
                itemStack = slot.getStack();
                if (!(slot instanceof GT_Slot_Holo) && !(slot instanceof GT_Slot_Output)
                        && slot.isItemValid(aStack)
                        && itemStack != null
                        && itemStack.getItem() == aStack.getItem()
                        && (!aStack.getHasSubtypes() || aStack.getItemDamage() == itemStack.getItemDamage())
                        && ItemStack.areItemStackTagsEqual(aStack, itemStack)) {
                    int combinedStackSize = itemStack.stackSize + aStack.stackSize;
                    if (itemStack.stackSize < mTileEntity.getInventoryStackLimit()) {
                        if (combinedStackSize <= aStack.getMaxStackSize()) {
                            aStack.stackSize = 0;
                            itemStack.stackSize = combinedStackSize;
                            slot.onSlotChanged();
                            transferredStack = true;
                        } else if (itemStack.stackSize < aStack.getMaxStackSize()) {
                            aStack.stackSize -= aStack.getMaxStackSize() - itemStack.stackSize;
                            itemStack.stackSize = aStack.getMaxStackSize();
                            slot.onSlotChanged();
                            transferredStack = true;
                        }
                    }
                }

                if (reverseOrder) {
                    --slotIndex;
                } else {
                    ++slotIndex;
                }
            }
        }
        if (aStack.stackSize > 0) {
            if (reverseOrder) {
                slotIndex = aSlotCount - 1;
            } else {
                slotIndex = aStartIndex;
            }

            while (!reverseOrder && slotIndex < aSlotCount || reverseOrder && slotIndex >= aStartIndex) {
                slot = (Slot) this.inventorySlots.get(slotIndex);
                itemStack = slot.getStack();

                if (slot.isItemValid(aStack) && itemStack == null) {
                    int quantityToTransfer = Math.min(aStack.stackSize, mTileEntity.getInventoryStackLimit());
                    slot.putStack(GT_Utility.copyAmount(quantityToTransfer, aStack));
                    slot.onSlotChanged();
                    aStack.stackSize -= quantityToTransfer;
                    transferredStack = true;
                    break;
                }

                if (reverseOrder) {
                    --slotIndex;
                } else {
                    ++slotIndex;
                }
            }
        }

        return transferredStack;
    }

    @Override
    protected Slot addSlotToContainer(Slot slot) {
        try {
            return super.addSlotToContainer(slot);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return slot;
    }

    @Override
    public void addCraftingToCrafters(ICrafting player) {
        try {
            super.addCraftingToCrafters(player);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public List getInventory() {
        try {
            return super.getInventory();
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return null;
    }

    @Override
    public void removeCraftingFromCrafters(ICrafting player) {
        try {
            super.removeCraftingFromCrafters(player);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public void detectAndSendChanges() {
        try {
            super.detectAndSendChanges();
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public boolean enchantItem(EntityPlayer player, int slotIndex) {
        try {
            return super.enchantItem(player, slotIndex);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return false;
    }

    @Override
    public Slot getSlotFromInventory(IInventory inventory, int slotIndex) {
        try {
            return super.getSlotFromInventory(inventory, slotIndex);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return null;
    }

    @Override
    public Slot getSlot(int slotIndex) {
        try {
            if (this.inventorySlots.size() > slotIndex) return super.getSlot(slotIndex);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return null;
    }

    @Override
    public boolean func_94530_a(ItemStack itemStack, Slot slot) {
        try {
            return super.func_94530_a(itemStack, slot);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return true;
    }

    @Override
    protected void retrySlotClick(int slotIndex, int mouseButton, boolean aShifthold, EntityPlayer player) {
        try {
            super.retrySlotClick(slotIndex, mouseButton, aShifthold, player);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        try {
            super.onContainerClosed(player);
            mTileEntity.closeInventory();
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        try {
            super.onCraftMatrixChanged(inventory);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public void putStackInSlot(int slotIndex, ItemStack itemStack) {
        try {
            super.putStackInSlot(slotIndex, itemStack);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public void putStacksInSlots(ItemStack[] itemStacks) {
        try {
            super.putStacksInSlots(itemStacks);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public void updateProgressBar(int id, int value) {
        try {
            super.updateProgressBar(id, value);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public short getNextTransactionID(InventoryPlayer inventoryPlayer) {
        try {
            return super.getNextTransactionID(inventoryPlayer);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return 0;
    }

    @Override
    public boolean isPlayerNotUsingContainer(EntityPlayer player) {
        try {
            return super.isPlayerNotUsingContainer(player);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return true;
    }

    @Override
    public void setPlayerIsPresent(EntityPlayer player, boolean value) {
        try {
            super.setPlayerIsPresent(player, value);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    protected void func_94533_d() {
        try {
            super.func_94533_d();
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
    }

    @Override
    public boolean canDragIntoSlot(Slot slot) {
        try {
            return super.canDragIntoSlot(slot);
        } catch (Throwable e) {
            e.printStackTrace(GT_Log.err);
        }
        return true;
    }

    protected static ItemStack handleFluidSlotClick(IFluidAccess aFluidAccess, EntityPlayer aPlayer,
            boolean aProcessFullStack, boolean aCanDrain, boolean aCanFill) {
        ItemStack tStackHeld = aPlayer.inventory.getItemStack();
        ItemStack tStackSizedOne = GT_Utility.copyAmount(1, tStackHeld);
        if (tStackSizedOne == null || tStackHeld.stackSize == 0) return null;
        FluidStack tInputFluid = aFluidAccess.get();
        FluidStack tFluidHeld = GT_Utility.getFluidForFilledItem(tStackSizedOne, true);
        if (tFluidHeld != null && tFluidHeld.amount <= 0) tFluidHeld = null;
        if (tInputFluid == null) {
            // tank empty, consider fill only from now on
            if (!aCanFill)
                // cannot fill and nothing to take, bail out
                return null;
            if (tFluidHeld == null)
                // no fluid to fill
                return null;
            return fillFluid(aFluidAccess, aPlayer, tFluidHeld, aProcessFullStack);
        }
        // tank not empty, both action possible
        if (tFluidHeld != null && tInputFluid.amount < aFluidAccess.getCapacity()) {
            // both nonnull and have space left for filling.
            if (aCanFill)
                // actually both pickup and fill is reasonable, but I'll go with fill here
                return fillFluid(aFluidAccess, aPlayer, tFluidHeld, aProcessFullStack);
            if (!aCanDrain)
                // cannot take AND cannot fill, why make this call then?
                return null;
            // the slot does not allow filling, so try take some
            return drainFluid(aFluidAccess, aPlayer, aProcessFullStack);
        } else {
            // cannot fill and there is something to take
            if (!aCanDrain)
                // but the slot does not allow taking, so bail out
                return null;
            return drainFluid(aFluidAccess, aPlayer, aProcessFullStack);
        }
    }

    protected static ItemStack drainFluid(IFluidAccess aFluidAccess, EntityPlayer aPlayer, boolean aProcessFullStack) {
        FluidStack tTankStack = aFluidAccess.get();
        if (tTankStack == null) return null;
        ItemStack tStackHeld = aPlayer.inventory.getItemStack();
        ItemStack tStackSizedOne = GT_Utility.copyAmount(1, tStackHeld);
        if (tStackSizedOne == null || tStackHeld.stackSize == 0) return null;
        int tOriginalFluidAmount = tTankStack.amount;
        ItemStack tFilledContainer = GT_Utility.fillFluidContainer(tTankStack, tStackSizedOne, true, false);
        if (tFilledContainer == null && tStackSizedOne.getItem() instanceof IFluidContainerItem tContainerItem) {
            int tFilledAmount = tContainerItem.fill(tStackSizedOne, tTankStack, true);
            if (tFilledAmount > 0) {
                tFilledContainer = tStackSizedOne;
                tTankStack.amount -= tFilledAmount;
            }
        }
        if (tFilledContainer != null) {
            if (aProcessFullStack) {
                int tFilledAmount = tOriginalFluidAmount - tTankStack.amount;
                /*
                 * work out how many more items we can fill one cell is already used, so account for that the round down
                 * behavior will left over a fraction of a cell worth of fluid the user then get to decide what to do
                 * with it it will not be too fancy if it spills out partially filled cells
                 */
                int tAdditionalParallel = Math.min(tStackHeld.stackSize - 1, tTankStack.amount / tFilledAmount);
                tTankStack.amount -= tFilledAmount * tAdditionalParallel;
                tFilledContainer.stackSize += tAdditionalParallel;
            }
            replaceCursorItemStack(aPlayer, tFilledContainer);
        }
        aFluidAccess.verifyFluidStack();
        return tFilledContainer;
    }

    protected static ItemStack fillFluid(IFluidAccess aFluidAccess, EntityPlayer aPlayer, FluidStack aFluidHeld,
            boolean aProcessFullStack) {
        // we are not using aMachine.fill() here any more, so we need to check for fluid type here ourselves
        if (aFluidAccess.get() != null && !aFluidAccess.get()
                                                       .isFluidEqual(aFluidHeld))
            return null;
        ItemStack tStackHeld = aPlayer.inventory.getItemStack();
        ItemStack tStackSizedOne = GT_Utility.copyAmount(1, tStackHeld);
        if (tStackSizedOne == null) return null;

        int tFreeSpace = aFluidAccess.getCapacity() - (aFluidAccess.get() != null ? aFluidAccess.get().amount : 0);
        if (tFreeSpace <= 0)
            // no space left
            return null;

        // find out how much fluid can be taken
        // some cells cannot be partially filled
        ItemStack tStackEmptied = null;
        int tAmountTaken = 0;
        if (tFreeSpace >= aFluidHeld.amount) {
            // fully accepted - try take it from item now
            // IFluidContainerItem is intentionally not checked here. it will be checked later
            tStackEmptied = GT_Utility.getContainerForFilledItem(tStackSizedOne, false);
            tAmountTaken = aFluidHeld.amount;
        }
        if (tStackEmptied == null && tStackSizedOne.getItem() instanceof IFluidContainerItem container) {
            // either partially accepted, or is IFluidContainerItem
            FluidStack tDrained = container.drain(tStackSizedOne, tFreeSpace, true);
            if (tDrained != null && tDrained.amount > 0) {
                // something is actually drained - change the cell and drop it to player
                tStackEmptied = tStackSizedOne;
                tAmountTaken = tDrained.amount;
            }
        }
        if (tStackEmptied == null)
            // somehow the cell refuse to give out that amount of fluid, no op then
            return null;

        // find out how many fill can we do
        // same round down behavior as above
        // however here the fluid stack is not changed at all, so the exact code will slightly differ
        int tParallel = aProcessFullStack ? Math.min(tFreeSpace / tAmountTaken, tStackHeld.stackSize) : 1;
        if (aFluidAccess.get() == null) {
            FluidStack tNewFillableStack = aFluidHeld.copy();
            tNewFillableStack.amount = tAmountTaken * tParallel;
            aFluidAccess.set(tNewFillableStack);
        } else {
            aFluidAccess.addAmount(tAmountTaken * tParallel);
        }
        tStackEmptied.stackSize = tParallel;
        replaceCursorItemStack(aPlayer, tStackEmptied);
        return tStackEmptied;
    }

    private static void replaceCursorItemStack(EntityPlayer aPlayer, ItemStack tStackResult) {
        int tStackResultMaxStackSize = tStackResult.getMaxStackSize();
        while (tStackResult.stackSize > tStackResultMaxStackSize) {
            aPlayer.inventory.getItemStack().stackSize -= tStackResultMaxStackSize;
            GT_Utility.addItemToPlayerInventory(aPlayer, tStackResult.splitStack(tStackResultMaxStackSize));
        }
        if (aPlayer.inventory.getItemStack().stackSize == tStackResult.stackSize) {
            // every cell is mutated. it could just stay on the cursor.
            aPlayer.inventory.setItemStack(tStackResult);
        } else {
            // some cells not mutated. The mutated cells must go into the inventory
            // or drop into the world if there isn't enough space.
            ItemStack tStackHeld = aPlayer.inventory.getItemStack();
            tStackHeld.stackSize -= tStackResult.stackSize;
            GT_Utility.addItemToPlayerInventory(aPlayer, tStackResult);
        }
    }
}
