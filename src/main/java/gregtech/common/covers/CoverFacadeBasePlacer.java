package gregtech.common.covers;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.covers.CoverPlacerBase;
import gregtech.api.interfaces.tileentity.ICoverable;

public abstract class CoverFacadeBasePlacer extends CoverPlacerBase {

    @Override
    public boolean isSimpleCover() {
        return true;
    }

    protected abstract Block getTargetBlock(ItemStack aFacadeStack);

    protected abstract int getTargetMeta(ItemStack aFacadeStack);

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack aStack, ICoverable coverable) {
        // blocks that are not rendered in pass 0 are now accepted but rendered awkwardly
        // to render it correctly require changing GT_Block_Machine to render in both pass, which is not really a good
        // idea...
        if (!super.isCoverPlaceable(side, aStack, coverable)) return false;
        final Block targetBlock = getTargetBlock(aStack);
        if (targetBlock == null) return false;
        // we allow one single type of facade on the same block for now
        // otherwise it's not clear which block this block should impersonate
        // this restriction can be lifted later by specifying a certain facade as dominate one as an extension to this
        // class
        for (final ForgeDirection iSide : ForgeDirection.VALID_DIRECTIONS) {
            if (iSide == side) continue;
            final Cover cover = coverable.getCoverAtSide(iSide);
            if (!cover.isValid()) continue;
            final Block facadeBlock = cover.getFacadeBlock();
            if (facadeBlock == null) continue;
            if (facadeBlock != targetBlock) return false;
            if (cover.getFacadeMeta() != getTargetMeta(aStack)) return false;
        }
        return true;
    }
}
