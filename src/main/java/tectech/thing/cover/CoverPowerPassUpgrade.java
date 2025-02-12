package tectech.thing.cover;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.covers.CoverBehavior;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

public class CoverPowerPassUpgrade extends CoverBehavior {

    public CoverPowerPassUpgrade(CoverContext context) {
        super(context);
    }

    @Override
    public void onPlayerAttach(EntityPlayer player, ItemStack cover) {
        ICoverable coverable = coveredTile.get();
        if (coverable != null && coverable.getIGregTechTileEntityOffset(0, 0, 0)
            .getMetaTileEntity() instanceof TTMultiblockBase multi) {
            multi.ePowerPassCover = true;
            multi.ePowerPass = true;
        }
    }

    public boolean onCoverRemoval(boolean aForced) {
        ICoverable coverable = coveredTile.get();
        if (coverable != null && coverable.getIGregTechTileEntityOffset(0, 0, 0)
            .getMetaTileEntity() instanceof TTMultiblockBase multi) {
            multi.ePowerPassCover = false;
            multi.ePowerPass = false;
        }
        return true;
    }

    @Deprecated
    public int getTickRate() {
        return 0;
    }
}
