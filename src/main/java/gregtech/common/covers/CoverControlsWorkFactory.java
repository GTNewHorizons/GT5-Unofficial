package gregtech.common.covers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;

public class CoverControlsWorkFactory extends IntCoverFactory {

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack aStack, ICoverable aTileEntity) {
        if (!super.isCoverPlaceable(side, aStack, aTileEntity)) return false;
        for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
            if (aTileEntity.getCoverInfoAtSide(tSide)
                .getCoverBehavior() instanceof CoverControlsWork) {
                return false;
            }
        }
        return true;
    }
}
