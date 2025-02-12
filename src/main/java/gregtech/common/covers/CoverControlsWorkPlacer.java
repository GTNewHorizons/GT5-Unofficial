package gregtech.common.covers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.covers.CoverPlacerBase;
import gregtech.api.interfaces.tileentity.ICoverable;

public class CoverControlsWorkPlacer extends CoverPlacerBase {

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack aStack, ICoverable coverable) {
        if (!super.isCoverPlaceable(side, aStack, coverable)) return false;
        for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
            if (coverable.getCoverInfoAtSide(tSide) instanceof CoverControlsWork) {
                return false;
            }
        }
        return true;
    }
}
