package tectech.thing.cover;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.CoverBehavior;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

public class CoverPowerPassUpgrade extends CoverBehavior {

    public CoverPowerPassUpgrade() {}

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack aStack, ICoverable aTileEntity) {
        IMetaTileEntity iGregTechTileEntityOffset = aTileEntity.getIGregTechTileEntityOffset(0, 0, 0)
            .getMetaTileEntity();
        if (iGregTechTileEntityOffset instanceof TTMultiblockBase multi) {
            return !multi.ePowerPassCover;
        }
        return false;
    }

    @Override
    public void placeCover(ForgeDirection side, ItemStack aCover, ICoverable aTileEntity) {
        IMetaTileEntity iGregTechTileEntityOffset = aTileEntity.getIGregTechTileEntityOffset(0, 0, 0)
            .getMetaTileEntity();
        if (iGregTechTileEntityOffset instanceof TTMultiblockBase multi) {
            multi.ePowerPassCover = true;
            multi.ePowerPass = true;
        }
        super.placeCover(side, aCover, aTileEntity);
    }

    @Override
    public boolean onCoverRemoval(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        boolean aForced) {
        IMetaTileEntity iGregTechTileEntityOffset = aTileEntity.getIGregTechTileEntityOffset(0, 0, 0)
            .getMetaTileEntity();
        if (iGregTechTileEntityOffset instanceof TTMultiblockBase multi) {
            multi.ePowerPassCover = false;
            multi.ePowerPass = false;
        }
        return true;
    }

    @Deprecated
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 0;
    }
}
