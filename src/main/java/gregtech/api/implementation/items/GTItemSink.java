package gregtech.api.implementation.items;

import java.util.OptionalInt;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.item.FastImmutableItemStack;
import com.gtnewhorizon.gtnhlib.item.InventoryItemSink;
import com.gtnewhorizon.gtnhlib.item.ItemStackPredicate;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.util.GTUtility;

public class GTItemSink extends InventoryItemSink {

    private final IMetaTileEntity imte;

    public GTItemSink(IMetaTileEntity imte, ForgeDirection side) {
        super(imte, side);
        this.imte = imte;
    }

    @Override
    public OptionalInt getStoredItemsInSink(@Nullable ItemStackPredicate filter) {
        int size = imte.getSizeInventory();

        FastImmutableItemStack immutable = new FastImmutableItemStack(null);

        long total = 0;

        for (int i = 0; i < size; i++) {
            if (!imte.isIOSlot(i)) continue;

            immutable.stack = imte.getStackInSlot(i);

            if (immutable.stack == null) continue;

            if (filter == null || filter.test(immutable)) {
                total += immutable.stack.stackSize;
            }
        }

        return OptionalInt.of(GTUtility.longToInt(total));
    }

    @Override
    protected int getSlotStackLimit(int slot, ItemStack stack) {
        int invStackLimit = imte.getStackSizeLimit(slot, stack);

        int existingMaxStack = stack == null ? 64 : stack.getMaxStackSize();

        if (invStackLimit > 64) {
            return invStackLimit / 64 * existingMaxStack;
        } else {
            return Math.min(invStackLimit, existingMaxStack);
        }
    }
}
