package gregtech.api.util;

import net.minecraft.item.ItemStack;

import ic2.api.item.IBoxable;

public class GTIBoxableWrapper implements IBoxable {

    @Override
    public boolean canBeStoredInToolbox(ItemStack itemstack) {
        return GTUtility.isStackInList(itemstack, GTModHandler.sBoxableItems);
    }
}
