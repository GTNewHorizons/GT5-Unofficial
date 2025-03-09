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
    public void addListener(IMEMonitorHandlerReceiver<IAEItemStack> l, Object verificationToken) {
        listeners.put(l, verificationToken);
    }

    @Override
    public void removeListener(IMEMonitorHandlerReceiver<IAEItemStack> l) {
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
        ItemStack storedStack = machine.getItemStack();
        if (storedStack != null) {
            AEItemStack s = AEItemStack.create(storedStack);
            s.setStackSize(machine.getItemCount());
            out.add(s);
        }
        return out;
    }

    @Override
    public IItemList<IAEItemStack> getStorageList() {
        IItemList<IAEItemStack> res = new ItemList();
        ItemStack storedStack = machine.getItemStack();
        if (storedStack != null) {
            AEItemStack s = AEItemStack.create(storedStack);
            s.setStackSize(machine.getItemCount());
            res.add(s);
        }
        return res;
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
        if (request.isSameType(machine.getItemStack())) {
            if (machine.getBaseMetaTileEntity() == null) return null;
            if (mode != Actionable.SIMULATE) machine.getBaseMetaTileEntity()
                .markDirty();
            if (request.getStackSize() >= machine.getItemCount()) {
                AEItemStack result = AEItemStack.create(machine.getItemStack());
                result.setStackSize(machine.getItemCount());
                if (mode != Actionable.SIMULATE) machine.setItemCount(0);
                return result;
            } else {
                if (mode != Actionable.SIMULATE)
                    machine.setItemCount(machine.getItemCount() - (int) request.getStackSize());
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
