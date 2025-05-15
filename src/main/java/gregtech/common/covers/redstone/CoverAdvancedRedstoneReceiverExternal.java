package gregtech.common.covers.redstone;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;

public class CoverAdvancedRedstoneReceiverExternal extends CoverAdvancedRedstoneReceiverBase {

    public CoverAdvancedRedstoneReceiverExternal(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        coverable.setOutputRedstoneSignal(coverSide, getSignalAt(getUuid(), getFrequency(), getGateMode()));
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }
}
