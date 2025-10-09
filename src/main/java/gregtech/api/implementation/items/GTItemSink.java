package gregtech.api.implementation.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.capability.item.InventoryItemSink;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;

public class GTItemSink extends InventoryItemSink {

    public GTItemSink(IMetaTileEntity imte, ForgeDirection side) {
        super(imte, side);
    }

    @Override
    protected int getSlotStackLimit(int slot, ItemStack stack) {
        // Cast here instead of storing it as a field because this is called in the super ctor, and by that point a
        // field wouldn't have been set, leading to an NPE
        int invStackLimit = ((IMetaTileEntity) inv).getStackSizeLimit(slot, stack);

        int existingMaxStack = stack == null ? 64 : stack.getMaxStackSize();

        if (invStackLimit > 64) {
            return invStackLimit / 64 * existingMaxStack;
        } else {
            return Math.min(invStackLimit, existingMaxStack);
        }
    }
}
