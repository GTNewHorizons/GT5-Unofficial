package gregtech.common.items.matterManipulator;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

public interface IItemProvider {

    public default byte getType() {
        if (this instanceof PortableItemStack) {
            return 0;
        } else if (this instanceof AECellItemProvider) {
            return 1;
        } else {
            throw new IllegalStateException();
        }
    }

    public @Nullable ItemStack getStack(IPseudoInventory inv, boolean consume);
}
