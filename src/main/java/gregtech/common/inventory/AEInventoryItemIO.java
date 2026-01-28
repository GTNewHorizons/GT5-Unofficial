package gregtech.common.inventory;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.item.AbstractInventoryIterator;
import com.gtnewhorizon.gtnhlib.item.ImmutableItemStack;
import com.gtnewhorizon.gtnhlib.item.InventoryIterator;

import gregtech.api.implementation.items.SimpleItemIO;

public class AEInventoryItemIO extends SimpleItemIO {

    private final AEInventory inventory;

    public AEInventoryItemIO(AEInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    protected @NotNull InventoryIterator iterator(int[] allowedSlots) {
        return new AbstractInventoryIterator(inventory.allSlots, allowedSlots) {

            @Override
            protected ItemStack getStackInSlot(int slot) {
                return inventory.getStackInSlot(slot);
            }

            @Override
            public ItemStack extract(int amount, boolean forced) {
                return inventory.extractItem(getCurrentSlot(), amount, false);
            }

            @Override
            public int insert(ImmutableItemStack stack, boolean forced) {
                if (stack.isEmpty()) return 0;

                return inventory.insertItem(getCurrentSlot(), stack, false, forced);
            }
        };
    }
}
