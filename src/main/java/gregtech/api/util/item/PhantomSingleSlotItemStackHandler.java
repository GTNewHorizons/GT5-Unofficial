package gregtech.api.util.item;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;

public class PhantomSingleSlotItemStackHandler implements IItemHandlerModifiable {

    private final Supplier<ItemStack> getter;
    private final Consumer<ItemStack> setter;

    public PhantomSingleSlotItemStackHandler(Supplier<ItemStack> getter, Consumer<ItemStack> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public void setStackInSlot(int slot, @Nullable ItemStack stack) {
        if (slot == 0) {
            setter.accept(stack);
        }
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public @Nullable ItemStack getStackInSlot(int slot) {
        return slot == 0 ? getter.get() : null;
    }

    @Override
    public @Nullable ItemStack insertItem(int slot, @Nullable ItemStack stack, boolean simulate) {
        return null;
    }

    @Override
    public @Nullable ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }
}
