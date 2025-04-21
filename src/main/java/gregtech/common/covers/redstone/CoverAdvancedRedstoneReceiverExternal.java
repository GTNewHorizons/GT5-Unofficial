package gregtech.common.covers.redstone;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;

public class CoverAdvancedRedstoneReceiverExternal extends CoverAdvancedRedstoneReceiverBase {

    public CoverAdvancedRedstoneReceiverExternal(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public ReceiverData doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return coverData;
        }
        coverable.setOutputRedstoneSignal(
            coverSide,
            getSignalAt(coverData.getUuid(), coverData.getFrequency(), coverData.getGateMode()));

        return coverData;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }
}
