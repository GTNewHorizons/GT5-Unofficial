package gregtech.crossmod.ae2;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.parts.IPart;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.util.item.AEItemStack;
import appeng.util.item.ItemList;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.util.GTUtility;

public class MEItemInventoryHandler<T extends IMetaTileEntity & IMEAwareItemInventory>
    implements IMEMonitor<IAEItemStack> {

    private final T machine;

    private final Map<IMEMonitorHandlerReceiver<IAEItemStack>, Object> listeners = new HashMap<>();

    public MEItemInventoryHandler(T machine) {
        this.machine = machine;
    }

    @Override
    public void addListener(IMEMonitorHandlerReceiver l, Object verificationToken) {
        listeners.put(l, verificationToken);
    }

    @Override
    public void removeListener(IMEMonitorHandlerReceiver l) {
        listeners.remove(l);
    }

    @Override
    public AccessRestriction getAccess() {
        return AccessRestriction.READ_WRITE;
    }

    @Override
    public boolean isPrioritized(IAEItemStack aeItemStack) {
        ItemStack s = machine.getItemStack();
        if (s == null || aeItemStack == null) return false;
        return aeItemStack.isSameType(s);
    }

    @Override
    public boolean canAccept(IAEItemStack aeItemStack) {
        ItemStack s = machine.getItemStack();
        if (s == null || aeItemStack == null) return true;
        return aeItemStack.isSameType(s);
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public boolean validForPass(int i) {
        return true;
    }

    @Override
    public IItemList<IAEItemStack> getAvailableItems(IItemList<IAEItemStack> out, int iteration) {
        return gatherItems(out);
    }

    @Override
    public IItemList<IAEItemStack> getStorageList() {
        return gatherItems(new ItemList());
    }

    private IItemList<IAEItemStack> gatherItems(IItemList<IAEItemStack> out) {
        ItemStack storedStack = machine.getItemStack();
        if (storedStack != null) {
            AEItemStack s = AEItemStack.create(storedStack);
            s.setStackSize(machine.getItemCount());
            out.add(s);
        }
        ItemStack extraStoredStack = machine.getExtraItemStack();
        if (extraStoredStack != null) {
            AEItemStack s = AEItemStack.create(extraStoredStack);
            out.add(s);
        }
        return out;
    }

    @Override
    public IAEItemStack injectItems(IAEItemStack input, Actionable mode, BaseActionSource src) {
        if (machine.getBaseMetaTileEntity() == null) return input;

        final ItemStack inputStack = input.getItemStack();
        final int maxCapacity = machine.getItemCapacity();
        final int itemCount = machine.getItemCount();
        final long toAdd = input.getStackSize();
        final ItemStack storedStack = machine.getItemStack();

        if (storedStack != null && !GTUtility.areStacksEqual(storedStack, inputStack)) {
            // Can't stack with existing item, just return the input.
            return input;
        }

        if (storedStack == null && !machine.isValidItem(inputStack)) {
            // Invalid item, return input.
            return input;
        }

        // Number of items not added because there's too much to add.
        final long notAdded = itemCount + toAdd - maxCapacity;

        if (mode == appeng.api.config.Actionable.MODULATE) {
            final int newCount = (int) Math.min(maxCapacity, itemCount + toAdd);

            if (storedStack == null) {
                machine.setItemStack(inputStack.copy());
            }
            machine.setItemCount(newCount);
            machine.getBaseMetaTileEntity()
                .markDirty();
        }

        if (notAdded <= 0) {
            return null;
        } else {
            return input.copy()
                .setStackSize(notAdded);
        }
    }

    @Override
    public IAEItemStack extractItems(IAEItemStack request, Actionable mode, BaseActionSource src) {
        if (machine.getBaseMetaTileEntity() == null) return null;

        if (request.isSameType(machine.getItemStack())) {
            // Internal inv content is queried
            if (mode != Actionable.SIMULATE) {
                machine.getBaseMetaTileEntity()
                    .markDirty();
            }
            final ItemStack storedItemStack = machine.getItemStack();
            final ItemStack storedExtraItemStack = machine.getExtraItemStack();
            final int storedItemCount = machine.getItemCount();
            final boolean isExtraItemSameType = GTUtility.areStacksEqual(storedItemStack, storedExtraItemStack);
            // Also account for extra inv if it's the same item
            final int extraItemCount = isExtraItemSameType ? storedExtraItemStack.stackSize : 0;
            final long totalItemCount = (long) storedItemCount + extraItemCount;
            if (request.getStackSize() >= totalItemCount) {
                // Cannot provide all the items required with internal inv and extra inv combined
                AEItemStack result = AEItemStack.create(machine.getItemStack());
                result.setStackSize(totalItemCount);
                if (mode != Actionable.SIMULATE) {
                    // Consume items
                    machine.setItemCount(0);
                    if (isExtraItemSameType) {
                        machine.setExtraItemStack(null);
                    }
                }
                return result;
            } else {
                // Requested items can be fully provided
                if (mode != Actionable.SIMULATE) {
                    // Consume items
                    if (request.getStackSize() > storedItemCount) {
                        // Internal inv cannot provide all, but with extra inv it can
                        machine.setItemCount(0);
                        // request.getStackSize() < storedItemCount + extraItemCount
                        // <=> request.getStackSize() - storedItemCount < extraItemCount
                        storedExtraItemStack.stackSize -= (int) (request.getStackSize() - storedItemCount);
                    } else {
                        // Internal inv can provide all
                        machine.setItemCount(storedItemCount - (int) request.getStackSize());
                    }
                }
                return request.copy();
            }
        } else if (request.isSameType(machine.getExtraItemStack())) {
            // Requested specifically for extra inv
            ItemStack extraItemStack = machine.getExtraItemStack();
            if (mode != Actionable.SIMULATE) {
                machine.getBaseMetaTileEntity()
                    .markDirty();
            }
            if (request.getStackSize() >= extraItemStack.stackSize) {
                // Cannot provide all the items required
                AEItemStack result = AEItemStack.create(extraItemStack);
                if (mode != Actionable.SIMULATE) {
                    // Consume items
                    machine.setExtraItemStack(null);
                }
                return result;
            } else {
                // Can provide all
                if (mode != Actionable.SIMULATE) {
                    // Consume items
                    extraItemStack.stackSize -= (int) request.getStackSize();
                }
                return request.copy();
            }
        }
        return null;
    }

    @Override
    public StorageChannel getChannel() {
        return StorageChannel.ITEMS;
    }

    public void notifyListeners(int count, ItemStack stack) {
        if (count == 0 || stack == null) return;
        ItemList change = new ItemList();
        AEItemStack s = AEItemStack.create(stack);
        s.setStackSize(count);
        change.add(s);
        listeners.forEach((l, o) -> {
            if (l.isValid(o)) l.postChange(this, change, null);
            else removeListener(l);
        });
    }

    public boolean hasActiveMEConnection() {
        if (listeners.isEmpty()) return false;
        for (Map.Entry<IMEMonitorHandlerReceiver<IAEItemStack>, Object> e : listeners.entrySet()) {
            if ((e.getKey() instanceof IPart)) {
                IGridNode n = ((IPart) e.getKey()).getGridNode();
                if (n != null && n.isActive()) return true;
            }
        }
        // if there are no active storage buses - clear the listeners
        listeners.clear();
        return false;
    }
}
