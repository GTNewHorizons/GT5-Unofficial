package gregtech.common.inventory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.utils.item.INBTSerializable;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.StorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.util.item.AEItemStack;
import appeng.util.item.ItemList;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.widget.IAEItemHandlerModifiable;

/**
 * An inventory that stores AE item stacks. This also sends monitor updates as needed.
 */
public abstract class AEInventory
    implements IAEInventory, IMEMonitor<IAEItemStack>, IAEItemHandlerModifiable, INBTSerializable<NBTTagCompound> {

    private final Map<IMEMonitorHandlerReceiver<IAEItemStack>, Object> listeners = new HashMap<>();

    public final int slotCount;
    public final IAEItemStack[] inventory;

    protected final int[] allSlots;

    public AEInventory(IAEItemStack[] inventory) {
        this.inventory = inventory;
        slotCount = inventory.length;
        allSlots = IntStream.range(0, slotCount)
            .toArray();
    }

    public AEInventory(int slotCount) {
        this.inventory = new IAEItemStack[slotCount];
        this.slotCount = slotCount;
        allSlots = IntStream.range(0, slotCount)
            .toArray();
    }

    @Override
    public IItemList<IAEItemStack> getStorageList() {
        ItemList list = new ItemList();

        for (IAEItemStack stack : inventory) {
            if (stack != null) list.add(stack.copy());
        }

        return list;
    }

    @Override
    public IAEItemStack getAvailableItem(@NotNull IAEItemStack request, int iteration) {
        IAEItemStack result = request.empty();

        for (IAEItemStack stack : inventory) {
            if (stack != null && stack.isSameType(request)) {
                result.incStackSize(stack.getStackSize());
            }
        }

        return result;
    }

    @Override
    public IItemList<IAEItemStack> getAvailableItems(IItemList<IAEItemStack> out, int iteration) {
        for (IAEItemStack stack : inventory) {
            if (stack != null) out.add(stack.copy());
        }

        return out;
    }

    @Override
    public void addListener(IMEMonitorHandlerReceiver l, Object verificationToken) {
        // noinspection unchecked
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
    public boolean isPrioritized(IAEItemStack input) {
        return false;
    }

    @Override
    public boolean canAccept(IAEItemStack input) {
        return true;
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
        return false;
    }

    protected int[] getValidInjectionSlots(IAEItemStack input) {
        return allSlots;
    }

    protected int[] getValidExtractionSlots(IAEItemStack request) {
        return allSlots;
    }

    protected boolean allowPullStack(int slotIndex) {
        return true;
    }

    protected boolean allowPutStack(int slotIndex, IAEItemStack toInsert) {
        return true;
    }

    @Override
    public IAEItemStack injectItems(IAEItemStack input, Actionable mode, BaseActionSource src) {
        return injectItems(input, mode, src, getValidInjectionSlots(input));
    }

    public @Nullable IAEItemStack injectItems(IAEItemStack input, Actionable mode, BaseActionSource src, int[] slots) {
        IAEItemStack inserted = null;

        input = input.copy();

        // First pass: try to find slots that have the item already
        for (int slotIndex : slots) {
            if (input.getStackSize() <= 0) break;

            if (!allowPutStack(slotIndex, input)) continue;

            IAEItemStack slot = inventory[slotIndex];

            if (slot == null) continue;
            if (!input.isSameType(slot)) continue;

            long maxStack = getAESlotLimit(slotIndex, slot);

            long toTransfer = Math.min(maxStack - slot.getStackSize(), input.getStackSize());

            if (toTransfer > 0) {
                input.decStackSize(toTransfer);
                if (mode == Actionable.MODULATE) slot.incStackSize(toTransfer);

                if (inserted == null) {
                    inserted = input.empty();
                }

                inserted.incStackSize(toTransfer);
            }
        }

        // Second pass: try to find any empty slot
        for (int slotIndex : slots) {
            if (input.getStackSize() <= 0) break;

            if (!allowPutStack(slotIndex, input)) continue;

            IAEItemStack slot = inventory[slotIndex];

            if (slot != null) continue;

            slot = input.empty();

            if (mode == Actionable.MODULATE) inventory[slotIndex] = slot;

            long maxStack = getAESlotLimit(slotIndex, slot);

            long toTransfer = Math.min(maxStack - slot.getStackSize(), input.getStackSize());

            if (toTransfer > 0) {
                input.decStackSize(toTransfer);
                if (mode == Actionable.MODULATE) slot.incStackSize(toTransfer);

                if (inserted == null) {
                    inserted = input.empty();
                }

                inserted.incStackSize(toTransfer);
            }
        }

        if (inserted != null && mode == Actionable.MODULATE) {
            postChange(src, inserted);
        }

        return input.getStackSize() == 0 ? null : input;
    }

    private void postChange(BaseActionSource src, IAEItemStack inserted) {
        Iterable<IAEItemStack> changes = GTDataUtils.singletonIterable(inserted);

        listeners.forEach((handler, o) -> {
            if (handler.isValid(o)) {
                handler.postChange(this, changes, src);
            }
        });
    }

    @Override
    public IAEItemStack extractItems(IAEItemStack request, Actionable mode, BaseActionSource src) {
        return extractItems(request, mode, src, getValidExtractionSlots(request));
    }

    private @Nullable IAEItemStack extractItems(IAEItemStack request, Actionable mode, BaseActionSource src,
        int[] slots) {
        IAEItemStack extracted = null;

        for (int slotIndex : slots) {
            if (!allowPullStack(slotIndex)) continue;

            IAEItemStack slot = inventory[slotIndex];

            if (slot == null || !request.isSameType(slot)) continue;

            long toConsume = Math.min(slot.getStackSize(), request.getStackSize());

            if (mode == Actionable.MODULATE) {
                slot.decStackSize(toConsume);

                if (slot.getStackSize() <= 0) {
                    inventory[slotIndex] = null;
                }
            }

            if (extracted == null) {
                extracted = slot.empty();
            }

            extracted.incStackSize(toConsume);
        }

        if (extracted != null && mode == Actionable.MODULATE) {
            postChange(
                src,
                extracted.empty()
                    .setStackSize(-extracted.getStackSize()));
        }

        return extracted;
    }

    @Override
    public StorageChannel getChannel() {
        return StorageChannel.ITEMS;
    }

    @Override
    public int getSlots() {
        return slotCount;
    }

    @Override
    public @Nullable IAEItemStack getAEStackInSlot(int slot) {
        return GTDataUtils.getIndexSafe(inventory, slot);
    }

    public int indexOf(ItemStack stack) {
        for (int i = 0; i < inventory.length; i++) {
            IAEItemStack slot = inventory[i];

            if (slot != null && slot.isSameType(stack)) return i;
        }

        return -1;
    }

    public int indexOf(Predicate<IAEItemStack> filter) {
        for (int i = 0; i < inventory.length; i++) {
            if (filter.test(inventory[i])) return i;
        }

        return -1;
    }

    @Override
    public @Nullable IAEItemStack insertAEItem(int slot, @NotNull IAEItemStack stack, boolean simulate,
        boolean forced) {
        if (slot < 0 || slot >= slotCount) return stack;
        if (!forced && !allowPutStack(slot, stack)) return stack;

        IAEItemStack existing = inventory[slot];

        if (existing == null) {
            existing = stack.empty();
        }

        if (simulate) {
            existing = existing.copy();
        }

        if (existing.isSameType(stack)) {
            long maxStorable = forced ? Long.MAX_VALUE : getAESlotLimit(slot, existing);

            long toTransfer = Math.min(maxStorable - existing.getStackSize(), stack.getStackSize());

            if (toTransfer > 0) {
                existing.incStackSize(toTransfer);
                stack.decStackSize(toTransfer);

                if (!simulate) {
                    inventory[slot] = existing;

                    postChange(
                        getActionSource(),
                        existing.empty()
                            .setStackSize(toTransfer));
                }
            }
        }

        return stack.getStackSize() > 0 ? stack : null;
    }

    @Override
    public @Nullable IAEItemStack extractAEItem(int slot, long amount, boolean simulate, boolean forced) {
        if (slot < 0 || slot >= slotCount) return null;
        if (!forced && !allowPullStack(slot)) return null;

        IAEItemStack existing = inventory[slot];

        if (existing == null) return null;

        long toExtract = Math.min(existing.getStackSize(), amount);

        IAEItemStack extracted = existing.empty()
            .setStackSize(toExtract);

        if (!simulate) {
            existing.decStackSize(toExtract);

            if (existing.getStackSize() <= 0) {
                inventory[slot] = null;
            }

            postChange(
                getActionSource(),
                existing.empty()
                    .setStackSize(-toExtract));
        }

        return extracted;
    }

    @Override
    public long getAESlotLimit(int slot, @Nullable IAEItemStack stack) {
        return Long.MAX_VALUE;
    }

    @Override
    public void setStackInSlot(int slot, @Nullable IAEItemStack stack) {
        if (slot < 0 || slot >= slotCount) return;

        IAEItemStack existing = inventory[slot];

        inventory[slot] = stack != null ? stack.copy() : null;

        if (existing != null && !existing.isSameType(stack)) {
            postChange(
                getActionSource(),
                existing.empty()
                    .setStackSize(-existing.getStackSize()));
            existing = null;
        }

        if (stack != null) {
            long previousAmount = existing == null ? 0 : existing.getStackSize();

            long delta = stack.getStackSize() - previousAmount;

            postChange(
                getActionSource(),
                stack.empty()
                    .setStackSize(delta));
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return writeToNBT(new NBTTagCompound());
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagList inv = new NBTTagList();
        tag.setTag("inv", inv);

        for (int i = 0; i < inventory.length; i++) {
            IAEItemStack stack = inventory[i];

            if (stack == null) continue;

            NBTTagCompound item = new NBTTagCompound();

            stack.writeToNBT(item);

            item.setInteger("index", i);

            inv.appendTag(item);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        readFromNBT(nbt);
    }

    public void readFromNBT(NBTTagCompound tag) {
        Arrays.fill(inventory, null);

        for (NBTTagCompound item : GTUtility.getCompoundTagList(tag, "inv")) {
            int index = item.getInteger("index");

            if (index < 0 || index >= slotCount) continue;

            inventory[index] = AEItemStack.loadItemStackFromNBT(item);
        }
    }

    protected abstract AEInventory copyImpl();

    public AEInventory copy() {
        AEInventory copy = copyImpl();

        for (int i = 0; i < inventory.length; i++) {
            IAEItemStack stack = inventory[i];

            copy.inventory[i] = stack == null ? null : stack.copy();
        }

        return copy;
    }

    public abstract BaseActionSource getActionSource();

    public AEInventoryItemIO getItemIO() {
        return new AEInventoryItemIO(this);
    }
}
