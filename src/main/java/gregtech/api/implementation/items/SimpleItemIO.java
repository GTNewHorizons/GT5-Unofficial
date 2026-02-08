package gregtech.api.implementation.items;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.capability.item.ItemIO;
import com.gtnewhorizon.gtnhlib.item.ImmutableItemStack;
import com.gtnewhorizon.gtnhlib.item.InsertionItemStack;
import com.gtnewhorizon.gtnhlib.item.InventoryIterator;
import com.gtnewhorizon.gtnhlib.item.ItemStack2IntFunction;
import com.gtnewhorizon.gtnhlib.item.ItemStackPredicate;

public abstract class SimpleItemIO implements ItemIO {

    private int[] allowedSourceSlots;
    private int[] allowedSinkSlots;

    @Override
    public void setAllowedSinkSlots(int @Nullable [] slots) {
        allowedSinkSlots = slots;
    }

    @Override
    public void setAllowedSourceSlots(int @Nullable [] slots) {
        allowedSourceSlots = slots;
    }

    @Override
    public @Nullable ItemStack pull(@Nullable ItemStackPredicate filter, @Nullable ItemStack2IntFunction amount) {
        InventoryIterator iter = sourceIterator();

        while (iter.hasNext()) {
            ImmutableItemStack stack = iter.next();

            if (stack == null || stack.isEmpty()) continue;

            if (filter == null || filter.test(stack)) {
                int toExtract = amount == null ? stack.getStackSize() : amount.apply(stack);

                return iter.extract(toExtract, false);
            }
        }

        return null;
    }

    @Override
    public int store(ImmutableItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;

        InventoryIterator iter = sinkIterator();

        InsertionItemStack insertion = new InsertionItemStack(stack);

        while (iter.hasNext()) {
            iter.next();

            insertion.set(iter.insert(insertion, false));

            if (insertion.isEmpty()) return 0;
        }

        return insertion.getStackSize();
    }

    @Override
    public @NotNull InventoryIterator sourceIterator() {
        return iterator(allowedSourceSlots);
    }

    @Override
    public @NotNull InventoryIterator sinkIterator() {
        return iterator(allowedSinkSlots);
    }

    @NotNull
    protected abstract InventoryIterator iterator(int[] allowedSlots);
}
