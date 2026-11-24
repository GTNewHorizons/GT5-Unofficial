package gregtech.api.ec.component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.util.GTUtility;

public interface IItemInputLogicComponent extends IComponent {

    ComponentKey.ListKey<IItemInputLogicComponent> KEY = ComponentKey
        .list(Comparator.comparingInt(IItemInputLogicComponent::getItemInputPriority));

    enum ItemInputPriority {

        FLUID_HATCH_SLOT(1000),
        STANDARD_BUS(2000);

        public final int priority;

        ItemInputPriority(int priority) {
            this.priority = priority;
        }
    }

    int getItemInputPriority();

    long depleteItemRange(@NotNull ItemStack stack, long minAmount, long maxAmount, boolean simulate);

    default boolean depleteItemAtomic(@NotNull ItemStack stack, long amount, boolean simulate) {
        return depleteItemRange(stack, amount, amount, simulate) >= amount;
    }

    default long depleteItemPartial(@NotNull ItemStack stack, long maxAmount, boolean simulate) {
        return depleteItemRange(stack, 1, maxAmount, simulate);
    }

    @Nullable
    ItemStack depleteItemRange(@NotNull Predicate<ItemStack> filter, long minAmount, long maxAmount, boolean simulate);

    default @Nullable ItemStack depleteItemAtomic(Predicate<ItemStack> filter, long amount, boolean simulate) {
        return depleteItemRange(filter, amount, amount, simulate);
    }

    default @Nullable ItemStack depleteItemPartial(Predicate<ItemStack> filter, long maxAmount, boolean simulate) {
        return depleteItemRange(filter, 1, maxAmount, simulate);
    }

    default void appendStoredItems(@NotNull List<ItemStack> list) {
        appendStoredItemsForColor(list, null);
    }

    void appendStoredItemsForColor(@NotNull List<ItemStack> list, @Nullable Byte color);

    default List<ItemStack> getStoredItemsForColor(@Nullable Byte color) {
        List<ItemStack> list = new ArrayList<>();
        appendStoredItemsForColor(list, color);
        return list;
    }

    static long depleteFromItemList(@NotNull List<ItemStack> list, @NotNull ItemStack target, long minAmount,
        long maxAmount, boolean simulate) {
        if (minAmount <= 0 || maxAmount < minAmount) return 0;
        long available = 0;
        for (var item : list) {
            if (item != null && item.stackSize > 0 && GTUtility.areStacksEqual(item, target)) {
                available = GTUtility.addSafe(available, item.stackSize);
                if (available >= maxAmount) break;
            }
        }
        if (available < minAmount) return 0;
        long toDec = Math.min(available, maxAmount);
        if (simulate) {
            return toDec;
        }
        long remaining = toDec;
        for (var item : list) {
            if (item != null && item.stackSize > 0 && GTUtility.areStacksEqual(item, target)) {
                int decAmount = (int) Math.min(remaining, item.stackSize);
                item.stackSize -= decAmount;
                remaining -= decAmount;
                if (remaining <= 0) break;
            }
        }
        return toDec;
    }

    static @Nullable ItemStack depleteFromItemList(@NotNull List<ItemStack> list, @NotNull Predicate<ItemStack> filter,
        long minAmount, long maxAmount, boolean simulate) {
        if (minAmount <= 0 || maxAmount < minAmount) return null;
        Set<GTUtility.ItemId> failedItems = null;
        for (var item : list) {
            if (item != null && item.stackSize > 0) {
                GTUtility.ItemId id = null;
                if (failedItems != null) {
                    id = GTUtility.ItemId.createNoCopy(item);
                    if (failedItems.contains(id)) continue;
                }
                if (filter.test(item)) {
                    long amount = depleteFromItemList(list, item, minAmount, maxAmount, simulate);
                    if (amount >= minAmount) {
                        // todo support long
                        var result = item.copy();
                        result.stackSize = (int) Math.min(amount, Integer.MAX_VALUE);
                        return result;
                    }
                    if (failedItems == null) failedItems = new HashSet<>();
                    if (id == null) id = GTUtility.ItemId.createNoCopy(item);
                    failedItems.add(id);
                }
            }
        }
        return null;
    }
}
