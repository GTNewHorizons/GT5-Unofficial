package gregtech.common.covers;

import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.covers.CoverFactoryBase;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;

public abstract class CoverFacadeBaseFactory extends CoverFactoryBase<CoverFacadeBase.FacadeData> {

    @Override
    public boolean isSimpleCover() {
        return true;
    }

    @Override
    public CoverFacadeBase.FacadeData createDataObject() {
        return new CoverFacadeBase.FacadeData();
    }

    @Override
    public CoverFacadeBase.FacadeData initializeDataFromCover(ItemStack cover) {
        if (Objects.isNull(cover)) return createDataObject();
        return new CoverFacadeBase.FacadeData(GTUtility.copyAmount(1, cover), 0);
    }

    protected abstract Block getTargetBlock(ItemStack aFacadeStack);

    protected abstract int getTargetMeta(ItemStack aFacadeStack);

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack aStack, ICoverable aTileEntity) {
        // blocks that are not rendered in pass 0 are now accepted but rendered awkwardly
        // to render it correctly require changing GT_Block_Machine to render in both pass, which is not really a good
        // idea...
        if (!super.isCoverPlaceable(side, aStack, aTileEntity)) return false;
        final Block targetBlock = getTargetBlock(aStack);
        if (targetBlock == null) return false;
        // we allow one single type of facade on the same block for now
        // otherwise it's not clear which block this block should impersonate
        // this restriction can be lifted later by specifying a certain facade as dominate one as an extension to this
        // class
        for (final ForgeDirection iSide : ForgeDirection.VALID_DIRECTIONS) {
            if (iSide == side) continue;
            final CoverInfo coverInfo = aTileEntity.getCoverInfoAtSide(iSide);
            if (!coverInfo.isValid()) continue;
            final Block facadeBlock = coverInfo.getFacadeBlock();
            if (facadeBlock == null) continue;
            if (facadeBlock != targetBlock) return false;
            if (coverInfo.getFacadeMeta() != getTargetMeta(aStack)) return false;
        }
        return true;
    }
}
