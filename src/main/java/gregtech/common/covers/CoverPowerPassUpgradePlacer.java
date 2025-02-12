package gregtech.common.covers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.covers.CoverPlacerBase;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

public class CoverPowerPassUpgradePlacer extends CoverPlacerBase {

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack aStack, ICoverable aTileEntity) {
        IMetaTileEntity iGregTechTileEntityOffset = aTileEntity.getIGregTechTileEntityOffset(0, 0, 0)
            .getMetaTileEntity();
        if (iGregTechTileEntityOffset instanceof TTMultiblockBase multi) {
            return !multi.ePowerPassCover;
        }
        return false;
    }

}
