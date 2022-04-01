package gregtech.api.util;

import ic2.api.item.IBoxable;
import net.minecraft.item.ItemStack;

public class GT_IBoxableWrapper implements IBoxable {
    @Override
    public boolean canBeStoredInToolbox(ItemStack itemstack) {
        return GT_Utility.isStackInList(itemstack, GT_ModHandler.sBoxableItems);
    }
}
