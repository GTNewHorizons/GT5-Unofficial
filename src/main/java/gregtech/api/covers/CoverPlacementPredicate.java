package gregtech.api.covers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;

@FunctionalInterface
public interface CoverPlacementPredicate {

    boolean isCoverPlaceable(ForgeDirection side, ItemStack coverItem, ICoverable tileEntity);
}
