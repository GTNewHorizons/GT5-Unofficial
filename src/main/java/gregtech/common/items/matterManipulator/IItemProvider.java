package gregtech.common.items.matterManipulator;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

public interface IItemProvider {

    public @Nullable ItemStack getStack(IPseudoInventory inv, boolean consume);
}
