package gregtech.api.util;

import net.minecraft.item.ItemStack;

import ic2.api.item.IBoxable;

public class GT_IBoxableWrapper implements IBoxable {

    @Override
    public boolean canBeStoredInToolbox(ItemStack itemstack) {
        return GT_Utility.isStackInList(itemstack, GT_ModHandler.sBoxableItems);
    }
}
