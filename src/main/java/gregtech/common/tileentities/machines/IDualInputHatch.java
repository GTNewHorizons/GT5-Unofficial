package gregtech.common.tileentities.machines;

import net.minecraft.item.ItemStack;

import java.util.Iterator;

public interface IDualInputHatch {
    boolean justUpdated();
    Iterator<? extends IDualInputInventory> inventories();
    void updateTexture(int id);
    void updateCraftingIcon(ItemStack icon);
}
