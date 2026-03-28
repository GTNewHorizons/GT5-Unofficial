package gregtech.common.inventory;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.item.AbstractInventoryIterator;
import com.gtnewhorizon.gtnhlib.item.ImmutableItemStack;
import com.gtnewhorizon.gtnhlib.item.InventoryIterator;
import com.gtnewhorizon.gtnhlib.item.SimpleItemIO;

public class AEInventoryItemIO extends SimpleItemIO {

    private final AEInventory inventory;

    public AEInventoryItemIO(AEInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    protected @NotNull InventoryIterator iterator(int[] allowedSlots) {
        return new AEInventoryItemIOInventoryIterator(inventory.allSlots, allowedSlots, false);
    }

    @Override
    public @Nullable InventoryIterator simulatedSinkIterator() {
        return new AEInventoryItemIOInventoryIterator(inventory.allSlots, allowedSinkSlots, true);
    }

    private class AEInventoryItemIOInventoryIterator extends AbstractInventoryIterator {

        private final boolean simulated;

        public AEInventoryItemIOInventoryIterator(int[] slots, int[] allowedSlots, boolean simulated) {
            super(slots, allowedSlots);
            this.simulated = simulated;
        }

        @Override
        protected ItemStack getStackInSlot(int slot) {
            return inventory.getStackInSlot(slot);
        }

        @Override
        public ItemStack extract(int amount, boolean forced) {
            return inventory.extractItem(getCurrentSlot(), amount, simulated);
        }

        @Override
        public int insert(ImmutableItemStack stack, boolean forced) {
            if (stack.isEmpty()) return 0;

            return inventory.insertItem(getCurrentSlot(), stack, simulated, forced);
        }
    }
}
