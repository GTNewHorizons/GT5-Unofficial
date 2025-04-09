package tectech.thing.cover;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.covers.Cover;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

public class CoverPowerPassUpgrade extends Cover {

    public static boolean isCoverPlaceable(ForgeDirection side, ItemStack coverItem, ICoverable coverable) {
        IMetaTileEntity iGregTechTileEntityOffset = coverable.getIGregTechTileEntityOffset(0, 0, 0)
            .getMetaTileEntity();
        if (iGregTechTileEntityOffset instanceof TTMultiblockBase multi) {
            return !multi.ePowerPassCover;
        }
        return false;
    }

    public CoverPowerPassUpgrade(CoverContext context) {
        super(context, null);
    }

    @Override
    public void onPlayerAttach(EntityPlayer player, ItemStack coverItem) {
        ICoverable coverable = coveredTile.get();
        if (coverable != null && coverable.getIGregTechTileEntityOffset(0, 0, 0)
            .getMetaTileEntity() instanceof TTMultiblockBase multi) {
            multi.ePowerPassCover = true;
            multi.ePowerPass = true;
        }
    }

    @Override
    public void onCoverRemoval() {
        ICoverable coverable = coveredTile.get();
        if (coverable != null && coverable.getIGregTechTileEntityOffset(0, 0, 0)
            .getMetaTileEntity() instanceof TTMultiblockBase multi) {
            multi.ePowerPassCover = false;
            multi.ePowerPass = false;
        }
    }

    @Deprecated
    public int getMinimumTickRate() {
        return 0;
    }
}
