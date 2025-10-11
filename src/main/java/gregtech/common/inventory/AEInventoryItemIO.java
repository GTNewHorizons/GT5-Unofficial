package gregtech.common.inventory;

import java.util.OptionalInt;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.capability.item.AbstractInventorySourceIterator;
import com.gtnewhorizon.gtnhlib.capability.item.IItemIO;
import com.gtnewhorizon.gtnhlib.capability.item.InventorySourceIterator;

import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.storage.data.IAEItemStack;
import appeng.util.item.AEItemStack;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTUtility;

public class AEInventoryItemIO implements IItemIO {

    private final AEInventory inventory;
    private final BaseActionSource src;

    private int[] allowedSourceSlots, allowedSinkSlots;

    public AEInventoryItemIO(AEInventory inventory, BaseActionSource src) {
        this.inventory = inventory;
        this.src = src;
    }

    @Override
    public @NotNull InventorySourceIterator iterator() {
        int[] effectiveSlots = allowedSourceSlots != null
            ? GTDataUtils.intersect(allowedSourceSlots, inventory.allSlots)
            : inventory.allSlots;

        return new AbstractInventorySourceIterator(effectiveSlots) {

            @Override
            protected ItemStack getStackInSlot(int slot) {
                return inventory.getStackInSlot(slot);
            }

            @Override
            public ItemStack extract(int amount) {
                IAEItemStack inSlot = inventory.getAEStackInSlot(getCurrentSlot());

                if (inSlot == null) return null;

                long toExtract = Math.min(inSlot.getStackSize(), amount);

                IAEItemStack extracted = inSlot.copy()
                    .setStackSize(toExtract);
                inSlot.decStackSize(toExtract);

                inventory.setStackInSlot(getCurrentSlot(), inSlot.getStackSize() == 0 ? null : inSlot);

                return extracted.getItemStack();
            }

            @Override
            public void insert(ItemStack stack) {
                if (!GTUtility.isStackValid(stack) || stack.stackSize <= 0) return;

                IAEItemStack inSlot = inventory.getAEStackInSlot(getCurrentSlot());

                if (inSlot != null && !inSlot.isSameType(stack)) {
                    throw new IllegalArgumentException(
                        "Cannot insert stack that does not match the existing stack. Attempted to inject: " + stack
                            + ", already had: "
                            + inSlot);
                }

                IAEItemStack out;

                if (inSlot != null) {
                    out = inSlot.copy();
                    out.incStackSize(stack.stackSize);
                } else {
                    out = AEItemStack.create(stack);
                }

                inventory.setStackInSlot(getCurrentSlot(), out);
            }

            @Override
            protected void setInventorySlotContents(int slot, ItemStack stack) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public void setAllowedSourceSlots(int @Nullable [] slots) {
        allowedSourceSlots = slots;
    }

    @Override
    public ItemStack store(ItemStack itemStack) {
        IAEItemStack input = AEItemStack.create(itemStack);

        int[] slots = allowedSinkSlots != null ? GTDataUtils.intersect(allowedSinkSlots, inventory.allSlots)
            : inventory.allSlots;

        IAEItemStack rejected = inventory.injectItems(input, Actionable.MODULATE, src, slots);

        return rejected == null ? null : rejected.getItemStack();
    }

    @Override
    public void setAllowedSinkSlots(int @Nullable [] slots) {
        allowedSinkSlots = slots;
    }

    @Override
    public OptionalInt getStoredAmount(@Nullable ItemStack query) {
        long sum = 0;

        for (IAEItemStack stack : inventory.inventory) {
            if (stack != null && stack.isSameType(query)) {
                sum = GTUtility.addSafe(sum, stack.getStackSize());
            }
        }

        return OptionalInt.of(GTUtility.longToInt(sum));
    }
}
