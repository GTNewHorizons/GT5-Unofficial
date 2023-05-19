package gregtech.common.gui.modularui.widget;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.GT_Mod;
import gregtech.api.interfaces.IFluidAccess;
import gregtech.api.interfaces.IHasFluidDisplayItem;
import gregtech.api.interfaces.metatileentity.IFluidLockable;
import gregtech.api.util.GT_Utility;

@Deprecated
public class FluidDisplaySlotWidget extends SlotWidget {

    private IHasFluidDisplayItem iHasFluidDisplay;
    private Supplier<IFluidAccess> fluidAccessConstructor;
    private Supplier<Boolean> canDrainGetter;
    private Supplier<Boolean> canFillGetter;
    private Predicate<Fluid> canFillFilter;
    private Action actionRealClick = Action.NONE;
    private Action actionDragAndDrop = Action.NONE;
    private BiFunction<ClickData, FluidDisplaySlotWidget, Boolean> beforeRealClick;
    private BiFunction<ClickData, FluidDisplaySlotWidget, Boolean> beforeDragAndDrop;
    private Runnable updateFluidDisplayItem = () -> {};

    public FluidDisplaySlotWidget(BaseSlot slot) {
        super(slot);
        setAccess(false, false);
        disableShiftInsert();
    }

    public FluidDisplaySlotWidget(IItemHandlerModifiable handler, int index) {
        this(new BaseSlot(handler, index, true));
    }

    // === client actions ===

    @Override
    public ClickResult onClick(int buttonId, boolean doubleClick) {
        if (actionRealClick == Action.NONE) return ClickResult.REJECT;
        if (interactionDisabled) return ClickResult.REJECT;

        /*
         * While a logical client don't really need to process fluid cells upon click (it could have just wait for
         * server side to send the result), doing so would result in every fluid interaction having a noticeable delay
         * between clicking and changes happening even on single player. I'd imagine this lag to become only more severe
         * when playing MP over ethernet, which would have much more latency than a memory connection
         */
        ClickData clickData = ClickData.create(buttonId, doubleClick);
        ItemStack verifyToken = executeRealClick(clickData);
        syncToServer(2, buffer -> {
            clickData.writeToPacket(buffer);
            try {
                buffer.writeItemStackToBuffer(verifyToken);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return ClickResult.ACCEPT;
    }

    @Override
    public boolean handleDragAndDrop(ItemStack draggedStack, int button) {
        if (actionDragAndDrop == Action.NONE || actionDragAndDrop == Action.TRANSFER) return false;
        if (interactionDisabled) return false;

        ClickData clickData = ClickData.create(button, false);
        executeDragAndDrop(clickData, draggedStack);
        syncToServer(5, buffer -> {
            try {
                clickData.writeToPacket(buffer);
                buffer.writeItemStackToBuffer(draggedStack);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        draggedStack.stackSize = 0;
        return true;
    }

    @Override
    public List<String> getExtraTooltip() {
        return Collections.emptyList();
    }

    // === server actions ===

    @Override
    public void readOnServer(int id, PacketBuffer buf) throws IOException {
        if (id == 1) {
            getMcSlot().xDisplayPosition = buf.readVarIntFromBuffer();
            getMcSlot().yDisplayPosition = buf.readVarIntFromBuffer();
        } else if (id == 2) {
            onClickServer(ClickData.readPacket(buf), buf.readItemStackFromBuffer());
        } else if (id == 3) {
            phantomScroll(buf.readVarIntFromBuffer());
        } else if (id == 4) {
            setEnabled(buf.readBoolean());
        } else if (id == 5) {
            executeDragAndDrop(ClickData.readPacket(buf), buf.readItemStackFromBuffer());
            if (onDragAndDropComplete != null) {
                onDragAndDropComplete.accept(this);
            }
        }
        markForUpdate();
    }

    private void onClickServer(ClickData clickData, ItemStack clientVerifyToken) {
        ItemStack serverVerifyToken = executeRealClick(clickData);
        // similar to what NetHandlerPlayServer#processClickWindow does
        if (!ItemStack.areItemStacksEqual(clientVerifyToken, serverVerifyToken)) {
            ((EntityPlayerMP) getContext().getPlayer()).sendContainerToPlayer(getContext().getContainer());
        }
    }

    // === client/server actions ===

    private ItemStack executeRealClick(ClickData clickData) {
        if (actionRealClick == Action.NONE) return null;
        if (beforeRealClick != null && !beforeRealClick.apply(clickData, this)) return null;

        ItemStack ret = null;
        if (actionRealClick == Action.TRANSFER) {
            if (fluidAccessConstructor == null) {
                GT_Mod.GT_FML_LOGGER
                    .warn("FluidDisplaySlotWidget is asked to transfer fluid, but fluidAccessConstructor is null!");
                return null;
            }
            ret = transferFluid(
                fluidAccessConstructor.get(),
                getContext().getPlayer(),
                clickData.mouseButton == 0,
                canDrainGetter != null ? canDrainGetter.get() : true,
                canFillGetter != null ? canFillGetter.get() : true);
        } else if (actionRealClick == Action.LOCK) {
            lockFluid(getContext().getPlayer().inventory.getItemStack());
        }

        updateFluidDisplayItem.run();
        return ret;
    }

    protected ItemStack transferFluid(IFluidAccess aFluidAccess, EntityPlayer aPlayer, boolean aProcessFullStack,
        boolean aCanDrain, boolean aCanFill) {
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
            // apply filter here
            if (canFillFilter != null && !canFillFilter.test(tFluidHeld.getFluid())) return null;
            return fillFluid(aFluidAccess, aPlayer, tFluidHeld, aProcessFullStack);
        }
        // tank not empty, both action possible
        if (tFluidHeld != null && tInputFluid.amount < aFluidAccess.getCapacity()) {
            // both nonnull and have space left for filling.
            if (aCanFill)
                // actually both pickup and fill is reasonable, but I'll go with fill here
                // there is already fluid in here. so we assume the slot will not accept this fluid anyway if it doesn't
                // pass the filter.
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
            .isFluidEqual(aFluidHeld)) return null;
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

    protected static void replaceCursorItemStack(EntityPlayer aPlayer, ItemStack tStackResult) {
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

    protected void executeDragAndDrop(ClickData clickData, ItemStack draggedStack) {
        if (actionDragAndDrop == Action.NONE || actionDragAndDrop == Action.TRANSFER) return;
        if (beforeDragAndDrop != null && !beforeDragAndDrop.apply(clickData, this)) return;

        if (actionDragAndDrop == Action.LOCK) {
            lockFluid(draggedStack);
        }
        updateFluidDisplayItem.run();
    }

    protected void lockFluid(ItemStack cursorStack) {}

    protected void updateFluidDisplayItem() {}

    // === setters ===

    public FluidDisplaySlotWidget setFluidAccessConstructor(Supplier<IFluidAccess> fluidAccessConstructor) {
        this.fluidAccessConstructor = fluidAccessConstructor;
        return this;
    }

    public FluidDisplaySlotWidget setIHasFluidDisplay(IHasFluidDisplayItem iHasFluidDisplay) {
        this.iHasFluidDisplay = iHasFluidDisplay;
        return this;
    }

    public FluidDisplaySlotWidget setCanDrainGetter(Supplier<Boolean> canDrainGetter) {
        this.canDrainGetter = canDrainGetter;
        return this;
    }

    public FluidDisplaySlotWidget setCanDrain(boolean canDrain) {
        return setCanDrainGetter(() -> canDrain);
    }

    public FluidDisplaySlotWidget setCanFillGetter(Supplier<Boolean> canFillGetter) {
        this.canFillGetter = canFillGetter;
        return this;
    }

    public FluidDisplaySlotWidget setCanFill(boolean canFill) {
        return setCanFillGetter(() -> canFill);
    }

    /**
     * Sets action called on click while holding the real item.
     */
    public FluidDisplaySlotWidget setActionRealClick(Action actionRealClick) {
        this.actionRealClick = actionRealClick;
        return this;
    }

    /**
     * Add a predicate on whether a client stack will be accepted. Note this will only be called when this slot is
     * already empty. It is assumed whatever is already in the slot will pass the filter.
     */
    public FluidDisplaySlotWidget setEmptyCanFillFilter(Predicate<Fluid> canFillFilter) {
        this.canFillFilter = canFillFilter;
        return this;
    }

    /**
     * Sets action called on drag-and-drop from NEI. You can't use {@link Action#TRANSFER} here.
     */
    public FluidDisplaySlotWidget setActionDragAndDrop(Action actionDragAndDrop) {
        this.actionDragAndDrop = actionDragAndDrop;
        return this;
    }

    /**
     * Sets function called before {@link #executeRealClick}.
     *
     * @param beforeRealClick (click data, this widget) -> if allow click
     */
    public FluidDisplaySlotWidget setBeforeRealClick(
        BiFunction<ClickData, FluidDisplaySlotWidget, Boolean> beforeRealClick) {
        this.beforeRealClick = beforeRealClick;
        return this;
    }

    /**
     * Sets function called before {@link #executeDragAndDrop}.
     *
     * @param beforeDragAndDrop (click data, this widget) -> if allow click
     */
    public FluidDisplaySlotWidget setBeforeDragAndDrop(
        BiFunction<ClickData, FluidDisplaySlotWidget, Boolean> beforeDragAndDrop) {
        this.beforeDragAndDrop = beforeDragAndDrop;
        return this;
    }

    /**
     * Sets function called before both of {@link #executeRealClick} and {@link #executeDragAndDrop}.
     *
     * @param beforeClick (click data, this widget) -> if allow click
     */
    public FluidDisplaySlotWidget setBeforeClick(BiFunction<ClickData, FluidDisplaySlotWidget, Boolean> beforeClick) {
        setBeforeRealClick(beforeClick);
        setBeforeDragAndDrop(beforeClick);
        return this;
    }

    public FluidDisplaySlotWidget setUpdateFluidDisplayItem(Runnable updateFluidDisplayItem) {
        this.updateFluidDisplayItem = updateFluidDisplayItem;
        return this;
    }

    /**
     * Action triggered on mouse click or NEI drag-and-drop.
     */
    public enum Action {

        /**
         * Fill/drain fluid into/from the tank. Uses fluid amount, so drag-and-drop cannot use this mode.
         */
        TRANSFER,

        /**
         * Lock fluid for {@link IFluidLockable}. Does not use fluid amount.
         */
        LOCK,

        /**
         * Set filter for the tank. (not implemented yet) Does not use fluid amount.
         */
        FILTER,

        /**
         * Does nothing.
         */
        NONE
    }
}
