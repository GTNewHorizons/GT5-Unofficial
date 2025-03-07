package gregtech.common.covers.redstone;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;

public class CoverAdvancedRedstoneTransmitterInternal
    extends CoverAdvancedRedstoneTransmitterBase<CoverAdvancedRedstoneTransmitterBase.TransmitterData> {

    public CoverAdvancedRedstoneTransmitterInternal(CoverContext context, ITexture coverTexture) {
        super(context, TransmitterData.class, coverTexture);
    }

    @Override
    protected TransmitterData initializeData() {
        return new CoverAdvancedRedstoneTransmitterBase.TransmitterData();
    }

    @Override
    public TransmitterData doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return coverData;
        }
        byte outputRedstone = coverable.getOutputRedstoneSignal(coverSide);
        if (coverData.isInvert()) {
            if (outputRedstone > 0) outputRedstone = 0;
            else outputRedstone = 15;
        }

        final CoverData key = getCoverKey(coverable, coverSide);
        setSignalAt(coverData.getUuid(), coverData.getFrequency(), key, outputRedstone);
        return coverData;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }
}
